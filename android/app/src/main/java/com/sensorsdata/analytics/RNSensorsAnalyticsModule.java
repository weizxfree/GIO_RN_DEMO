/*
 * Created by chenru on 2019/08/27.
 * Copyright 2015－2019 Sensors Data Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sensorsdata.analytics;

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.sensorsdata.analytics.android.sdk.SALog;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.sensorsdata.analytics.android.sdk.SensorsDataAutoTrackHelper;
import com.sensorsdata.analytics.android.sdk.SensorsDataDynamicSuperProperties;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by yang on 2017/4/5
 * <p>参数类型在@ReactMethod注明的方法中，会被直接映射到它们对应的JavaScript类型 String -> String ReadableMap -> Object
 * Boolean -> Bool Integer -> Number Double -> Number Float -> Number Callback -> function
 * ReadableArray -> Array
 */
public class RNSensorsAnalyticsModule extends ReactContextBaseJavaModule {

    private static final String MODULE_NAME = "RNSensorsAnalyticsModule";
    private static final String MODULE_VERSION = "1.1.0";
    private static final String LOGTAG = "SA.RN";

    private static WeakReference<View> onTouchViewReference;

    private static SparseArray<Boolean> viewArrays = new SparseArray();

    public RNSensorsAnalyticsModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /**
     * 返回一个字符串名字，这个名字在 JavaScript (RN)端标记这个模块。
     */
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    /**
     * ReadableMap 转换成 JSONObject
     */
    private JSONObject convertToJSONObject(ReadableMap properties) {
        if (properties == null) {
            return null;
        }

        JSONObject json = null;
        ReadableNativeMap nativeMap = null;
        try {
            nativeMap = (ReadableNativeMap) properties;
            json = new JSONObject(properties.toString()).getJSONObject("NativeMap");
        } catch (Exception e) {
            Log.e(LOGTAG, "" + e.getMessage());
            String superName = nativeMap.getClass().getSuperclass().getSimpleName();
            try {
                json = new JSONObject(properties.toString()).getJSONObject(superName);
            } catch (Exception e1) {
                Log.e(LOGTAG, "" + e1.getMessage());
            }
        }
        return json;
    }

    /**
     * 参数类型在@ReactMethod注明的方法中，会被直接映射到它们对应的JavaScript类型 String -> String ReadableMap -> Object Boolean
     * -> Bool Integer -> Number Double -> Number Float -> Number Callback -> function ReadableArray
     * -> Array
     * <p>导出 track 方法给 RN 使用.
     *
     * @param eventName 事件名称
     * @param properties 事件的具体属性 RN 中使用示例：（记录 RN_AddToFav 事件，事件属性
     * "ProductID":123456,"UserLevel":"VIP"） <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.track("RN_AddToFav",{"ProductID":123456,"UserLevel":"VIP"})}>
     * </Button>
     */
    @ReactMethod
    public void track(String eventName, ReadableMap properties) {
        try {
            SensorsDataAPI.sharedInstance().track(eventName, convertToJSONObject(properties));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 参数类型在@ReactMethod注明的方法中，会被直接映射到它们对应的JavaScript类型 String -> String ReadableMap -> Object Boolean
     * -> Bool Integer -> Number Double -> Number Float -> Number Callback -> function ReadableArray
     * -> Array
     * <p>导出 trackChannelEvent 方法给 RN 使用.
     *
     * @param eventName 事件名称
     * @param properties 事件的具体属性 RN 中使用示例：（记录 RN_AddToFav 事件，事件属性
     * "ProductID":123456,"UserLevel":"VIP"） <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.trackChannelEvent("RN_AddToFav",{"ProductID":123456,"UserLevel":"VIP"})}>
     * </Button>
     */
    @ReactMethod
    public void trackChannelEvent(String eventName, ReadableMap properties) {
        try {
            SensorsDataAPI.sharedInstance().trackChannelEvent(eventName, convertToJSONObject(properties));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 trackTimerStart 方法给 RN 使用.
     * <p>初始化事件的计时器，默认计时单位为秒(计时开始).
     *
     * @param eventName 事件的名称. RN 中使用示例：（计时器事件名称 viewTimer ） <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.trackTimerStart("viewTimer")}> </Button>
     */
    @ReactMethod
    public void trackTimerStart(String eventName) {
        try {
            SensorsDataAPI.sharedInstance().trackTimerStart(eventName);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 trackTimerBegin 方法给 RN 使用.
     * <p>初始化事件的计时器，默认计时单位为毫秒(计时开始).
     *
     * @param eventName 事件的名称. RN 中使用示例：（计时器事件名称 viewTimer ） <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.trackTimerBegin("viewTimer")}> </Button>
     */
    @ReactMethod
    public void trackTimerBegin(String eventName) {
        try {
            SensorsDataAPI.sharedInstance().trackTimerBegin(eventName);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 trackTimerEnd 方法给 RN 使用.
     * <p>初始化事件的计时器，默认计时单位为毫秒(计时结束，并触发事件)
     *
     * @param eventName 事件的名称. RN 中使用示例：（计时器事件名称 viewTimer ） <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.trackTimerEnd("viewTimer",{"ProductID":123456,"UserLevel":"VIP"})}>
     * </Button>
     */
    @ReactMethod
    public void trackTimerEnd(String eventName, ReadableMap properties) {
        try {
            SensorsDataAPI.sharedInstance().trackTimerEnd(eventName, convertToJSONObject(properties));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 clearTrackTimer 方法给 RN 使用.
     * <p>清除所有事件计时器
     * <p>RN 中使用示例：（保存用户的属性 "sex":"男"） <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.clearTrackTimer()}> </Button>
     */
    @ReactMethod
    public void clearTrackTimer() {
        try {
            SensorsDataAPI.sharedInstance().clearTrackTimer();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 login 方法给 RN 使用.
     *
     * @param loginId RN 中使用示例： <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.login("developer@sensorsdata.cn")}> </Button>
     */
    @ReactMethod
    public void login(String loginId) {
        try {
            SensorsDataAPI.sharedInstance().login(loginId);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 logout 方法给 RN 使用.
     * <p>RN 中使用示例： <Button title="Button" onPress={()=> SensorsDataAPI_Android.logout()}> </Button>
     */
    @ReactMethod
    public void logout() {
        try {
            SensorsDataAPI.sharedInstance().logout();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 identify 方法给 RN 使用.
     * <p>RN 中使用示例： <Button title="Button" onPress={()=> SensorsDataAPI_Android.identify(distinctId)}>
     * </Button>
     */
    @ReactMethod
    public void identify(String distinctId) {
        try {
            SensorsDataAPI.sharedInstance().identify(distinctId);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 trackInstallation 方法给 RN 使用.
     * <p>用于记录首次安装激活、渠道追踪的事件.
     *
     * @param eventName 事件名.
     * @param properties 事件属性. RN 中使用示例：（ 这里事件名为 AppInstall ,事件的渠道属性
     * "$utm_source":"渠道A","$utm_campaign":"广告A" ） <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.trackInstallation("AppInstall",{"$utm_source":"渠道A","$utm_campaign":"广告A"})}>
     * </Button>
     */
    @ReactMethod
    public void trackInstallation(String eventName, ReadableMap properties) {
        try {
            SensorsDataAPI.sharedInstance().trackInstallation(eventName, convertToJSONObject(properties));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 trackViewScreen 方法给 RN 使用.
     * <p>此方法用于 RN 中 Tab 切换页面的时候调用，用于记录 $AppViewScreen 事件.
     *
     * @param url 页面的 url 记录到 $url 字段中(如果不需要此属性，可以传 null ).
     * @param properties 页面的属性. 注：为保证记录到的 $AppViewScreen 事件和 Auto Track 采集的一致， 需要传入 $title（页面的title）
     * 、$screen_name （页面的名称，即 包名.类名）字段. RN 中使用示例： <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.trackViewScreen(null,{"$title":"RN主页","$screen_name":"cn.sensorsdata.demo.RNHome"})}>
     * </Button>
     */
    @ReactMethod
    public void trackViewScreen(String url, ReadableMap properties) {
        try {
            SensorsDataAPI.sharedInstance().trackViewScreen(url, convertToJSONObject(properties));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 profileSet 方法给 RN 使用.
     *
     * @param properties 用户属性 RN 中使用示例：（保存用户的属性 "sex":"男"） <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.profileSet({"sex":"男"})}> </Button>
     */
    @ReactMethod
    public void profileSet(ReadableMap properties) {
        try {
            SensorsDataAPI.sharedInstance().profileSet(convertToJSONObject(properties));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 profileSetOnce 方法给 RN 使用.
     * <p>首次设置用户的一个或多个 Profile. 与profileSet接口不同的是，如果之前存在，则忽略，否则，新创建.
     *
     * @param properties 属性列表 RN 中使用示例：（保存用户的属性 "sex":"男"） <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.profileSetOnce({"sex":"男"})}> </Button>
     */
    @ReactMethod
    public void profileSetOnce(ReadableMap properties) {
        try {
            SensorsDataAPI.sharedInstance().profileSetOnce(convertToJSONObject(properties));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 profileIncrement 方法给 RN 使用.
     * <p>给一个数值类型的Profile增加一个数值. 只能对数值型属性进行操作，若该属性 未设置，则添加属性并设置默认值为0.
     *
     * @param property 属性名称
     * @param value 属性的值，值的类型只允许为 Number . RN 中使用示例： <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.profileIncrement("money",10)}> </Button>
     */
    @ReactMethod
    public void profileIncrement(String property, Double value) {
        try {
            SensorsDataAPI.sharedInstance().profileIncrement(property, value);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 profileAppend 方法给 RN 使用.
     * <p>给一个列表类型的 Profile 增加一个元素.
     *
     * @param property 属性名称.
     * @param value 新增的元素. RN 中使用示例： <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.profileAppend("VIP","Gold")}> </Button>
     */
    @ReactMethod
    public void profileAppend(String property, String value) {
        try {
            SensorsDataAPI.sharedInstance().profileAppend(property, value);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 profileUnset 方法给 RN 使用.
     * <p>删除用户的一个 Profile.
     *
     * @param property 属性名称. RN 中使用示例： <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.profileUnset("sex")}> </Button>
     */
    @ReactMethod
    public void profileUnset(String property) {
        try {
            SensorsDataAPI.sharedInstance().profileUnset(property);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 profileDelete 方法给 RN 使用.
     * <p>删除用户所有 Profile.
     * <p>RN 中使用示例： <Button title="Button" onPress={()=> SensorsDataAPI_Android.profileDelete()}>
     * </Button>
     */
    @ReactMethod
    public void profileDelete() {
        try {
            SensorsDataAPI.sharedInstance().profileDelete();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 getDistinctId 方法给 RN 使用.
     * <p>获取当前的 DistinctId.
     * <p>successCallback 优先返回 mLoginId ，否则返回 mAnonymousId
     * <p>RN 中使用示例： <Button title="Button" onPress={()=>
     * SensorsDataAPI_Android.getDistinctId(success=>{ console.log(success) }, error=>{
     * console.log(error) }) }> </Button>
     */
    @ReactMethod
    public void getDistinctId(Callback successCallback, Callback errorCallback) {
        try {
            String mLoginId = SensorsDataAPI.sharedInstance().getLoginId();
            if (!TextUtils.isEmpty(mLoginId)) {
                successCallback.invoke(mLoginId);
            } else {
                successCallback.invoke(SensorsDataAPI.sharedInstance().getAnonymousId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
            errorCallback.invoke(e.getMessage());
        }
    }

    /**
     * 导出 getDistinctIdPromise 方法给 RN 使用.
     * <p>Promise 方式，获取 distinctId
     * <p>RN 中使用示例： async getDistinctIdPromise() { var distinctId = await
     * RNSensorsAnalyticsModule.getDistinctIdPromise() };
     */
    @ReactMethod
    public void getDistinctIdPromise(Promise promise) {
        try {
            String mLoginId = SensorsDataAPI.sharedInstance().getLoginId();
            if (!TextUtils.isEmpty(mLoginId)) {
                promise.resolve(mLoginId);
            } else {
                promise.resolve(SensorsDataAPI.sharedInstance().getAnonymousId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
            promise.reject("getDistinctId fail", e);
        }
    }

    /**
     * 导出 getAnonymousIdPromise 方法给 RN 使用.
     * <p>Promise 方式 getAnonymousId 获取匿名 ID.
     * <p>RN 中使用示例： async getAnonymousIdPromise() { var anonymousId = await
     * RNSensorsAnalyticsModule.getAnonymousIdPromise() };
     */
    @ReactMethod
    public void getAnonymousIdPromise(Promise promise) {
        try {
            promise.resolve(SensorsDataAPI.sharedInstance().getAnonymousId());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
            promise.reject("getDistinctId fail", e);
        }
    }

    /**
     * 导出 registerSuperProperties 方法给 RN 使用.
     *
     * @param properties 要设置的公共属性 RN 中使用示例：（设置公共属性 "Platform":"Android"） <Button title="Button"
     * onPress={()=> RNSensorsAnalyticsModule.registerSuperProperties({"Platform":"Android"})}>
     * </Button>
     */
    @ReactMethod
    public void registerSuperProperties(ReadableMap properties) {
        try {
            SensorsDataAPI.sharedInstance().registerSuperProperties(convertToJSONObject(properties));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 unregisterSuperProperty 方法给 RN 使用.
     *
     * @param property 要删除的公共属性属性 RN 中使用示例：（删除公共属性 "Platform"） <Button title="Button" onPress={()=>
     * RNSensorsAnalyticsModule.unregisterSuperProperty("Platform")}> </Button>
     */
    @ReactMethod
    public void unregisterSuperProperty(String property) {
        try {
            SensorsDataAPI.sharedInstance().unregisterSuperProperty(property);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 clearSuperProperties 方法给 RN 使用.
     * <p>RN 中使用示例：（删除所有已设置的公共属性） <Button title="Button" onPress={()=>
     * RNSensorsAnalyticsModule.clearSuperProperties()}> </Button>
     */
    @ReactMethod
    public void clearSuperProperties() {
        try {
            SensorsDataAPI.sharedInstance().clearSuperProperties();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOGTAG, e.toString() + "");
        }
    }

    /**
     * 导出 registerDynamicSuperProperties 方法给 RN 使用.
     * <p>RN 中使用示例：（注册事件动态公共属性） <Button title="Button" onPress={()=>
     * RNSensorsAnalyticsModule.registerDynamicSuperProperties({"Platform":"Android"})}> </Button>
     */
    @ReactMethod
    public void registerDynamicSuperProperties(final ReadableMap properties) {
        try {
            SensorsDataDynamicSuperProperties dynamicSuperProperties =
                    new SensorsDataDynamicSuperProperties() {
                        @Override
                        public JSONObject getDynamicSuperProperties() {
                            return convertToJSONObject(properties);
                        }
                    };
            SensorsDataAPI.sharedInstance().registerDynamicSuperProperties(dynamicSuperProperties);
        } catch (Exception e) {
            SALog.printStackTrace(e);
        }
    }

    public static void setOnTouchView(View nativeTargetView) {
        onTouchViewReference = new WeakReference(nativeTargetView);
    }

    private View getClickView(int viewId, View onTouchView) {
        View currentView = onTouchView;
        while (currentView.getId() != viewId) {
            SALog.i("SA.RN----->parentId", viewId + "");
            ViewParent parent = currentView.getParent();
            if (parent == null || !(parent instanceof View)) {
                return null;
            }
            currentView = (View) parent;
        }
        return currentView;
    }

    private View getClickViewInChild(int viewId, ViewGroup currentView) {
        int currentViewCount = currentView.getChildCount();
        for (int i = 0; i < currentViewCount; i++) {
            SALog.i("SA.RN----->childId", viewId + "");
            View childView = currentView.getChildAt(i);
            if (childView != null) {
                if (childView.getId() == viewId) {
                    return childView;
                }
                if (childView instanceof ViewGroup) {
                    View clickView = getClickViewInChild(viewId, (ViewGroup) childView);
                    if (clickView != null) {
                        return clickView;
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    @ReactMethod
    public void trackRNClick(int viewId) {
        try {
            if (RNSensorsAnalyticsModule.onTouchViewReference != null) {
                View onTouchView = (View) RNSensorsAnalyticsModule.onTouchViewReference.get();
                if (onTouchView != null) {
                    View clickView = RNSensorsAnalyticsModule.this.getClickView(viewId, onTouchView);
                    if (clickView == null && (onTouchView instanceof ViewGroup)) {
                        SALog.i("SA.RN----->", "clickView is ViewGroup");
                        clickView =
                                RNSensorsAnalyticsModule.this.getClickViewInChild(viewId, (ViewGroup) onTouchView);
                    }
                    if (clickView != null) {
                        SALog.i("SA.RN----->", "onclick---init:" + clickView.hashCode());
                        SALog.i("SA.RN----->", "clickable is" + clickView.isClickable());
                        SensorsDataAutoTrackHelper.trackViewOnClick(clickView, true);
                    }
                }
            }
        } catch (Exception e) {
            SALog.printStackTrace(e);
        }
    }

    @ReactMethod
    public void prepareView(final int tag, final boolean clickable) {
        SALog.i("SA.RN----->", "prepareView--->" + tag + "cilckable" + clickable);
        if (clickable) {
            RNSensorsAnalyticsModule.viewArrays.append(tag, true);
        }
    }

    public static void initReactViewAttrs(View view, int tag) {
        if (viewArrays.get(tag, false)) {
            view.setClickable(true);
            viewArrays.remove(tag);
        }
    }
}
