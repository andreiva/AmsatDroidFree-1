package uk.me.g4dpz.HamSatDroid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.me.g4dpz.HamSatDroid.utils.IaruLocator;
import uk.me.g4dpz.satellite.InvalidTleException;
import uk.me.g4dpz.satellite.PassPredictor;
import uk.me.g4dpz.satellite.SatNotFoundException;
import uk.me.g4dpz.satellite.SatPassTime;
import uk.me.g4dpz.satellite.SatelliteFactory;
import uk.me.g4dpz.satellite.TLE;

public class PassListActivity extends ASDActivity  implements GestureDetector.OnGestureListener {

    private static final int SWIPE_MINDISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;




    private boolean noPassesFound = false;
    private static PassPredictor passPredictor;



    private Context context;
    private GestureDetector gestureScanner;
    private Calendar startTimeCalendar;
    private static List<SatPassTime> passes = new ArrayList<SatPassTime>();
    private String passHeader;

    private PassRowListAdapter passAdapter;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.FullscreenTheme);


        gestureScanner = new GestureDetector(this);

        // Load layout from XML
        setContentView(R.layout.pass_screen);
        context = this;

    }


    public void bindPassView() {
        passAdapter = new PassRowListAdapter(context, (ArrayList)passes, null);

        ((ListView)findViewById(R.id.PASS_LIST_VIEW)).setAdapter(passAdapter);
        ((ListView)findViewById(R.id.PASS_LIST_VIEW)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                // Get selected passes and start SkyView
                HamSatDroid.setSelectedPass((SatPassTime)parent.getItemAtPosition(position));
                final Intent launchSkyViewIntent = new Intent().setClass(context, SkyView.class);
                startActivity(launchSkyViewIntent);

                // Uri uri = Uri.parse("content://calendar/events");
                // ContentResolver cr = context.getContentResolver();
                // ContentValues values = new ContentValues();
                // values.put("eventTimezone", "EST");
                // values.put("calendarId", 1); // query
                // content://calendar/calendars for
                // more
                // values.put("title", "Sat Pass");
                // values.put("allDay", 0);
                // values.put("dtstart", dtstart); // long (start date
                // in ms)
                // values.put("dtend", dtend); // long (end date in ms)
                // values.put("description",
                // "Bring computers and alcohol");
                // values.put("eventLocation", "ZA WARULDO");
                // values.put("transparency", 0);
                // values.put("visibility", 0);
                // values.put("hasAlarm", 0);
                // cr.insert(uri, values);

            }
        });
    }

    private void recalcPass(final int hoursAhead) {

        // How long to go back/forward in time to find a passes (in hours)
        final int calcRange = 24;

        // Get home lat and lon from saved preferences
        setObserver();

        Calendar myCal;
        if (startTimeCalendar != null) {
            myCal = startTimeCalendar;
        }
        else {
            // Get current GMT date and time
            myCal = Calendar.getInstance();
        }

        Log.d("HamSatDroid time: ", myCal.toString());

        TLE myelem = null;

        // Calculate next satellite passes
        try {
            myelem = setSatellite();

            HamSatDroid.setPassPredictor(new PassPredictor(myelem, HamSatDroid.getGroundStation()));

            HamSatDroid.setPasses(getPassPredictor().getPasses(myCal.getTime(), hoursAhead, true));

            noPassesFound = false;
        }
        catch (final InvalidTleException e) {
            passHeader = "ERROR: Bad Keplerian Elements";
            passAdapter.clear();
            return;
        }
        catch (final SatNotFoundException e) {
            passHeader = e.getMessage();
            passAdapter.clear();
            noPassesFound = true;
            return;
        }
        catch (final IllegalArgumentException e) {
            passHeader = e.getMessage();
            passAdapter.clear();
            return;
        }
        passHeader = "";


        final NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(4);

        final double homeLat = HamSatDroid.getGroundStation().getLatitude();
        final double homeLong = HamSatDroid.getGroundStation().getLongitude();

        final IaruLocator locator = new IaruLocator(homeLat, homeLong);

        if (passes.isEmpty()) {
            passHeader = "Coffee & bunns";
        }
        else {
            passHeader = "";
        }
    }


    private class CalcPassTask extends AsyncTask<Integer, Integer, Long> {

        @Override
        protected Long doInBackground(final Integer... timeOffsetArray) {
            // do not do any UI work here
            final Integer timeOffset = timeOffsetArray[0];
            recalcPass(timeOffset);
            return (long)0;
        }

        @Override
        protected void onProgressUpdate(final Integer... progress) {
        }

        @Override
        protected void onPostExecute(final Long result) {
//			((ProgressBar)findViewById(R.id.PassProgressBar)).setVisibility(View.GONE);
//			bindPassView();
//
//			if(noPassesFound) {
//				Toast toast = Toast.makeText(context, "No visible passes at your location", Toast.LENGTH_LONG);
//				toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
//				toast.show();
//			}
        }

        @Override
        protected void onPreExecute() {
            ((ProgressBar)findViewById(R.id.PassProgressBar)).setVisibility(View.VISIBLE);
        }
    }

    /**
     * @return
     * @throws IllegalArgumentException
     * @throws SatNotFoundException
     * @throws InvalidTleException
     */
    private TLE setSatellite() throws IllegalArgumentException, InvalidTleException, SatNotFoundException {
        final TLE myelem = (TLE)((Spinner)findViewById(R.id.SatelliteSelectorSpinner)).getSelectedItem();

        // TODO
//        HamSatDroid.setSelectedSatellite(SatelliteFactory.createSatellite(myelem));

        HamSatDroid.setPassPredictor(new PassPredictor(myelem, HamSatDroid.getGroundStation()));

        return myelem;
    }

    /**
     * @return the passPredictor
     */
    public static PassPredictor getPassPredictor() {
        return passPredictor;
    }

    private void launchGroundView() {
        final Intent launchGroundViewIntent = new Intent().setClass(this, GroundView.class);
        startActivity(launchGroundViewIntent);
    }




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
    public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
        if (e1.getX() - e2.getX() > SWIPE_MINDISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY && !noPassesFound) {
            launchGroundView();
        }
        return true;
    }

}
