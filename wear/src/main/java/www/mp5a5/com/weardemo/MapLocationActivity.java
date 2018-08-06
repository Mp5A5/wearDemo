package www.mp5a5.com.weardemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import www.mp5a5.com.weardemo.utils.ConstantUtils;
import www.mp5a5.com.weardemo.utils.SpUtil;
import www.mp5a5.com.weardemo.utils.UiUtils;

public class MapLocationActivity extends WearableActivity implements AMapLocationListener {


  private AMapLocationClient mLocationClient;
  private AMapLocationClientOption mLocationOption = null;
  private final String TAG = this.getClass().getSimpleName();
  private TextView tvLongitude;
  private TextView tvLatitude;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map_location);
    tvLongitude = UiUtils.findViewById(this, R.id.tv_longitude);
    tvLatitude = UiUtils.findViewById(this, R.id.tv_latitude);
    setAmbientEnabled();
    initPermission();
  }

  @SuppressLint("CheckResult")
  private void initPermission() {
    new RxPermissions(this).requestEachCombined(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
        .ACCESS_FINE_LOCATION).subscribe(permission -> {
      if (permission.granted) {
        initMap();
      } else if (permission.shouldShowRequestPermissionRationale) {
        Toast.makeText(this, "请开启定位权限！", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(this, "请到设置中开启定位权限！", Toast.LENGTH_SHORT).show();
      }
    });

  }

  private void initMap() {

    mLocationClient = new AMapLocationClient(this);
    //初始化定位参数
    mLocationOption = new AMapLocationClientOption();
    //设置定位监听
    mLocationClient.setLocationListener(this);
    //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

    //设置是否返回地址信息（默认返回地址信息）
    mLocationOption.setNeedAddress(true);
    //设置是否只定位一次,默认为false
    mLocationOption.setOnceLocation(false);
    //设置是否强制刷新WIFI，默认为强制刷新
    mLocationOption.setWifiScan(true);
    //设置是否允许模拟位置,默认为false，不允许模拟位置
    mLocationOption.setMockEnable(false);
    //设置定位间隔,单位毫秒,默认为2000ms

    if (SpUtil.getInt(ConstantUtils.SETTING_TIME) == -1) {

      mLocationOption.setInterval(5000);
    } else {
      mLocationOption.setInterval(formatTime(SpUtil.getInt(ConstantUtils.SETTING_TIME)));
    }

    //设置定位参数
    mLocationClient.setLocationOption(mLocationOption);
    // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
    // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
    // 在定位结束后，在合适的生命周期调用onDestroy()方法
    // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    //启动定位
    mLocationClient.startLocation();
  }


  @SuppressLint("SetTextI18n")
  @Override
  public void onLocationChanged(AMapLocation amapLocation) {
    if (amapLocation != null) {
      //定位成功回调信息，设置相关消息
      if (amapLocation.getErrorCode() == 0) {
        //获取当前定位结果来源，如网络定位结果，详见定位类型表
        amapLocation.getLocationType();
        //获取纬度
        amapLocation.getLatitude();
        //获取经度
        amapLocation.getLongitude();
        //获取精度信息
        amapLocation.getAccuracy();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = new Date(amapLocation.getTime());
        //定位时间
        df.format(date);
        Log.e(TAG, amapLocation.getLocationType() + "," + amapLocation.getLatitude() + "," + amapLocation
            .getLongitude());
        tvLongitude.setText("经度：" + amapLocation.getLongitude());
        tvLatitude.setText("经度：" + amapLocation.getLatitude());
      } else {
        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
        Log.e("TAG", "location Error, ErrCode:"
            + amapLocation.getErrorCode() + ", errInfo:"
            + amapLocation.getErrorInfo());
      }
    }
  }

  private long formatTime(int minute) {
    return minute * 60 * 1000;
  }
}
