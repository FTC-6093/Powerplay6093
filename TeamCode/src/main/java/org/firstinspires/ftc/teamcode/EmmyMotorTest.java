package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.Arrays;

@TeleOp (name = "Motor Test", group = "Iterative Opmode")
public class EmmyMotorTest extends OpMode {

    //Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FLDrive = null;
    private DcMotor FRDrive = null;
    private DcMotor BLDrive = null;
    private DcMotor BRDrive = null;
    private DcMotor VertLift = null;
    private CRServo OpenClaw = null;
    private CRServo LiftUp = null;
    OpenCvWebcam webcam = null;
    EmmyColorPipeline pipeline = new EmmyColorPipeline();
    
    int HOW_LONG_MAX = 0;
    final double powerMultiplier = 0.5;
    final double WHEEL_DIAMETER = 3.875;
    final double TICKS_PER_ROTATION = 1120;
    final double PIE = 3.14159;
    final double TICKS_PER_INCH = TICKS_PER_ROTATION/(WHEEL_DIAMETER * PIE);
    @Override
    public void init() { //Code to run ONCE when the driver hits INIT
        telemetry.addData("Status", "Initializing");
        telemetry.update();
        //Configure OpMode members to Driver Hub Device Names
        FLDrive = hardwareMap.get(DcMotor.class, "FLDrive");
        FRDrive = hardwareMap.get(DcMotor.class, "FRDrive");
        BLDrive = hardwareMap.get(DcMotor.class, "BLDrive");
        BRDrive = hardwareMap.get(DcMotor.class, "BRDrive");

        OpenClaw = hardwareMap.get(CRServo.class, "OpenClaw");
        LiftUp = hardwareMap.get(CRServo.class, "LiftUp");

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



        FLDrive.setDirection(DcMotor.Direction.FORWARD);
        BLDrive.setDirection(DcMotor.Direction.FORWARD);
        FRDrive.setDirection(DcMotor.Direction.REVERSE);
        BRDrive.setDirection(DcMotor.Direction.REVERSE);

        VertLift.setDirection(DcMotor.Direction.FORWARD);

        webcam.setPipeline(pipeline);

        //Set OpMode Member's directions
        FLDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //EmptyPipeline pLine = new EmptyPipeline();
        //pLine.processFrame(isMat);


        double pastMotorPower = 0;



        telemetry.addData("Status", "Initialized");
    }

        @Override
    public void start() { //Code to run ONCE when the driver hits PLAY
        runtime.reset();
    }

    @Override
    public void loop() { //Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP

        HOW_LONG_MAX = Math.max(FLDrive.getCurrentPosition(), HOW_LONG_MAX);
//        webcam.getFrameBitmap(Bitmap.getPixel);

        //get dpad values for lift

        boolean dpad_up = gamepad1.dpad_up;
        boolean dpad_down = gamepad1.dpad_down;
        boolean dpad_left = gamepad1.dpad_left;
        boolean dpad_right = gamepad1.dpad_right;

//        if (dpad_up) {
//            LiftUp.setPosition(LiftUp.getPosition()+10);
//        } else if (dpad_down){
//            LiftUp.setPosition(LiftUp.getPosition()-10);
//        } else {
//            LiftUp.setPosition(LiftUp.getPosition());
//        }







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
        } else {
            VertLift.setPower(0);
        }

        telemetry.addData("Pixel: ", ""+ Arrays.toString(pipeline.getMiddlePixel()));
        telemetry.addData("Color: ", ""+pipeline.getColor());

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
