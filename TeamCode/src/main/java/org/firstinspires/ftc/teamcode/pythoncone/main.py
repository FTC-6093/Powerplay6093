from tkinter import Tk, Label
from PIL import Image, ImageTk, ImageDraw, ImageOps
import cv2 as cv
import numpy as np
import time
import img
import pickle

# replit had an update that broke cv2 importing, so
# I forked an existing cv2 project -_-
win = Tk()
win.geometry("480x640")

t = time.time_ns()

def compose(*args): #compose a bunch of one arg functions
    if len(args) == 0:
        return lambda x: x
    else:
        return lambda x: args[-1](compose(*args[:-1])(x))

color = 1

if color == 0:
    color_pipeline = compose(
        img.pil_to_cv,
        lambda x: cv.Canny(x, 250, 800),
        lambda x: cv.blur(x, (5,5)),
    )
    
    cvpiped = [color_pipeline(x) for x in img.images]

    for i, imge in enumerate(cvpiped):
        # l = img.fillhole((240,320), imge)
        h, w = imge.shape[:2]
        #when translating, look up cv.CreateImage instead of np.zeros()
        mask = np.zeros((h+2, w+2), np.uint8)
        imge = imge.astype("uint8")
        cv.floodFill(imge, mask, (240, 320), 255)
        cvpiped[i] = (cv.threshold(imge, 254, 255, cv.THRESH_BINARY)[1] \
                      / 255).astype("uint8")
        # cvpiped[i] =  np.vectorize(
        #     lambda x: 0 if x < 255 else 1)\
        #     (img.fillhole((240,320), imge))\
        #     .astype("uint8")

    print(cvpiped[0][320,240])
    
    cvmask = [np.multiply(
        x,
        np.dstack([y,y,y])
    ) for y,x in zip(cvpiped, (
        img.pil_to_cv(x) for x in img.images
    ))]

    # with open("cvlists", 'wb') as f:
    #     pickle.dump((cvpiped, cvmask), f)
elif color == 1:
    shape_pipeline = compose(
        ImageOps.exif_transpose,
        img.pil_to_cv,
        lambda x: cv.Canny(x, 600, 1000),
        lambda x: cv.blur(x, (5,5)),
        lambda x: cv.threshold(x, 1, 255, cv.THRESH_BINARY)[1],
    )

    cvpiped = [shape_pipeline(x) for x in img.images]

    # cvcontours = [img.findPoly(x) for x in cvpiped]
    
    # cvpiped = [np.dstack([x,x,x]) for x in cvpiped]
    
    # for i, im in enumerate(cvpiped):
    #     cv.drawContours(im, [cvcontours[i]], 0, (0,255,0), 3)
elif color == 2:
    color_profile_pipeline = compose(
        img.pil_to_cv,
        lambda x: cv.Canny(x, 250, 900),
        lambda x: cv.blur(x, (3,3)),
        lambda x: cv.threshold(x, 1, 255, cv.THRESH_BINARY)[1],
    )

    cvpiped = [color_profile_pipeline(x) for x in img.images]
    
    cvcontours = [img.findPoly(x, 1) for x in cvpiped]
    
    cvpiped = [np.dstack([x,x,x]) for x in cvpiped]
    
    for i, im in enumerate(cvpiped):
        cv.drawContours(im, [cvcontours[i]], 0, (0,255,0), 3)

else:
    with open("cvlists", 'rb') as f:
        (cvpiped, cvmask) = pickle.load(f)

print(time.time_ns()-t)

while 1:
    for i in range(3):
        cv.imshow('image',cvpiped[i])
        cv.waitKey(0)
