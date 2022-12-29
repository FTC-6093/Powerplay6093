package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public abstract class EmmyDriveFunctions extends LinearOpMode {
    protected DcMotor FlDrive, FrDrive, BlDrive, BrDrive;

    protected final double WHEEL_DIAMETER = 3.875;
    protected final double TICKS_PER_ROTATION = 313;
    // Pi = Math.PI
    // Ticks per rotation / (Diameter * Pi)
    protected final double TICKS_PER_INCH = TICKS_PER_ROTATION / (WHEEL_DIAMETER * Math.PI);

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

        while (this.opModeIsActive() && (FlDrive.isBusy()||FrDrive.isBusy()|| BlDrive.isBusy()||BrDrive.isBusy()));
    }

    private final int[] straight = {1,1,1,1};
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
}
