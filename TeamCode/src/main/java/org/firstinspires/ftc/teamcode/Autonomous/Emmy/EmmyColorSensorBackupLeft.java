package org.firstinspires.ftc.teamcode.Autonomous.Emmy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "EmmyCSBackupL")
public class EmmyColorSensorBackupLeft extends EmmyAutoLeft {
    private final ElapsedTime runtime = new ElapsedTime();
    ColorSensor color;

    protected void initializeDetection() {
        color = hardwareMap.get(ColorSensor.class, "Color");
    }

    protected int getParkZone() {
        int outputZone;
        double IR = color.red();
        double IG = color.green();
        double IB = color.blue();
        if (IR > IG && IR > IB) {
            outputZone = 0;
        } else if (IB > IG && IB > IR) {
            outputZone = 1;
        } else if (IG > IB && IG > IR) {
            outputZone = 2;
        } else {
            outputZone = 10; // larger than 2 is error
        }
        return outputZone;
    }
}