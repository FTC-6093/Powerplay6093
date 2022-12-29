package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "EmmyCSBackupR")
public class EmmyCSBackup extends EmmyDriveFunctions {
    // Declare OpMode members. (attributes of OP mode)

    // Troy wuz here
    // yo mama 100
    private final ElapsedTime runtime = new ElapsedTime();
//    private DcMotor FlDrive = null;
//    private DcMotor FRDrive = null;
//    private DcMotor BLDrive = null;
//    private DcMotor BRDrive = null;
    private DcMotor VertLift = null;
    private CRServo OpenClaw = null;
    OpenCvWebcam webcam = null;
    private BNO055IMU imu;
    final double WHEEL_DIAMETER = 3.875;
    final double TICKS_PER_ROTATION = 313;
    final double PIE = Math.PI;
    // Ticks per rotation / (Diameter * Pi)
    final double TICKS_PER_INCH = 36.6428571;
    final double LIFT_TICK_PER_REVOLUTION = 103.6;
    ColorSensor color;

    // Circumfrence / TicksPerInch
/*
    private static char checkForRGB(double r, double g, double b, double error) {
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
*/

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

//        initializeIMU();


        FlDrive = hardwareMap.get(DcMotor.class, "FLDrive");
        FrDrive = hardwareMap.get(DcMotor.class, "FLDrive");
        BlDrive = hardwareMap.get(DcMotor.class, "FLDrive");
        BrDrive = hardwareMap.get(DcMotor.class, "FLDrive");
        color = hardwareMap.get(ColorSensor.class, "Color");
        VertLift  = hardwareMap.get(DcMotor.class, "VertLift");
        OpenClaw = hardwareMap.get(CRServo.class, "OpenClaw");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motors that run backwards when connected directly to the battery
        FlDrive.setDirection(DcMotor.Direction.FORWARD);
        FrDrive.setDirection(DcMotor.Direction.REVERSE);
        BlDrive.setDirection(DcMotor.Direction.FORWARD);
        BrDrive.setDirection(DcMotor.Direction.REVERSE);
        VertLift.setDirection(DcMotor.Direction.FORWARD);

        FlDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BlDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BrDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        VertLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        OpenClaw.setPower(1);
        raiseLift(16, 0.20);
//      servo(Positive) is Clockwise[From Front View]

//        Grab cone colors
        driveStraight(22, 0.40);
        double IR = color.red();
        double IG = color.green();
        double IB = color.blue();

//        Drop cone on the pole
        driveStraight(13, 0.20);
        driveStraight(-5, 0.20);
        strafeLeft(29, 0.20);
        driveStraight(27, 0.20);
        strafeLeft(17.5, 0.20);
        raiseLift(170,1);
        driveStraight(-3.5,0.10);
        OpenClaw.setPower(-0.5);
        driveStraight(3.5, 0.10);
        VertLift.setPower(-0.20);

        // Defaults color to green in case there are color sensor issues.
        char colorChar;
        if(IR > IG && IR > IB) {
            colorChar = 'r';
            strafeRight(12,0.20);
        } else if(IB > IR && IB > IG){
            colorChar = 'b';
            strafeRight(78,0.20);
        } else {
            colorChar = 'g';
            strafeRight(46,0.20);
        }

//        Drive forward into zones
        driveStraight(-6, 0.20);

        while (opModeIsActive()) {
            telemetry.addData("Red", color.red());
            telemetry.addData("Green", color.green());
            telemetry.addData("Blue", color.blue());
            telemetry.addData("Color", colorChar);
            telemetry.update();
        }
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

    private double getHeading() {
        return this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
    }

    public void IMUTurn (double angle, double power) {
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
//        Wait while abs of wrapped difference in angle since start is less than the desired angle change
        Math.abs((getHeading() - startAngle - 180) % 360 + 180) < Math.abs(angle));

        FlDrive.setPower(0);
        FrDrive.setPower(0);
        BlDrive.setPower(0);
        BrDrive.setPower(0);
    }

    public void raiseLift(double inch, double power) {
        //TicksPerRevolution = 103.6
        // Jaren, this is bad coding practice. Less magic variables please.
        final double ticksPerInch = 16.5;
        // Is now to the height of the claw

        VertLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int ticksNeeded = (int) (ticksPerInch * inch);
        VertLift.setTargetPosition(ticksNeeded);

        VertLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        VertLift.setPower(power);

        while (this.opModeIsActive() && VertLift.isBusy()) {
            telemetry.addData("TicksNeeded ", ticksNeeded);
            telemetry.addData("Current Position ", VertLift.getCurrentPosition());
            telemetry.update();
        }

        VertLift.setPower(0);
    }
}

// Jaren chan: とてもかわいいですねえ。
// UWU OWO UVU OVO

// Yo Mamma so fat, her weight in pounds can't be stored as an integer.
