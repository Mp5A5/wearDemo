package www.mp5a5.com.weardemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FirstActivity extends WearableActivity implements AMapLocationListener {

  private RelativeLayout rlMeSetting;
  //声明mlocationClient对象
  public AMapLocationClient mlocationClient;
  //声明mLocationOption对象
  public AMapLocationClientOption mLocationOption = null;
  private final String TAG = this.getClass().getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_first);
    setAmbientEnabled();

    initPermission();

    //initMap();
    //Log.e(TAG, getLongLat().getLatitude() + "--" + getLongLat().getLongitude());

  }

  private void initPermission() {
    new RxPermissions(this).requestEachCombined(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
        .ACCESS_FINE_LOCATION).subscribe(permission -> {
      if (permission.granted) {
        initMap();
      } else if (permission.shouldShowRequestPermissionRationale) {
        Toast.makeText(this, "请开启权限", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(this, "请到设置中开启权限", Toast.LENGTH_SHORT).show();
      }
    });

  }

  private void initMap() {

    mlocationClient = new AMapLocationClient(this);
    //初始化定位参数
    mLocationOption = new AMapLocationClientOption();
    //设置定位监听
    mlocationClient.setLocationListener(this);
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
    mLocationOption.setInterval(2000);
    //设置定位参数
    mlocationClient.setLocationOption(mLocationOption);
    // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
    // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
    // 在定位结束后，在合适的生命周期调用onDestroy()方法
    // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    //启动定位
    mlocationClient.startLocation();
  }


  public Location getLongLat() {
    LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
    // 获取所有可用的位置提供器
    List<String> providerList = locationManager != null ? locationManager.getProviders(true) : null;

    String provider = null;
    if (providerList != null) {
      if (providerList.contains(LocationManager.GPS_PROVIDER)) {
        //优先使用gps
        provider = LocationManager.GPS_PROVIDER;
      } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
        provider = LocationManager.NETWORK_PROVIDER;
      } else if (providerList.contains(LocationManager.PASSIVE_PROVIDER)) {
        provider = LocationManager.PASSIVE_PROVIDER;
      } else {
        //UIUtils.showToastSafe("无法获得当前位置");
        return null;
      }
    }


    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission
        .ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

      Location location = locationManager.getLastKnownLocation(provider);

      if (location != null) {
        return location;
      } else {

        return null;
      }
    } else {
      return null;
    }

  }

  @Override
  public void onLocationChanged(AMapLocation amapLocation) {
    if (amapLocation != null) {
      if (amapLocation.getErrorCode() == 0) {
        //定位成功回调信息，设置相关消息
        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
        amapLocation.getLatitude();//获取纬度
        amapLocation.getLongitude();//获取经度
        amapLocation.getAccuracy();//获取精度信息
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = new Date(amapLocation.getTime());
        df.format(date);//定位时间
        Log.e(TAG, amapLocation.getLocationType() + "," + amapLocation.getLatitude() + "," + amapLocation
            .getLongitude());
      } else {
        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
        Log.e("TAG", "location Error, ErrCode:"
            + amapLocation.getErrorCode() + ", errInfo:"
            + amapLocation.getErrorInfo());
      }
    }
  }
}
