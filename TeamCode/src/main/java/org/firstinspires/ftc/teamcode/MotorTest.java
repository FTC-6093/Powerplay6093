package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.Arrays;

@TeleOp (name = "Motor Test", group = "Iterative Opmode")
public class MotorTest extends OpMode {

    //Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor BLDrive = null;
    private DcMotor FRDrive = null;
    private DcMotor FLDrive = null;
    private DcMotor BRDrive = null;
    int HOW_LONG_MAX = 0;
    final double powerMultiplier = 0.5;

    OpenCvWebcam webcam = null;
    ColorPipeline pipeline = new ColorPipeline();
    private DcMotor VertLift = null;

    private static char checkForColor(double r, double g, double b, double error) {
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

    @Override
    public void init() { //Code to run ONCE when the driver hits INIT
        telemetry.addData("Status", "Initializing");
        telemetry.update();
        //Configure OpMode members to Driver Hub Device Names
        BLDrive = hardwareMap.get(DcMotor.class, "BLDrive");
        FRDrive = hardwareMap.get(DcMotor.class, "FRDrive");
        FLDrive = hardwareMap.get(DcMotor.class, "FLDrive");
        BRDrive = hardwareMap.get(DcMotor.class, "BRDrive");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        VertLift  = hardwareMap.get(DcMotor.class, "VertLift");

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode) {
            }
        });



        webcam.setPipeline(pipeline);

        //Set OpMode Member's directions
        BLDrive.setDirection(DcMotor.Direction.FORWARD);
        BRDrive.setDirection(DcMotor.Direction.REVERSE);
        FRDrive.setDirection(DcMotor.Direction.REVERSE);
        FLDrive.setDirection(DcMotor.Direction.FORWARD);
        VertLift.setDirection(DcMotor.Direction.FORWARD);

        //EmptyPipeline pLine = new EmptyPipeline();
        //pLine.processFrame(isMat);


        double pastMotorPower = 0;



        telemetry.addData("Status", "Initialized");
    }

    public class ColorPipeline extends OpenCvPipeline implements org.firstinspires.ftc.teamcode.ColorPipeline {
        Mat inputMask = new Mat();
        Size boxSize = new Size(5, 5);
        Mat mask = new Mat(new Size(1282,722), org.opencv.core.CvType.CV_8U);
        org.opencv.core.Point seed = new org.opencv.core.Point(640, 360);
        Scalar one = new Scalar(1);



        @Override
        public Mat processFrame(@NonNull Mat input) {
            //screen is 720 by 1280
            input.copyTo(inputMask);
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
                if (rgb.length == 4) {
                    double r = rgb[0];
                    double g = rgb[1];
                    double b = rgb[2];
                    return checkForColor(r, g, b, 70);
                }
            }
            return 'a';
        }

        public double[] getMiddlePixel() {
            return inputMask.get(640, 360);
        }
    }

        @Override
    public void start() { //Code to run ONCE when the driver hits PLAY
        runtime.reset();
    }

    @Override
    public void loop() { //Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP

        HOW_LONG_MAX = Math.max(FLDrive.getCurrentPosition(), HOW_LONG_MAX);
//        webcam.getFrameBitmap(Bitmap.getPixel);


        //Retrieve driving values from controller
        double y = gamepad1.left_stick_y * 0.8; // Is reversed
        double x = gamepad1.left_stick_x * 0.8;// Counteract imperfect strafing
        double rx = gamepad1.right_stick_x * 0.8;
        boolean up;
        boolean down;
        up = (gamepad1.left_trigger > 0);
        down = (gamepad1.right_trigger > 0);
        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when at least one is out
        // of the range [-1, 1]
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x - rx) / denominator;
        double backLeftPower = (y - x - rx) / denominator;
        double frontRightPower = (y - x + rx) / denominator;
        double backRightPower = (y + x + rx) / denominator;

        FLDrive.setPower(frontLeftPower);
        FRDrive.setPower(frontRightPower);
        BLDrive.setPower(backLeftPower);
        BRDrive.setPower(backRightPower);

        if (down && !up) {
            VertLift.setPower(1.0);
        } else if (!down && up){
            VertLift.setPower(-1.0);
        }else{
            VertLift.setPower(0);
        }

        telemetry.addData("Pixel: ", ""+ Arrays.toString(pipeline.getMiddlePixel()));
        telemetry.addData("Color: ", ""+pipeline.getColor());

        switch (pipeline.getColor()) {
            case 'r':
                //red
            case 'g':
                //green
            case 'b':
                //blue
            case 'e':
                //not looking at cone
            case 'a':
                //something with config seriously messed up
            default:
                //how tf did you get here
        }

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("HOW_LONG_MAX", FRDrive.getCurrentPosition());
        // telemetry.addData("Motors", "Forward (%f), Backward (%f)", motorForward, motorBackward);
        telemetry.update();
    }


    @Override
    public void stop() { //Code to run ONCE after the driver hits STOP
        telemetry.addData("Status", "STOPPING");
        FLDrive.setPower(0);
        FRDrive.setPower(0);
        BLDrive.setPower(0);
        BRDrive.setPower(0);
        telemetry.addData("Status", "STOPPED");
    }
}
