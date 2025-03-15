import cv2
from ultralytics import YOLO
import numpy as np
import os
import datetime

txt_file = "show_objects.txt"
if os.path.exists(txt_file):
    with open(txt_file, "r") as f:
        show_objects = [line.strip() for line in f if line.strip()]
else:
    show_objects = ["person"]
    with open(txt_file, "w") as f:
        for obj in show_objects:
            f.write(obj + "\n")

model = YOLO('yolov8n.pt')
cap = cv2.VideoCapture(0)

cv2.namedWindow("Forest Guards", cv2.WINDOW_NORMAL)
cv2.setWindowProperty("Forest Guards", cv2.WND_PROP_FULLSCREEN, cv2.WINDOW_NORMAL)
cv2.resizeWindow("Forest Guards", 1920, 1080)

frame_width = int(cap.get(3))
frame_height = int(cap.get(4))
output_filename = f"output_{datetime.datetime.now().strftime('%Y%m%d_%H%M%S')}.mp4"
fourcc = cv2.VideoWriter_fourcc(*'mp4v')
out = cv2.VideoWriter(output_filename, fourcc, 20.0, (frame_width, frame_height))

while True:
    ret, frame = cap.read()
    if not ret:
        break
    frame = cv2.resize(frame, (1920, 1080))
    frame = cv2.flip(frame, 1)
    results = model(frame, verbose=False)
    boxes = results[0].boxes
    annotated_frame = frame.copy()
    person_count = 0
    for i in range(len(boxes)):
        box = boxes.xyxy[i].cpu().numpy().astype(int).flatten()
        conf = boxes.conf[i].cpu().numpy().item()
        cls = int(boxes.cls[i].cpu().numpy())
        label = model.names[cls]
        if label in show_objects:
            person_count += 1
            cv2.rectangle(annotated_frame, (box[0], box[1]), (box[2], box[3]), (0, 255, 0), 2)
            cv2.putText(annotated_frame, f"{label}: {conf:.2f}", (box[0], box[1] - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)

    cv2.putText(annotated_frame, f"People detected: {person_count}", (10, 30),
                cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 0, 0), 2)

    out.write(annotated_frame)
    cv2.imshow("Forest Guards", annotated_frame)
    if cv2.waitKey(1) & 0xFF == ord("q"):
        break

cap.release()
out.release()
cv2.destroyAllWindows()
