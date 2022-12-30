package org.firstinspires.ftc.teamcode.Autonomous.Emmy;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.EocvSim.ShapePipelineClean;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@Autonomous(name = "EmmyShapeR")
public class EmmyShapeDetectionRight extends EmmyAutoRight {
    private final ElapsedTime runtime = new ElapsedTime();
    OpenCvWebcam webcam;
    ShapePipelineClean pipeline = new ShapePipelineClean();

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
        return pipeline.shape - 3;
    }
}
