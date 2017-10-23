package com.vasilievpavel.mobhub.di;

import android.app.Application;
import android.content.Context;

import com.vasilievpavel.mobhub.commons.ColorManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ApplicationModule {
    Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    public ColorManager provideColorManager(Context context) {
        return new ColorManager(context);
    }
}
