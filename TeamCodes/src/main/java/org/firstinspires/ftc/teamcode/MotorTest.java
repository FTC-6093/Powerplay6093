package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp (name = "Motor Test", group = "Iterative Opmode")
public class MotorTest extends OpMode {

    //Declare OpMode members
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor BLDrive = null;
    private DcMotor FRDrive = null;
    private DcMotor FLDrive = null;
    private DcMotor BRDrive = null;


    @Override
    public void init() { //Code to run ONCE when the driver hits INIT

        telemetry.addData("Status", "Initializing");
        telemetry.update();

        BLDrive  = hardwareMap.get(DcMotor.class, "BLDrive");
        FRDrive  = hardwareMap.get(DcMotor.class, "FRDrive");
        FLDrive  = hardwareMap.get(DcMotor.class, "FLDrive");
        BRDrive  = hardwareMap.get(DcMotor.class, "BRDrive");



        BLDrive.setDirection(DcMotor.Direction.FORWARD);
        BRDrive.setDirection(DcMotor.Direction.REVERSE);
        FRDrive.setDirection(DcMotor.Direction.REVERSE);
        FLDrive.setDirection(DcMotor.Direction.FORWARD);





        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }


    @Override
    public void start() { //Code to run ONCE when the driver hits PLAY
        runtime.reset();
    }


    @Override
    public void loop() { //Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP

        // Retrieve lift values from controller

        //Retrieve driving values from controller
        double y = gamepad1.left_stick_y * .8; // Remember, this is reversed!
        double x = gamepad1.left_stick_x * .8; // Counteract imperfect strafing
        double rx = -gamepad1.right_stick_x * .8;

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



        /*

        if (Forward && !Reverse) {
            testMotor.setPower(0.65);
        } else if (!Forward && Reverse){
            testMotor.setPower(-0.65);
        }else{
            testMotor.setPower(0);
        }
        */

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        // telemetry.addData("Motors", "Forward (%f), Backward (%f)", motorForward, motorBackward);
        telemetry.update();
    }

    @Override
    public void stop() { //Code to run ONCE after the driver hits STOP
        telemetry.addData("Status", "STOPPED");
        FLDrive.setPower(0);
        FRDrive.setPower(0);
        BLDrive.setPower(0);
        BRDrive.setPower(0);

    }
}
