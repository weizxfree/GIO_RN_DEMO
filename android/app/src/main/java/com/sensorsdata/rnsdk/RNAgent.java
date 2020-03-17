package com.sensorsdata.rnsdk;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.uimanager.JSTouchDispatcher;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.sensorsdata.analytics.RNSensorsAnalyticsModule;
import com.sensorsdata.analytics.android.sdk.SALog;
import com.sensorsdata.analytics.android.sdk.SensorsDataAutoTrackHelper;
import com.sensorsdata.rnsdk.utils.TouchTargetHelper;

import java.lang.reflect.Field;
import java.util.WeakHashMap;

public class RNAgent {
    private static final String TAG = "SAAgent";
    private static final WeakHashMap<JSTouchDispatcher, ViewGroup>
            jsTouchDispatcherViewGroupWeakHashMap = new WeakHashMap();

    public static void handleTouchEvent(
            JSTouchDispatcher jsTouchDispatcher, MotionEvent event, EventDispatcher eventDispatcher) {

        if (event.getAction() == 0) { // ActionDown
            ViewGroup viewGroup =
                    (ViewGroup) jsTouchDispatcherViewGroupWeakHashMap.get(jsTouchDispatcher);
            if (viewGroup != null) {
                SALog.i("SA.RN----->viewGroup ", viewGroup.toString());
            }
            if (viewGroup == null) {
                try {
                    Field viewGroupField = jsTouchDispatcher.getClass().getDeclaredField("mRootViewGroup");
                    viewGroupField.setAccessible(true);
                    viewGroup = (ViewGroup) viewGroupField.get(jsTouchDispatcher);
                    jsTouchDispatcherViewGroupWeakHashMap.put(jsTouchDispatcher, viewGroup);
                } catch (Exception e) {
                    SALog.printStackTrace(e);
                }
            }
            if (viewGroup != null) {
                View nativeTargetView =
                        TouchTargetHelper.findTouchTargetView(
                                new float[]{event.getX(), event.getY()}, viewGroup);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("handleTouchEvent: isClickable  ");
                stringBuilder.append(nativeTargetView.isClickable());
                stringBuilder.append("   ");
                stringBuilder.append(nativeTargetView.getClass().getName());
                SALog.i("SA.RN----->", stringBuilder.toString());
                if (nativeTargetView != null) {
                    View reactTargetView = TouchTargetHelper.findClosestReactAncestor(nativeTargetView);
                    if (reactTargetView != null) {
                        nativeTargetView = reactTargetView;
                        SALog.i("SA.RN----->viewtag", reactTargetView.getId() + "");
                    }
                }
                if (nativeTargetView != null) {
                    RNSensorsAnalyticsModule.setOnTouchView(nativeTargetView);
                    SensorsDataAutoTrackHelper.trackViewOnClick(nativeTargetView);
                }
            }
        }
    }

    public static void addView(View view, int tag) {
        Log.i(TAG, "view tag: " + tag);
        try {
            RNSensorsAnalyticsModule.initReactViewAttrs(view, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}