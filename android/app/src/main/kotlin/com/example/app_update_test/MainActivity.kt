package com.example.app_update_test

import com.appupdate.reactnativeappupdatelib.AppUpdateClass
import com.appupdate.reactnativeappupdatelib.UpdateCallBack
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val channel = "flutterUpdateChecker/isUpdateAvailable"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
         setContentView(R.layout.activity_main)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger,channel).setMethodCallHandler{call,result->
        when(call.method){
            "setApplicationID" ->
                if(call.argument<Boolean>("AppId").toString() != "") {
                    AppUpdateClass.setAppId(call.argument("AppId"), true)
                    result.success(true)
                }else{
                    result.success(false)
                }
            "isUpdateAvailable" ->
                AppUpdateClass.checkForAppUpdate(this, object : UpdateCallBack {
                override fun onSuccess(response: String) {
                }
                override fun onFailure(message: String) {
                }
            })
        else -> result.notImplemented()
        }


//        if( call.method == "setApplicationID"){
//            if(call.argument<Boolean>("AppId").toString() != "") {
//                AppUpdateClass.setAppId(call.argument("AppId"), true)
//                result.success(true)
//            }else{
//                result.success(false)
//            }
//        }
//          else if (call.method == "isUpdateAvailable") {
//
//            AppUpdateClass.checkForAppUpdate(this, object : UpdateCallBack {
//                override fun onSuccess(response: String) {
//                    Log.e("mye", "" + response)
//                }
//
//                override fun onFailure(message: String) {
//                    Log.e("mye", "onFailure$message")
//                }
//            })
//        } else {
//            result.notImplemented()
//        }
        }
    }


}
