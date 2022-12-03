package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp (name = "CompCodeFallback", group = "Iterative Opmode")
public class EmmyCompCodeFallback extends OpMode {

    //Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor BLDrive = null;
    private DcMotor FRDrive = null;
    private DcMotor FLDrive = null;
    private DcMotor BRDrive = null;
//    private BNO055IMU imu = null;
//    private DcMotor VertLift = null;
//    private CRServo OpenClaw = null;
//    private CRServo LiftUp = null;
    final double motorMultiplier = 0.65;


    @Override
    public void init() { //Code to run ONCE when the driver hits INIT
        telemetry.addData("Status", "Initializing");
        telemetry.update();

        BLDrive  = hardwareMap.get(DcMotor.class, "BLDrive");
        FRDrive  = hardwareMap.get(DcMotor.class, "FRDrive");
        FLDrive  = hardwareMap.get(DcMotor.class, "FLDrive");
        BRDrive  = hardwareMap.get(DcMotor.class, "BRDrive");
//        VertLift  = hardwareMap.get(DcMotor.class, "VertLift");

//        OpenClaw = hardwareMap.get(CRServo.class, "OpenClaw");
//        LiftUp = hardwareMap.get(CRServo.class, "LiftUp");

        BLDrive.setDirection(DcMotor.Direction.FORWARD);
        BRDrive.setDirection(DcMotor.Direction.REVERSE);
        FRDrive.setDirection(DcMotor.Direction.REVERSE);
        FLDrive.setDirection(DcMotor.Direction.FORWARD);
//        VertLift.setDirection(DcMotor.Direction.FORWARD);

//        OpenClaw.setDirection(CRServo.Direction.FORWARD);
//        LiftUp.setDirection(CRServo.Direction.FORWARD);


        // Retrieve the IMU from the hardware map
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        // Technically this is the default, however specifying it is clearer
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        // Without this, data retrieving from the IMU throws an exception
        imu.initialize(parameters);


        double pastMotorPower = 0;

        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }


    @Override
    public void start() { //Code to run ONCE when the driver hits PLAY
        runtime.reset();
    }


    @Override
    public void loop() { //Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP

        // get servos

//        boolean servoClose = gamepad2.dpad_left;
//        boolean servoOpen = gamepad2.dpad_right;
//
//        boolean tiltUp = gamepad2.dpad_up;
//        boolean tiltDown = gamepad2.dpad_down;


//        if (servoOpen) {
//            OpenClaw.setPower(0.5);
//        } else if (servoClose) {
//            OpenClaw.setPower(-0.5);
//        } else {
//            OpenClaw.setPower(0.85);
//        }
//
//        if (tiltUp) {
//            LiftUp.setPower(0.5);
//        } else if (tiltDown) {
//            LiftUp.setPower(-0.5);
//        } else {
//            LiftUp.setPower(0);
//        }

        // Retrieve lift values from controller

//        double botHeading;
//        if (imu != null) {
//            botHeading = -imu.getAngularOrientation().firstAngle;
//        } else {
//            botHeading = 0;
//        }

        //Retrieve driving values from controller
        double y = gamepad1.left_stick_y * .8; // Is reversed
        double x = gamepad1.left_stick_x * -.8;// Counteract imperfect strafing
        double rx = gamepad1.right_stick_x * .8;

//        double rotX = x * Math.cos(botHeading) - y * Math.sin(botHeading);
//        double rotY = x * Math.sin(botHeading) + y * Math.cos(botHeading);


        boolean up = (gamepad1.right_trigger > 0);
        boolean down = (gamepad1.left_trigger > 0);
        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when at least one is out
        // of the range [-1, 1]
        double frontLeftPower;
        double backLeftPower;
        double frontRightPower;
        double backRightPower;
        if (up) {
            frontLeftPower = -0.8;
            backLeftPower = 0.8;
            frontRightPower = 0.8;
            backRightPower = -0.8;
        } else if (down) {
            frontLeftPower = 0.8;
            backLeftPower = -0.8;
            frontRightPower = -0.8;
            backRightPower = 0.8;
        } else {
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            frontLeftPower = (y + x - rx) / denominator;
            backLeftPower = (y - x - rx) / denominator;
            frontRightPower = (y - x + rx) / denominator;
            backRightPower = (y + x + rx) / denominator;
        }

        FLDrive.setPower(frontLeftPower*motorMultiplier);
        FRDrive.setPower(frontRightPower*motorMultiplier);
        BLDrive.setPower(backLeftPower*motorMultiplier);
        BRDrive.setPower(backRightPower*motorMultiplier);

//        if (down && !up) {
//            VertLift.setPower(0.65);
//        } else if (!down && up){
//            VertLift.setPower(-0.65);
//        }else{
//            VertLift.setPower(0);
//        }

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
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
//        VertLift.setPower(0);
//        LiftUp.setPower(0);
//        OpenClaw.setPower(0);
        telemetry.addData("Status", "STOPPED");
    }
}

