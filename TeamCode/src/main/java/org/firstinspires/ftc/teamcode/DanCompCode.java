package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="DanCompCode")
public class DanCompCode extends OpMode {
//    private DcMotor FLDrive, FRDrive, BLDrive, BRDrive;
    private DcMotor LiftMotor1, LiftMotor2, RackAndPinion;
    private Servo Claw;
    public final double MOTOR_MULTIPLIER = 0.8;
    public final double TICKS_PER_REVOLUTION = 537.6;

    public final double LIFT_TICK_THRESHOLD = 10;
//    this times amount of ticks off is how hard it corrects for
    public final double LIFT_CORRECTION_STRENGTH = 0.05;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing Motors");
        telemetry.update();

//        FLDrive = hardwareMap.get(DcMotor.class, "FLDrive");
//        FRDrive = hardwareMap.get(DcMotor.class, "FRDrive");
//        BLDrive = hardwareMap.get(DcMotor.class, "BLDrive");
//        BRDrive = hardwareMap.get(DcMotor.class, "BRDrive");

//        FLDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        FRDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        BLDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        BRDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

//        FLDrive.setDirection(DcMotor.Direction.FORWARD);
//        FRDrive.setDirection(DcMotor.Direction.FORWARD);
//        BLDrive.setDirection(DcMotor.Direction.FORWARD);
//        BRDrive.setDirection(DcMotor.Direction.FORWARD);

        telemetry.addData("Status", "Initializing Slide");
        telemetry.update();

        LiftMotor1 = hardwareMap.get(DcMotor.class, "liftMotor1");
        LiftMotor2 = hardwareMap.get(DcMotor.class, "liftMotor2");
        RackAndPinion = hardwareMap.get(DcMotor.class, "pinionMotor");

        LiftMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LiftMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RackAndPinion.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        LiftMotor1.setDirection(DcMotor.Direction.REVERSE);
        LiftMotor2.setDirection(DcMotor.Direction.FORWARD);
        RackAndPinion.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initializing Claw");
        telemetry.update();

        Claw = hardwareMap.get(Servo.class, "servo");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {
        final double y = gamepad1.left_stick_y;
        final double x = -gamepad1.left_stick_x;
        final double rx = gamepad1.right_stick_x;

        final double frontLeftPower;
        final double backLeftPower;
        final double frontRightPower;
        final double backRightPower;

//        Clamp motor values in a ratio if one of them tries to go faster than 1
        final double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        frontLeftPower = (y + x - rx) / denominator;
        backLeftPower = (y - x - rx) / denominator;
        frontRightPower = (y - x + rx) / denominator;
        backRightPower = (y + x + rx) / denominator;

//        FLDrive.setPower(frontLeftPower * MOTOR_MULTIPLIER);
//        FRDrive.setPower(frontRightPower * MOTOR_MULTIPLIER);
//        BLDrive.setPower(backLeftPower * MOTOR_MULTIPLIER);
//        BRDrive.setPower(backRightPower * MOTOR_MULTIPLIER);

        if (gamepad2.left_stick_y > 0) {
            setLiftPowerTo(0.25);
        } else if (gamepad2.left_stick_y < 0) {
            setLiftPowerTo(-0.25);
        } else {
            setLiftPowerTo(0);
        }

        RackAndPinion.setPower(gamepad2.right_stick_y);

        if (gamepad2.a) {
            Claw.setPosition(0.7);
        } else if (gamepad2.b) {
            Claw.setPosition(1.0);
        }

        telemetry.addData("Servo", ""+Claw.getPosition());

        telemetry.update();
    }

    private void setLiftPowerTo(double power) {
        final double ticksOff = (double) (LiftMotor1.getCurrentPosition() - LiftMotor2.getCurrentPosition());
//        diff between ticks off and correction offset, 0 if ticksOff < Lift_tick_threshold
        final double correctionOffset = Math.copySign(Math.max(Math.abs(ticksOff) - LIFT_TICK_THRESHOLD, 0), ticksOff);
//
        LiftMotor1.setPower(power - (correctionOffset * LIFT_CORRECTION_STRENGTH));
        LiftMotor2.setPower(power + (correctionOffset * LIFT_CORRECTION_STRENGTH));
//        LiftMotor1.setPower(power);
//        LiftMotor2.setPower(power);

    }
}
