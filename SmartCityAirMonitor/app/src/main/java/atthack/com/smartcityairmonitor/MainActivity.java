package atthack.com.smartcityairmonitor;

import android.app.NotificationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView)findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://smartcityairmonitor.github.io/");
        setupPubNub();
    }

    private void setupPubNub()
    {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("[sub-key]");
        pnConfiguration.setPublishKey("[pub-key]");

        PubNub pubnub = new PubNub(pnConfiguration);

        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {


                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio / connectivity is lost
                }

                else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    // Connect event. You can do stuff like publish, and know you'll get it.
                    // Or just use the connected event to confirm you are subscribed for
                    // UI / internal notifications, etc
                }
                else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                }
                else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                // Handle new message stored in message.message
                if (message.getChannel() != null) {
                    // Message has been received on channel group stored in
                    // message.getChannel()
                    if(message.getChannel().equalsIgnoreCase("smartcityairmonitor"))
                    {
                        try {
                            JSONObject data = new JSONObject(message.getMessage().toString());
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(MainActivity.this)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("Smart City Air Monitor")
                                            .setContentText(data.optString("text", ""));
                            int mNotificationId = 001;
// Gets an instance of the NotificationManager service
                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                            mNotifyMgr.notify(mNotificationId, mBuilder.build());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
                else {
                    // Message has been received on channel stored in
                    // message.getSubscription()
                }

            /*
                log the following items with your favorite logger
                    - message.getMessage()
                    - message.getSubscription()
                    - message.getTimetoken()
            */
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });

        pubnub.subscribe().channels(Arrays.asList("smartcityairmonitor")).execute();
    }
}
