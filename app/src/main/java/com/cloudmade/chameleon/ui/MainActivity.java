package com.cloudmade.chameleon.ui;

import android.os.Bundle;

import com.cloudmade.chameleon.ChameleonSampleApplication;
import com.cloudmade.chameleon.R;
import com.cloudmade.chameleon.ThemeColorProvider;
import com.cloudmade.chameleon.ThemeDrawableProvider;
import com.cloudmade.chameleon.databinding.BindingMainActivity;
import com.cloudmade.chameleon.di.ActivityComponent;
import com.cloudmade.chameleon.di.ActivityModule;
import com.cloudmade.chameleon.di.ApplicationComponent;
import com.cloudmade.chameleon.viewmodel.ViewModelMainActivity;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelMainActivity viewModelMainActivity;

    @Inject
    ThemeColorProvider themeColorProvider;

    @Inject
    ThemeDrawableProvider themeDrawableProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindingMainActivity binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ApplicationComponent applicationComponent = ((ChameleonSampleApplication) getApplication()).getApplicationComponent();
        ActivityComponent activityComponent = applicationComponent.plus(new ActivityModule(this));
        activityComponent.inject(this);
        activityComponent.inject(viewModelMainActivity);

        binding.setViewModel(viewModelMainActivity);
        binding.setThemeColorProvider(themeColorProvider);
        binding.setThemeDrawableProvider(themeDrawableProvider);

        binding.setLifecycleOwner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModelMainActivity.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModelMainActivity.onPause();
    }
}
