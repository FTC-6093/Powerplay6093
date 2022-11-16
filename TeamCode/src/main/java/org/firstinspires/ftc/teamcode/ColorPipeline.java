package org.firstinspires.ftc.teamcode;
import androidx.annotation.NonNull;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public interface ColorPipeline {
    public class ConvertToGreyPipeline extends OpenCvPipeline
    {
        // Notice this is declared as an instance variable (and re-used), not a local variable
        Mat grey = new Mat();

        Telemetry telemetry;
        public ConvertToGreyPipeline(Telemetry t) {telemetry = t;}
        @Override
        public Mat processFrame(@NonNull Mat input) {
            return input;
    }
//            Mat inputMask = new Mat();
//            input.copyTo(inputMask);
//            Imgproc.Canny(inputMask, inputMask, 250, 800);
//            Imgproc.blur(inputMask, inputMask, new Size(5, 5));
//            telemetry.addData("Image size: ", ""+input.size());
//            telemetry.update();
//            //assuming Mat.size() gets mat size
//            Size imgDims = input.size();
//            Mat mask = new Mat();
//            mask.reshape(((int) imgDims.width), ((int) imgDims.height));
//            org.opencv.core.Point seed = new org.opencv.core.Point(imgDims.width/2, imgDims.height/2);
//            Imgproc.floodFill(inputMask, mask, seed, new Scalar(1));
//            Imgproc.threshold(inputMask,inputMask,254,255,Imgproc.THRESH_BINARY);
//            Mat masked = input.mul(inputMask);
//            return masked;
      //  }
    }
}
