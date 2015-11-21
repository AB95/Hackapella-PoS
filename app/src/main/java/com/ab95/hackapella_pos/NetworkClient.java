package com.ab95.hackapella_pos;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkClient implements Runnable{
    Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    @Override
    public void run() {
        try {
            socket = new Socket("10.83.3.175", 7000);
            Log.i("Network", "connected");
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            send("1");
        }
        catch (IOException e) {
            Log.e("NetworkClient", e.getMessage());
        }
    }

    public void send(String string) {
        try {
            outputStream.write(string.getBytes());
            Log.i("Network", "sent");
        }
        catch (IOException | NullPointerException e) {
            Log.e("NetworkClient", e.getMessage());
        }
    }


}
