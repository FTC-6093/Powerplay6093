from tkinter import Tk, Label
from PIL import Image, ImageTk, ImageDraw, ImageOps
import cv2 as cv
import numpy as np
import time
import img
import pickle

def compose(*args): #compose a bunch of one arg functions
    if len(args) == 0:
        return lambda x: x
    else:
        return lambda x: args[-1](compose(*args[:-1])(x))

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
    cvpiped[i] = (cv.threshold(imge, 254, 255, cv.THRESH_BINARY)[1] / 255).astype("uint8")
    # cvpiped[i] = np.vectorize(lambda x: 0 if x < 255 else 1)(img.fillhole((240,320), imge)).astype("uint8")

# print(cvpiped[0][320,240])

cvmask = [np.multiply(x, np.dstack([y,y,y])) for y, x in zip(cvpiped, (img.pil_to_cv(x) for x in img.images))]