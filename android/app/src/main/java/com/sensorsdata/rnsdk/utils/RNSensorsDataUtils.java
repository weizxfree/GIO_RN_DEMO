package com.sensorsdata.rnsdk.utils;

import android.view.View;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIImplementation;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.UIViewOperationQueue;

import java.lang.reflect.Field;

public class RNSensorsDataUtils {

  public static View getViewByTag(ReactContext reactContext, int viewTag) {
    NativeViewHierarchyManager manager = getNativeViewHierarchyManager(reactContext);
    if (manager == null) {
      return null;
    }
    return manager.resolveView(viewTag);
  }

  public static NativeViewHierarchyManager getNativeViewHierarchyManager(
      ReactContext reactContext) {
    try {
      // 获取 UIImplementation
      UIManagerModule uiManager = reactContext.getNativeModule(UIManagerModule.class);
      UIImplementation uiImplementation = uiManager.getUIImplementation();
      // 获取 UIImplementation#mOperationsQueue
      Field mOperationsQueueField =
          uiImplementation.getClass().getDeclaredField("mOperationsQueue");
      mOperationsQueueField.setAccessible(true);
      UIViewOperationQueue uiViewOperationQueue =
          (UIViewOperationQueue) mOperationsQueueField.get(uiImplementation);
      // 获取 UIViewOperationQueue#NativeViewHierarchyManager
      Field mNativeViewHierarchyManagerField =
          UIViewOperationQueue.class.getDeclaredField("mNativeViewHierarchyManager");
      mNativeViewHierarchyManagerField.setAccessible(true);
      NativeViewHierarchyManager mNativeViewHierarchyManager =
          (NativeViewHierarchyManager) mNativeViewHierarchyManagerField.get(uiViewOperationQueue);
      return mNativeViewHierarchyManager;
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
      return null;
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }
}
