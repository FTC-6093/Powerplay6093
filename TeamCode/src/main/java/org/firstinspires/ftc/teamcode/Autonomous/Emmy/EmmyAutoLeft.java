package org.firstinspires.ftc.teamcode.Autonomous.Emmy;

public abstract class EmmyAutoLeft extends EmmyAutoConfig {
    @Override
    public void runOpMode() {
        initializeMain();
        waitForStart();
        OpenClaw.setPower(1);
        raiseLift(16, 0.20);
//      servo(Positive) is Clockwise[From Front View]

//        Grab parking zone
        driveStraight(22, 0.40);
        int zone = getParkZone();

//        Drop cone on the pole
        driveStraight(13, 0.20);
        driveStraight(-5, 0.20);
        strafeRight(29, 0.20);
        driveStraight(54, 0.20);
        strafeLeft(15.5, 0.20);
        raiseLift(170,1);
        driveStraight(-3.5,0.10);
        OpenClaw.setPower(-0.5);
        driveStraight(3.5, 0.10);
        raiseLift(-186, 0.20);

//        Defaults zone to middle in case there are detection issues.
        char zoneChar;
        switch (zone) {
            case 0:
                zoneChar = 'l';
                strafeLeft(54,0.20);
                break;
            case 1:
                zoneChar = 'm';
                strafeRight(17,0.20);
                break;
            case 2:
                zoneChar = 'r';
                strafeLeft(16,0.20);
                break;
            default:
                zoneChar = 'e';
                strafeLeft(16,0.20);
                break;
        }

//        Drive forward into parking zone
        driveStraight(-27,0.20);


        while (opModeIsActive()) {
            telemetry.addData("Zone: ", zoneChar);
            telemetry.update();
        }
    }
}
