package com.cloudmade.chameleon.di;

import com.cloudmade.chameleon.ui.MainActivity;
import com.cloudmade.chameleon.viewmodel.ViewModelMainActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(ViewModelMainActivity viewModelMainActivity);
}
