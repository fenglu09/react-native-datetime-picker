package com.example.pickerview;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PickerviewPackage implements ReactPackage {


    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {

        List<NativeModule> modules = new ArrayList<>();
        //将我们创建的类添加进原生模块列表中
        modules.add(new Pickerview(reactContext));
        return modules;
    }

//    @Override
//    public List<Class<? extends JavaScriptModule>> createJSModules() {
//        return null;
//    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

//    @Override
//    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
//
//        return Arrays.<ViewManager>asList(
////                new PictureViewManager(reactContext)
//        );
//
//    }
}
