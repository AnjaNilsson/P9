package com.example.rasmus.meteorquest.Minigames;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by rasmu on 12-02-2018.
 */

public class Minigame {
    /*
    Set static boolean gameRunning to false and minigameDone1,2 and 3 to true after each game!!!
     */


    public void startGame(String game, Activity activity){

        if(game.equals("1")) {
            Intent intent = new Intent(activity,PuzzleQuest.class);
            activity.startActivity(intent);

        }

        if(game.equals("2")) {
            Intent intent = new Intent(activity, ChargeTheBattery.class);
            activity.startActivity(intent);
        }

        if(game.equals("3")) {
            Intent intent = new Intent(activity, MeteorQuest.class);
            activity.startActivity(intent);
        }



    }


}
