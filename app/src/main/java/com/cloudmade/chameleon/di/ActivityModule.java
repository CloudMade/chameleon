package com.cloudmade.chameleon.di;

import com.cloudmade.chameleon.ui.MainActivity;
import com.cloudmade.chameleon.viewmodel.ViewModelMainActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private MainActivity mainActivity;

    public ActivityModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    @ActivityScope
    ViewModelMainActivity viewModelMainActivity() {
        return new ViewModelMainActivity();
    }
}
