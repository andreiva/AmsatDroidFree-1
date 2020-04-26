package uk.me.g4dpz.HamSatDroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.me.g4dpz.satellite.Satellite;
import uk.me.g4dpz.satellite.TLE;

public class SatListAdapter extends BaseAdapter {

    private static final int LOW_ELEVATION_ANGLE = 30;
    private static final int HIGH_ELEVATION_ANGLE = 60;

    private ArrayList<TLE> objects;
    private LayoutInflater inflater;
    private Activity activity;
    Context context;

    public SatListAdapter(Context context, ArrayList<TLE> objects, Activity activity) {
        super();
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.objects = objects;
        this.activity= activity;
    }

    public void clear() {
        objects.clear();
       // this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.pass_row, null);

//        if(objects.isEmpty()) {
//			Toast toast = Toast.makeText(context, "No visible passes for your ASDASDASD", Toast.LENGTH_LONG);
//			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
//			toast.show();
//            return view;
//        }

        TextView startTime = (TextView) view.findViewById(R.id.name);
        startTime.setText("drfth");



//        TextView aos = (TextView) view.findViewById(R.id.aos);
//        aos.setText(objects.get(position).getAosAzimuth() +"°");
//
//        TextView los = (TextView) view.findViewById(R.id.los);
//        los.setText(objects.get(position).getLosAzimuth() +"°");


        return view;
    }

    private int getElevationColor(double elevation) {

        if(elevation <= LOW_ELEVATION_ANGLE)
            return Color.GRAY;
        if(elevation > LOW_ELEVATION_ANGLE && elevation < HIGH_ELEVATION_ANGLE)
            return Color.rgb(100,170,100);
        else
            return Color.rgb(10, 200, 10);

    }

}
