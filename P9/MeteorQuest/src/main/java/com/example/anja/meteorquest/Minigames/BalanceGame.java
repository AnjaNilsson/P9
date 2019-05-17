package com.example.anja.meteorquest.Minigames;

import com.example.anja.meteorquest.R;
import com.example.anja.meteorquest.Other.BallView;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.SensorEventListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.View;

public class BalanceGame extends Activity {

    BallView mBallView = null;
    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
    Timer mTmr = null;
    TimerTask mTsk = null;
    int mScrWidth, mScrHeight;
    android.graphics.PointF mBallPos, mBallSpd;
    SensorManager smanager;
    Sensor sensor;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar
        getWindow().setFlags(0xFFFFFFFF,
                LayoutParams.FLAG_FULLSCREEN|LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_game);
        //create pointer to main screen
        final RelativeLayout mainView = (android.widget.RelativeLayout) findViewById(R.id.main_view);
//        this.smanager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        this.sensor = smanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //get screen dimensions
        Point size = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);

        mScrWidth = size.x;
        mScrHeight = size.y;

//        String y = Integer.toString(mScrHeight);
//        String x = Integer.toString(mScrWidth);
//        Log.i("Width", x);
//        Log.i("Height", y);
//        String dimen = "Width =" + x + "Height =" + y;
//        Toast.makeText(this, dimen, Toast.LENGTH_LONG).show();

        mBallPos = new android.graphics.PointF();
        mBallSpd = new android.graphics.PointF();

        //ball position
        mBallPos.x = mScrWidth/(float)2;
        mBallPos.y = mScrHeight/(float)2;

        //ball speed
        mBallSpd.x = 0;
        mBallSpd.y = 0;

        //create initial ball
        mBallView = new BallView(this,mBallPos.x,mBallPos.y,60);

        mainView.addView(mBallView); //add ball to main screen
        mBallView.invalidate(); //call onDraw in BallView

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

        //create timer to move ball to new position
        mTmr = new Timer();
        mTsk = new TimerTask() {
            public void run() {

                //move ball based on current speed
                mBallPos.x += 3 * mBallSpd.x;
                mBallPos.y += 3 * mBallSpd.y;

                //if ball goes off screen, reposition to opposite side of screen
                if (mBallPos.x > mScrWidth - 90) mBallPos.x=mScrWidth - 90;
                if (mBallPos.y > mScrHeight - 90) mBallPos.y=mScrHeight - 90;
                if (mBallPos.x < 60) mBallPos.x=60;
                if (mBallPos.y < 60) mBallPos.y=60;
                //update ball class instance
                mBallView.x = mBallPos.x;
                mBallView.y = mBallPos.y;
                //redraw ball.
//                RedrawHandler.post(new Runnable() {
//                    public void run() {
//                        mBallView.invalidate();
//                    }});
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

}
