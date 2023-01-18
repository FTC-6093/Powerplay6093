package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public abstract class DriveFunctions extends LinearOpMode {
    protected DcMotor FlDrive, FrDrive, BlDrive, BrDrive;
    protected BNO055IMU imu;

    protected final double WHEEL_DIAMETER;
    protected final double TICKS_PER_ROTATION;
    protected final double TICKS_PER_INCH;

    protected DriveFunctions(double wheel_diameter, double ticks_per_rotation) {
        WHEEL_DIAMETER = wheel_diameter;
        TICKS_PER_ROTATION = ticks_per_rotation;
        TICKS_PER_INCH = TICKS_PER_ROTATION / (WHEEL_DIAMETER * Math.PI);
    }

    protected void initializeMain() {
        telemetry.addData("Status: ", "Initializing Motors");
        telemetry.update(); initializeMotors();
        telemetry.addData("Status: ", "Initializing IMU");
        telemetry.update(); initializeIMU();
        telemetry.addData("Status: ", "Initializing Detector");
        telemetry.update(); initializeDetection();
        telemetry.addData("Status: ", "Initialized");
        telemetry.update();
    }

//    Driving functions
    protected abstract void initializeMotors();

    private void wheelMove(double distance, double power, int[] inverts) {
        FlDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BlDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BrDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int tickNeeded = (int) (distance * TICKS_PER_INCH);
        FlDrive.setTargetPosition(tickNeeded * inverts[0]);
        FrDrive.setTargetPosition(tickNeeded * inverts[1]);
        BlDrive.setTargetPosition(tickNeeded * inverts[2]);
        BrDrive.setTargetPosition(tickNeeded * inverts[3]);

        FlDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FrDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BlDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BrDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        FlDrive.setPower(power);
        FrDrive.setPower(power);
        BlDrive.setPower(power);
        BrDrive.setPower(power);

        while (this.opModeIsActive() && (
                FlDrive.isBusy()||
                FrDrive.isBusy()||
                BlDrive.isBusy()||
                BrDrive.isBusy()));

        FlDrive.setPower(0);
        FrDrive.setPower(0);
        BlDrive.setPower(0);
        BrDrive.setPower(0);
    }

    private final int[] straight = {-1,-1,-1,-1};
    protected void driveStraight(double distance, double power) {
        wheelMove(distance,power,straight);
    }
    private final int[] left = {-1,1,1,-1};
    protected void strafeLeft(double distance, double power) {
        wheelMove(distance,power,left);
    }
    private final int[] right = {1,-1,-1,1};
    protected void strafeRight(double distance, double power) {
        wheelMove(distance,power,right);
    }

    protected abstract void initializeIMU();

    private double getHeadingDegrees() {
        return this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
    }

    protected void IMUTurn (double angle, double power) {
        final double startAngle = getHeadingDegrees();

//        Note from Will: I have no idea what it means, but we should try it without first
        angle = angle * .9; // account for overshoot

        FlDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BlDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BrDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if (angle < 0) {
            FlDrive.setPower(-power);
            FrDrive.setPower(power);
            BlDrive.setPower(-power);
            BrDrive.setPower(power);
        } else {
            FlDrive.setPower(power);
            FrDrive.setPower(-power);
            BlDrive.setPower(power);
            BrDrive.setPower(-power);
        }

        while (this.opModeIsActive() &&
//        Wait while (wrapped difference in angle since start) is less than (desired angle change)
                Math.abs((getHeadingDegrees() - startAngle - 180) % 360 + 180) < Math.abs(angle));

        FlDrive.setPower(0);
        FrDrive.setPower(0);
        BlDrive.setPower(0);
        BrDrive.setPower(0);
    }

    protected abstract void initializeDetection();

    protected abstract int getParkZone(); // needs to return an int from 0-2 corresponding to the parking zone
}
