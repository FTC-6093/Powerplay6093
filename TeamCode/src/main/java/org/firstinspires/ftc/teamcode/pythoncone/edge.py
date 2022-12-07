import cv2

cap = cv2.VideoCapture("person.mp4")

while True:
    _, frame = cap.read()

    edges = cv2.Canny(image=frame, threshold1=100, threshold2=200) # Canny Edge Detection
    cv2.imshow("webcam", edges)
    if cv2.waitKey(1) == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()