import cv2
import torch
import threading
from ultralytics import YOLO
import numpy as np
import os
import datetime


txt_file = "show_objects.txt"
if os.path.exists(txt_file):
    with open(txt_file, "r") as f:
        show_objects = {line.strip() for line in f if line.strip()}
else:
    show_objects = {"person"}
    with open(txt_file, "w") as f:
        f.write("\n".join(show_objects) + "\n")


model = YOLO('yolov8n.pt')
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



def read_camera():
    global frame, running
    while running:
        ret, temp_frame = cap.read()
        if ret:
            frame = temp_frame
        else:
            break


threading.Thread(target=read_camera, daemon=True).start()

while True:
    if frame is None:
        continue


    results = model(frame, verbose=False, device=device)
    boxes = results[0].boxes
    annotated_frame = frame.copy()
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
