package com.ab95.hackapella_pos;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkClient implements Runnable{
    Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    LocationManager locationManager;
    LocationListener locationListener;
    Location location;
    Activity activity;

    public NetworkClient(Activity activity) {
        this.activity = activity;

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location newLocation) {
                location = newLocation;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    @Override
    public void run() {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                }
            });
            socket = new Socket("10.83.3.175", 7000);
            Log.i("Network", "connected");
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            send(locationToString(location));
        }
        catch (IOException e) {
            Log.e("NetworkClient", e.getMessage());
        }
    }

    private void send(String string) {
        try {
            outputStream.write(string.getBytes());
            Log.i("Network", "sent");
        }
        catch (IOException | NullPointerException e) {
            Log.e("NetworkClient", e.getMessage());
        }
    }

    private String locationToString(Location location) {
        String string = "";
        string += Double.toString(location.getLatitude());
        string += ",";
        string += Double.toString(location.getLongitude());
        string += ",";
        string += Long.toString(location.getTime());
        string += ",";
        return string;
    }


}
