package a.sboev.smack;

import java.util.List;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.initializer.SmackInitializer;
import org.jivesoftware.smack.util.stringencoder.Base64;
import org.jivesoftware.smack.util.stringencoder.Base64UrlSafeEncoder;
import org.jivesoftware.smack.util.stringencoder.android.AndroidBase64Encoder;
import org.jivesoftware.smack.util.stringencoder.android.AndroidBase64UrlSafeEncoder;

import android.content.Context;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.minidns.dnsserverlookup.android21.AndroidUsingLinkProperties;

public class AndroidSmackInitializer implements SmackInitializer {

    @Override
    public List<Exception> initialize() {
        SmackConfiguration.setDefaultHostnameVerifier(new StrictHostnameVerifier());

        Base64.setEncoder(AndroidBase64Encoder.getInstance());
        Base64UrlSafeEncoder.setEncoder(AndroidBase64UrlSafeEncoder.getInstance());
        return null;
    }

    /**
     * Initializes Smack on Android. You should call this method fore performing your first XMPP connection with Smack.
     *
     * @param context an Android context.
     * @since 4.3
     */
    public static void initialize(Context context) {
        AndroidUsingLinkProperties.setup(context);
    }
}
