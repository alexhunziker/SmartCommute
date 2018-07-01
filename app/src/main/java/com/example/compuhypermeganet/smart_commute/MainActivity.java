package com.example.compuhypermeganet.smart_commute;
//
// SmartCommute
// Trip.java
//
// Alex Hunziker, Xinyuan Cai
// 2018
//


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compuhypermeganet.smart_commute.adapter.BikeTripAdapter;
import com.example.compuhypermeganet.smart_commute.adapter.TripAdapter;
import com.example.compuhypermeganet.smart_commute.model.Station;
import com.example.compuhypermeganet.smart_commute.model.Trip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private SectionStatePagerAdapter fragmentStatePagerAdapter;

    private ViewPager mViewPager;
    public static String depart_id;
    public static String dest_id;
    private Trip trip;
    private Station from;
    private Station to;
    public static View view1,view2;
    public static ListView listview1,listview2;
    public static Button Google;// = view2.findViewById(R.id.google);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        Intent intent = getIntent();
        depart_id = intent.getStringExtra("depart_id");
        dest_id = intent.getStringExtra("dest_id");

        //
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        fragmentStatePagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(fragmentStatePagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        view1 = getLayoutInflater().inflate(R.layout.activity_navigation, null);
        view2 = getLayoutInflater().inflate(R.layout.listview_smartplan, null);

        listview1 = (ListView) view1.findViewById(R.id.plan);
        listview2 = view2.findViewById(R.id.smartplan);
        new CallRMV().execute(depart_id, dest_id);
    }
    private class CallRMV extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {
            from = new Station(params[0]);
            to = new Station(params[1]);
            Log.d("destination_id_navi", to.getId() );
            trip = new Trip(from, to, new Date());
            return "";
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String result) {
            // this is executed on the main thread after the process is over
            // update your UI here
            if(trip!=null){
                TripAdapter adapter1 = new TripAdapter(MainActivity.this, R.layout.leg_item, trip);
                BikeTripAdapter adapter2 = new BikeTripAdapter(MainActivity.this, R.layout.leg_item, trip);
                listview1.setAdapter(adapter1);
                listview2.setAdapter(adapter2);
                listview1.setSelection(ListView.FOCUS_DOWN);
                listview2.setSelection(ListView.FOCUS_DOWN);
                if(trip.getBikeTrip()!=null){
                    trip.getBikeTrip().getDuration();
                    TextView availability = view2.findViewById(R.id.availability);
                    availability.setText(trip.getBikeTrip().getAvailability()+" bikes");
                    TextView Duration = view2.findViewById(R.id.duration);
                    Duration.setText("Duration " + (int) trip.getBikeTrip().getDuration() + " min Distance " + (int) trip.getBikeTrip().getDistance() + " m");
                    Google = view2.findViewById(R.id.google);
                    Google.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      if(trip!=null) {
//                                                  Log.d("Transfer().getX()",trip.getBikeTrip().getTransferStation().getX());
                                                          String uri = "http://maps.google.com/maps?saddr=" +
                                                                  trip.getBikeTrip().getTransferStation().getX()+ "," +
                                                                  trip.getBikeTrip().getTransferStation().getY()+ "&daddr=" +
                                                                  trip.getBikeTrip().getTo().getLat()+ "," + trip.getBikeTrip().getTo().getLon()+"&travelmode=bicycling";
                                                          Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                                          startActivity(intent);

                                                      }else{
                                                          Toast.makeText(MainActivity.this,"no bike trip",Toast.LENGTH_SHORT).show();
                                                      }


                                                  }
                                              }
                    );
                }else{
                    view2.findViewById(R.id.tipps).setVisibility(View.GONE);
                    view2.findViewById(R.id.google).setVisibility(View.GONE);
                }

            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static final String ARG_SECTION_NUMBER = "section_number";

        public  Context mContext;
        public static int position;
        public PlaceholderFragment() {
            mContext = getActivity();
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            position = sectionNumber;
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            position = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER) : 1;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            position = getArguments() != null ? getArguments().getInt(ARG_SECTION_NUMBER) : 1;
            if(position==1){
                return view1;
            }
            else{
                return view2;
            }
//            View rootView = inflater.inflate(R.layout.activity_navigation, container, false);
//            listView = (ListView) rootView.findViewById(R.id.plan);
//            return rootView;
//            if(position == 1){
//                View rootView = inflater.inflate(R.layout.activity_navigation, container, false);
//                listView = (ListView) rootView.findViewById(R.id.plan);
//            new CallRMV().execute(depart_id, dest_id);
//                return rootView;
//            }else{
//                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//                return rootView;
//            }
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
    }



    public class SectionStatePagerAdapter extends FragmentStatePagerAdapter {

        public SectionStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);        }

        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "traditional trip plan";
                case 1:
                    return "smart trip plan with bikes";
                case 2:
                    return "cars";
            }
            return null;
        }

    }

}
