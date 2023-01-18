package org.firstinspires.ftc.teamcode.Autonomous.Emmy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "EmmyCSPark")
public class EmmyColorSensorPark extends EmmyPark {
    private final ElapsedTime runtime = new ElapsedTime();
    ColorSensor color;

    protected void initializeDetection() {
        color = hardwareMap.get(ColorSensor.class, "Color");
    }

    protected int getParkZone() {
        int outputZone;
        final double IR = color.red();
        final double IG = color.green();
        final double IB = color.blue();

        telemetry.addData("IR", ""+IR);
        telemetry.addData("IG", ""+IG);
        telemetry.addData("IB", ""+IB);

        telemetry.update();

        if (IR > IG && IR > IB) {
            outputZone = 0;
        } else if (IB > IG && IB > IR) {
            outputZone = 2;
        } else if (IG > IB && IG > IR) {
            outputZone = 1;
        } else {
            outputZone = 10; // larger than 2 is error
        }
        return outputZone;
    }
}