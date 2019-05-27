package com.example.anja.meteorquest;

public class Player {

    public String playerRole;
    public double latitude;
    public double longitude;

    public Player(){

    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }


}
