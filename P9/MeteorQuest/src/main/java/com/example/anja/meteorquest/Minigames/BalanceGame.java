package com.example.anja.meteorquest.Minigames;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorEventListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.CountDownTimer;

import com.example.anja.meteorquest.Other.BallView;
import com.example.anja.meteorquest.Other.Victory;
import com.example.anja.meteorquest.PlayerRole;
import com.example.anja.meteorquest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BalanceGame extends Activity {

    BallView mBallView = null;
    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
    Timer mTmr = null;
    TimerTask mTsk = null;
    int mScrWidth, mScrHeight;
    float xRect, yRect, wRect, hRect;
    android.graphics.PointF mBallPos, mBallSpd;
    MediaPlayer mp;
    ImageView box;
    boolean inside;
    TextView mTextField;
    CountDownTimer timer;
    boolean stateChanged;
    String playerRole;
    Boolean player1ready, player2ready;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar
        getWindow().setFlags(0xFFFFFFFF,
                LayoutParams.FLAG_FULLSCREEN|LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_game);
        mTextField = findViewById(R.id.timer);

//        SharedPreferences shared = getSharedPreferences("your_file_name", MODE_PRIVATE);
//        playerRole = (shared.getString("PLAYERROLE", ""));

        //create pointer to main screen
        final ConstraintLayout balance_game = findViewById(R.id.balance_game);

        //get screen dimensions
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);

        mScrWidth = size.x;
        mScrHeight = size.y;

        mBallPos = new android.graphics.PointF();
        mBallSpd = new android.graphics.PointF();

        wRect = 100;
        hRect = wRect; // Square
        xRect = mScrWidth/(float)2 - wRect/(float)2;
        yRect = mScrHeight/(float)2 - hRect/(float)2;

        //ball position
        mBallPos.x = mScrWidth/(float)2;
        mBallPos.y = mScrHeight/(float)2;


        //ball speed
        mBallSpd.x = 0;
        mBallSpd.y = 0;

        //create initial ball
        mBallView = new BallView(this,mBallPos.x,mBallPos.y,60);
        balance_game.addView(mBallView); //add ball to main screen
        mBallView.invalidate(); //call onDraw in BallView

        box = findViewById(R.id.rbox);
        //listener for accelerometer
        ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).registerListener(
                new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        //set ball speed based on phone tilt (ignore Z axis)
                        mBallSpd.x = -event.values[0];
                        mBallSpd.y = event.values[1];
                        //timer event will redraw ball
                    }
                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
                },
                ((SensorManager)getSystemService(Context.SENSOR_SERVICE))
                        .getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_GAME);

        stateChanged = true;

        timer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mTextField.setText("done!");
                final Intent i = new Intent(BalanceGame.this, Victory.class);
                startActivity(i);
            }
        };

//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("https://p10project-1fc98.firebaseio.com/");
//        DatabaseReference playersReady = ref.child("PlayersReady");
//
//        if(playerRole.equals("1")){
//            playersReady.child("player1ready").setValue("true");
//        }
//        if(playerRole.equals("2")){
//            playersReady.child("player2ready").setValue("true");
//
//        }
//
//        playersReady.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot ds : dataSnapshot.getChildren()){
//                    String value = ds.getValue().toString();
//                    if(value.equals("true")){
//                        String key = ds.getKey().toString();
//                        if(key.equals("player1ready") && key.equals("player2ready")){
//                            player1ready = true;
//                            player2ready = true;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        if(player1ready && player2ready){
//            timer.start();
//        }
        timer.start();
    } //OnCreate


    //listener for menu button on phone
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Exit"); //only one menu item
        return super.onCreateOptionsMenu(menu);
    }

    //listener for menu item clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getTitle() == "Exit") //user clicked Exit
            finish(); //will call onPause
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause()
    {
        mTmr.cancel(); //kill\release timer (our only background thread)
        mTmr = null;
        mTsk = null;
        super.onPause();
    }


    @Override
    public void onResume()
    {
        mp = MediaPlayer.create(this, R.raw.fart);

        //create timer to move ball to new position
        mTmr = new Timer();
        mTsk = new TimerTask() {
            public void run() {
                final float distX, distY;
                //move ball based on current speed
                mBallPos.x += 10 * mBallSpd.x;
                mBallPos.y += 10 * mBallSpd.y;
                if (mBallPos.x > mScrWidth - 85) mBallPos.x=mScrWidth - 85;
                if (mBallPos.y > mScrHeight) mBallPos.y=mScrHeight - 5;
                if (mBallPos.x < 60) mBallPos.x=60;
                if (mBallPos.y < 60) mBallPos.y=60;

                //detect when ball goes inside or outside of the box
                distX = Math.abs(mBallPos.x - xRect - wRect/(float)2);
                distY = Math.abs(mBallPos.y - yRect - hRect/(float)2);

                //update ball class instance
                mBallView.x = mBallPos.x;
                mBallView.y = mBallPos.y;


                //redraw ball.
                RedrawHandler.post(new Runnable() {
                    public void run() {
                        mBallView.invalidate();

                        if(distX > (wRect/(float)2) + 80) {
                            inside = false;
                        } else if(distY > (hRect/(float)2) + 80){
                            inside = false;
                        } else{
                            inside = true;
                        }
                            if(inside){
                                box.setBackgroundResource(R.drawable.rectangle);
                            if(!stateChanged){
                                timer.start();
                                stateChanged = true;
                            }
                        }
                            if(!inside){
                                if(stateChanged){
                                    timer.cancel();
                                    stateChanged = false;
                                }
                                box.setBackgroundResource(R.drawable.redrectangle);
                            }
                    }});
            }};

        mTmr.schedule(mTsk,10,10); //start timer
        super.onResume();


    } // onResume

    @Override
    public void onDestroy() //main thread stopped
    {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());  //remove app from memory
    }

    //listener for config change.
    //This is called when user tilts phone enough to trigger landscape view
    //we want our app to stay in portrait view.
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}

