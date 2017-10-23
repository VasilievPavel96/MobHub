package com.vasilievpavel.mobhub.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vasilievpavel.mobhub.R;
import com.vasilievpavel.mobhub.commons.CustomApplication;
import com.vasilievpavel.mobhub.mvp.presenter.ProfilePresenter;
import com.vasilievpavel.mobhub.mvp.view.LceView;
import com.vasilievpavel.mobhub.rest.GitHubApi;
import com.vasilievpavel.mobhub.rest.XmlParser;
import com.vasilievpavel.mobhub.rest.model.ProfileInfo;
import com.vasilievpavel.mobhub.rest.model.User;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends MvpAppCompatFragment implements LceView<ProfileInfo> {
    public static final String ARGUMENT_LOGIN = "ARGUMENT_LOGIN";
    public static final String TAG = "PROFILE";
    @BindView(R.id.iv_profile)
    ImageView profile;
    @BindView(R.id.tv_login)
    TextView login;
    @BindView(R.id.tv_repos)
    TextView repos;
    @BindView(R.id.tv_followers)
    TextView followers;
    @BindView(R.id.tv_following)
    TextView following;
    @BindView(R.id.tv_name)
    TextView name;
    @BindView(R.id.tv_location)
    TextView location;
    @BindView(R.id.tv_email)
    TextView email;
    @BindView(R.id.tv_company)
    TextView company;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.iv_contributions)
    ImageView contributions;
    @BindView(R.id.profile_content)
    ConstraintLayout profile_content;
    @InjectPresenter
    ProfilePresenter presenter;
    @Inject
    GitHubApi gitHubApi;
    @Inject
    XmlParser parser;

    public static ProfileFragment newInstance(String login) {
        Bundle args = new Bundle();
        args.putString(ARGUMENT_LOGIN, login);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.getComponent().inject(this);
        String login = getArguments().getString(ARGUMENT_LOGIN);
        presenter.load(login);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        view.setTag(TAG);
    }

    @Override
    public void showLoading() {
        profile_content.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContent(ProfileInfo data) {
        User user = data.getUser();
        progressBar.setVisibility(View.GONE);
        profile_content.setVisibility(View.VISIBLE);
        Glide.with(this).load(user.getThumbnail()).apply(RequestOptions.circleCropTransform()).into(profile);
        login.setText(user.getLogin());
        repos.setText(formatString("Repos", user.getRepos()));
        followers.getText();
        followers.setText(formatString("Followers", user.getFollowers()));
        following.setText(formatString("Following", user.getFollowing()));
        name.setText(user.getName());
        location.setText(user.getLocation());
        email.setText(user.getEmail());
        company.setText(user.getCompany());
        List<Map<String, String>> contrib = data.getContributions();
        if (contrib.size() != 0) {
            Bitmap bitmap = createContributionsBitmap(contrib);
            contributions.setImageBitmap(bitmap);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Fade fade = new Fade(Fade.IN);
            fade.setDuration(300);
            TransitionManager.beginDelayedTransition(profile_content, fade);
        }
    }

    private String formatString(String name, int value) {
        return String.format("%d \n %s", value, name);
    }

    @Override
    public void showError(Throwable throwable) {
        progressBar.setVisibility(View.GONE);
        profile_content.setVisibility(View.GONE);
        Snackbar.make(contributions, "Can't fetch profile info", Snackbar.LENGTH_LONG).show();
    }

    public Bitmap createContributionsBitmap(List<Map<String, String>> days) {
        Bitmap bitmap = Bitmap.createBitmap(520, 70, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        int x = 0;
        int y = 0;
        int i = 0;
        for (Map<String, String> day : days) {
            if (i == 7) {
                i = 0;
                y = 0;
                x += 10;
            }
            String hexColor = day.get("fill");
            paint.setColor(Color.parseColor(hexColor));
            canvas.drawRect(x, y, x + 10, y + 10, paint);
            i++;
            y += 10;
        }
        paint.setColor(Color.WHITE);
        for (i = 10; i < 520; i += 10) {
            canvas.drawLine(i, 0, 1 + i, 70, paint);
        }
        for (i = 10; i < 70; i += 10) {
            canvas.drawLine(0, i, 520, 1 + i, paint);
        }
        return bitmap;
    }
}
