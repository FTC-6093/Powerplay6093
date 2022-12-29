package org.firstinspires.ftc.teamcode.Autonomous;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.EocvSim.ShapePipelineClean;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;
import java.util.Arrays;

@Autonomous(name = "Debug")
public class EmmyDebugging extends LinearOpMode {
    // create all debugging objects
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor FLDrive = null;
    private DcMotor FRDrive = null;
    private DcMotor BLDrive = null;
    private DcMotor BRDrive = null;
    private OpenCvWebcam webcam = null;
    private ShapePipelineClean pipeline = new ShapePipelineClean();
//    private BNO055IMU imu;

    // we need to extract this into a config file somehow
    final double WHEEL_DIAMETER = 3.875;
    final double TICKS_PER_ROTATION = 1120;
    final double PIE = 3.14159;
    final double TICKS_PER_INCH = TICKS_PER_ROTATION/(WHEEL_DIAMETER * PIE);

    // telemetry.update() clears all current telemetry
    // we make an list of all the telemetry we want to add
    ArrayList<String> teleKeys;
    ArrayList<String> teleValues;

    private void updateTelemetry() {
        // short circuit if the arrays are not initialized
        if (teleKeys == null || teleValues == null) {
            return;
        }
        assert teleKeys.size() == teleValues.size(): "Should be the same size";
        for (int i = 0; i < teleKeys.size(); i++) {
            telemetry.addData(teleKeys.get(i), teleValues.get(i));
        }
        telemetry.update();
    }

    private void addTelemetry(String key, String value) {
        // short circuit if the arrays are not initialized
        if (teleKeys == null || teleValues == null) {
            return;
        }
        if (teleKeys.contains(key)) {
            teleValues.set(teleKeys.indexOf(key), value);
        } else {
            teleKeys.add(key);
            teleValues.add(value);
        }
    }

    @Override
    public void runOpMode() {
        telemetry.addData("Status: ", "Initializing Motors");
        telemetry.update();

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

        telemetry.addData("Status: ", "Initializing Webcam");
        telemetry.update();

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
//        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
//            @Override
//            public void onOpened() {
//                webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
//            }
//            @Override
//            public void onError(int errorCode) {
//            }
//        });
        webcam.openCameraDevice();
        webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
        webcam.setPipeline(pipeline); //Actually set the pipeline??? lez go

        telemetry.addData("Status: ", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // drive in square if a pressed
            if (gamepad1.a) {
                // driveStraight doesn't block, async
                // same with strafing
                driveStraight(12, 0.75);
                waitForMotors();
                sleep(2000);

                strafeLeft(12, 0.75);
                waitForMotors();
                sleep(2000);

                driveStraight(-12, 0.75);
                waitForMotors();
                sleep(2000);

                strafeRight(12, 0.75);
                waitForMotors();
                sleep(2000);
            }

            // follow dpad
            {
                if (gamepad1.dpad_up) {
                    driveStraight(12, 0.75);
                    waitForMotors();
                } else if (gamepad1.dpad_down) {
                    driveStraight(12, 0.75);
                    waitForMotors();
                } else if (gamepad1.dpad_left) {
                    strafeLeft(12, 0.75);
                    waitForMotors();
                } else if (gamepad1.dpad_right) {
                    strafeRight(12, 0.75);
                    waitForMotors();
                }
            }

            // just updating telemetry
            waitForMotors();
        }
    }

    private void waitForMotors() {
        do {
            // telemetry
            telemetry.addData("FLDrive: ", "" + FLDrive.getCurrentPosition());
            telemetry.addData("FRDrive: ", "" + FRDrive.getCurrentPosition());
            telemetry.addData("BLDrive: ", "" + BLDrive.getCurrentPosition());
            telemetry.addData("BRDrive: ", "" + BRDrive.getCurrentPosition());

            String shapeName;
            int currentShape = pipeline.shape;
            if (currentShape < 3 || currentShape > 5) {
                shapeName = "Error";
            } else if (currentShape == 3) {
                shapeName = "Triangle";
            } else if (currentShape == 4) {
                shapeName = "Square";
            } else { // has to be 5
                shapeName = "Pentagon";
            }

            telemetry.addData("Shape: ", "" + shapeName);
            telemetry.addData("Frame: ", "" + pipeline.framesProcessed);
//            telemetry.addData("Pixel: ", Arrays.toString(pipeline.getMiddlePixel()));

            telemetry.update();
        } while (
                FLDrive.isBusy() ||
                FRDrive.isBusy() ||
                BLDrive.isBusy() ||
                BRDrive.isBusy()
        );

    }


    private void driveStraight(int inches, double power) {
        int targetEncoderPosition = (int) TICKS_PER_INCH * inches;

        FLDrive.setTargetPosition(targetEncoderPosition);
        FRDrive.setTargetPosition(targetEncoderPosition);
        BLDrive.setTargetPosition(targetEncoderPosition);
        BRDrive.setTargetPosition(targetEncoderPosition);

        FLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        FLDrive.setPower(power);
        FRDrive.setPower(power);
        BLDrive.setPower(power);
        BRDrive.setPower(power);
    }

    private void strafeLeft(int inches, double power) {
        int targetEncoderPosition = (int) TICKS_PER_INCH * inches;

        // the only difference between strafing and driving is 2 opposite motors are flipped
        FLDrive.setTargetPosition(targetEncoderPosition);
        FRDrive.setTargetPosition(-targetEncoderPosition);
        BLDrive.setTargetPosition(-targetEncoderPosition);
        BRDrive.setTargetPosition(targetEncoderPosition);

        FLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        FLDrive.setPower(power);
        FRDrive.setPower(power);
        BLDrive.setPower(power);
        BRDrive.setPower(power);
    }

    private void strafeRight(int inches, double power) {
        int targetEncoderPosition = (int) TICKS_PER_INCH * inches;

        // the only difference between strafing and driving is 2 opposite motors are flipped
        FLDrive.setTargetPosition(-targetEncoderPosition);
        FRDrive.setTargetPosition(targetEncoderPosition);
        BLDrive.setTargetPosition(targetEncoderPosition);
        BRDrive.setTargetPosition(-targetEncoderPosition);

        FLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        FRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BLDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BRDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        FLDrive.setPower(power);
        FRDrive.setPower(power);
        BLDrive.setPower(power);
        BRDrive.setPower(power);
    }

}
