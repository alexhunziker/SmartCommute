package com.example.compuhypermeganet.smart_commute;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.compuhypermeganet.smart_commute.API.rmv;

import java.util.Date;

public class Main_location extends AppCompatActivity {
    private EditText departure, destination;
    private Button search_button;
    private String depart_id, dest_id;
    private String depart_result, dest_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_location);
        search_button = (Button) findViewById(R.id.search_button);
        departure = (EditText) findViewById(R.id.departure);
        destination = (EditText) findViewById(R.id.destination);
        departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("requestCode", "send");

                Intent intent = new Intent(Main_location.this, address.class);
                startActivityForResult(intent, 1000);
            }
        });
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("requestCode", "send");

                Intent intent = new Intent(Main_location.this, address.class);
                startActivityForResult(intent, 1002);
            }
        });


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (depart_id.isEmpty() || dest_id.isEmpty()) {
                    Toast.makeText(Main_location.this, "please enter your departure and destination", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Main_location.this, navigation.class);
                    intent.putExtra("depart_id", depart_id);
                    intent.putExtra("dest_id", dest_id);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        departure.setText(depart_result);
        destination.setText(dest_result);
        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1001) {
            String result = data.getStringExtra("result");
            depart_id = data.getStringExtra("id");
            departure.setText(result);
            depart_result = result;
            Log.d("requestCode", "1");

        }
        if (requestCode == 1002 && resultCode == 1001) {
            String result = data.getStringExtra("result");

            dest_id = data.getStringExtra("id");
            destination.setText(result);
            dest_result = result;
            Log.d("requestCode", "2");
            Log.d("requestCode", result);
        }

    }


}
