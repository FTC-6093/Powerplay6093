package org.firstinspires.ftc.teamcode.Autonomous.Emmy;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Autonomous.DriveFunctions;

public abstract class EmmyAutoConfig extends DriveFunctions {
    DcMotor VertLift;
    CRServo OpenClaw;

    public EmmyAutoConfig() {
        super(3.875, 313);
    }

    @Override
    protected void initializeMotors() {
        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motors that run backwards when connected directly to the battery
        FlDrive = hardwareMap.get(DcMotor.class, "FLDrive");
        FrDrive = hardwareMap.get(DcMotor.class, "FRDrive");
        BlDrive = hardwareMap.get(DcMotor.class, "BLDrive");
        BrDrive = hardwareMap.get(DcMotor.class, "BRDrive");

        FlDrive.setDirection(DcMotor.Direction.FORWARD);
        FrDrive.setDirection(DcMotor.Direction.REVERSE);
        BlDrive.setDirection(DcMotor.Direction.FORWARD);
        BrDrive.setDirection(DcMotor.Direction.REVERSE);

        FlDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BlDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BrDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        VertLift = hardwareMap.get(DcMotor.class, "VertLift");
        VertLift.setDirection(DcMotor.Direction.FORWARD);
        VertLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        OpenClaw = hardwareMap.get(CRServo.class, "OpenClaw");
    }

    @Override
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

    protected final double LIFT_TICKS_PER_ROTATION = 103.6;
    protected final double LIFT_WHEEL_DIAMETER = 1.9986;
    protected final double LIFT_TICKS_PER_INCH = LIFT_TICKS_PER_ROTATION / (LIFT_WHEEL_DIAMETER * Math.PI);
    protected void raiseLift(double distance, double power) {
        VertLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int ticksNeeded = (int) (LIFT_TICKS_PER_INCH * distance);
        VertLift.setTargetPosition(ticksNeeded);

        VertLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        VertLift.setPower(power);

        while (this.opModeIsActive() && VertLift.isBusy()) {
            telemetry.addData("Ticks Needed: ", ticksNeeded);
            telemetry.addData("Current Position: ", VertLift.getCurrentPosition());
            telemetry.update();
        }

        VertLift.setPower(0);
    }
}
