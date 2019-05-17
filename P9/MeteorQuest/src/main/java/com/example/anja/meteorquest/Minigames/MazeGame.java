package com.example.anja.meteorquest.Minigames;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.support.design.widget.TextInputLayout;
import android.widget.Toast;
import android.media.MediaPlayer;
import com.example.anja.meteorquest.NavigationMethod.Navigation;
import com.example.anja.meteorquest.NavigationMethod.NavigationActivity;
import com.example.anja.meteorquest.Other.Database;
import com.example.anja.meteorquest.Other.Victory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.EditText;
import android.app.Activity;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.text.TextWatcher;


import com.example.anja.meteorquest.R;

public class MazeGame extends AppCompatActivity implements SensorEventListener{
    String playerRole;
    String value;
    ImageView background;
    View enter;
    View enter2;
    TextView txt;
    EditText input;
    int path = 2;
    SensorManager smanager;
    Sensor magnetometer;
    MediaPlayer mediaPlayer;

    public FirebaseDatabase database;
    public DatabaseReference rootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze_game);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        this.smanager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.magnetometer = smanager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        SharedPreferences shared = getSharedPreferences("your_file_name", MODE_PRIVATE);
        playerRole = (shared.getString("PLAYERROLE", ""));

        enter = (View) findViewById(R.id.enter);
        enter2 = (View) findViewById(R.id.enter2);
        input = (EditText) findViewById(R.id.input);
        txt = (TextView) findViewById(R.id.txt);
        background = (ImageView) findViewById(R.id.img);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {
        // get values for each axes X,Y,Z
        float magX = event.values[0];
        float magY = event.values[1];
        float magZ = event.values[2];
        //combine the axes to make one variable for magnetometer
        double magnitude = Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));

        enter.setVisibility(View.INVISIBLE);
        enter2.setVisibility(View.INVISIBLE);
        input.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);

        enter2.setOnClickListener(new Button.OnClickListener() {
            EditText input = (EditText) findViewById(R.id.input);
            String value = input.getText().toString();
            public void onClick(View v) {
                Intent intent = new Intent(MazeGame.this, Victory.class);
                startActivity(intent);
//                if (value == "2") {
//                    Intent intent = new Intent(MazeGame.this, Victory.class);
//                    startActivity(intent);
//                } else if(value != "2"){
//                    Toast.makeText(MazeGame.this, "Incorrect try again", Toast.LENGTH_LONG).show();
//                }
            }

        });

        if (magnitude < 90) {

            if (playerRole.equals("1")) {
                try {
                    background.setVisibility(View.VISIBLE);
                    background.setImageResource(R.drawable.player1maze);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (playerRole.equals("2")) {
                try {
                    background.setVisibility(View.VISIBLE);
                    background.setImageResource(R.drawable.player2maze);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (playerRole.equals("3")) {
                try {
                    background.setVisibility(View.VISIBLE);
                    background.setImageResource(R.drawable.player3maze);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (playerRole.equals("4")) {
                try {
                    background.setVisibility(View.VISIBLE);
                    background.setImageResource(R.drawable.player4maze);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (playerRole.equals("5")) {
                try {
                    background.setVisibility(View.VISIBLE);
                    background.setImageResource(R.drawable.player5maze);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (playerRole.equals("6")) {
                try {
                    background.setVisibility(View.VISIBLE);
                    background.setImageResource(R.drawable.player6maze);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (magnitude > 90) {
                background.setVisibility(View.GONE);
                enter2.setVisibility(View.VISIBLE);
                input.setVisibility(View.VISIBLE);
                txt.setVisibility(View.VISIBLE);
        }
    }

    public abstract class TextValidator implements TextWatcher{

    }

    @Override
    public void onAccuracyChanged(Sensor arg0,int arg1)
    {
        //might make something happen here
    }

    @Override
    public void onResume()
    {
        super.onResume();
        smanager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        smanager.unregisterListener(this);
    }


        @Override
        public void onWindowFocusChanged( boolean hasFocus){
            super.onWindowFocusChanged(hasFocus);
            if (hasFocus) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
