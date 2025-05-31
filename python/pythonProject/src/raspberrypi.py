import cv2
import torch
import threading
import numpy as np
import datetime
import os
import time
import serial
import asyncio
import websockets
from ultralytics import YOLO

YOLO_MODEL_PATH = 'yolov8n.pt'
SBUS_SERIAL_PORT = '/dev/ttyAMA0'
SBUS_BAUDRATE = 100000
WEBSOCKET_URI = "ws://xjyj.site/ws/device"
SBUS_CHANNELS = [1024] * 16

txt_file = "show_objects.txt"
if os.path.exists(txt_file):
    with open(txt_file, "r") as f:
        show_objects = {line.strip() for line in f if line.strip()}
else:
    show_objects = {"person"}
    with open(txt_file, "w") as f:
        f.write("\n".join(show_objects) + "\n")

model = YOLO(YOLO_MODEL_PATH)
device = "cuda" if torch.cuda.is_available() else "cpu"
model.to(device)

cap = cv2.VideoCapture(0)
frame_width = int(cap.get(3))
frame_height = int(cap.get(4))
output_filename = f"output_{datetime.datetime.now().strftime('%Y%m%d_%H%M%S')}.mp4"
fourcc = cv2.VideoWriter_fourcc(*'mp4v')
out = cv2.VideoWriter(output_filename, fourcc, 20.0, (frame_width, frame_height))

cv2.namedWindow("Forest Guards", cv2.WINDOW_NORMAL)
cv2.resizeWindow("Forest Guards", 1280, 720)

frame = None
running = True
latest_detections = []

def transmit_sbus():
    try:
        ser = serial.Serial(SBUS_SERIAL_PORT, SBUS_BAUDRATE, timeout=1)
        while running:
            packet = bytearray([0x0F] + [0x00] * 22 + [0x00, 0x00])
            ser.write(packet)
            time.sleep(0.05)
    except Exception as e:
        print(f"SBUS transmission failed: {e}")

async def send_detections():
    while running:
        try:
            async with websockets.connect(WEBSOCKET_URI) as ws:
                while running:
                    if latest_detections:
                        await ws.send(str(latest_detections[-1]))
                    await asyncio.sleep(1)
        except Exception as e:
            print(f"WebSocket error: {e}")
            await asyncio.sleep(5)

def read_camera():
    global frame
    while running:
        ret, temp_frame = cap.read()
        if ret:
            frame = temp_frame

threading.Thread(target=transmit_sbus, daemon=True).start()
threading.Thread(target=read_camera, daemon=True).start()
threading.Thread(target=lambda: asyncio.run(send_detections()), daemon=True).start()

while True:
    if frame is None:
        continue

    results = model(frame, verbose=False, device=device)
    boxes = results[0].boxes
    annotated_frame = frame.copy()
    detected_objects = []
    person_count = 0

    for box in boxes:
        cls = int(box.cls.cpu().numpy())
        label = model.names[cls]
        if label in show_objects:
            person_count += 1
            xyxy = box.xyxy.cpu().numpy().astype(int)[0]
            conf = box.conf.cpu().numpy().item()
            cv2.rectangle(annotated_frame, (xyxy[0], xyxy[1]), (xyxy[2], xyxy[3]), (0, 255, 0), 2)
            cv2.putText(annotated_frame, f"{label}: {conf:.2f}", (xyxy[0], xyxy[1] - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)
            detected_objects.append({
                "label": label,
                "confidence": round(conf, 2),
                "coordinates": [int(x) for x in xyxy]
            })

    latest_detections.append({
        "timestamp": datetime.datetime.now().isoformat(),
        "count": person_count,
        "objects": detected_objects
    })

    cv2.putText(annotated_frame, f"People detected: {person_count}", (10, 30),
                cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 0, 0), 2)

    out.write(annotated_frame)
    cv2.imshow("Forest Guards", annotated_frame)

    if cv2.waitKey(1) & 0xFF == ord("q"):
        break

running = False
cap.release()
out.release()
cv2.destroyAllWindows()
