package com.example.anja.meteorquest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.anja.meteorquest.Other.Database;
import com.example.anja.meteorquest.Other.Ready2StartGame;


public class PlayerRole extends AppCompatActivity {

    EditText editPlayerRole;
    Button addPlayer;
    String strPlayerRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullscreen();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_player_role);

        editPlayerRole = findViewById(R.id.playerRole);
        addPlayer = findViewById(R.id.addPlayer);
        Database database = new Database();
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

    public void addPlayer(View view) {
        //get text from EditText
        strPlayerRole = editPlayerRole.getText().toString();

        //save to sharedPreferences
        SharedPreferences prefs = getSharedPreferences("your_file_name", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PLAYERROLE", strPlayerRole);
        editor.commit();
        Intent intent = new Intent(PlayerRole.this, Ready2StartGame.class);
        startActivity(intent);

    }

}
