package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public abstract class EmmyDriveFunctions extends LinearOpMode {
    protected DcMotor FlDrive, FrDrive, BlDrive, BrDrive;
    protected BNO055IMU imu;

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

    protected void initializeIMU() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu.initialize(parameters);

        if(!imu.isGyroCalibrated()) {
            this.sleep(50);
        }
    }

    private double getHeading() {
        return this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
    }

    protected void IMUTurn (double angle, double power) {
        final double startAngle = getHeading();

//        Note from Will: I have no idea what it means, but we should try it without
//        angle = angle * .9; //incrementation to account for drift

        FlDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BlDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BrDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        FlDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BlDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BrDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
                Math.abs((getHeading() - startAngle - 180) % 360 + 180) < Math.abs(angle));

        FlDrive.setPower(0);
        FrDrive.setPower(0);
        BlDrive.setPower(0);
        BrDrive.setPower(0);
    }
}
