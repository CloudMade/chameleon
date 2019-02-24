package com.cloudmade.chameleon.di;

import android.annotation.SuppressLint;
import android.app.Application;

import com.cloudmade.chameleon.ThemeColorProvider;
import com.cloudmade.chameleon.ThemeDrawableProvider;
import com.cloudmade.chameleon.util.ui.UIStyleManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application application() {
        return application;
    }

    @Provides
    @Singleton
    UIStyleManager uiStyleManager() {
        return new UIStyleManager();
    }

    @SuppressLint("CheckResult")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Provides
    @Singleton
    ThemeColorProvider themeColorProvider() {
        return new ThemeColorProvider(application);
    }

    @SuppressLint("CheckResult")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Provides
    @Singleton
    ThemeDrawableProvider themeDrawableProvider() {
        return new ThemeDrawableProvider(application);
    }
}
