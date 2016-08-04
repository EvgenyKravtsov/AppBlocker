package evgenykravtsov.appblocker.presentation.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import evgenykravtsov.appblocker.DependencyInjection;
import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.App;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;
import evgenykravtsov.appblocker.presentation.adapter.AppsAdapter;
import evgenykravtsov.appblocker.presentation.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private MainPresenter presenter;

    private Button exerciseSettingsButton;
    private Button blockControlButton;
    private RecyclerView appsRecyclerView;

    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        setBlockControlState(presenter.getAppBlockerStatus());
        presenter.requestApps();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindPresenter();
    }

    ////

    @Override
    public void showApps(List<App> apps) {
        AppsAdapter adapter = new AppsAdapter(apps);
        appsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void setBlockControlState(boolean state) {
        if (state) blockControlButton.setText("Turn Off");
        else blockControlButton.setText("Turn On");
    }

    ////

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
        exerciseSettingsButton = (Button) findViewById(R.id.main_activity_exercise_settings_button);
        blockControlButton = (Button) findViewById(R.id.main_activity_block_control_button);
        appsRecyclerView = (RecyclerView) findViewById(R.id.main_activity_apps_recycler_view);
    }

    private void bindViewListeners() {
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
}
