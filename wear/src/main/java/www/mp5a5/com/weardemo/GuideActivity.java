package www.mp5a5.com.weardemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import www.mp5a5.com.weardemo.utils.ConstantUtils;
import www.mp5a5.com.weardemo.utils.SpUtil;

/**
 * @author ：mp5a5 on 2018/8/6 14：06
 * @describe
 * @email：wwb199055@126.com
 */
public class GuideActivity extends WearableActivity {


  private final int GOTO_SETTING_UI = 1;
  private final int GOTO_MAIN_UI = 2;

  private Handler mHandler = new Handler(msg -> {
    switch (msg.what) {
      case GOTO_SETTING_UI:
        gotoSettingAct();
        break;
      case GOTO_MAIN_UI:
        gotoMainAct();
        break;
      default:
        break;
    }
    return false;
  });

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_guide);
    setAmbientEnabled();
    initPermission();
  }

  @SuppressLint("CheckResult")
  private void initPermission() {
    new RxPermissions(this).requestEachCombined(Manifest.permission.CALL_PHONE, Manifest.permission
        .ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).subscribe(permission -> gotoWhere());
  }

  private void gotoWhere() {
    int time = SpUtil.getInt(ConstantUtils.SETTING_TIME);
    long contract = SpUtil.getLong(ConstantUtils.SETTING_CONTRACT);
    if (time == -1 && contract == -1) {
      mHandler.sendEmptyMessageDelayed(GOTO_SETTING_UI, 300);
    } else {
      mHandler.sendEmptyMessageDelayed(GOTO_MAIN_UI, 300);
    }
  }

  private void gotoSettingAct() {
    startActivity(new Intent(this, SettingActivity.class));
    finish();
  }

  private void gotoMainAct() {
    startActivity(new Intent(this, MainActivity.class));
    finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mHandler != null) {
      mHandler.removeMessages(GOTO_MAIN_UI);
      mHandler.removeMessages(GOTO_SETTING_UI);
    }
  }
}
