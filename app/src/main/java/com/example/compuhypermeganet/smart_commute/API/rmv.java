package com.example.compuhypermeganet.smart_commute.API;

//
// SmartCommute
// rmv.java
//
// Alex Hunziker, Xinyuan Cai
// 2018
//

import com.example.compuhypermeganet.smart_commute.model.Station;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class rmv {


    public static String getXML(URL url) {
        // BUGBUGBUG: Known issue: If URL contains a space instead of %20, it will return a HTTP Error 400. We might want to fix that...
        String output;
        System.out.println("Calling: " + url);
        String response = (String) "";
        try {
            // Open connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/xml");

            // Catch HTTP Errors
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP ERROR " + conn.getResponseCode());

            }

            // Buffer Response
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            // Combine to String + Debug Print
            // System.out.print("ServerResponse: ");
            while ((output = br.readLine()) != null) {
                // System.out.println(output);
                response = (String) response + (String) output;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static ArrayList<Station> get_matching_stops(String q) throws Exception {
        // System.out.println("Getting Matching Stops...");
        String response;

        q = q.replace(" ", "%20");
        URL url = new URL("https://www.rmv.de/hapi/location.name?accessId=0e10668a-6fde-4ffd-aabf-02dbfc43786f&input="+q);

        // Perform GET Request, getting XML String
        response = getXML(url);

        try {
            // Parse XML
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(new ByteArrayInputStream(response.getBytes("UTF-8")));
            ArrayList<Station> stations = new ArrayList<Station>();
            // Create Node List
            NodeList stopLocations = doc.getElementsByTagName("StopLocation");
            for(int i = 0; i<stopLocations.getLength(); i++) {
                Element stopLocation = (Element) stopLocations.item(i);
                stations.add(new Station(stopLocation));
            }
            NodeList coordLocations = doc.getElementsByTagName("CoordLocation");
            for(int i = 0; i<coordLocations.getLength(); i++) {
                Element coordLocation = (Element) coordLocations.item(i);
                stations.add(new Station(coordLocation));
            }
            return stations;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        // Never reached
        return null;
    }

    public static Document getTrip(Station origin, Station destination, Date dateTime) {
        //System.out.println("Getting Trip Plan...");
        String response;
        URL url;
        try {
            // Generate GET Request
            Format formatDate = new SimpleDateFormat("yyyy-MM-dd");
            Format formatTime = new SimpleDateFormat("HH:mm");
            String date = formatDate.format(dateTime);
            String time = formatTime.format(dateTime);
            String origin_in = origin.getId().replace(" ", "%20");
            String destination_in = destination.getId().replace(" ", "%20");
            url = new URL("https://www.rmv.de/hapi/trip?accessId=0e10668a-6fde-4ffd-aabf-02dbfc43786f&originId=" + origin_in
                    + "&destId=" + destination_in + "&numF=1&date=" + date + "&time=" + time + "&originBike=0&destBike=0");

            System.out.println("url"+url);
            // Perform GET Request, getting XML String
            response = getXML(url);

            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(new ByteArrayInputStream(response.getBytes("UTF-8")));
            return doc;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
