package com.gae.scaffolder.plugin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Map;

public class MyPopup extends Activity implements View.OnClickListener {

  private static final String TAG = "MyPopup";

  //RelativeLayout mainLayout;

  TextView tvTitle;
  TextView tvMsg;
  Button btnOk;
  String title = "";
  String msg = "";
  Map<String, Object> data;
  Bundle extras;
  boolean isNewIntent = false;
  private Activity _activity;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate: ");
    _activity = this;

    int myPopupLayout = this._activity.getResources().getIdentifier("my_popup", "layout", this._activity.getPackageName());
    int titleLayout = this._activity.getResources().getIdentifier("title_view", "id", this._activity.getPackageName());
    int msgLayout = this._activity.getResources().getIdentifier("msg_view", "id", this._activity.getPackageName());
    int btnOkLayout = this._activity.getResources().getIdentifier("btn_ok", "id", this._activity.getPackageName());

    View view = getLayoutInflater()
        .from(getApplication())
        .inflate(
            getApplicationContext()
                .getResources()
                .getLayout(myPopupLayout),
            null,
            false
        );
    tvTitle = view.findViewById(titleLayout);
    tvMsg = view.findViewById(msgLayout);
    btnOk = view.findViewById(btnOkLayout);

    data = new HashMap<String, Object>();
    btnOk.setOnClickListener(this);

    setPopupContent();
    setContentView(view);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    Log.d(TAG, "onNewIntent: ");
    if (intent != null) {
      Log.d(TAG, "onNewIntent: ");
      extras = intent.getExtras();
      isNewIntent = true;
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume: ");
    getIntentData();
    setPopupContent();
  }

  private void getIntentData() {

    if (!isNewIntent) extras = getIntent().getExtras();

    data.put("confirmTapped", false);

    for (String key : extras.keySet()) {
      Log.d(TAG, "bundle: " + key + " = " + extras.getString(key));
      if (key.equalsIgnoreCase("title")) {
        title = extras.getString(key);
      }
      if (key.equalsIgnoreCase("body")) {
        msg = extras.getString(key);
      }
      data.put(key, extras.getString(key));
    }

    isNewIntent = false;
  }

  private void setPopupContent() {
    tvTitle.setText(title);
    tvMsg.setText(msg);
  }

  @Override
  public void onClick(View v) {
    if (v == btnOk) {
      data.put("confirmTapped", true);
      FCMPlugin.sendPushPayload(data);
      this._activity.finish();
      forceMainActivityReload();
    }
  }

  private void forceMainActivityReload() {
    PackageManager pm = getPackageManager();
    Intent launchIntent = pm.getLaunchIntentForPackage(getApplicationContext().getPackageName());
    startActivity(launchIntent);
  }
}
