package com.example.wutao.graphguide.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.wutao.graphguide.data_struct.Stop;
import com.example.wutao.graphguide.data_struct.Track;
import com.example.wutao.graphguide.data_struct.TrainGraph;
import com.example.wutao.graphguide.utils.Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by 吴涛 on 2016/6/21.
 */
public class TrainGraphView extends View {

    private TrainGraph mGraph;

    private final int DISTANCE_STEP = 200;

    private final int STOP_SIZE = 50;

    private ArrayList<String> mTrips = new ArrayList<>();

    private Paint mStopPaint;

    private Paint mTrackPaint;

    private Paint mTextPaint;

    private char[] mStopName = new char[1];

    private Rect mTextRect = new Rect();

    private Random mColorRandom = new Random();

    public TrainGraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrainGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public TrainGraphView(Context context) {
        this(context, null, 0);
    }

    private void initPaint(){
        mStopPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStopPaint.setColor(Color.BLUE);
        mTrackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setFilterBitmap(true);
        mTextPaint.setTextSize(40);

    }

    private void setTrakPaintAsTrackUse(){
        mTrackPaint.setStrokeWidth(10);
        mTrackPaint.setColor(Color.BLACK);
    }

    private void setTrackPaintAsTripUse(){
        mTrackPaint.setStrokeWidth(15);
        int r = mColorRandom.nextInt(0xff);
        int g = mColorRandom.nextInt(0xff);
        int b = mColorRandom.nextInt(0xff);
        int color = 0xff << 24 | r << 16 | g << 8 | b;
        mTrackPaint.setColor(color);
    }

    public void setGraph(TrainGraph graph){
        mGraph = graph;
        invalidate();
    }

    public void setTrips(ArrayList<String> trips){
        mTrips.clear();
        if(!Utils.isEmpty(trips)) {
            for (String trip : trips) {
                if (!mTrips.contains(trip) && checkTripIlleageal(trip)){
                    mTrips.add(trip);
                }
            }
        }
        invalidate();
    }

    public void addTrip(String trip){
        if(!mTrips.contains(trip) && checkTripIlleageal(trip)){
            mTrips.add(trip);
        }
        invalidate();
    }

    public void clearTrips(){
        mTrips.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mGraph != null){
            drawStops(canvas);
            drawAllTrack(canvas);
            drawTrips(canvas);
        }
    }

    private void drawStops(Canvas canvas){
        ArrayList<Stop> stops = mGraph.getStops();
        if(!Utils.isEmpty(stops)){
            int saveCount = canvas.save();
            for (Stop stop : stops){
                drawStop(canvas, stop);
            }
            canvas.restoreToCount(saveCount);
        }
    }

    private void drawStop(Canvas canvas, Stop stop){
        int saveCount = canvas.save();
        int x = stop.getX() * DISTANCE_STEP;
        int y = stop.getY() * DISTANCE_STEP;
        canvas.drawCircle(x, y, STOP_SIZE, mStopPaint);
        mStopName[0] = stop.getStopName();
        mTextPaint.getTextBounds(mStopName, 0, 1, mTextRect);
        canvas.drawText(mStopName, 0, 1, x - (mTextRect.right - mTextRect.left) / 2 , y + (mTextRect.bottom - mTextRect.top) / 2, mTextPaint);
        canvas.restoreToCount(saveCount);
    }

    private void drawAllTrack(Canvas canvas){
        ArrayList<Stop> stops = mGraph.getStops();
        for (Stop stop : stops){
            setTrakPaintAsTrackUse();
            Track nextTrack = stop.getFirstTrack();
            while (nextTrack != null){
                Stop endStop = stops.get(nextTrack.getAdjStop());
                drawTrack(canvas, stop, endStop);
                nextTrack = nextTrack.getNext();
            }
        }
    }

    private void drawTrack(Canvas canvas, Stop start, Stop end){
        if(canvas != null && start != null && end != null) {
            int saveCount1 = canvas.save();
            canvas.translate(start.getX() * DISTANCE_STEP, start.getY() * DISTANCE_STEP);
            int dextX = end.getX() - start.getX();
            int dexlY = end.getY() - start.getY();
            float deltxFloat = dextX == 0 ? (float) 0.00001 : dextX;
            float deltYFloat = dexlY == 0 ? (float) 0.00001 : dexlY;
            double degree = Math.toDegrees(Math.atan(deltYFloat / deltxFloat));
            if (dextX < 0) {
                degree += 180;
            }
            canvas.rotate((int) degree);
            int lenght = (int) Math.sqrt(Math.pow((end.getX() - start.getX()) * DISTANCE_STEP, 2) + Math.pow((end.getY() - start.getY()) * DISTANCE_STEP, 2)) - 2 * STOP_SIZE;
            canvas.drawLine(STOP_SIZE, 0f, lenght + STOP_SIZE, 0f, mTrackPaint);
            canvas.translate(lenght + STOP_SIZE, 0);
            canvas.rotate(-135);
            canvas.drawLine(0, 0, 100, 0, mTrackPaint);
            canvas.rotate(-90);
            canvas.drawLine(0, 0, 100, 0, mTrackPaint);
            canvas.restoreToCount(saveCount1);
        }
    }


    private void drawTrips(Canvas canvas){
        if(!Utils.isEmpty(mTrips)){
            for (String trip : mTrips){
                setTrackPaintAsTripUse();
                for (int i = 0; i < trip.length() - 1; i++){
                    char start = trip.charAt(i);
                    char end = trip.charAt(i + 1);
                    Stop startStop = mGraph.findStopWithName(start);
                    Stop endStop = mGraph.findStopWithName(end);
                    drawTrack(canvas, startStop, endStop);
                }
            }
        }
    }

    private boolean checkTripIlleageal(String trip){
        return !Utils.isEmpty(trip) && mGraph != null && mGraph.distanceOf(trip) != TrainGraph.INVALID_TRIP;
    }

}
