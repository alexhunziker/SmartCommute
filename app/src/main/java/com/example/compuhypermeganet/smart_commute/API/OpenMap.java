package com.example.compuhypermeganet.smart_commute.API;

import com.example.compuhypermeganet.smart_commute.model.BikeTrip;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

//
//SmartCommute
//openMap.java
//
//Alex Hunziker, Xinyuan Cai
//2018
//

// The server used is very unreliable. Needs to be replaced.


public class OpenMap {

    public static double getDuration(double x1, double y1, double x2, double y2) {
        BikeTrip bt = new BikeTrip();
        setDurationDistance(x1, y1, x2, y2, bt);
        return bt.getDuration();
    }

    public static void setDurationDistance(double x1, double y1, double x2, double y2, BikeTrip bt) {

        try {
            URL request = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + x1 + "," + y1 + "&destinations=" + x2 + "," + y2 + "&key=AIzaSyCR-i30y12Khnd2i37hslr4fkU3HaWDMT4");
            System.out.println("Calling: " + request);
            HttpURLConnection conn = (HttpURLConnection) request.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP ERROR " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            // Combine to String
            String response = "", output;
            while ((output = br.readLine()) != null) {
                // System.out.println(output);
                response = response + output;
            }


            conn.disconnect();
            JSONObject respObj = new JSONObject(response);

            JSONObject elements = respObj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
            JSONObject duration = elements.getJSONObject("duration");
            bt.setDuration(duration.getDouble("value") / 60);
            JSONObject distance = elements.getJSONObject("distance");
            bt.setDistance(distance.getDouble("value"));
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }


        bt.setDuration(Double.POSITIVE_INFINITY);
        bt.setDistance(Double.POSITIVE_INFINITY);
    }

}