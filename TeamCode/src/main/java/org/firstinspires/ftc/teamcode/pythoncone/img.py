from tkinter import Label
from PIL import Image, ImageTk, ImageDraw, ImageOps
import cv2 as cv
import numpy as np
import time

# replit had an update that broke cv2 importing, so
# I forked an existing cv2 project -_-


# Import cone images
images = [
    Image.open(f"Images/Color/Downscaled/{x}_downscaled_cone.jpg")
    for x in ("red", "blue", "green")
]


#convert between image formats
def pil_to_cv(image):
    return cv.cvtColor(np.array(image), cv.COLOR_RGB2BGR)
def cv_to_pil(image):
    return Image.fromarray(cv.cvtColor(image, cv.COLOR_BGR2RGB))

# show image code
def showimage(win, image, t):
    win.update()
    if type(image) == np.ndarray:
        image = cv_to_pil(image)
    i = ImageTk.PhotoImage(image)
    j = Label(win, image=i)
    j.pack()
    if t != -1:
        win.update()
        time.sleep(t)
        j.pack_forget()
    else:
        win.mainloop()

#draw a dot
def spot(xy: tuple, radius: int, color: str, draw: ImageDraw):
    draw.ellipse((
        xy[0]-radius,
        xy[1]-radius,
        xy[0]+radius,
        xy[1]+radius,
    ), color)

#floodfill image
def fillhole(xy, input_image):
    im_flood_fill = input_image.copy()
    h, w = input_image.shape[:2]
    mask = np.zeros((h + 2, w + 2), np.uint8)
    im_flood_fill = im_flood_fill.astype("uint8")
    cv.floodFill(im_flood_fill, mask, xy, 255)
    # im_flood_fill_inv = cv.bitwise_not(im_flood_fill)
    # img_out = input_image | im_flood_fill_inv
    return im_flood_fill

#get average pixel color, excluding 0's
def avg_pix_mask(input_image):
    z = input_image.transpose(2,0,1)
    return tuple(x.sum()/np.count_nonzero(z[0]) for x in z)

#return a contour of a polygon in an image
def findPoly(input_image, grain=10):
    contours = cv.findContours(input_image, cv.RETR_TREE, cv.CHAIN_APPROX_SIMPLE)
    arr = contours[1][0]
    filter_arr = arr[:, 2] != -1
    polycontour = cv.approxPolyDP(contours[0][filter_arr], grain, True)
    return polycontour
