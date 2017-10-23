package com.vasilievpavel.mobhub.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vasilievpavel.mobhub.commons.ColorManager;
import com.vasilievpavel.mobhub.commons.CustomApplication;
import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.rest.model.Repository;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RepositoryViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_name)
    TextView name;
    @BindView(R.id.tv_description)
    TextView description;
    @BindView(R.id.iv_language)
    ImageView languageImage;
    @BindView(R.id.tv_language)
    TextView language;
    @BindView(R.id.tv_date)
    TextView date;
    @Inject
    ColorManager manager;

    public RepositoryViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        CustomApplication.getComponent().inject(this);
    }

    public void bindRepository(Repository repository) {
        name.setText(repository.getName());
        if (repository.getDescription() != null) {
            description.setVisibility(View.VISIBLE);
            description.setText(repository.getDescription());
        } else {
            description.setVisibility(View.GONE);
        }
        if (repository.getLanguage() != null && manager.getColor(repository.getLanguage()) != null) {
            language.setVisibility(View.VISIBLE);
            language.setText(repository.getLanguage());
            languageImage.setVisibility(View.VISIBLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            String hexColor = manager.getColor(repository.getLanguage());
            drawable.setColor(Color.parseColor(hexColor));
            languageImage.setImageDrawable(drawable);
        } else {
            language.setVisibility(View.GONE);
            languageImage.setVisibility(View.GONE);
        }
        date.setText(repository.getFormattedDate());
    }
}
