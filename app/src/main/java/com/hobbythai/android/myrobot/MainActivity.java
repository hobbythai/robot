package com.hobbythai.android.myrobot;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class MainActivity extends AppCompatActivity {

    //explicit
    private TextView textView;
    private SeekBar seekBar;
    private int anInt = 0;
    private RadioGroup radioGroup;
    private static final String[] strings = new String[]{"servo1=","servo2=","servo3=","servo4="};
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind widget
        textView = (TextView) findViewById(R.id.textView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        radioGroup = (RadioGroup) findViewById(R.id.ragServo);

        //radio group controller
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.radioButton1:
                        index = 0;
                        break;
                    case R.id.radioButton2:
                        index = 1;
                        break;
                    case R.id.radioButton3:
                        index = 2;
                        break;
                    case R.id.radioButton4:
                        index = 3;
                        break;

                }

            }//on checked
        });

        //seekbar control
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                anInt = i*10;
                textView.setText(Integer.toString(anInt));

            }//on progress

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Log.d("Robot", "I --> " + anInt);

                uploadIntegerToDweet(anInt);

            }//on stop
        });

    }//main method

    private void uploadIntegerToDweet(int anInt) {

        UploadValue uploadValue = new UploadValue(MainActivity.this);
        uploadValue.execute(anInt);

    }//upload

    private class UploadValue extends AsyncTask<Integer, Void, String> {

        //explicit
        private Context context;
        private static final String urlSTRING = "https://dweet.io/dweet/for/ks_bot?";

        //constructor
        public UploadValue(Context context) {
            this.context = context;
        }//contructor

        @Override
        protected String doInBackground(Integer... integers) {

            try {

                String urlDweet = urlSTRING + strings[index] + Integer.toString(integers[0]);
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlDweet).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();


            } catch (Exception e) {
                Log.d("RobotV2", "e doinBack --> " + e.toString()); //log error
                return null;
            }

        }//do in back

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("RobotV2", "Result JSON --> " + s);

        }//on post


    }//upload thread



}//main class
