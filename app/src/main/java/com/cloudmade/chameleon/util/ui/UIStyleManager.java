package com.cloudmade.chameleon.util.ui;

import com.cloudmade.chameleon.ChameleonTheme;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class UIStyleManager {

    private BehaviorSubject<ChameleonTheme> themeBehaviorSubject = BehaviorSubject.createDefault(ChameleonTheme.DAY_LEFT_WINTER);

    public Observable<ChameleonTheme> getThemeObservable() {
        return themeBehaviorSubject;
    }

    public void changeTheme(ChameleonTheme theme) {
        themeBehaviorSubject.onNext(theme);
    }
}
