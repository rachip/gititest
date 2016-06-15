package com.news.kikar.biosense;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A simple XYPlot
 */
public class Raw_ECG extends Activity implements View.OnTouchListener
{

    private XYPlot plot;

    public void setSeriesList(List<Number> seriesList) {
        this.seriesList = seriesList;
    }

    List<Number> seriesList=new ArrayList<>();
    private XYSeries series1;
    private PointF minXY;
    private PointF maxXY;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_ecg);
       // Intent intent = new Intent(this, Lorentz_plot.class);
        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);
        readECG();
        plot.setVerticalScrollBarEnabled(true);
        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
         series1 = new SimpleXYSeries(seriesList,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,"ECG");
        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_labels);


        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Uniform));


        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
       // plot.addSeries(series2, series2Format);

        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);

      //  plot.setTicksPerDomainLabel(5);
         plot.setDomainStepValue(10);
        // rotate domain labels 45 degrees to make them more compact horizontally:
        plot.getGraphWidget().setDomainLabelOrientation(-45);
      //  plot.getGraphWidget().setShowDomainLabels(false);
      //  plot.getGraphWidget().setShowRangeLabels(false);

        plot.redraw();
       // plot.disableAllMarkup();
        //Set of internal variables for keeping track of the boundaries
        plot.calculateMinMaxVals();
       // minXY=new PointF(plot.getCalculatedMinX().floatValue(),plot.getCalculatedMinY().floatValue());
       // maxXY=new PointF(plot.getCalculatedMaxX().floatValue(),plot.getCalculatedMaxY().floatValue());
        minXY=new PointF(series1.getX(0).floatValue(),series1.getY(0).floatValue());
        maxXY=new PointF(series1.getX(series1.size() - 1).floatValue(),series1.getY(series1.size() - 1).floatValue());
        //plot.setOnTouchListener(onTouch(findViewById(R.id.plot),null)));
        plot.setOnTouchListener(this);
    }


    protected void readECG()
    {
        int index=0;
        short val;

        try
        {

            InputStream is = getResources().getAssets().open("ecg.txt");
            //      BufferedReader br = new BufferedReader(new InputStreamReader(is));
            Scanner scan=new Scanner(is);
            while(scan.hasNextShort())
            {
                val=scan.nextShort();
                seriesList.add(val);
                index++;
                if(index>1500)
                    break;
            }
            scan.close();
            is.close();

        }catch (IOException e)
        {

        }
    }

    // Definition of the touch states
    static final int NONE = 0;
    static final int ONE_FINGER_DRAG = 1;
    static final int TWO_FINGERS_DRAG = 2;
    int mode = NONE;

    PointF firstFinger;
    float distBetweenFingers;
    boolean stopThread = false;

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // Start gesture
                firstFinger = new PointF(event.getX(), event.getY());
                mode = ONE_FINGER_DRAG;
                stopThread = true;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // second finger
                distBetweenFingers = spacing(event);
                // the distance check is done to avoid false alarms
                if (distBetweenFingers > 5f) {
                    mode = TWO_FINGERS_DRAG;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ONE_FINGER_DRAG) {
                    PointF oldFirstFinger = firstFinger;
                    firstFinger = new PointF(event.getX(), event.getY());
                    scroll(oldFirstFinger.x - firstFinger.x);
                    plot.setDomainBoundaries(minXY.x, maxXY.x,
                            BoundaryMode.FIXED);
                    plot.redraw();

                } else if (mode == TWO_FINGERS_DRAG) {
                    float oldDist = distBetweenFingers;
                    distBetweenFingers = spacing(event);
                    zoom(oldDist / distBetweenFingers);
                    plot.setDomainBoundaries(minXY.x, maxXY.x,
                            BoundaryMode.FIXED);
                    plot.redraw();
                }
                break;
        }
        return true;
    }

    private void zoom(float scale) {
        float domainSpan = maxXY.x - minXY.x;
        float domainMidPoint = maxXY.x - domainSpan / 2.0f;
        float offset = domainSpan * scale / 2.0f;

        minXY.x = domainMidPoint - offset;
        maxXY.x = domainMidPoint + offset;

        minXY.x = Math.min(minXY.x, series1.getX(series1.size() - 3)
                .floatValue());
        maxXY.x = Math.max(maxXY.x, series1.getX(1).floatValue());
        clampToDomainBounds(domainSpan);
    }

    private void scroll(float pan) {
        float domainSpan = maxXY.x - minXY.x;
       // float step = domainSpan / plot.getWidth();

        float offset = pan /* * step*/;
        minXY.x = minXY.x + offset;
        maxXY.x = maxXY.x + offset;
        clampToDomainBounds(domainSpan);
    }

    private void clampToDomainBounds(float domainSpan) {

        float leftBoundary = series1.getX(0).floatValue();
        float rightBoundary = series1.getX(series1.size() - 1).floatValue();
        // enforce left scroll boundary:
        if (minXY.x < leftBoundary) {
            minXY.x = leftBoundary;
            maxXY.x = leftBoundary + domainSpan;
        } else if (maxXY.x > series1.getX(series1.size() - 1).floatValue()) {
            maxXY.x = rightBoundary;
            minXY.x = rightBoundary - domainSpan;
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt (x * x + y * y);
    }
}


