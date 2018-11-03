package com.bookbud.hp.firebasebook;

/**
 * Created by HP on 29-12-2017.
 */
import android.content.Intent;
import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class FCM extends FirebaseMessagingService {

    public FCM() {
    }

    public static final String INTENT_FILTER = "Remote_Execute";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Intent intent = new Intent(INTENT_FILTER);
        sendBroadcast(intent);

        Map<String, String> data = remoteMessage.getData();
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}
