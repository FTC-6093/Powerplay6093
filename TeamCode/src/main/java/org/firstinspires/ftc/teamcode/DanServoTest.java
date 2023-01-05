package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.app.ActionBar;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.openftc.easyopencv.OpenCvWebcam;

@TeleOp(name = "DanServoTest")
public class DanServoTest extends OpMode {

    public Servo claw = null; // servo exists to the robot now
    public DcMotor pinionMotor = null; // servo exists to the robot now
    public DcMotor liftMotor = null; // servo exists to the robot now

    double servoPosition;
    double pinionMotorPower;
    double liftMotorPower;

    // Isaiah wuz here joe mama

    public void init(){

    claw = hardwareMap.get(Servo.class, "servo"); // assign this claw to the correct name in the config
    pinionMotor = hardwareMap.get(DcMotor.class, "pinionMotor"); // assign this claw to the correct name in the config
    liftMotor = hardwareMap.get(DcMotor.class, "liftMotor");

    final double CLAW_HOLD = 0.5; // hold position of servo
    final double CLAW_RELEASE = 0.0; // release position of the servo

    servoPosition = 0;
    pinionMotorPower = 0;
    liftMotorPower = 0;
    }

    @Override
    public void loop() {
        telemetry.addData("claw location: ", claw.getPosition());
        telemetry.addData("desired location: ", servoPosition);
        telemetry.update();

        if(gamepad1.a){
            servoPosition = .15;
        }

        if(gamepad1.b){
            servoPosition = 0.4;
        }

        pinionMotorPower = -gamepad1.left_stick_y;
        liftMotorPower = gamepad1.right_stick_y;

        claw.setPosition(servoPosition);
        pinionMotor.setPower(pinionMotorPower);
        liftMotor.setPower(liftMotorPower);
    }
}
