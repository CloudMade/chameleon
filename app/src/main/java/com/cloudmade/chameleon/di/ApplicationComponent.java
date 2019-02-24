package com.cloudmade.chameleon.di;

import com.cloudmade.chameleon.ThemeColorProvider;
import com.cloudmade.chameleon.ThemeDrawableProvider;
import com.cloudmade.chameleon.util.ui.UIStyleManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    ActivityComponent plus(ActivityModule activityModule);

    ThemeColorProvider themeColorProvider();

    ThemeDrawableProvider themeDrawableProvider();

    UIStyleManager uiStyleManager();
}
