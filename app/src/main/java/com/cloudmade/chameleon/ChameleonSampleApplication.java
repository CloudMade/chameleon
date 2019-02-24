package com.cloudmade.chameleon;

import android.annotation.SuppressLint;
import android.app.Application;

import com.cloudmade.chameleon.di.ApplicationComponent;
import com.cloudmade.chameleon.di.ApplicationModule;
import com.cloudmade.chameleon.di.DaggerApplicationComponent;
import com.cloudmade.chameleon.util.ui.UIStyleManager;

public class ChameleonSampleApplication extends Application {

    private ApplicationComponent applicationComponent;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        UIStyleManager uiStyleManager = applicationComponent.uiStyleManager();

        uiStyleManager.getThemeObservable().subscribe(chameleonTheme -> {
            applicationComponent.themeColorProvider().onThemeChange(chameleonTheme);
            applicationComponent.themeDrawableProvider().onThemeChange(chameleonTheme);
        });
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
