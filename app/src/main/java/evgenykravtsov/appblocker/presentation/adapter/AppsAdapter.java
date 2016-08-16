package evgenykravtsov.appblocker.presentation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import evgenykravtsov.appblocker.R;
import evgenykravtsov.appblocker.domain.model.App;
import evgenykravtsov.appblocker.domain.usecase.UseCaseFactory;
import evgenykravtsov.appblocker.domain.usecase.UseCaseThreadPool;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    private List<App> apps;

    ////

    public AppsAdapter(List<App> apps) {
        this.apps = apps;
    }

    ////

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_apps, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        App app = apps.get(position);
        holder.iconImageView.setImageDrawable(app.getIcon());
        holder.appTitleTextView.setText(app.getTitle());
        holder.blockedCheckBox.setChecked(app.isBlocked());
        holder.blockedCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                UseCaseThreadPool threadPool = UseCaseThreadPool.getInstance();
                App chosenApp = apps.get(holder.getAdapterPosition());

                if (checked) threadPool.execute(UseCaseFactory.provideBlockAppUseCase(chosenApp));
                else threadPool.execute(UseCaseFactory.provideUnblockAppUseCase(chosenApp));
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    ////

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iconImageView;
        TextView appTitleTextView;
        CheckBox blockedCheckBox;

        ////

        public ViewHolder(View itemView) {
            super(itemView);
            iconImageView = (ImageView) itemView
                    .findViewById(R.id.apps_list_item_app_icon_image_view);
            appTitleTextView = (TextView) itemView
                    .findViewById(R.id.apps_list_item_app_title_text_view);
            blockedCheckBox = (CheckBox) itemView
                    .findViewById(R.id.apps_list_item_blocked_check_box);
        }
    }
}
