package org.firstinspires.ftc.teamcode.Autonomous.Emmy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.EocvSim.ColorPipeline;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "EmmyCamColorL")
public class EmmyCamColorDetectionLeft extends EmmyAutoLeft {
    private final ElapsedTime runtime = new ElapsedTime();
    OpenCvWebcam webcam;
    ColorPipeline pipeline = new ColorPipeline();

    @Override
    protected void initializeDetection() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        webcam.openCameraDevice();
        webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
        webcam.setPipeline(pipeline);
    }

    @Override
    protected int getParkZone() {
        int outputZone;
        switch (pipeline.getColor(60)) {
            case 'r': outputZone = 0; break;
            case 'g': outputZone = 1; break;
            case 'b': outputZone = 2; break;
            case 'e': outputZone = 10; break;// 10 and 20 get mapped to g or rechecked
            case 'a': outputZone = 20; break;
            default: outputZone = 30; break;
        }
        return outputZone;
    }
}
