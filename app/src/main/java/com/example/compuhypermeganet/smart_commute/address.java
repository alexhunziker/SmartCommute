package com.example.compuhypermeganet.smart_commute;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.text.TextWatcher;

import com.example.compuhypermeganet.smart_commute.API.rmv;
import com.example.compuhypermeganet.smart_commute.adapter.StationAdapter;
import com.example.compuhypermeganet.smart_commute.model.Station;

import java.util.ArrayList;
import java.util.List;


public class address extends AppCompatActivity {

    private ListView listView;
    private EditText editText;
    private StationAdapter adapter;

    private List<Station> stationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        listView = (ListView) findViewById(R.id.sync_suggestions);
        editText = (EditText) findViewById(R.id.from);
        stationList = new ArrayList<Station>();


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(ShowAddressActivity.this,"beforeTextChanged ",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(ShowAddressActivity.this,"onTextChanged ",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Toast.makeText(ShowAddressActivity.this,"afterTextChanged ",Toast.LENGTH_SHORT).show();
                new address.CallRMV().execute(editText.getText().toString());
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("id", stationList.get(position).getId());
                intent.putExtra("result", stationList.get(position).getName());
                setResult(1001, intent);
                finish();
            }
        });
        editText.addTextChangedListener(textWatcher);

    }

    private class CallRMV extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... params) {
            try {
                stationList = rmv.get_matching_stops(params[0]);
                return "success";
            } catch (Exception e) {

                e.printStackTrace();
            }
            return "fail";
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(String result) {
            // this is executed on the main thread after the process is over
            // update your UI here
            if (result == "success") {
                adapter = new StationAdapter(address.this, R.layout.station_item, stationList);
            }
            listView.setAdapter(adapter);
        }
    }
}
