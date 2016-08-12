package evgenykravtsov.appblocker.external.android;

import android.content.Context;
import android.content.Intent;

public class MailAgent {

    private final Context context;
    private final String address;
    private final String subject;
    private final String text;

    ////

    public MailAgent(Context context, String address, String subject, String text) {
        this.context = context;
        this.address = address;
        this.subject = subject;
        this.text = text;
    }

    ////

    public void send() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");

        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {address});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "Sending email..."));
    }
}
