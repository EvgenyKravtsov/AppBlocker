package evgenykravtsov.appblocker.presentation.view.activity;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.App;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.external.android.AppBlockerController;
import evgenykravtsov.appblocker.presentation.adapter.AppsAdapter;
import evgenykravtsov.appblocker.presentation.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity
        implements MainPresenter.View {

    private MainPresenter presenter;

    private DrawerLayout navigationDrawer;
    private LinearLayout mainLayout;
    private LinearLayout feedbackButton;
    private LinearLayout exerciseSettingsButton;
    private TextView blockStatusTextView;
    private SwitchCompat blockControlSwitch;
    private RecyclerView appsRecyclerView;

    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForPackageUserStatsPermission();

        prepareActionBar();
        bindViews();
        bindViewListeners();
        prepareAppsRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindPresenter();

        if (presenter.getPasswordActivationStatus() && !AppBlockerController.passwordPassed)
            showRequestPasswordDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean appBlockerStatus = presenter.getAppBlockerStatus();
        setBlockControlState(appBlockerStatus);
        switchControlButtonState(appBlockerStatus);

        presenter.requestApps();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindPresenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (navigationDrawer.isDrawerOpen(GravityCompat.START)) {
                    navigationDrawer.closeDrawer(GravityCompat.START);
                    switchActionBarHomeIcon(false);
                } else {
                    navigationDrawer.openDrawer(GravityCompat.START);
                    switchActionBarHomeIcon(true);
                }

                return true;
            case R.id.menu_main_item_share:
                Intent sendIntent = prepareShareIntent();
                startActivity(Intent.createChooser(sendIntent, "Share"));
                presenter.appHasBeenShared();
                return true;

            default: return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (navigationDrawer.isDrawerOpen(GravityCompat.START)) {
                navigationDrawer.closeDrawer(GravityCompat.START);
                switchActionBarHomeIcon(false);
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    ////

    @Override
    public void showApps(List<App> apps) {

        AppsAdapter adapter = new AppsAdapter(apps);
        appsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setBlockControlState(boolean state) {
        switchControlButtonState(state);
    }

    @Override
    public void notifyBlockControlStateChanged(boolean state) {
        String message = state ?
                getString(R.string.main_screen_blocker_activated) :
                getString(R.string.main_screen_blocker_deactivated);
        showSnackbar(message);
    }

    @Override
    public void showSetParentPasswordRecommendation() {
        showOnboardingDialog(
                getString(R.string.main_screen_set_parent_password_recommendation_text),
                getString(R.string.main_screen_set_parent_password_recommendation_button_label),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, ExerciseSettingsActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void showUserFeedbackRequest() {
        showOnboardingDialog(
                getString(R.string.main_screen_user_feedback_request_text),
                getString(R.string.main_screen_user_feedback_recommendation_button_label),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                        startActivity(intent);
                    }
                });
    }

    ////

    @SuppressWarnings("ConstantConditions")
    private void prepareActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.navigation_menu_icon);
    }

    private void bindPresenter() {
        presenter = new MainPresenter(this,
                DependencyInjection.provideAppBlockerSettings(),
                UseCaseThreadPool.getInstance());
        EventBus.getDefault().register(presenter);
    }

    private void unbindPresenter() {
        presenter.unbindView();
        EventBus.getDefault().unregister(presenter);
        presenter = null;
    }

    private void bindViews() {
        navigationDrawer = (DrawerLayout) findViewById(R.id.main_activity_navigation_drawer);
        mainLayout = (LinearLayout) findViewById(R.id.main_activity_main_layout);
        feedbackButton = (LinearLayout) findViewById(R.id.main_activity_feedback_button);
        exerciseSettingsButton = (LinearLayout) findViewById(R.id.main_activity_exercise_settings_button);
        blockStatusTextView = (TextView) findViewById(R.id.main_activity_block_status_text_view);
        blockControlSwitch = (SwitchCompat) findViewById(R.id.main_activity_block_control_switch);
        appsRecyclerView = (RecyclerView) findViewById(R.id.main_activity_apps_recycler_view);
    }

    private void bindViewListeners() {
        navigationDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                switchActionBarHomeIcon(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                switchActionBarHomeIcon(false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        exerciseSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExerciseSettingsActivity.class);
                startActivity(intent);
            }
        });

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });
    }

    private void prepareAppsRecyclerView() {
        appsRecyclerView.setHasFixedSize(true);
        appsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        appsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @SuppressWarnings("ConstantConditions")
    private void switchActionBarHomeIcon(boolean navigationDrawerOpened) {
        if (navigationDrawerOpened) getSupportActionBar().setHomeAsUpIndicator(R.drawable.navigation_arrow_back);
        else getSupportActionBar().setHomeAsUpIndicator(R.drawable.navigation_menu_icon);
    }

    private void switchControlButtonState(boolean enabled) {
        String blockStatus = enabled ?
                getString(R.string.main_screen_activated) :
                getString(R.string.main_screen_deactivated);
        setTextAnimatedForTextView(blockStatusTextView, blockStatus);

        blockControlSwitch.setChecked(enabled);
        blockControlSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                presenter.toggleAppBlocker();
            }
        });
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(mainLayout, message, Snackbar.LENGTH_SHORT);
        View snackbarLayout = snackbar.getView();
        snackbarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    private void setTextAnimatedForTextView(TextView textView, String text) {
        Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(400);
        textView.startAnimation(fadeOut);

        textView.setText(text);

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(400);
        textView.startAnimation(fadeIn);
    }

    @SuppressLint("InflateParams")
    private void showRequestPasswordDialog() {
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
                    AppBlockerController.passwordPassed = true;
                    dialog.dismiss();
                } else notifyWrongPassword();
            }
        });
    }

    private void notifyWrongPassword() {
        showSnackbar(getString(R.string.main_screen_wrong_password));
    }

    private void showOnboardingDialog(
            String message,
            String positiveLabel,
            DialogInterface.OnClickListener positiveListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(positiveLabel, positiveListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPackageUserStatsPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.main_screen_permission_dialog_text))
                .setPositiveButton(
                        getString(R.string.main_screen_permission_dialog_button_label),
                        new DialogInterface.OnClickListener() {
                    @SuppressLint("InlinedApi")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void checkForPackageUserStatsPermission() {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
            AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(
                    "android:get_usage_stats",
                    android.os.Process.myUid(),
                    getPackageName());

            boolean granted = mode == AppOpsManager.MODE_ALLOWED;

            if (!granted) showPackageUserStatsPermissionDialog();
        }
    }

    private Intent prepareShareIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        intent.setType("text/plain");
        return intent;
    }
}
