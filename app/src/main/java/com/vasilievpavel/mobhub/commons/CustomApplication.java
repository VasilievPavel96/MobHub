package com.vasilievpavel.mobhub.commons;

import android.app.Application;

import com.vasilievpavel.mobhub.di.ApplicationComponent;
import com.vasilievpavel.mobhub.di.ApplicationModule;
import com.vasilievpavel.mobhub.di.DaggerApplicationComponent;


public class CustomApplication extends Application {
    private static ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
    }

    private void initComponent() {
        component = DaggerApplicationComponent.builder().
                applicationModule(new ApplicationModule(this))
                .build();
    }

    public static ApplicationComponent getComponent() {
        return component;
    }
}
