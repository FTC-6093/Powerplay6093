package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "EmmyCSBackupL")
public class EmmyCSBackupL extends LinearOpMode {
    // Declare OpMode members. (attributes of OP mode)

    // Troy wuz here
    // yo mama 100
    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotor FLDrive = null;
    private DcMotor FRDrive = null;
    private DcMotor BLDrive = null;
    private DcMotor BRDrive = null;
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

        waitForStart();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        FLDrive = hardwareMap.get(DcMotor.class, "FLDrive");
        FRDrive = hardwareMap.get(DcMotor.class, "FRDrive");
        BLDrive = hardwareMap.get(DcMotor.class, "BLDrive");
        BRDrive = hardwareMap.get(DcMotor.class, "BRDrive");
        color = hardwareMap.get(ColorSensor.class, "Color");
        VertLift  = hardwareMap.get(DcMotor.class, "VertLift");
        OpenClaw = hardwareMap.get(CRServo.class, "OpenClaw");

// Bad words
        // REALLY BAD WORDS
        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        FLDrive.setDirection(DcMotor.Direction.FORWARD);
        FRDrive.setDirection(DcMotor.Direction.REVERSE);
        BLDrive.setDirection(DcMotor.Direction.FORWARD);
        BRDrive.setDirection(DcMotor.Direction.REVERSE);
        VertLift.setDirection(DcMotor.Direction.FORWARD);



//        VertLift.setDirection(DcMotor.Direction.FORWARD);


        FLDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        VertLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Wait for the game to start (driver presses PLAY)
        OpenClaw.setPower(1);
        LiftExtension(16, 0.20);
//      servo(Positive) is Clockwise[From Front View]
        driveStraight(22, 0.40);

        double IR = color.red();
        double IG = color.green();
        double IB = color.blue();
        // Defaults color to green in case there are color sensor issues.
        char colorChar = 'g';
        if(IR > IG && IR > IB) {
            colorChar = 'r';
            driveStraight(13, 0.20);
            driveStraight(-5, 0.20);
            strafeRight(29, 0.20);
            driveStraight(29, 0.20);
            strafeRight(19.5, 0.20);
            LiftExtension(170,1);
            driveStraight(-1.5,0.10);
            OpenClaw.setPower(-0.5);
            driveStraight(2, 0.10);
            VertLift.setPower(-0.20);
            strafeLeft(65,0.20);
            driveStraight(-6,0.20);

        }
        else if(IB > IR && IB > IG){
            colorChar = 'b';
            driveStraight(13, 0.20);
            driveStraight(-5, 0.20);
            strafeRight(29, 0.20);
            driveStraight(29, 0.20);
            strafeRight(19.5, 0.20);
            LiftExtension(170,1);
            driveStraight(-1.5,0.10);
            OpenClaw.setPower(-0.5);
            driveStraight(2, 0.10);
            VertLift.setPower(-0.20);
            strafeLeft(16,0.20);
            driveStraight(-5, 0.20);

        }
        else {
            colorChar = 'g';
            driveStraight(13, 0.20);
            driveStraight(-5, 0.20);
            strafeRight(29, 0.20);
            driveStraight(29, 0.20);
            strafeRight(19.5, 0.20);
            LiftExtension(170,1);
            driveStraight(-1.5,0.10);
            OpenClaw.setPower(-0.5);
            driveStraight(2, 0.10);
            VertLift.setPower(-0.20);
            strafeLeft(50,0.20);
            driveStraight(-6, 0.20);
        }

        while (opModeIsActive()) {
            telemetry.addData("Red", color.red());
            telemetry.addData("Green", color.green());
            telemetry.addData("Blue", color.blue());
            telemetry.addData("Color", colorChar);
            telemetry.update();
        }
// Sussy amogus
// am crewmate :)
//        switch(colorChar) {
//            case 'r':
//                strafeLeft(24, 0.20);
//            case 'g':
//                driveStraight(10, 0.20);
//            case 'b':
//                strafeRight(24,0.20);
//        }

        telemetry.update();

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
// youre a programmer. nerd.
// Get rekt
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
            } // Imagine coding
            // troy likes kids
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


        while (this.opModeIsActive() && (FLDrive.isBusy() || FRDrive.isBusy() || BLDrive.isBusy() || BRDrive.isBusy())) {
            FlPosition = FLDrive.getCurrentPosition();
            FrPosition = FRDrive.getCurrentPosition();
            BlPosition = BLDrive.getCurrentPosition();
            BrPosition = BRDrive.getCurrentPosition();
//            We are just waiting until the motors reach the position (based on the ticks passed)
//            telemetry.addData("Fl Position", ""+FlPosition);
//            telemetry.addData("Fr Position", ""+FrPosition);
//            telemetry.addData("Bl Position", ""+BlPosition);
//            telemetry.addData("Br Position", ""+BrPosition);
//            telemetry.addData("Ticks Needed", ""+tickNeeded);
//            telemetry.addData("Success!", null);

            telemetry.update();
        } // When you hit the griddy but the griddy hits back
        // then I HIT YOU
        //When the motors have passed the required ticks, stop each motor
        FLDrive.setPower(0);
        FRDrive.setPower(0);
        BLDrive.setPower(0);
        BRDrive.setPower(0);
    }

    public void strafeRight(double inch, double power) {
        //encoder's resolution: 537.6 in
        //Going straight constant: 42.78
        // final double ticksPerInch = 95.94  ;


        // driving is fine, strafing needs double
        final double ticksPerInch = TICKS_PER_INCH * 1;

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
        FRDrive.setTargetPosition(-tickNeeded);
        BLDrive.setTargetPosition(-tickNeeded);
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


        while (this.opModeIsActive() && (FLDrive.isBusy() || FRDrive.isBusy() || BLDrive.isBusy() || BRDrive.isBusy())) {
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
        } // Jaren wus not here
        // im always here.
        //When the motors have passed the required ticks, stop each motor
        FLDrive.setPower(0);
        FRDrive.setPower(0);
        BLDrive.setPower(0);
        BRDrive.setPower(0);
    }


    public void LiftExtension(double inch, double power) {
        // extend da lift function
        //TicksPerRevolution = 103.6
        final double ticksPerInch = 16.5;
        double CurrentPOS = VertLift.getCurrentPosition();
        // Is now to the height of the claw
        int ticksNeeded = (int) (ticksPerInch * inch);
        int VertLiftPos;
        // I have no clue what this is for lol i stole it from radical <3
        final double directionBias = 0;

        // Reset the Encoder value
        VertLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Find The necessary tick value for motor
        VertLift.setTargetPosition(ticksNeeded);
        // Instill power limitations
        VertLift.setPower(power+directionBias);
        while(ticksNeeded > CurrentPOS) {
            VertLift.setPower(power);
            VertLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            CurrentPOS = VertLift.getCurrentPosition();
            telemetry.addData("TicksNeeded ", ticksNeeded);
            telemetry.addData("Current Position ", CurrentPOS);
            telemetry.update();
        }

        VertLift.setPower(0);
    }

    public void strafeLeft(double inch, double power) {
        //encoder's resolution: 537.6 in
        //Going straight constant: 42.78
        // final double ticksPerInch = 95.94  ;


        // driving is fine, strafing needs double
        final double ticksPerInch = TICKS_PER_INCH * 1;

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
        FLDrive.setTargetPosition(-tickNeeded);
        FRDrive.setTargetPosition(tickNeeded);
        BLDrive.setTargetPosition(tickNeeded);
        BRDrive.setTargetPosition(-tickNeeded);

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


        while (this.opModeIsActive() && (FLDrive.isBusy() || FRDrive.isBusy() || BLDrive.isBusy() || BRDrive.isBusy())) {
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
}

// Jyaren chan: totemo kawaii desu nee
// UWU OWO UVU OVO

// Yo Mamma so fat, her weight in pounds can't be stored as an integer.
