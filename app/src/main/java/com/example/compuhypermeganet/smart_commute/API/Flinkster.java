package com.example.compuhypermeganet.smart_commute.API;

//
// SmartCommute
// flinkster.java
//
// Alex Hunziker, Xinyuan Cai
// 2018
//

import com.example.compuhypermeganet.smart_commute.model.BikeStation;
import com.example.compuhypermeganet.smart_commute.model.BikeTrip;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class Flinkster {

    public static JSONObject getFlinksterJSON(URL url) {
        String response;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer 31c6a6ab3ee4e66b04f5117bcedb964d");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP ERROR " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            response = br.readLine();

            conn.disconnect();
            return new JSONObject(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BikeStation getAvailableBike(double x, double y, BikeTrip t) {
        return getAvailableBike(x, y, t, 300);
    }

    public static BikeStation getAvailableBike(double x, double y, BikeTrip t, int radius) {

        try {
            URL url = new URL("https://api.deutschebahn.com/flinkster-api-ng/v1/bookingproposals?lat=" + x + "&lon=" + y + "&radius=" + radius + "&providernetwork=2");
            System.out.println("Calling: " + url);
            JSONObject respObj = getFlinksterJSON(url);
            JSONArray offers = respObj.getJSONArray("items");

            BikeStation closestStation = null;
            JSONObject thisOffer;
            JSONArray thisCoord;
            double dist, x1, y1, thisDist;
            int totalAvailability = 0, thisAvailability;
            dist = Double.POSITIVE_INFINITY;

            for (int i = 0; i < offers.length(); i++) {
                thisOffer = offers.getJSONObject(i);
                thisCoord = thisOffer.getJSONObject("position").getJSONArray("coordinates");
                x1 = thisCoord.getDouble(1);
                y1 = thisCoord.getDouble(0);
                thisDist = Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
                if (thisDist < dist) {
                    closestStation = new BikeStation(y1, x1);
                    closestStation.setReservationLink(thisOffer.getJSONArray("_links").getJSONObject(0).getString("href"));
                    thisAvailability = thisOffer.getJSONArray("_links").length();
                    closestStation.setAvailability(thisAvailability);
                    closestStation.setDistToDest(thisDist);
                    dist = thisDist;
                    closestStation.setAddress("Dummy; if we need this another call of Flinkster API is needed");
                }
                totalAvailability += thisOffer.getJSONArray("_links").length();
            }
            t.setAvailability(totalAvailability);
            return closestStation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BikeStation getNearbyBikeStation(double x, double y) {
        return getNearbyBikeStation(x, y, 300);
    }

    public static BikeStation getNearbyBikeStation(double x, double y, int radius) {
        JSONObject respObj;

        try {
            URL url = new URL("https://api.deutschebahn.com/flinkster-api-ng/v1/areas?lat=" + x + "&lon=" + y + "&radius=" + radius + "&type=station&providernetwork=2");
            System.out.println("Calling: " + url);
            respObj = getFlinksterJSON(url);
            JSONArray stations = respObj.getJSONArray("items");

            BikeStation closestStation = null;
            String positionKey;
            double dist = Double.POSITIVE_INFINITY;
            for (int i = 0; i < stations.length(); i++) {
                JSONObject geometry = stations.getJSONObject(i).getJSONObject("geometry");
                if (geometry.has("centroid")) {
                    positionKey = "centroid";
                } else {
                    positionKey = "position";
                }
                JSONArray statLoc = geometry.getJSONObject(positionKey).getJSONArray("coordinates");
                double x1 = statLoc.getDouble(1);
                double y1 = statLoc.getDouble(0);
                double thisDist = Math.sqrt(Math.pow(x - x1, 2) + Math.pow(y - y1, 2));
                if (thisDist < dist) {
                    dist = thisDist;
                    closestStation = new BikeStation(y1, x1);
                    closestStation.setDistToDest(thisDist);
                    JSONObject address = stations.getJSONObject(i).getJSONObject("address");
                    closestStation.setAddress(address.getString("street") + ", " + address.getString("city") + ", " + address.getString("district"));
                }
            }
            return closestStation;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
