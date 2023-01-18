package org.firstinspires.ftc.teamcode.Autonomous.Emmy;

public abstract class EmmyPark extends EmmyAutoConfig {
    @Override
    public void runOpMode() {
        initializeMain();
        waitForStart();
        driveStraight(22, 0.40);

        sleep(1000);

        driveStraight(18, 0.40);

        sleep(1000);

        int zone = getParkZone();
        driveStraight(4,0.40);
        driveStraight(-2, 0.40);

//        Defaults zone to middle in case there are detection issues.
        char zoneChar;
        switch (zone) {
            case 0:
                zoneChar = 'l';
                strafeLeft(46,0.20);
                break;
            case 1:
                zoneChar = 'm';
//                strafeRight(17,0.20);
                break;
            case 2:
                zoneChar = 'r';
                strafeRight(46,0.20);
                break;
            default:
                zoneChar = 'e';
//                strafeLeft(16,0.20);
                break;
        }

//        Drive forward into parking zone
        driveStraight(3,0.20);


        while (opModeIsActive()) {
            telemetry.addData("Zone: ", zoneChar);
            telemetry.update();
        }
    }
}
