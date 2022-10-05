package com.appupdate.reactnativeappupdatelib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class AppUpdateActivity extends Activity {

    Boolean activityClose = false;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_Light_NoActionBar);
        setContentView(R.layout.activity_app_update);
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle1 = ai.metaData;
            int icon = bundle1.getInt("com.appupdate.icon");
            String name = bundle1.getString("com.appupdate.name");

            Bundle bundle = this.getIntent().getExtras();
            String data = bundle.getString("res"); // NullPointerException.
            JSONObject jsonObject = new JSONObject(data);
            JSONObject updateData = jsonObject.getJSONObject("updateData");
            Boolean isAndroidForcedUpdate = updateData.getBoolean("isAndroidForcedUpdate");
            Boolean isAndroidUpdate = updateData.getBoolean("isAndroidUpdate");
            String  androidBuildNumber= updateData.getString("androidBuildNumber");
            String  playStoreURL= updateData.getString("androidUpdateLink");
            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionCode = info.versionCode;
            int buildNum = 0;

            if(!(androidBuildNumber.equals(null))){
                buildNum =  Integer.parseInt(androidBuildNumber);
            }
            boolean isUpdate = versionCode < buildNum;

//            AlertDialog alertDialog = new AlertDialog.Builder(this).setCancelable(false).create();
//            View customLayout = getLayoutInflater().inflate(R.layout.activity_app_update, null);
//            alertDialog.setView(customLayout);
            ImageView img_icon = findViewById(R.id.img_icon);

            if ((isAndroidForcedUpdate || isAndroidUpdate) && (isUpdate)) {
                TextView txt_title = findViewById(R.id.txt_title);
                TextView txt_des = findViewById(R.id.txt_des);
                TextView txt_no_thanks = findViewById(R.id.txt_no_thanks);
                TextView btn_update = findViewById(R.id.btn_update);
                img_icon.setImageResource(icon);
                txt_title.setText(name + " " + getString(R.string.update_title));
                if(isAndroidForcedUpdate){
                    txt_no_thanks.setVisibility(View.GONE);
                    txt_des.setText(getString(R.string.update_force_dsc));
                } else{
                    txt_no_thanks.setVisibility(View.VISIBLE);
                    txt_des.setText(getString(R.string.update_dsc));
                    txt_no_thanks.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activityClose = true;
                            onBackPressed();
//                            alertDialog.dismiss();
                        }
                    });

                }
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isAndroidForcedUpdate){
                            activityClose = true;
                            onBackPressed();
                        }
                        Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreURL));
                        startActivity(marketIntent);
                        if(!isAndroidForcedUpdate){
//                            alertDialog.dismiss();
                        }
                    }
                });
            }

//            alertDialog.show();

        } catch (JSONException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {
        if (activityClose) {
            super.onBackPressed();
        }
    }
}