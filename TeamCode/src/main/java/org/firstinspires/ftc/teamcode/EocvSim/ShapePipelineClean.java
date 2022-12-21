package org.firstinspires.ftc.teamcode.EocvSim;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.Arrays;

public class ShapePipelineClean extends OpenCvPipeline {
//    helper functions for simple functionality
    private double dist_to_center(Point p) {
        return Math.sqrt(Math.pow(middle.x-p.x,2) + Math.pow(middle.y-p.y,2));
    }
    private double average_double(Double[] averageArray) {
        double sum = 0;
        for (double value : averageArray) {
            sum += value;
        }
        return sum / averageArray.length;
    }
    private int index_of_min(Double[] minArray) {
        int index = 0;
        double min = minArray[index];
        for (int i = 1; i < minArray.length; i++){
            if (minArray[i] <= min){
                min = minArray[i];
                index = i;
            }
        }
        return index;
    }
    private Point average_point(Point[] pointArray) {
        Double[] xCoords = new Double[pointArray.length];
        Double[] yCoords = new Double[pointArray.length];
        for (int i = 0; i < pointArray.length; i++) {
            xCoords[i] = pointArray[i].x;
            yCoords[i] = pointArray[i].y;
        }
        return new Point(average_double(xCoords), average_double(yCoords));
    }

//    how you add telemetry in EasyOpenCV-Sim, maybe remove all telemetry for real pipeline
    Telemetry telemetry;
    public ShapePipelineClean(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    ArrayList<Mat> hsvSplit = new ArrayList<Mat>(){{
        add(new Mat());
        add(new Mat());
        add(new Mat());
    }};

//    Flipping back and forth between 2 mat arrays to save memory
//    alternate procA, procB as src & dst
    Mat procA = new Mat();
    Mat procB = new Mat();

//    Middle gets reassigned later, so these can be arbitrary values
    Point middle = new Point(0,0);
    Point seed = middle.clone();

//    blur kernel size
    Size blur = new Size(3,3);

//    throwaway mat for unneeded values, used for hierarchy
    Mat trash = new Mat();

//    point arrays for edge detection
    MatOfPoint2f poly = new MatOfPoint2f();
    MatOfPoint2f highDefPoly = new MatOfPoint2f();
    ArrayList<MatOfPoint> edges;

    int framesProcessed = 0;
    int shape = 0;

//    filling in OpenCV is actually really inconvenient
    Scalar fill = new Scalar(255);
    Mat flat = new Mat();
    Mat mask = new Mat();
    private void fill_from_pixel(Mat input, Point seed, Mat out) {
        int h = input.height();
        int w = input.width();
        mask = Mat.zeros(h+2, w+2, CvType.CV_8U);
        input.convertTo(flat, CvType.CV_8U);
        Imgproc.floodFill(flat, mask, seed, fill);
        Imgproc.threshold(flat, out, 254, 255, Imgproc.THRESH_BINARY);
        out.convertTo(flat, CvType.CV_8U);
    }

//    was gonna write an extract contours function, but the WET principle is pretty important

//    executed every time a new frame is dispatched
    @Override
    public Mat processFrame(Mat input) {
//        sometimes, input has a depth of 4 for some reason
        if (input.depth() == 4) {
            return input;
        }

//        convert input to hsv then split on channels
        Imgproc.cvtColor(input,procA,Imgproc.COLOR_RGB2HSV);
        Core.split(procA, hsvSplit);

//        using V of hsV, canny, then blur
        Imgproc.Canny(hsvSplit.get(2), procA, 200, 200);
        Imgproc.blur(procA, procB, blur);

//        fill from middle pixel
        middle.x = (double) input.width()/2;
        middle.y = (double) input.height()/2;
        fill_from_pixel(procB, middle, procA);

//        does this cause a mem leak? kinda paranoid after last time lol
        edges = new ArrayList<>();
        Imgproc.findContours(procA, edges, trash, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

//        here is the magic; find the average distance per outline, and use the closest one to the middle
//        maybe a simpler approach of choosing the last contour in the array would work better?
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

//        print out the array as int for readability
/*
        final int[] int_average_edges = new int[average_edges.size()];
        int index = 0;
        for (final Double value: average_edges) {
            int_average_edges[(index++)] = value.intValue();
        }
        telemetry.addData("dists", Arrays.toString(int_average_edges));
*/

//        find the average point in the selected contour, and set the new fill seed to that
        seed = average_point(edges.get(index_of_min(average_edges.toArray(average_edge_array))).toArray());
        telemetry.addData("seed", (int) seed.x+", "+(int) seed.y);
        fill_from_pixel(procB, seed, procA);

//        edge detection and blur
        Imgproc.Canny(procA, procB, 250, 300);
        Imgproc.blur(procB,procA,blur);

//        actually get the shape, update telemetry
        shape = getShape();
        telemetry.addData("shape", ""+shape);
        framesProcessed ++;
        telemetry.addData("x", ""+framesProcessed);
        telemetry.update();
        return procA;
    }

//    0-1 is an error, 2-3 is triangle, 4 sq, 5 pentagon, 6+ is error
    public int getShape() {
        edges = new ArrayList<>();
        Imgproc.findContours(procA, edges, trash, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        if (edges.size() == 0) {return 0;}
        highDefPoly.fromArray(edges.get(0).toArray());
        Imgproc.approxPolyDP(highDefPoly, poly, 10, true);
        telemetry.addData("poly", ""+Arrays.toString(poly.toArray()));
        return poly.toArray().length;
    }
}