package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.Arrays;

@Autonomous(name = "Auto Green")
@Disabled
public class EmmyAutoGreen extends LinearOpMode {
    // Declare OpMode members. (attributes of OP mode)
    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotor FLDrive = null;
    private DcMotor FRDrive = null;
    private DcMotor BLDrive = null;
    private DcMotor BRDrive = null;
    final double WHEEL_DIAMETER = 3.875;
    final double TICKS_PER_ROTATION = 1120 / 2;
    final double PIE = 3.14159;
    final double TICKS_PER_INCH = TICKS_PER_ROTATION/(WHEEL_DIAMETER * PIE);

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        FLDrive = hardwareMap.get(DcMotor.class, "FLDrive");
        FRDrive = hardwareMap.get(DcMotor.class, "FRDrive");
        BLDrive = hardwareMap.get(DcMotor.class, "BLDrive");
        BRDrive = hardwareMap.get(DcMotor.class, "BRDrive");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        FLDrive.setDirection(DcMotor.Direction.FORWARD);
        FRDrive.setDirection(DcMotor.Direction.REVERSE);
        BLDrive.setDirection(DcMotor.Direction.FORWARD);
        BRDrive.setDirection(DcMotor.Direction.REVERSE);

        FLDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the game to start (driver presses PLAY)
        runtime.reset();

        driveStraight(8.5, 0.75/2);
        driveStraight(12, 0.75/2);


        //Jaren, run code here
        //pipeline.getColor() will return
        //'r' for red, 'b' for blue, 'g' for green, 'e' if not pointing at cone, 'a' if something went wrong in config

//        sleep(500);
//
//        driveStraight(12,0.75);
//        int i = 0;
//        char color = pipeline.getColor();
//        while (i < 5000){
//            i++;
//            switch (color) {
//                case 'r':
//                    driveStraight(12, 0.75);
//                    strafeLeft(24, 0.75);
//                    i = 5001;
//                    break;
//                case 'g':
//                    driveStraight(12, 0.75);
//                    i = 5001;
//                    break;
//                case 'b':
//                    driveStraight(12, 0.75);
//                    strafeRight(24, 0.75);
//                    i = 5001;
//                    break;
//                case 'e':
//                case 'a':
//                    sleep(2000);
//                    break;
                //                case 'e':
//                    telemetry.addData("Can't find cone","");
//                    telemetry.update();
//                    break;
//                case 'a':
//                    telemetry.addData("Camera config issue", "");
//                    telemetry.update();
//                    break;
//                default:
//                    telemetry.addData("Shouldn't be here", "");
//                    telemetry.update();
//                    break;
//            }
//            color = pipeline.getColor();
//        }
//
//        telemetry.addData("Color: ", ""+color);
//        telemetry.addData("Middle pixel: ", ""+ Arrays.toString(pipeline.getMiddlePixel()));
//        telemetry.update();
//
//        while (true) {}
//


        }





        //snip ftc code
        /*
        //This is were we put the code we want
        IMUTurn(360, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        sleep(1000);
        IMUTurn(90, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        sleep(1000);
        IMUTurn(-90, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        sleep(1000);
        //This section is if we are the robot closer to the storage unit
        IMUTurn(-90, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        driveStraight(25, 0.5, FLDrive, FRDrive, BLDrive, BRDrive);
        //Use a motor to return duck from carousel here
        sleep(1000);
        IMUTurn(90, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        driveStraight(10, 0.5, FLDrive, FRDrive, BLDrive, BRDrive);
        driveStraight(-12, -0.5, FLDrive, FRDrive, BLDrive, BRDrive);
        IMUTurn(90, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        driveStraight(120, 0.9, FLDrive, FRDrive, BLDrive, BRDrive);

        //This part is if we are the robot nearest to the warehouse and the other team has working auto
        driveStraight(2, 0.5, FLDrive, FRDrive, BLDrive, BRDrive);
        sleep(5000);
        IMUTurn(-83.5, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        driveStraight(60, 0.5, FLDrive, FRDrive, BLDrive, BRDrive);
        //Use a motor to return duck from carousel here
        sleep(1000);
        IMUTurn(83.5, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        driveStraight(10, 0.5, FLDrive, FRDrive, BLDrive, BRDrive);
        driveStraight(-5, -0.5, FLDrive, FRDrive, BLDrive, BRDrive);
        IMUTurn(83.5, .5, FLDrive, FRDrive, BLDrive, BRDrive);
        driveStraight(50, 0.9, FLDrive, FRDrive, BLDrive, BRDrive);
        */


        //sleep(5000);


//    public void initializeIMU() {
//        this.imu = this.hardwareMap.get(BNO055IMU.class, "imu");
//        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
//        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
//        this.imu.initialize(parameters);
//
//        if(!this.imu.isGyroCalibrated()) {
//            this.sleep(50);
//        }
//    }

    //snip old IMUTurn
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
//                    }*/
//    public void IMUTurn (double angle, double power, DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight){
//
//        double startAngle = this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZXY, AngleUnit.DEGREES).firstAngle;
//        double robotTurnedAngle = 0;
//
//        angle = angle * .9; //incrementation to account for drift
//
//        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//        while(this.opModeIsActive() &&
//                Math.abs(robotTurnedAngle) < Math.abs(angle)) {
//            robotTurnedAngle = this.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle-startAngle;
//
//            if((Math.abs(robotTurnedAngle)) >= (Math.abs(angle)))
//            {
//                frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//                frontLeft.setPower(0);
//                frontRight.setPower(0);
//                backLeft.setPower(0);
//                backRight.setPower(0);
//                break;
//            }
//            if(robotTurnedAngle>180) {
//                robotTurnedAngle = robotTurnedAngle - 360;
//            }
//
//            if(robotTurnedAngle<-180) {
//                robotTurnedAngle = robotTurnedAngle + 360;
//            }
//
//            if(angle<0) {
//                frontLeft.setPower(-power);
//                frontRight.setPower(power);
//                backLeft.setPower(-power);
//                backRight.setPower(power);
//
//            }
//
//            else{
//                frontLeft.setPower(power);
//                frontRight.setPower(-power);
//                backLeft.setPower(power);
//                backRight.setPower(-power);
//            }
//        }
//    }

    public void driveStraight(double inch, double power) {
        //encoder's resolution: 537.6 in
        //Going straight constant: 42.78
        // final double ticksPerInch = 95.94  ;


        // only strafing needs doubling ticks per inch
        final double ticksPerInch = TICKS_PER_INCH;

        // correct for weight imbalances
        // note that the directions are not set yet
        final double directionBias = 0;

        //Reset encoder positions
        FLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        BRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        int tickNeeded = (int) (ticksPerInch * inch);

        int FlPosition;
        int FrPosition;
        int BlPosition;
        int BrPosition;

        //How many ticks the motor needs to move
        FLDrive.setTargetPosition(tickNeeded);
        FRDrive.setTargetPosition(tickNeeded);
        BLDrive.setTargetPosition(tickNeeded);
        BRDrive.setTargetPosition(tickNeeded);

        //Changes what information we send to the motors.
        FLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Set the power value for each motor
        FLDrive.setPower(power-directionBias);
        FRDrive.setPower(power+directionBias);
        BLDrive.setPower(power-directionBias);
        BRDrive.setPower(power+directionBias);


        while (this.opModeIsActive() && (FRDrive.isBusy() || BLDrive.isBusy())) {
            FlPosition = FLDrive.getCurrentPosition();
            FrPosition = FRDrive.getCurrentPosition();
            BlPosition = BLDrive.getCurrentPosition();
            BrPosition = BRDrive.getCurrentPosition();
//            We are just waiting until the motors reach the position (based on the ticks passed)
             telemetry.addData("Fl Position", ""+FlPosition);
            telemetry.addData("Fr Position", ""+FrPosition);
            telemetry.addData("Bl Position", ""+BlPosition);
            telemetry.addData("Br Position", ""+BrPosition);
             telemetry.addData("Ticks Needed", ""+tickNeeded);
            telemetry.addData("Success!", null);

            telemetry.update();
        }
        //When the motors have passed the required ticks, stop each motor
        FLDrive.setPower(0);
        FRDrive.setPower(0);
        BLDrive.setPower(0);
        BRDrive.setPower(0);
    }

//    public void strafeRight(double inch, double power) {
//        //encoder's resolution: 537.6 in
//        //Going straight constant: 42.78
//        // final double ticksPerInch = 95.94  ;
//
//
//        // driving is fine, strafing needs double
//        final double ticksPerInch = TICKS_PER_INCH * 1;
//
//        // correct for weight imbalances
//        // note that the directions are not set yet
//        final double directionBias = 0;
//
//        //Reset encoder positions
//        FLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        FRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        BLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        BRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        int tickNeeded = (int) (ticksPerInch * inch);
//
//        int FlPosition;
//        int FrPosition;
//        int BlPosition;
//        int BrPosition;
//
//        //How many ticks the motor needs to move
//        FLDrive.setTargetPosition(tickNeeded);
//        FRDrive.setTargetPosition(-tickNeeded);
//        BLDrive.setTargetPosition(-tickNeeded);
//        BRDrive.setTargetPosition(tickNeeded);
//
//        //Changes what information we send to the motors.
//        FLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        FRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        BLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        BRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        //Set the power value for each motor
//        FLDrive.setPower(power-directionBias);
//        FRDrive.setPower(power+directionBias);
//        BLDrive.setPower(power-directionBias);
//        BRDrive.setPower(power+directionBias);
//
//
//        while (this.opModeIsActive() && (FLDrive.isBusy() || FRDrive.isBusy() || BLDrive.isBusy() || BRDrive.isBusy())) {
//            FlPosition = FLDrive.getCurrentPosition();
//            FrPosition = FRDrive.getCurrentPosition();
//            BlPosition = BLDrive.getCurrentPosition();
//            BrPosition = BRDrive.getCurrentPosition();
////            We are just waiting until the motors reach the position (based on the ticks passed)
//            telemetry.addData("Fl Position", ""+FlPosition);
//            telemetry.addData("Fr Position", ""+FrPosition);
//            telemetry.addData("Bl Position", ""+BlPosition);
//            telemetry.addData("Br Position", ""+BrPosition);
//            telemetry.addData("Ticks Needed", ""+tickNeeded);
//            telemetry.addData("Success!", null);
//
//            telemetry.update();
//        }
//        //When the motors have passed the required ticks, stop each motor
//        FLDrive.setPower(0);
//        FRDrive.setPower(0);
//        BLDrive.setPower(0);
//        BRDrive.setPower(0);
//    }

//    public void strafeLeft(double inch, double power) {
//        //encoder's resolution: 537.6 in
//        //Going straight constant: 42.78
//        // final double ticksPerInch = 95.94  ;
//
//
//        // driving is fine, strafing needs double
//        final double ticksPerInch = TICKS_PER_INCH * 1;
//
//        // correct for weight imbalances
//        // note that the directions are not set yet
//        final double directionBias = 0;
//
//        //Reset encoder positions
//        FLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        FRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        BLDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        BRDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        int tickNeeded = (int) (ticksPerInch * inch);
//
//        int FlPosition;
//        int FrPosition;
//        int BlPosition;
//        int BrPosition;
//
//        //How many ticks the motor needs to move
//        FLDrive.setTargetPosition(-tickNeeded);
//        FRDrive.setTargetPosition(tickNeeded);
//        BLDrive.setTargetPosition(tickNeeded);
//        BRDrive.setTargetPosition(-tickNeeded);
//
//        //Changes what information we send to the motors.
//        FLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        FRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        BLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        BRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        //Set the power value for each motor
//        FLDrive.setPower(power-directionBias);
//        FRDrive.setPower(power+directionBias);
//        BLDrive.setPower(power-directionBias);
//        BRDrive.setPower(power+directionBias);
//
//
//        while (this.opModeIsActive() && (FLDrive.isBusy() || FRDrive.isBusy() || BLDrive.isBusy() || BRDrive.isBusy())) {
//            FlPosition = FLDrive.getCurrentPosition();
//            FrPosition = FRDrive.getCurrentPosition();
//            BlPosition = BLDrive.getCurrentPosition();
//            BrPosition = BRDrive.getCurrentPosition();
////            We are just waiting until the motors reach the position (based on the ticks passed)
//            telemetry.addData("Fl Position", ""+FlPosition);
//            telemetry.addData("Fr Position", ""+FrPosition);
//            telemetry.addData("Bl Position", ""+BlPosition);
//            telemetry.addData("Br Position", ""+BrPosition);
//            telemetry.addData("Ticks Needed", ""+tickNeeded);
//            telemetry.addData("Success!", null);
//
//            telemetry.update();
//        }
//        //When the motors have passed the required ticks, stop each motor
//        FLDrive.setPower(0);
//        FRDrive.setPower(0);
//        BLDrive.setPower(0);
//        BRDrive.setPower(0);
//    }
}
