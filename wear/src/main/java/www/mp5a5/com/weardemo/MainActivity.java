package www.mp5a5.com.weardemo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import www.mp5a5.com.weardemo.utils.ConstantUtils;
import www.mp5a5.com.weardemo.utils.SpUtil;
import www.mp5a5.com.weardemo.utils.UiUtils;

public class MainActivity extends WearableActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setAmbientEnabled();
    TextView tvMePhone = UiUtils.findViewById(this, R.id.tv_me_phone);
    tvMePhone.setText(String.valueOf(SpUtil.getLong(ConstantUtils.SETTING_CONTRACT)));
    initListener();

  }

  private void initListener() {

    UiUtils.findViewById(this, R.id.rl_me_setting).setOnClickListener(v ->
        startActivity(new Intent(this, SettingActivity.class))
    );

    UiUtils.findViewById(this, R.id.rl_me_phone).setOnClickListener(v ->

        new RxPermissions(this).requestEachCombined(Manifest.permission.CALL_PHONE).subscribe(permission -> {
          if (permission.granted) {
            callPhone();
          } else if (permission.shouldShowRequestPermissionRationale) {
            Toast.makeText(this, "请开启打电话权限！", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(this, "请到设置中开启打电话权限！", Toast.LENGTH_SHORT).show();
          }
        })
    );

    UiUtils.findViewById(this, R.id.btn_location).setOnClickListener(v ->
        startActivity(new Intent(this, MapLocationActivity.class))
    );
  }

  private void callPhone() {
    Intent intent = new Intent(Intent.ACTION_DIAL);
    Uri uri = Uri.parse("tel:" + String.valueOf(SpUtil.getLong(ConstantUtils.SETTING_CONTRACT)));
    intent.setData(uri);
    startActivity(intent);
  }
}
