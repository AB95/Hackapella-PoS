package com.ab95.hackapella_pos;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.util.Log;

import java.lang.ref.WeakReference;

public class CardReader implements NfcAdapter.ReaderCallback {
    private WeakReference<AccountCallback> accountCallback;

    public interface AccountCallback {
        void onAccountReceived();
    }

    public CardReader(AccountCallback accountCallback) {
        this.accountCallback = new WeakReference<>(accountCallback);
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        Log.i("Tag", "Tag");
        accountCallback.get().onAccountReceived();
    }
}
