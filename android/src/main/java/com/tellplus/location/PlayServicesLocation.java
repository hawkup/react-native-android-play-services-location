package com.tellplus.location;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by hhravn on 20/06/2017.
 */
public class PlayServicesLocation implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules( ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
                new PlayServicesLocationModule(reactContext)
        );
    }
    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }
    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        return Collections.emptyList();
    }
}
