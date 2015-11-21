package com.ab95.hackapella_pos;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
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
    Location location;

    public NetworkClient(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void run() {
        try {
            socket = new Socket("10.83.3.175", 7000);
            Log.i("Network", "connected");
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

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
        return string;
    }


}
