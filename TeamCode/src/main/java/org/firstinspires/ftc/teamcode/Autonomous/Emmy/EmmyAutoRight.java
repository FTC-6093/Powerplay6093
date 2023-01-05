package org.firstinspires.ftc.teamcode.Autonomous.Emmy;

public abstract class EmmyAutoRight extends EmmyAutoConfig {
    @Override
    public void runOpMode() {
        initializeMain();
        waitForStart();
        OpenClaw.setPower(1);
        raiseLift(20, 0.20);
//      servo(Positive) is Clockwise[From Front View]

//        Grab parking zone
        driveStraight(22, 0.40);
        int zone = getParkZone();

//        drive to the pole and raise lift
        driveStraight(13, 0.20);
        driveStraight(-5, 0.20);
        strafeLeft(29, 0.20);
        driveStraight(27, 0.20);
        strafeLeft(17.5, 0.20);
        raiseLift(170,1);
        driveStraight(-3.5,0.10);
        OpenClaw.setPower(-0.5);
        driveStraight(3.5, 0.10);
        VertLift.setPower(-0.50);

        char zoneChar;

//        drop cone on the pole and park
        switch (zone) {
            case 0:
                zoneChar = 'l';
                strafeRight(12,0.20);
                break;
            case 1:
                zoneChar = 'm';
                strafeRight(78,0.20);
                break;
            case 2:
                zoneChar = 'r';
                strafeRight(46,0.20);
                break;
            default:
                zoneChar = 'e';
                strafeRight(46,0.20);
                break;
        }

//        Drive forward into parking zone
        driveStraight(-6, 0.20);

        while (opModeIsActive()) {
            telemetry.addData("Zone: ", zoneChar);
            telemetry.update();
        }
    }
}
