package com.example.compuhypermeganet.smart_commute.adapter;

import android.content.Context;
import android.opengl.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.compuhypermeganet.smart_commute.R;
import com.example.compuhypermeganet.smart_commute.model.BikeTrip;
import com.example.compuhypermeganet.smart_commute.model.Leg;
import com.example.compuhypermeganet.smart_commute.model.Trip;

import java.util.List;

public class TripAdapter extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    private List<Leg> legList;
    private Trip trip;

    public TripAdapter(Context context, int resource, Trip trip){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.resource = resource;
        this.legList = trip.getLegs();
        this.trip = trip;
    }
    public TripAdapter(Trip trip, Context context) {
        this.legList = trip.getLegs();
        this.trip = trip;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return legList == null ? 0:legList.size();}

    @Override
    public Leg getItem(int position) {return legList.get(position);}

    @Override
    public long getItemId(int position) { return position;}

    public static class ViewHolder {
        TextView depart_time;
        TextView from;
        TextView mode;
        TextView arriv_time;
        TextView arriv_address;
        TextView availability;
        TextView duration;
        TextView distance;
        View tip_layout;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TripAdapter.ViewHolder vh = null;
        if(convertView == null) {
            convertView = inflater.inflate(resource,null);
            vh = new TripAdapter.ViewHolder();
            vh.arriv_address =(TextView) convertView.findViewById(R.id.arriv_address);
            vh.arriv_time = (TextView) convertView.findViewById(R.id.arriv_time);
            vh.depart_time =(TextView) convertView.findViewById(R.id.depart_time);
            vh.from = (TextView) convertView.findViewById(R.id.from);
            vh.mode = (TextView) convertView.findViewById(R.id.mode);
            vh.availability = (TextView) convertView.findViewById(R.id.availability);
            vh.duration = (TextView) convertView.findViewById(R.id.duration);
            vh.distance = (TextView) convertView.findViewById(R.id.distance);
            vh.tip_layout = convertView.findViewById(R.id.tip_layout);
            convertView.setTag(vh);
        } else {
            vh = (TripAdapter.ViewHolder) convertView.getTag();
        }
        Leg leg = getItem(position);
        if(trip.getBikeTrip()==null||!leg.getFrom().getName().equals(trip.getBikeTrip().getTransferStation().getName())){
            vh.tip_layout.setVisibility(View.GONE);
            Log.d("visibility","gone");
        }else {
            BikeTrip bt = trip.getBikeTrip();
            vh.availability.setText(bt.getAvailability()+" bikes available at "+bt.getTransferStation().getName());
            vh.duration.setText("Duration "+(int)bt.getDuration()+" min ");
            vh.distance.setText("Distance "+(int)bt.getDistance()+" m");
            Log.d("visibility","show");

        }
        vh.depart_time.setText(leg.getDeparture().toString().substring(11,16));
        vh.arriv_time.setText(leg.getArrival().toString().substring(11,16));
        vh.arriv_address.setText(leg.getTo().getName());
        vh.from.setText(leg.getFrom().getName());
        vh.mode.setText(leg.getLine()+" "+leg.getDirection());
        return convertView;
    }
}
