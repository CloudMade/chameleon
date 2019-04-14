# Chameleon

**Chameleon** is a library which allows to change themes in Android application in runtime.

It allows to change resources like colors and drawables (which are declared in **R** class) in runtime.

In order to make resource support theme changing in runtime it should be declared in a separate module with theme suffixes. After moving all the resources which should support runtime theming to a separate module and adding theme suffixes to them, any class in this module should be annotated with `@ChameleonThemes` annotation.
Theme suffixes supported by the app and theme groups amount should be passed as parameters to this annotation.

After that **ChameleonTheme** enum will be generated which will contain all possible themes created from theme suffixes passed as parameter to the `@ChameleonThemes` annotation and **ThemeColorProvider** along with **ThemeDrawableProvider** will be generated which will contain methods with names of colors and drawables you declared in the separate module. The return value of these methods is Data Binding **ObservableInt** which will emit themed resource id when theme is changed. When theme is changed **ThemeColorProvider#onThemeChange(Theme)** and **ThemeDrawableProvider#onThemeChange(Theme)** should be called. Methods of **ThemeColorProvider** and **ThemeDrawableProvider** classes can be used in xml layouts.

### Example:

Separate module with name **chameleonwrapper** is created. In **chameleonwrapper** module class is declared and annotated with `@ChameleonThemes` annotation:

    @ChameleonThemes(suffixes = {
            ThemeSuffixes.DAY_THEME_SUFFIX,
            ThemeSuffixes.NIGHT_THEME_SUFFIX,
            ThemeSuffixes.WINTER_THEME_SUFFIX,
            ThemeSuffixes.SUMMER_THEME_SUFFIX,
            ThemeSuffixes.AUTUMN_THEME_SUFFIX,
            ThemeSuffixes.SPRING_THEME_SUFFIX
    }, amount = {2, 4})
    class ThemeSuffixes {

        static final String DAY_THEME_SUFFIX = "_day";
        static final String NIGHT_THEME_SUFFIX = "_night";

        static final String WINTER_THEME_SUFFIX = "_winter";
        static final String SUMMER_THEME_SUFFIX = "_summer";
        static final String AUTUMN_THEME_SUFFIX = "_autumn";
        static final String SPRING_THEME_SUFFIX = "_spring";
    }
Theme suffixes amount describes belonging to the same theme group. First group: **ThemeSuffixes.DAY_THEME_SUFFIX**, **ThemeSuffixes.NIGHT_THEME_SUFFIX**; second group: **ThemeSuffixes.WINTER_THEME_SUFFIX**, **ThemeSuffixes.SUMMER_THEME_SUFFIX**, **ThemeSuffixes.AUTUMN_THEME_SUFFIX**, **ThemeSuffixes.SPRING_THEME_SUFFIX**.
In **chameleonwrapper** module **colors.xml** file is created:

    <?xml version="1.0" encoding="utf-8"?>
    <resources>
        <color name="hello_world_text_color_day_winter">#EF5350</color>
        <color name="hello_world_text_color_day_spring">#880E4F</color>
        <color name="hello_world_text_color_day_summer">#6A1B9A</color>
        <color name="hello_world_text_color_day_autumn">#673AB7</color>
    
        <color name="hello_world_text_color_night_winter">#26A69A</color>
        <color name="hello_world_text_color_night_spring">#66BB6A</color>
        <color name="hello_world_text_color_night_summer">#AED581</color>
        <color name="hello_world_text_color_night_autumn">#DCE775</color>
    </resources>
If resource should be the same during day and night **colors.xml** can be simplified:

    <?xml version="1.0" encoding="utf-8"?>
    <resources>
        <color name="hello_world_text_color_winter">#EF5350</color>
        <color name="hello_world_text_color_spring">#880E4F</color>
        <color name="hello_world_text_color_summer">#6A1B9A</color>
        <color name="hello_world_text_color_autumn">#673AB7</color>
    </resources>
After compiling the project **ChameleonTheme** enum will be generated:

    public enum ChameleonTheme implements com.cloudmade.chameleon.Theme {
    
        DAY_WINTER("_day_winter"),
        DAY_SPRING("_day_spring"),
        DAY_SUMMER("_day_summer"),
        DAY_AUTUMN("_day_autumn"),
    
        NIGHT_WINTER("_night_winter"),
        NIGHT_SPRING("_night_spring"),
        NIGHT_SUMMER("_night_summer"),
        NIGHT_AUTUMN("_night_autumn");
    
        public final String resourceIdSuffix;
    
        ChameleonTheme(String resourceIdSuffix) {
            this.resourceIdSuffix = resourceIdSuffix;
        }
    
        @Override
        public String getResourceIdSuffix() {
            return resourceIdSuffix;
        }
    }
Also **ThemeColorProvider** class will be generated:

    public class ThemeColorProvider implements com.cloudmade.chameleon.OnThemeChangeListener {
    
        ...
        
        public androidx.lifecycle.LiveData<Integer> helloWorldTextColor() {
            return themeElementLiveDataMap.get("hello_world_text_color");
        }
        
        ...
        
    }
**ThemeColorProvider#helloWorldTextColor()** can be used in xml layout:

    <?xml version="1.0" encoding="utf-8"?>
    <layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto">
    
        <data>
    
            <variable
                name="themeColorProvider"
                type="com.cloudmade.chameleon.ThemeColorProvider" />
        </data>
    
        <android.support.constraint.ConstraintLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">
    
            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Hello World!"
                android:textColor="@{context.getColor(themeColorProvider.helloWorldTextColor())}"
                android:textSize="22sp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent" />
    
        </android.support.constraint.ConstraintLayout>
    </layout>

### Installation:

Create separate module with annotated class with `@ChameleonThemes` annotation and resources which should support changing themes and add to its **build.gradle**:
    
    api 'com.cloudmade.chameleon:chameleon:1.2.0'
    annotationProcessor 'com.cloudmade.chameleon:chameleon:1.2.0'
    compileOnly 'androidx.lifecycle:lifecycle-livedata:2.0.0'
In your app's **build.gradle** add dependency to created separate module (let's call it **chameleonwrapper**) and specify annotation processor from **Chameleon** library:
    
    implementation project(":chameleonwrapper")
    annotationProcessor 'com.cloudmade.chameleon:chameleon:1.2.0'