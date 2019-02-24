package com.cloudmade.chameleon.viewmodel;

import com.cloudmade.chameleon.ChameleonTheme;
import com.cloudmade.chameleon.util.ui.UIStyleManager;

import javax.inject.Inject;

public class ViewModelMainActivity extends BaseViewModel {

    @Inject
    UIStyleManager uiStyleManager;

    public void onToggleThemeButtonClick() {
        uiStyleManager.changeTheme(uiStyleManager.getThemeObservable()
                .blockingFirst() == ChameleonTheme.DAY_LEFT_WINTER ? ChameleonTheme.NIGHT_LEFT_WINTER : ChameleonTheme.DAY_LEFT_WINTER);
    }
}
