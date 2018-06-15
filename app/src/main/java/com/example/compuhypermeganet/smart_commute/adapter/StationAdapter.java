package com.example.compuhypermeganet.smart_commute.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.compuhypermeganet.smart_commute.R;
import com.example.compuhypermeganet.smart_commute.model.Station;

import java.util.List;

public class StationAdapter extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    private List<Station> stationList;
//    private LayoutInflater inflater;

    public StationAdapter(Context context, int resource, List<Station> stationList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.resource = resource;
        if (stationList == null) {
            this.stationList = null;
        } else {
            this.stationList = stationList;
        }
    }

    public StationAdapter(List<Station> stationList, Context context) {
        this.stationList = stationList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return stationList == null ? 0 : stationList.size();
    }

    @Override
    public Station getItem(int position) {
        return stationList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    // caches our TextView
    public static class ViewHolder {
        TextView name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StationAdapter.ViewHolder vh = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            vh = new StationAdapter.ViewHolder();
            vh.name = (TextView) convertView.findViewById(R.id.station_name);
            convertView.setTag(vh);
        } else {
            vh = (StationAdapter.ViewHolder) convertView.getTag();
        }
        Station station = getItem(position);

        vh.name.setText(station.getName());
        return convertView;
    }
}
