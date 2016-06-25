package com.example.wutao.graphguide.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.wutao.graphguide.data_struct.TrainGraph;

import java.util.ArrayList;

/**
 * Created by 吴涛 on 2016/6/23.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<String> mTrips;
    private TrainGraph mGraph;
    private Context mContext;

    public ViewPagerAdapter(Context context, TrainGraph graph, ArrayList<String> trips){
        mGraph = graph;
        mTrips = trips;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mTrips.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TrainGraphView graphView = new TrainGraphView(mContext);
        graphView.setGraph(mGraph);
        graphView.addTrip(mTrips.get(position));
        container.addView(graphView);
        return graphView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View)object));
    }
}
