package www.mp5a5.com.weardemo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import www.mp5a5.com.weardemo.utils.ConstantUtils;
import www.mp5a5.com.weardemo.utils.UiUtils;

public class MainActivity extends WearableActivity {

  private TextView location;
  private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 0;
  private MapLockReceiver receiver;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setAmbientEnabled();
    location = findViewById(R.id.tv_location);
    initReceiver();
    initListener();

  }

  private void initReceiver() {
    receiver = new MainActivity.MapLockReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addAction("www.mp5a5.com.weardemo.MapLocationService");
    registerReceiver(receiver, filter);
  }

  private void initListener() {

    UiUtils.<TextView>findViewById(this, R.id.tv_setting).setOnClickListener(v ->
        startActivity(new Intent(this, SettingActivity.class))
    );

    UiUtils.<TextView>findViewById(this, R.id.tv_phone).setOnClickListener(v -> {

   /*   if (TextUtils.isEmpty(ConstantUtils.SETTING_CONTRACT)) {
        Toast.makeText(this, "请到设置中添加电话号码！", Toast.LENGTH_SHORT).show();
      } else {
        callPhonePermission();
      }*/

      callPhonePermission();
    });

  }

  private void callPhone() {
    Intent intent = new Intent(Intent.ACTION_DIAL);
    //Uri uri = Uri.parse("tel:" + String.valueOf(SpUtil.getLong(ConstantUtils.SETTING_CONTRACT)));
    Uri uri = Uri.parse("tel:" + "13269927610");
    intent.setData(uri);
    startActivity(intent);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(receiver);
  }


  private void callPhonePermission() {

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager
          .PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
          Toast.makeText(this, "请打开拨打电话权限！", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
          Uri uri = Uri.fromParts("package", this.getPackageName(), null);
          intent.setData(uri);
          startActivity(intent);
        } else {
          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
              MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
      } else {
        callPhone();
      }
    } else {
      callPhone();
    }

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_CALL_PHONE:
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          callPhone();
        } else {
          Toast.makeText(this, "暂未授权打电话功能，请您在设置中授权！", Toast.LENGTH_SHORT).show();
        }
        break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

  }

  class MapLockReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

      if (ConstantUtils.MAP_RECEIVER_FILTER.equals(intent.getAction())) {
        String msg = intent.getStringExtra(ConstantUtils.MAP_LOCATION);
        if (!TextUtils.isEmpty(msg)) {
          UiUtils.<TextView>findViewById(MainActivity.this, R.id.tv_location).setText(msg);
          location.setText(msg);
        } else {
          UiUtils.<TextView>findViewById(MainActivity.this, R.id.tv_location).setText("无定位地点");
          location.setText(msg);
        }
      }
    }
  }

}
