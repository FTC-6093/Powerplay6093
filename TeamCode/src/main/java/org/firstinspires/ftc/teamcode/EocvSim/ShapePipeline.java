package org.firstinspires.ftc.teamcode.EocvSim;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
// import org.opencv.core.Core;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import java.util.ArrayList;
import java.util.Arrays;

public class ShapePipeline extends OpenCvPipeline {
    Mat hsv = new Mat();
    ArrayList<Mat> hsvsplit = new ArrayList<Mat>(){{
        add(new Mat());
        add(new Mat());
        add(new Mat());
        }};
    Mat b = new Mat();
    Point middle = new Point(360,480);
    Point seed = middle.clone();
    Size blur = new Size(5,5);
    Scalar fill = new Scalar(255);
//    Mat out = new Mat();
    Mat flat = new Mat();
    ArrayList<MatOfPoint> edges = new ArrayList<>();
    Mat filled = new Mat();
    MatOfPoint2f poly = new MatOfPoint2f();
    MatOfPoint2f thing = new MatOfPoint2f();

    int shape = 4;

    Telemetry telemetry;

    public ShapePipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    @Override
    public void init(Mat input) {
        // Executed before the first call to processFrame
    }

    private void fill_from_pixel(Mat input, Point seed, Mat out) {
        int h = input.height();
        int w = input.width();
        Mat mask = Mat.zeros(h+2, w+2, CvType.CV_8U);
        middle.x = (double) w/2;
        middle.y = (double) h/2;
        input.convertTo(flat, CvType.CV_8U);
        Imgproc.floodFill(flat, mask, seed, fill);
        Imgproc.threshold(flat, out, 254, 255, Imgproc.THRESH_BINARY);
        out.convertTo(flat, CvType.CV_8U);
//        return input;
    }

    private double dist_to_center(Point a) {
        return Math.sqrt(Math.pow(middle.x-a.x,2) + Math.pow(middle.y-a.y,2));
    }

    private double average_double(Double[] av) {
        double sum = 0;
        for (double value : av) {
            sum += value;
        }
        return sum / av.length;
    }

    private int index_of_min(Double[] ma) {
        int index = 0;
        double min = ma[index];

        for (int i = 1; i < ma.length; i++){
            if (ma[i] <= min){
                min = ma[i];
                index = i;
            }
        }
        return index;
    }

    private Point average_point(Point[] pa) {
        Double[] xcoords = new Double[pa.length];
        Double[] ycoords = new Double[pa.length];
        for (int i = 0; i < pa.length; i++) {
            xcoords[i] = pa[i].x;
            ycoords[i] = pa[i].y;
        }
        return new Point(average_double(xcoords), average_double(ycoords));
    }

    @Override
    public Mat processFrame(Mat input) {
        // Executed every time a new frame is dispatched
        Imgproc.cvtColor(input,hsv,Imgproc.COLOR_RGB2HSV);
        org.opencv.core.Core.split(hsv,hsvsplit);

        Imgproc.Canny(hsvsplit.get(2), b, 250, 300);
        Imgproc.blur(b, hsv, blur);
        filled = hsv.clone();
        fill_from_pixel(hsv, middle, b);

        Imgproc.findContours(b, edges, hsv, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        ArrayList<Double> average_edges = new ArrayList<>();
        for (MatOfPoint edge:edges) {
            ArrayList<Double> edge_average_dist = new ArrayList<>();
            for (Point point :
                    edge.toArray()) {
                edge_average_dist.add(dist_to_center(point));
            }
            Double[] dblarraycast = new Double[edge_average_dist.size()];
            dblarraycast = edge_average_dist.toArray(dblarraycast);
            average_edges.add(average_double(dblarraycast));
        }
        Double[] average_edge_array = new Double[average_edges.size()];
        seed = average_point(edges.get(index_of_min(average_edges.toArray(average_edge_array))).toArray());
        fill_from_pixel(filled, seed, b);
        Imgproc.findContours(b, edges, hsv, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        thing.fromArray(edges.get(0).toArray());
        Imgproc.approxPolyDP(thing, poly, 10, true);

        telemetry.addData("poly", ""+Arrays.toString(poly.toArray()));
        shape = poly.toArray().length;
        telemetry.addData("shape", ""+shape);

        telemetry.update();
        return b;
    }

    public int getShape() {
        return shape;
    }

    @Override
    public void onViewportTapped() {
        // Executed when the image display is clicked by the mouse or tapped
        // This method is executed from the UI thread, so be careful to not
        // perform any sort heavy processing here! Your app might hang otherwise
    }

}