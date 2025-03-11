import cv2
from ultralytics import YOLO
import numpy as np
import os

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

while True:
    ret, frame = cap.read()
    if not ret:
        break
    results = model(frame, verbose=False)
    boxes = results[0].boxes
    annotated_frame = frame.copy()
    for i in range(len(boxes)):
        box = boxes.xyxy[i].cpu().numpy().astype(int).flatten()
        conf = boxes.conf[i].cpu().numpy().item()  # Güven değeri artık skaler
        cls = int(boxes.cls[i].cpu().numpy())
        label = model.names[cls]
        if label in show_objects:
            cv2.rectangle(annotated_frame, (box[0], box[1]), (box[2], box[3]), (0, 255, 0), 2)
            cv2.putText(annotated_frame, f"{label}: {conf:.2f}", (box[0], box[1]-10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)
    cv2.imshow("Forest Guards", annotated_frame)
    if cv2.waitKey(1) & 0xFF == ord("q"):
        break

cap.release()
cv2.destroyAllWindows()
