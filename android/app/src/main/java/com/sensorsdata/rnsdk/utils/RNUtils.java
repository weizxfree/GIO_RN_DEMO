package com.sensorsdata.rnsdk.utils;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.sensorsdata.analytics.android.sdk.SALog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RNUtils {
    public static JSONObject convertMapToJson(ReadableMap properties){
        if (properties == null) {
            return null;
        }

        JSONObject json = null;
        ReadableNativeMap nativeMap = null;
        try {
            nativeMap = (ReadableNativeMap) properties;
            json = new JSONObject(properties.toString()).getJSONObject("NativeMap");
        } catch (Exception e) {
            SALog.printStackTrace(e);
            String superName = nativeMap.getClass().getSuperclass().getSimpleName();
            try {
                json = new JSONObject(properties.toString()).getJSONObject(superName);
            } catch (Exception e1) {
                SALog.printStackTrace(e1);
            }
        }
        return json;
    }

    public static JSONArray convertArrayToJson(ReadableArray readableArray) throws JSONException {
        JSONArray array = new JSONArray();
        if (readableArray == null) {
            return array;
        }
        for (int i = 0; i < readableArray.size(); i++) {
            switch (readableArray.getType(i)) {
                case Boolean:
                    array.put(readableArray.getBoolean(i));
                    break;
                case Number:
                    array.put(readableArray.getDouble(i));
                    break;
                case String:
                    array.put(readableArray.getString(i));
                    break;
                case Map:
                    array.put(RNUtils.convertMapToJson(readableArray.getMap(i)));
                    break;
                case Array:
                    array.put(RNUtils.convertArrayToJson(readableArray.getArray(i)));
                    break;
                default:
                    break;
            }
        }
        return array;
    }
}