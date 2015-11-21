package com.ab95.hackapella_pos;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements CardReader.AccountCallback {

    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    public CardReader cardReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardReader = new CardReader(this);

        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null) {
            nfc.enableReaderMode(this, cardReader, READER_FLAGS, null);
        }
    }

    @Override
    public void onAccountReceived() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)MainActivity.this.findViewById(R.id.reading)).setText("Reading card...");
            }
        });
        NetworkClient networkClient = new NetworkClient(this);
        Thread networkClientThread = new Thread(networkClient);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.findViewById(R.id.processing).setVisibility(View.VISIBLE);
            }
        });
        networkClientThread.start();
    }

}
