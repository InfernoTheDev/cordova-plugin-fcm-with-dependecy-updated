package com.gae.scaffolder.plugin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.voova.mtbuller.paxapp.R;

import org.w3c.dom.Attr;

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
  private Activity _activity;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    _activity = this;
    View view = getLayoutInflater().from(getApplication()).inflate(R.layout.my_popup, null, false);

    tvTitle = view.findViewById(R.id.title_view);
    tvMsg = view.findViewById(R.id.msg_view);
    btnOk = view.findViewById(R.id.btn_ok);

    data = new HashMap<String, Object>();
    btnOk.setOnClickListener(this);

    setPopupContent();
    setContentView(view);
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume: ");
    getIntentData();
    setPopupContent();
  }

  private void getIntentData() {
    Bundle extras = getIntent().getExtras();
    for (String key : extras.keySet()) {
      Log.d(TAG, "bundle: " + key + " = " + extras.getString(key));
      if (key.equalsIgnoreCase("title")){
        title = extras.getString(key);
      }
      if (key.equalsIgnoreCase("body")){
        msg = extras.getString(key);
      }
      data.put(key, extras.getString(key));
    }
  }

  private void setPopupContent(){
    tvTitle.setText(title);
    tvMsg.setText(msg);
  }

  @Override
  public void onClick(View v) {
    if (v == btnOk){
      FCMPlugin.sendPushPayload( data );
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
