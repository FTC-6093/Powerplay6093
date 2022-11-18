package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Mat;

import androidx.annotation.NonNull;

import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "Auto 1")
//@Disabled
public class Auto1 extends LinearOpMode {
    // Declare OpMode members. (attributes of OP mode)
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FLDrive = null;
    private DcMotor FRDrive = null;
    private DcMotor BLDrive = null;
    private DcMotor BRDrive = null;
    OpenCvWebcam webcam = null;
    private  BNO055IMU imu;
    private DcMotor VertLift = null;
    ColorPipeline pipeline = new ColorPipeline();
    final double WHEEL_DIAMETER = 3.875;
    final double TICKS_PER_ROTATION = 1120;
    final double PIE = 3.14159;
    final double TICKS_PER_INCH = TICKS_PER_ROTATION/(WHEEL_DIAMETER * PIE);
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
    public void runOpMode() {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        initializeIMU();

        waitForStart();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        FLDrive = hardwareMap.get(DcMotor.class, "FLDrive");
        FRDrive = hardwareMap.get(DcMotor.class, "FRDrive");
        BLDrive = hardwareMap.get(DcMotor.class, "BLDrive");
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

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        FLDrive.setDirection(DcMotor.Direction.FORWARD);
        BLDrive.setDirection(DcMotor.Direction.FORWARD);
        FRDrive.setDirection(DcMotor.Direction.REVERSE);
        BRDrive.setDirection(DcMotor.Direction.REVERSE);

        FLDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the game to start (driver presses PLAY)

        runtime.reset();

//==================================================================================================
        //Jaren, run code here
        //pipeline.getColor() will return
        //'r' for red, 'b' for blue, 'g' for green, 'e' if not pointing at cone, 'a' if something went wrong in config.
        driveStraight(1, 0.5, FRDrive, FLDrive, BLDrive, BRDrive);
        sleep(1000);
        driveStraight(1, -0.5, FRDrive, FLDrive, BLDrive, BRDrive);


//        switch (pipeline.getColor()) {
//            case 'r':
//                IMUTurn(-90, 0.5, FRDrive, FLDrive, BRDrive, BLDrive);
//                driveStraight(36, 0.5, FLDrive, FRDrive, BRDrive, BLDrive);
//                telemetry.addData("Camera Status: ", "Red Detected");
//            case 'g':
//                driveStraight(43, 0.5, FRDrive, FLDrive, BRDrive, BLDrive);
//                telemetry.addData("Camera Status: ", "Green Detected");
//            case 'b':
//                IMUTurn(90, 0.5, FRDrive, FLDrive, BRDrive, BLDrive);
//                driveStraight(36, 0.5, FRDrive, FLDrive, BRDrive, BLDrive);
//                telemetry.addData("Camera Status: ", "Blue Detected");
//            case 'e':
//                IMUTurn(10, 0.2, FRDrive, FLDrive, BRDrive, BLDrive);
//                IMUTurn(-10,0.2, FRDrive, FLDrive, BLDrive, BRDrive);
//                telemetry.addData("Camera Status: ", "Camera Searching");
//            case 'a':
//                //trigger plan c
//                //panic_and_catch_fire("gun");
//                telemetry.addData("Camera Status: ", "Camera malfunction");
//                break;
//            default:
//                //logically, this case is impossible
//                //trigger plan d
//                //panic_and_catch_fire("nuke");
//                telemetry.addData("Camera Status: ", "??????????????????");
//        }
//
//        sleep(2000);

        //This is were we put the code we want

        //IMUTurn(360, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        //sleep(1000);
        //IMUTurn(90, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        //sleep(1000);
        //IMUTurn(-90, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        //sleep(1000);





        //This section is if we are the robot closer to the storage unit


        /*IMUTurn(-90, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        driveStraight(25, 0.5, FLDrive, FRDrive, BLDrive, BRDrive);
        //Use a motor to return duck from carousel here
        sleep(1000);
        IMUTurn(90, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        driveStraight(10, 0.5, FLDrive, FRDrive, BLDrive, BRDrive);
        driveStraight(-12, -0.5, FLDrive, FRDrive, BLDrive, BRDrive);
        IMUTurn(90, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        driveStraight(120, 0.9, FLDrive, FRDrive, BLDrive, BRDrive);
*/
        //This part is if we are the robot nearest to the warehouse and the other team has working auto
            /*driveStraight(2, 0.5, FLDrive, FRDrive, BLDrive, BRDrive);
            sleep(5000);
            IMUTurn(-83.5, .5, FLDrive, FRDrive, BLDrive, BRDrive);
            driveStraight(60, 0.5, FLDrive, FRDrive, BLDrive, BRDrive);
            //Use a motor to return duck from carousel here
            sleep(1000)
            IMUTurn(83.5, .5, FLDrive, FRDrive, BLDrive, BRDrive);
            driveStraight(10, 0.5, FLDrive, FRDrive, BLDrive, BRDrive);
            driveStraight(-5, -0.5, FLDrive, FRDrive, BLDrive, BRDrive);
            IMUTurn(83.5, .5, FLDrive, FRDrive, BLDrive, BRDrive);
            driveStraight(50, 0.9, FLDrive, FRDrive, BLDrive, BRDrive);
             */


        telemetry.update();
        //sleep(5000);
    }




    public void initializeIMU() {
        this.imu = this.hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        this.imu.initialize(parameters);

        if(!this.imu.isGyroCalibrated()) {
            this.sleep(50);
        }
    }
    /*
        public void IMUTurn (double angle, double power, DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight){

            double startAngle = this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
            double robotTurnedAngle = 0;

                //angle = angle * .9; //incrementation to account for drift

            frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            while(this.opModeIsActive() &&
                    Math.abs(robotTurnedAngle) < Math.abs(angle)) {//ZYX
                robotTurnedAngle = this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle-startAngle;

                if((Math.abs(robotTurnedAngle)) >= (Math.abs(angle)))
                {
                    frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    break;
                }

                if(robotTurnedAngle>180) {
                    robotTurnedAngle = robotTurnedAngle - 360;

                }
                if(robotTurnedAngle<-180) {
                    robotTurnedAngle = robotTurnedAngle + 360;

                }
                if(angle<0) {
                    frontLeft.setPower(-power);
                    frontRight.setPower(power);
                    backLeft.setPower(-power);
                    backRight.setPower(power);
                }
                else{
                    frontLeft.setPower(power);
                    frontRight.setPower(-power);
                    backLeft.setPower(power);
                    backRight.setPower(-power);
                    /*
                    frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
                }
            }
                    }*/
    public void IMUTurn (double angle, double power, DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight){

        double startAngle = this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
        double robotTurnedAngle = 0;

        angle = angle * .9; //incrementation to account for drift

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        while(this.opModeIsActive() &&
                Math.abs(robotTurnedAngle) < Math.abs(angle)) {
            robotTurnedAngle = this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle-startAngle;









            if((Math.abs(robotTurnedAngle)) >= (Math.abs(angle)))
            {
                frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);
                break;
            }
            if(robotTurnedAngle>180) {
                robotTurnedAngle = robotTurnedAngle - 360;

            }

            if(robotTurnedAngle<-180) {
                robotTurnedAngle = robotTurnedAngle + 360;

            }

            if(angle<0) {
                frontLeft.setPower(-power);
                frontRight.setPower(power);
                backLeft.setPower(-power);
                backRight.setPower(power);

            }

            else{
                frontLeft.setPower(power);
                frontRight.setPower(-power);
                backLeft.setPower(power);
                backRight.setPower(-power);
            }
        }
    }

    public void driveStraight(double inch, double power, DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight) {
        //encoder's resolution: 537.6 in
        //Going straight constant: 42.78
        final double ticksPerInch = TICKS_PER_INCH;
        // final double ticksPerInch = 95.94  ; //ticks doubled because the rollers on the mechanam wheels apply some force sideways

        //Reset encoder positions
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int tickNeeded = (int) (ticksPerInch * inch);

        int FlPosition;
        int FrPosition;
        int BLpositiom;
        int Brposition;

        //How many ticks the motor needs to move
        frontLeft.setTargetPosition(tickNeeded);
        frontRight.setTargetPosition(tickNeeded);
        backLeft.setTargetPosition(tickNeeded);
        backRight.setTargetPosition(tickNeeded);

        //Changes what information we send to the motors.
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Set the power value for each motor
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        FlPosition = frontLeft.getCurrentPosition();
        FrPosition = frontRight.getCurrentPosition();
        BLpositiom = backLeft.getCurrentPosition();
        Brposition = backRight.getCurrentPosition();

        while (this.opModeIsActive() && (frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy())) {
            //We are just waiting until the motors reach the position (based on the ticks passed)
//             telemetry.addData("FL Position", FlPosition);
//            telemetry.addData("Fr Position", FrPosition);
//            telemetry.addData("Bl Position", BLpositiom);
//            telemetry.addData("Br Position", Brposition);
//             telemetry.addData("Ticks Needed", tickNeeded);
            telemetry.addData("Success!", null);

            telemetry.update();
        }
        //When the motors have passed the required ticks, stop each motor
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }
}
