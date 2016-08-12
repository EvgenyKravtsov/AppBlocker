package evgenykravtsov.appblocker.presentation.view.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.presentation.presenter.LoginPresenter;

public class LoginActivity extends AppCompatActivity
        implements LoginPresenter.View {

    private LoginPresenter presenter;

    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkPasswordStatus();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindPresenter();
    }

    ////

    @SuppressLint("InflateParams")
    @Override
    public void showSetPasswordDialog() {
        View dialogLayout = LayoutInflater.from(this).inflate(R.layout.dialog_set_password, null);

        final EditText passwordEditText = (EditText)
                dialogLayout.findViewById(R.id.set_password_dialog_password_edit_text);
        final EditText controlPasswordEditText = (EditText)
                dialogLayout.findViewById(R.id.set_password_dialog_control_password_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setView(dialogLayout)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    return true;
                } else return false;
            }
        });

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString();
                String controlPassword = controlPasswordEditText.getText().toString();

                if (!password.equals(controlPassword))
                    notifyPasswordsDontMatch();
                else if (password.equals("") && controlPassword.equals(""))
                    notifyPasswordsDontMatch();
                else {
                    notifyPasswordSaved();
                    presenter.setPassword(password);
                    navigateToMainActivity();
                    finish();
                }
            }
        });
    }

    @SuppressLint("InflateParams")
    @Override
    public void showRequestPasswordDialog() {
        View dialogLayout = LayoutInflater.from(this).inflate(R.layout.dialog_request_password, null);

        final EditText passwordEditText = (EditText)
                dialogLayout.findViewById(R.id.request_password_dialog_password_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setView(dialogLayout)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    return true;
                } else return false;
            }
        });

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString();

                if (presenter.checkPassword(password)) {
                    navigateToMainActivity();
                    finish();
                } else notifyWrongPassword();
            }
        });
    }

    ////

    private void bindPresenter() {
        presenter = new LoginPresenter(this, DependencyInjection.provideAppBlockerSettings());
    }

    private void unbindPresenter() {
        if (presenter != null) presenter.unbindView();
        presenter = null;
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void notifyPasswordsDontMatch() {
        Toast.makeText(this, "Passwords dont match", Toast.LENGTH_SHORT).show();
    }

    private void notifyPasswordSaved() {
        Toast.makeText(this, "Password saved", Toast.LENGTH_SHORT).show();
    }

    private void notifyWrongPassword() {
        Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show();
    }
}






























