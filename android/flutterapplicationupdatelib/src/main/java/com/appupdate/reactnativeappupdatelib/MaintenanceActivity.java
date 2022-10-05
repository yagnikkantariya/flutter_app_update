package com.appupdate.reactnativeappupdatelib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class MaintenanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        try {
            Bundle bundle = this.getIntent().getExtras();
            String data = bundle.getString("res"); // NullPointerException.

            JSONObject jsonObject = new JSONObject(data);
            boolean isMaintenance = jsonObject.getBoolean("isMaintenance");
            if (isMaintenance) {
                JSONObject maintenanceData = jsonObject.getJSONObject("maintenanceData");

                String title = maintenanceData.getString("title");
                String description = maintenanceData.getString("description");
                String image = maintenanceData.getString("image");
                String textColorCode = maintenanceData.getString("textColorCode");
                String backgroundColorCode = maintenanceData.getString("backgroundColorCode");
                LinearLayout maintenance_layout = findViewById(R.id.ll_root);

                if (backgroundColorCode != "") {
                    maintenance_layout.setBackgroundColor(Color.parseColor(backgroundColorCode));
                }
                ImageView img_icon = findViewById(R.id.img_icon);
                TextView txt_title_maintain = findViewById(R.id.txt_title_maintain);
                TextView txt_des_maintain = findViewById(R.id.txt_des_maintain);
                TextView txt_app_name = findViewById(R.id.txt_app_name);

                if (image != "") {
                    new DownloadImageTask(img_icon)
                            .execute(image);
                }

                txt_title_maintain.setText(title);
                txt_des_maintain.setText(description);

                ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle1 = ai.metaData;
                String appName = bundle1.getString("com.appupdate.name");
                txt_app_name.setText(appName);
                if (textColorCode != "") {
                    txt_title_maintain.setTextColor(Color.parseColor(textColorCode));
                    txt_des_maintain.setTextColor(Color.parseColor(textColorCode));
                    txt_app_name.setTextColor(Color.parseColor(textColorCode));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {}
}