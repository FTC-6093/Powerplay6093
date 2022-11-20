package org.firstinspires.ftc.teamcode;
import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;


public class ColorPipeline extends OpenCvPipeline {
    Mat inputMask = new Mat();
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

//    private static char checkForHSV(double h, double error) {
////        assert h <= 60;
//        double horiz_rot = (h-60) % 360;
//
//        //red = 300
//        if (error-300 < horiz_rot && horiz_rot < error+300) {
//            return 'r';
//        }
//        //green = 60
//        if (error-60 < horiz_rot && horiz_rot < error+60) {
//            return 'g';
//        }
//        //blue = 180
//        if (error-180 < horiz_rot && horiz_rot < error+180) {
//            return 'b';
//        }
//
//        return 'e';
//    }


    @Override
    public void init(Mat input) {
    }

    @Override
    public Mat processFrame(@NonNull Mat input) {
        //screen is 720 by 1280
        input.copyTo(inputMask);
        Imgproc.cvtColor(inputMask,inputMask,Imgproc.COLOR_RGB2BGR);
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

    public char getColor() {
        double[] rgb = inputMask.get(360, 640);
        if (rgb != null) {
            if (rgb.length == 4 || rgb.length == 3) {
                double r = rgb[0];
                double g = rgb[1];
                double b = rgb[1];
                return checkForRGB(r,g,b, 60);
            }
        }
        return 'a';
    }

    public double[] getMiddlePixel() {
        return inputMask.get(640, 360);
    }
}
