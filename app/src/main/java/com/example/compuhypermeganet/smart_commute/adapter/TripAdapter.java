package com.example.compuhypermeganet.smart_commute.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compuhypermeganet.smart_commute.MainActivity;
import com.example.compuhypermeganet.smart_commute.R;
import com.example.compuhypermeganet.smart_commute.model.Leg;
import com.example.compuhypermeganet.smart_commute.model.Trip;

import java.util.List;

public class TripAdapter extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    private List<Leg> legList;
    public Trip trip;

    public TripAdapter(Context context, int resource, Trip trip){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.resource = resource;
        this.legList = trip.getLegs();
        this.trip = trip;
    }
    @Override
    public int getCount() { return legList == null ? 0:legList.size()+1;}

    @Override
    public Leg getItem(int position) {return legList == null? null:legList.get(position);}

    @Override
    public long getItemId(int position) { return position;}

    public static class ViewHolder {
        TextView depart_time;
        TextView from;
        TextView mode;
        TextView arriv_time;
        TextView arriv_address;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TripAdapter.ViewHolder vh = null;
        View view;
        Leg leg = null;
        if(position < getCount()-1) leg = getItem(position);
        if (leg != null) {
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
                vh = new TripAdapter.ViewHolder();
                vh.arriv_address = (TextView) convertView.findViewById(R.id.arriv_address);
                vh.arriv_time = (TextView) convertView.findViewById(R.id.arriv_time);
                vh.depart_time = (TextView) convertView.findViewById(R.id.depart_time);
                vh.from = (TextView) convertView.findViewById(R.id.from);
                vh.mode = (TextView) convertView.findViewById(R.id.mode);

                convertView.setTag(vh);
            } else {
                vh = (TripAdapter.ViewHolder) convertView.getTag();

            }
                vh.depart_time.setText(leg.getDeparture().toString().substring(11, 16));
                vh.arriv_time.setText(leg.getArrival().toString().substring(11, 16));
                vh.arriv_address.setText(leg.getTo().getName());
                vh.from.setText(leg.getFrom().getName());
                if (leg.getLine() == null) {
                    vh.mode.setText(leg.getMode());
                } else {
                    vh.mode.setText(leg.getLine() + " " + leg.getDirection());
                }
                Log.d("line", leg.getLine() + " " + leg.getDirection() + " " + leg.getMode());
            }else{
            if(convertView == null){
                convertView = inflater.inflate(R.layout.smart_listview_button,null);
                TextView t1,t2,t3;
                Button b1,b2;
                b1=convertView.findViewById(R.id.google);
                b2=convertView.findViewById(R.id.callbike);
                t1=convertView.findViewById(R.id.stathl);
                t2=convertView.findViewById(R.id.bikestats);
                t3=convertView.findViewById(R.id.availability);
                b1.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

//                                                  Log.d("Transfer().getX()",trip.getBikeTrip().getTransferStation().getX());
                                              String uri = "http://maps.google.com/maps?saddr=" +
                                                      trip.getBikeTrip().getTransferStation().getX()+ "," +
                                                      trip.getBikeTrip().getTransferStation().getY()+ "&daddr=" +
                                                      trip.getBikeTrip().getTo().getLat()+ "," + trip.getBikeTrip().getTo().getLon()+"&travelmode=bicycling";
                                              Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                              context.startActivity(intent);
                                          }
                                      }
                );
                b2.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent openCall = new Intent (Intent.ACTION_VIEW);
                        if(openCall==null){
                            Toast.makeText(context, "CallaBike is not installed", Toast.LENGTH_SHORT).show();
                        }else{
                            openCall.setData(Uri.parse(trip.getBikeTrip().getFrom().getReservationLink()));
                            context.startActivity(openCall);
                        }
                    }
                });

            }
        }





            return convertView;
        }
    }


