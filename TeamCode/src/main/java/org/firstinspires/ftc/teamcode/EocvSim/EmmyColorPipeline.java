package org.firstinspires.ftc.teamcode.EocvSim;
import static java.lang.Thread.sleep;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;


public class EmmyColorPipeline extends OpenCvPipeline {
    Mat rgb = new Mat();
    Mat hsvMask = new Mat();
    Size boxSize = new Size(5, 5);
    Mat mask = new Mat(new Size(1282,722), org.opencv.core.CvType.CV_8U);
    org.opencv.core.Point seed = new org.opencv.core.Point(640, 360);
    Scalar one = new Scalar(1);

    private static char checkForRGB(double r, double g, double b, double error) {
        if (255-error < r+g+b || r+g+b < 255+error) {
            char[] rgb = {'r', 'g', 'b'};
            double[] findMax = {r, g, b};

            int currentMaxIndex = 0;
            for (int i=0; i < 3; i++) {
                if (findMax[i] > findMax[currentMaxIndex]) {
                    currentMaxIndex = i;
                }
            }

            return rgb[currentMaxIndex];
        }
        return 'e';
    }

    private static char checkForHSV(double h, double error) {
//        assert h <= 60;

        double horiz_rot = (h-error) % 360;
        if (horiz_rot < 0) {horiz_rot += 360;} //AAAAAAAAAAAAAAAAAAA

        //red = 300
        if (240 < horiz_rot && horiz_rot < 360) {
            return 'r';
        }
        //green = 60
        if (0 < horiz_rot && horiz_rot < 120) {
            return 'g';
        }
        //blue = 180
        if (120 < horiz_rot && horiz_rot < 240) {
            return 'b';
        }

        return 'e';
    }


    @Override
    public void init(Mat input) {

    }

    @Override
    public Mat processFrame(@NonNull Mat input) {
        //screen is 720 by 1280
//        input.copyTo(inputMask);
        Imgproc.cvtColor(input,rgb,Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(rgb,hsvMask,Imgproc.COLOR_RGB2HSV);

//            Imgproc.Canny(input, inputMask, 250, 800);
//            Imgproc.blur(inputMask, inputMask, boxSize);
//            //assuming Mat.size() gets mat size
//            Imgproc.floodFill(inputMask, mask, seed, one);
//            Imgproc.threshold(inputMask,inputMask,254,255,Imgproc.THRESH_BINARY);
//
//            telemetry.addData("Image size: ", ""+input.size().toString());
//            telemetry.addData("Mask size: ", ""+inputMask.size().toString());
//            telemetry.update();

//            input.mul(inputMask);



        return input;
    }

    public char getColor(double[] clr) {
        double[] hsv = hsvMask.get(360, 640);
        if (hsv != null) {
            if (hsv.length == 4 || hsv.length == 3 || hsv.length == 1) {
                double r = hsv[0];
//                double g = hsv[1];
//                double b = hsv[2];
//                return checkForRGB(r,g,b,60);
                return checkForHSV(r*2, 60);
            }
        }
        return 'a';
    }

    public char getSample() {
        double hues = 0;
        for (int i = 0; i < 100; i++) {
            hues += getMiddlePixel()[0];
        }

        return getColor(new double[]{hues/100});
    }

    public double[] getMiddlePixel() {
        return hsvMask.get(640, 360);
    }
}
