package evgenykravtsov.appblocker.presentation.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.external.android.MailAgent;
import evgenykravtsov.appblocker.presentation.presenter.FeedbackPresenter;

public class FeedbackActivity extends AppCompatActivity
        implements FeedbackPresenter.View {

    private static final String DEVELOPERS_ADDRESS = "rse@kevers.top";
    private static final String FEEDBACK_SUBJECT = "Feedback from user";

    private FeedbackPresenter presenter;

    private EditText feedbackEditText;
    private Button sendButton;

    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        bindViews();
        bindViewListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindPresenter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindPresenter();
    }

    ////

    private void bindPresenter() {
        presenter = new FeedbackPresenter(this);
    }

    private void unbindPresenter() {
        if (presenter != null) presenter.unbindView();
        presenter = null;
    }

    private void bindViews() {
        feedbackEditText = (EditText) findViewById(R.id.feedback_activity_feedback_edit_text);
        sendButton = (Button) findViewById(R.id.feedback_activity_send_button);
    }

    private void bindViewListeners() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback = feedbackEditText.getText().toString();
                if (!feedback.equals("")) {
                    MailAgent mailAgent = new MailAgent(
                            FeedbackActivity.this,
                            DEVELOPERS_ADDRESS,
                            FEEDBACK_SUBJECT,
                            feedback
                    );

                    mailAgent.send();
                }
            }
        });
    }
}
