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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewTreeObserver;
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
    boolean first = true;


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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(first){
            first = false;


                listview1.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                final int[] location = new int[2];
                                final int[] loc2 = new int[2];
                                //listview1.getViewTreeObserver().removeGlobalOnLayoutListener(MainActivity.this);//

                                Log.d("locs not null"," "+listview1.getTop()+" "+listview1.getBottom()+" "+listview1.getHeight()+" "
                                        +location[0]+" "+location[1]);
                                location[0]=50;
                                loc2[0]=location[0];
                                loc2[1]=location[1]+listview1.getBottom()-50;

                                final int[] locs = { location[0] ,location[1] ,location[0] ,loc2[1]};
                                ConstraintLayout layout = findViewById(R.id.constraintLayout);
                                final MyCanvas view=new MyCanvas(MainActivity.this,locs);
                                view.invalidate();//
                                layout.addView(view);
                            }
                        }
                );


        }
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
//                view1.getViewTreeObserver().addOnGlobalLayoutListener(
//                        new ViewTreeObserver.OnGlobalLayoutListener() {
//                            @Override
//                            public void onGlobalLayout() {
//                                //listview1.getViewTreeObserver().removeGlobalOnLayoutListener(MainActivity.this);//
//                                final int[] location = new int[2];
//                                final int[] loc2 = new int[2];
//
//                                listview1.getLocationOnScreen(location);
//                                location[0] = (int)listview1.getTranslationX();
//                                location[1] = (int)listview1.getTranslationY();
//                                loc2[0]=location[0];
//                                loc2[1]=location[1]+listview1.getHeight();
//                                final int[] locs = { location[0] ,location[1] ,loc2[0] ,loc2[1]};
//                                ConstraintLayout layout = findViewById(R.id.constraintLayout);
//                                final MyCanvas view=new MyCanvas(MainActivity.this);
//                                view.invalidate();//
//                                layout.addView(view);
//                            }
//                        }
//                );
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
    public class MyCanvas extends View{

        private Canvas myCanvas;
        private Paint myPaint=new Paint();
        private int[] locs;

        public MyCanvas(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }
        public MyCanvas(Context context, int[]  locs) {
            super(context);
            this.locs = locs;
        }

        public MyCanvas(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            // TODO Auto-generated constructor stub
        }

        public MyCanvas(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // TODO Auto-generated method stub
            super.onDraw(canvas);
            setPaintDefaultStyle();
            this.myCanvas=canvas;
            if(locs!=null) {
                Log.d("locs not null"," "+locs[0]+" "+locs[1]+" "+locs[2]+" "+locs[3]);
                drawAL(locs[0], locs[1], locs[2], locs[3]);
            }else{
                drawAL(10,10,10,700);
                Log.d("locs not null","null");
            }
        }

        /**
         * 设置画笔默认样式
         */
        public void setPaintDefaultStyle(){
            myPaint.setAntiAlias(true);
            myPaint.setColor(Color.BLACK);
            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setStrokeWidth(10);
        }


        /**
         * 画圆
         * @param x x坐标
         * @param y	y坐标
         * @param radius	圆的半径
         */
        public void drawCircle(float x,float y,float radius){
            myCanvas.drawCircle(x, y, radius, myPaint);
            invalidate();
        }

        /**
         * 画一条直线
         * @param fromX 起点x坐标
         * @param fromY	起点Y坐标
         * @param toX	终点X坐标
         * @param toY	终点Y坐标
         */
        public void drawLine(float fromX,float fromY,float toX,float toY){
            Path linePath=new Path();
            linePath.moveTo(fromX, fromY);
            linePath.lineTo(toX, toY);
            linePath.close();
            myCanvas.drawPath(linePath, myPaint);
            invalidate();
        }


        /**
         * draw arrow
         * @param sx
         * @param sy
         * @param ex
         * @param ey
         */
        public void drawAL(int sx, int sy, int ex, int ey)
        {
            double H = 8; // 箭头高度
            double L = 3.5; // 底边的一半
            int x3 = 0;
            int y3 = 0;
            int x4 = 0;
            int y4 = 0;
            double awrad = Math.atan(L / H); // 箭头角度
            double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
            double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
            double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
            double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
            double y_3 = ey - arrXY_1[1];
            double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
            double y_4 = ey - arrXY_2[1];
            Double X3 = new Double(x_3);
            x3 = X3.intValue();
            Double Y3 = new Double(y_3);
            y3 = Y3.intValue();
            Double X4 = new Double(x_4);
            x4 = X4.intValue();
            Double Y4 = new Double(y_4);
            y4 = Y4.intValue();
            // draw line
            myCanvas.drawLine(sx, sy, ex, ey,myPaint);
            Path triangle = new Path();
            triangle.moveTo(ex, ey);
            triangle.lineTo(x3, y3);
            triangle.lineTo(x4, y4);
            triangle.close();
            myCanvas.drawPath(triangle,myPaint);

        }
        // 计算
        public double[] rotateVec(int px, int py, double ang, boolean isChLen, double newLen)
        {
            double mathstr[] = new double[2];
            // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
            double vx = px * Math.cos(ang) - py * Math.sin(ang);
            double vy = px * Math.sin(ang) + py * Math.cos(ang);
            if (isChLen) {
                double d = Math.sqrt(vx * vx + vy * vy);
                vx = vx / d * newLen;
                vy = vy / d * newLen;
                mathstr[0] = vx;
                mathstr[1] = vy;
            }
            return mathstr;
        }


    }


}
