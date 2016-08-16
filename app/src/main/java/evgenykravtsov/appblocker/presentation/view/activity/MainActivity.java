package evgenykravtsov.appblocker.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.App;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.adapter.AppsAdapter;
import evgenykravtsov.appblocker.presentation.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity
        implements MainPresenter.View {

    private MainPresenter presenter;

    private DrawerLayout navigationDrawer;
    private LinearLayout feedbackButton;
    private LinearLayout exerciseSettingsButton;
    private ImageButton blockControlButton;
    private RecyclerView appsRecyclerView;

    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareActionBar();

        bindViews();
        bindViewListeners();
        prepareAppsRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindPresenter();
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
        feedbackButton = (LinearLayout) findViewById(R.id.main_activity_feedback_button);
        exerciseSettingsButton = (LinearLayout) findViewById(R.id.main_activity_exercise_settings_button);
        blockControlButton = (ImageButton) findViewById(R.id.main_activity_block_control_button);
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

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        exerciseSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExerciseSettingsActivity.class);
                startActivity(intent);
            }
        });

        blockControlButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                presenter.toggleAppBlocker();
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
        if (navigationDrawerOpened)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.navigation_arrow_back);
        else
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.navigation_menu_icon);
    }

    private void switchControlButtonState(boolean enabled) {
        if (enabled) {
            blockControlButton.setImageDrawable(
                    getResources().getDrawable(R.drawable.block_control_on_button_icon));
            blockControlButton.setBackgroundDrawable(
                    getResources().getDrawable(R.drawable.block_control_on_button_background_selector));
        } else {
            blockControlButton.setImageDrawable(
                    getResources().getDrawable(R.drawable.block_control_off_button_icon));
            blockControlButton.setBackgroundDrawable(
                    getResources().getDrawable(R.drawable.block_control_off_button_background_selector));
        }
    }
}
