package com.appupdate.reactnativeappupdatelib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;


import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AppUpdateClass {

    static String BASE_URL = "https://server.dev.appsonair.com/v1/app-services/";
    static String appId;
    static Boolean isNativeUIShow;

    public static void setAppId(String appId, boolean isNativeUIShow) {
        AppUpdateClass.appId = appId;
        AppUpdateClass.isNativeUIShow = isNativeUIShow;
    }


    public static void checkForAppUpdate(Context context, UpdateCallBack callback) {
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                String url = BASE_URL + AppUpdateClass.appId;
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .method("GET", null)
                        .build();
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d("EX:", String.valueOf(e));
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        try {
                            if (response.code() == 200) {
                                String myResponse = response.body().string();
                                JSONObject jsonObject = new JSONObject(myResponse);
                                JSONObject updateData = jsonObject.getJSONObject("updateData");
                                boolean isAndroidUpdate = updateData.getBoolean("isAndroidUpdate");
                                boolean isMaintenance = jsonObject.getBoolean("isMaintenance");
                                if (isAndroidUpdate) {
                                    boolean isAndroidForcedUpdate = updateData.getBoolean("isAndroidForcedUpdate");
                                    String androidBuildNumber = updateData.getString("androidBuildNumber");
                                    PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                                    int versionCode = info.versionCode;
                                    int buildNum = 0;

                                    if (!(androidBuildNumber.equals(null))) {
                                        buildNum = Integer.parseInt(androidBuildNumber);
                                    }
                                    boolean isUpdate = versionCode < buildNum;
                                    if (isNativeUIShow && isUpdate && (isAndroidForcedUpdate || isAndroidUpdate)) {
                                        Intent intent = new Intent(context, AppUpdateActivity.class);
                                        intent.putExtra("res", myResponse);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                } else if (isMaintenance && isNativeUIShow) {
                                    Intent intent = new Intent(context, MaintenanceActivity.class);
                                    intent.putExtra("res", myResponse);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                } else {
                                    //TODO : There is No Update and No Maintenance.
                                }
                                callback.onSuccess(myResponse);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onFailure(e.getMessage());
                            Log.d("AAAA", String.valueOf(e.getMessage()));

                        }
                    }
                });
            }
            @Override
            public void onLost(Network network) {
                //Lost connection
            }
        };

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }
    }
}
