package com.example.anja.meteorquest.Minigames;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.anja.meteorquest.NavigationMethod.Navigation;
import com.example.anja.meteorquest.NavigationMethod.NavigationActivity;
import com.example.anja.meteorquest.Other.Database;
import com.example.anja.meteorquest.Other.Victory;
import com.example.anja.meteorquest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChargeTheBattery extends AppCompatActivity implements SensorEventListener {
    public Sensor mySensor;
    public SensorManager SM;

    ImageButton button1;
    ImageButton button2;
    ImageButton button3;

    Boolean player1Ready = false;
    Boolean player2Ready = false;
    Boolean player3Ready = false;

    String gameState;
    int counter = 0;
    int imageCounter = 0;
    ImageView middleImage;
    MediaPlayer mediaPlayer, mediaDrainBattery;
    TextView txt1, txt2, txt3;
    public Handler handler = new Handler();
    public int delay = 500; //milliseconds
    int oldNumber = 0;
    public boolean chargingSound = false;
    int length = 0;
    ProgressBar progressBar;
    int secondTime = 0;
    int drainBattery = 500;
    public boolean stopHandler = false;
    boolean actionUp = false;
    public FirebaseDatabase database;
    public DatabaseReference rootReference;
    boolean guideTalking = false;
    public static boolean chargeBatteryDone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_charge_battery);

        rootReference = Database.getDatabaseRootReference();
        DatabaseReference chargethebatteryReference = rootReference.child("chargethebattery");
        chargethebatteryReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey().toString();
                    String value = ds.getValue().toString();
                    if(key.equals("soundfile1") && value.equals("true")){
                        //String key = ds.getKey().toString();
                        playHelpSoundfile(key);
                        break;
                    }
                    if(key.equals("soundfile2") && value.equals("true")){
                        //play soundfile1
                        playHelpSoundfile(key);
                        break;

                    }
                    if(key.equals("soundfile3") && value.equals("true")){
                        //play soundfile1
                        playHelpSoundfile(key);
                        break;

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });


        progressBar = (ProgressBar)findViewById(R.id.progress);

        CharSequence colors[] = new CharSequence[] {"1", "2"};

        button1 = (ImageButton)findViewById(R.id.button1);
        button2 = (ImageButton)findViewById(R.id.button2);
        button3 = (ImageButton)findViewById(R.id.button3);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);
        middleImage = (ImageView) findViewById(R.id.animationDown);
        mediaPlayer = MediaPlayer.create(this, R.raw.power_up);
        // Create sensor manager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // MiniGameDrink sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //changeImage();
        drainBattery();
        //changeThumbImg();

        button1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        player1Ready = true;
                        actionUp = false;
                        button1.setBackgroundResource(R.drawable.greenthumb3);
                        if (player1Ready == true && player2Ready == true && player3Ready == true){
                            // Register sensor listener
                            SM.registerListener(ChargeTheBattery.this, mySensor, SensorManager.SENSOR_DELAY_GAME);
                            txt1.setText("SHAKE!");
                            txt2.setText("SHAKE!");
                            txt3.setText("SHAKE!");
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        // End
                        //SM.unregisterListener(ChargeTheBattery.this);
                        if(stopHandler == false) {
                            mediaPlayer.pause();
                        }
                        actionUp = true;
                        actionUp();

                        txt1.setText("");
                        txt2.setText("");
                        txt3.setText("");
                        //so the players can start over if someone fails. They just have to release the button and press it again.
                        player1Ready = false;
                        button1.setBackgroundResource(R.drawable.thumb_scanner3);
                        return true;
                }
                return false;
            }
        });

        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        player2Ready = true;
                        actionUp = false;
                        button2.setBackgroundResource(R.drawable.greenthumb2);
                        if (player1Ready == true && player2Ready == true && player3Ready == true){
                            // Register sensor listener
                            SM.registerListener(ChargeTheBattery.this, mySensor, SensorManager.SENSOR_DELAY_GAME);
                            txt1.setText("SHAKE!");
                            txt2.setText("SHAKE!");
                            txt3.setText("SHAKE!");
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        // End
                        //SM.unregisterListener(ChargeTheBattery.this);
                        if(stopHandler == false) {
                            mediaPlayer.pause();
                        }
                        actionUp = true;
                        actionUp();
                        //counter = 0;
                        txt1.setText("");
                        txt2.setText("");
                        txt3.setText("");
                        //so the players can start over if someone fails. They just have to release the button and press it again.
                        //txt2.setText("Place thumb on marker");
                        player2Ready = false;
                        button2.setBackgroundResource(R.drawable.thumb_scanner2);
                        return true;
                }
                return false;
            }
        });

        button3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        player3Ready = true;
                        actionUp = false;
                        button3.setBackgroundResource(R.drawable.greenthumb);
                        if (player1Ready == true && player2Ready == true && player3Ready == true){
                            // Register sensor listener
                            SM.registerListener(ChargeTheBattery.this, mySensor, SensorManager.SENSOR_DELAY_GAME);
                            txt1.setText("SHAKE!");
                            txt2.setText("SHAKE!");
                            txt3.setText("SHAKE!");
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        // End
                        //SM.unregisterListener(ChargeTheBattery.this);
                        if(stopHandler == false) {
                            mediaPlayer.pause();
                        }
                        actionUp = true;
                        actionUp();
                        //counter = 0;
                        txt1.setText("");
                        txt2.setText("");
                        txt3.setText("");
                        //so the players can start over if someone fails. They just have to release the button and press it again.
                        //txt2.setText("Place thumb on marker");
                        player3Ready = false;
                        button3.setBackgroundResource(R.drawable.thumb_scanner);
                        return true;
                }
                return false;
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        SM.unregisterListener(ChargeTheBattery.this);
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float yFloat = event.values[1];
        float zFloat = event.values[2];

        //change zFloat back to 11
        if(zFloat > 11 && yFloat > -5 && yFloat < 5) {
            counter = counter + 8;
            //play charging sound
            if(chargingSound != true && guideTalking != true){
                chargingSound = true;
                //start long charging sound!!
                mediaPlayer.start();
                mediaPlayer.seekTo(length);
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));

            }
        }

        if(oldNumber > counter){
            chargingSound = false;
            mediaPlayer.pause();
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-drainBattery);
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
            length = mediaPlayer.getCurrentPosition();
        }
        oldNumber = counter;
    }

    public void playHelpSoundfile(String soundfile){
        length = mediaPlayer.getCurrentPosition();
        guideTalking = true;
        mediaPlayer.release();
        mediaPlayer = null;
        if(soundfile.equals("soundfile1")){
            mediaPlayer = MediaPlayer.create(this, R.raw.tada);
        }
        if(soundfile.equals("soundfile2")){
            mediaPlayer = MediaPlayer.create(this, R.raw.tada);
        }
        if(soundfile.equals("soundfile3")){
            mediaPlayer = MediaPlayer.create(this, R.raw.tada);
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                guideTalking = false;
                chargingSound = false;
                mediaPlayer.release();
                mediaPlayer = null;
                createMediaPlayer();
            }

        });

        mediaPlayer.start();
    }

    public void createMediaPlayer(){
        mediaPlayer = MediaPlayer.create(this, R.raw.power_up);
        mediaPlayer.seekTo(length);
    }

    public void drainBattery(){
        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(stopHandler == false) {

                    if (progressBar.getProgress() >= 12) {
                        stopHandler = true;
                        victory();
                    }

                    if (counter > 0) {
                        counter--;

                        progressBar.setProgress(mediaPlayer.getCurrentPosition() / 1000);

                    }
                }

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    public void actionUp(){
        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(actionUp == true) {
                    if (stopHandler == false) {
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 100);
                        progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                        progressBar.setProgress(mediaPlayer.getCurrentPosition() / 1000);

                    }
                }

                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    public void changeImage(){
        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                imageCounter ++;
                if(player1Ready == false || player2Ready == false || player3Ready == false) {
                    if (imageCounter == 1) {
                        middleImage.setImageResource(R.drawable.battery_singleplayerdown);
                    }
                    if (imageCounter == 2) {
                        middleImage.setImageResource(R.drawable.battery_singleplayerup);
                        imageCounter = 0;
                    }
                }

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    public void victory(){
        SM.unregisterListener(ChargeTheBattery.this);
        Navigation.minigame1Done = true;
        Navigation.gameRunning = false;
        mediaPlayer.release();
        mediaPlayer = null;
        mediaPlayer = MediaPlayer.create(this, R.raw.tada);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                mediaPlayer = null;
                chargeBatteryDone = true;
                Intent intent = new Intent(ChargeTheBattery.this, Victory.class);
                startActivity(intent);
            }

        });


    }

    public void fullscreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_charge_battery);
    }

    @Override
    public void onBackPressed() {
        // your code.
        mediaPlayer.release();
        mediaPlayer = null;
        Intent intent = new Intent(ChargeTheBattery.this, NavigationActivity.class);
        startActivity(intent);
    }



}
