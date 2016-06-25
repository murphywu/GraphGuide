package com.example.wutao.graphguide;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.wutao.graphguide.data_struct.TrainGraph;
import com.example.wutao.graphguide.test.GraphGuide;
import com.example.wutao.graphguide.utils.Utils;
import com.example.wutao.graphguide.view.TrainGraphView;
import com.example.wutao.graphguide.view.ViewPagerAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphActivity extends AppCompatActivity {

    private TrainGraphView mTrainGraphView;
    private TrainGraph mGraph;
    private TextView mTitleView;
    private TextView mSubTitleView;
    private ViewPager mViewPager;
    private static StringBuilder sRouteSB = new StringBuilder();
    public static final String NO_SUCH_ROUTE = "NO_SUCH_ROUTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTrainGraphView = (TrainGraphView) findViewById(R.id.train_graph_view);
        mTitleView = (TextView) findViewById(R.id.title);
        mSubTitleView = (TextView) findViewById(R.id.subtitle);
        mViewPager = (ViewPager) findViewById(R.id.trip_view_pager);
        mGraph = TrainGraph.createGraph(GraphGuide.sEdgeInputParams, GraphGuide.sStopLocationInputParams);
        mTrainGraphView.setGraph(mGraph);
        parserIntentParams();
    }

    private void parserIntentParams(){
        if(getIntent() != null){
            Intent intent = getIntent();
            int commond = intent.getIntExtra(HomeActivity.COMMOND, -1);
            String params = intent.getStringExtra(HomeActivity.PARAMS);
            switch (commond){
                case HomeActivity.DISTANCE_OF_TRPI:
                    doCaculateDistanceBussiness(params);
                    break;
                case HomeActivity.TRIPS_WITH_MAX_STOPS:
                    doMaxStepTripBusiness(params);
                    break;
                case HomeActivity.TRIPS_WIYH_EXACTLY_STOPS:
                    doExactlyStepTripBusiness(params);
                    break;
                case HomeActivity.SHORTEST_LENGTH_OF_TWO_STOP:
                    doShortDistanceBusiness(params);
                    break;
                default:
                    break;
            }
        }
    }

    private void doCaculateDistanceBussiness(String trip){
        mTitleView.setText(R.string.caculate_route_distance);
        mSubTitleView.setText(theDistanceOfRoute(trip));
        mTrainGraphView.setVisibility(View.VISIBLE);
        mTrainGraphView.addTrip(trip);
        mViewPager.setVisibility(View.GONE);
    }

    private void doMaxStepTripBusiness(String params){
        mTitleView.setText(R.string.step_limit_solution_title);
        char start = params.charAt(0);
        char end = params.charAt(1);
        Matcher matcher = Pattern.compile("\\d+").matcher(params);
        if(matcher.find()){
            int maxStep = Integer.parseInt(matcher.group());
            final ArrayList<String> trips = mGraph.tripsOfRoute(start, end, maxStep);
            if(!Utils.isEmpty(trips)) {
                mTrainGraphView.setVisibility(View.GONE);
                ViewPagerAdapter adapter = new ViewPagerAdapter(this, mGraph, trips);
                mViewPager.setAdapter(adapter);

                mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        String trip = trips.get(position);
                        mSubTitleView.setText(getRouteString(trip) + ": " + String.format("%d step", trip.length() - 1));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                mViewPager.setVisibility(View.VISIBLE);
            }else {
                mViewPager.setVisibility(View.GONE);
                mTrainGraphView.setVisibility(View.VISIBLE);
                sRouteSB.setLength(0);
                sRouteSB.append(start).append("-").append(end).append(", max step ").append(maxStep).append(": 未匹配任何路径");
                mSubTitleView.setText(sRouteSB.toString());
            }
        }
    }

    private void doExactlyStepTripBusiness(String params){
        mTitleView.setText(R.string.fix_step_solution_title);
        char start = params.charAt(0);
        char end = params.charAt(1);
        Matcher matcher = Pattern.compile("\\d+").matcher(params);
        if(matcher.find()){
            int step = Integer.parseInt(matcher.group());
            ArrayList<String> allTrips = mGraph.tripsOfRoute(start, end, step);
            final ArrayList<String> trips = new ArrayList<>();
            for (String trip : allTrips){
                if(trip.length() == 5){
                    trips.add(trip);
                }
            }
            if(!Utils.isEmpty(trips)) {
                mTrainGraphView.setVisibility(View.GONE);
                ViewPagerAdapter adapter = new ViewPagerAdapter(this, mGraph, trips);
                mViewPager.setAdapter(adapter);
                mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        String trip = trips.get(position);
                        mSubTitleView.setText(getRouteString(trip) + ": " + String.format("%d step", trip.length() - 1));
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                mViewPager.setVisibility(View.VISIBLE);
            }else {
                mViewPager.setVisibility(View.GONE);
                mTrainGraphView.setVisibility(View.VISIBLE);
                sRouteSB.setLength(0);
                sRouteSB.append(start).append("-").append(end).append(", step ").append(step).append(": 未匹配任何路径");
                mSubTitleView.setText(sRouteSB.toString());
            }
        }
    }

    private void doShortDistanceBusiness(String params){
        mTitleView.setText(R.string.shortest_distance_solution);
        char start = params.charAt(0);
        char end = params.charAt(1);
        String trip = mGraph.shortestRouteOf(start, end);
        mTrainGraphView.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
        if(!TextUtils.isEmpty(trip)) {
            mTrainGraphView.addTrip(trip);
            mSubTitleView.setText(getRouteString(trip) + ": " + mGraph.distanceOf(trip));
        }else {
            sRouteSB.setLength(0);
            sRouteSB.append(start).append("-").append(end).append(": ").append(NO_SUCH_ROUTE);
            mSubTitleView.setText(sRouteSB.toString());
        }
    }

    private String getRouteString(String trip){
        sRouteSB.setLength(0);
        for(int i = 0; i < trip.length(); i++){
            sRouteSB.append(trip.charAt(i));
            if(i != trip.length() - 1){
                sRouteSB.append("-");
            }
        }
       return sRouteSB.toString();
    }

    private String distanceString(String route){
        if(mGraph != null && !Utils.isEmpty(route)){
            int distance = mGraph.distanceOf(route);
            return distance >= 0 ? String.valueOf(distance) : NO_SUCH_ROUTE;
        }
        return NO_SUCH_ROUTE;
    }

    private String theDistanceOfRoute(String route) {
        if(!Utils.isEmpty(route)){
            sRouteSB.setLength(0);
            for(int i = 0; i < route.length(); i++){
                sRouteSB.append(route.charAt(i));
                if(i != route.length() - 1){
                    sRouteSB.append("-");
                }
            }
            sRouteSB.append(": ");
            sRouteSB.append(distanceString( route));
            return sRouteSB.toString();
        }
        return "";
    }
}
