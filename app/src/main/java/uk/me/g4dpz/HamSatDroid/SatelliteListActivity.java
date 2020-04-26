package uk.me.g4dpz.HamSatDroid;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.ListFragment;
import uk.me.g4dpz.satellite.TLE;

public class SatelliteListActivity extends ListFragment implements GestureDetector.OnGestureListener {




    private TextView text;
    private List<String> listValues;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        listValues = new ArrayList<String>();
        listValues.add("Android");
        listValues.add("iOS");
        listValues.add("Symbian");
        listValues.add("Blackberry");
        listValues.add("Windows Phone");


        // Populate list with our static array of titles.
        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.sat_row, R.id.name, listValues));


    }



//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setTheme(R.style.FullscreenTheme);
//        setContentView(R.layout.sat_list);
//
//        text = (TextView) findViewById(R.id.name);
//
//        listValues = new ArrayList<String>();
//        listValues.add("Android");
//        listValues.add("iOS");
//        listValues.add("Symbian");
//        listValues.add("Blackberry");
//        listValues.add("Windows Phone");
//
//        // initiate the listadapter
//        ArrayAdapter<String> myAdapter = new ArrayAdapter <String>(this,
//                R.layout.sat_row, R.id.name, listValues);
//
//        // assign the list adapter
//        setListAdapter(myAdapter);
//
//    }

    // when an item of the list is clicked
//    @Override
//    protected void onListItemClick(ListView list, View view, int position, long id) {
//        super.onListItemClick(list, view, position, id);
//
//        String selectedItem = (String) getListView().getItemAtPosition(position);
//        //String selectedItem = (String) getListAdapter().getItem(position);
//
//        text.setText("You clicked " + selectedItem + " at position " + position);
//    }



    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
