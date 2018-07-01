package com.example.compuhypermeganet.smart_commute;
//
// SmartCommute
// Trip.java
//
// Alex Hunziker, Xinyuan Cai
// 2018
//


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.compuhypermeganet.smart_commute.API.Flinkster;
import com.example.compuhypermeganet.smart_commute.adapter.TripAdapter;
import com.example.compuhypermeganet.smart_commute.model.BikeStation;
import com.example.compuhypermeganet.smart_commute.model.Station;
import com.example.compuhypermeganet.smart_commute.model.Trip;

import java.util.Date;


public class navigation extends AppCompatActivity {

    private ListView listView;
    private TripAdapter adapter;
    private Trip trip;
    private Station from;
    private Station to;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        listView = (ListView) findViewById(R.id.plan);

        Intent intent = getIntent();
        new CallRMV().execute(intent.getStringExtra("depart_id"), intent.getStringExtra("dest_id"));
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
            adapter = new TripAdapter(navigation.this, R.layout.leg_item, trip);
            listView.setAdapter(adapter);
        }
    }




}