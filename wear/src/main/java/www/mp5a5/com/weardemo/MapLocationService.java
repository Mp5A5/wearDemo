package www.mp5a5.com.weardemo;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.simple.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import www.mp5a5.com.weardemo.keeplive.KeepLiveService;
import www.mp5a5.com.weardemo.utils.ConstantUtils;

/**
 * @author ：mp5a5 on 2018/8/26 19：12
 * @describe
 * @email：wwb199055@126.com
 */
public class MapLocationService extends KeepLiveService {

  private AMapLocationClient mLocationClient;
  private AMapLocationClientOption mLocationOption = null;
  private final String TAG = this.getClass().getSimpleName();

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.e(TAG, "开启服务");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    Log.e(TAG, "开启地图");
    initMap();
    int i = super.onStartCommand(intent, flags, startId);
    Log.e("keeplive", "DemoService process = " + android.os.Process.myPid());
    return i;
  }


  @Override
  public void onDestroy() {
    super.onDestroy();
  }


  private void initMap() {

    mLocationClient = new AMapLocationClient(this);
    //初始化定位参数
    mLocationOption = new AMapLocationClientOption();
    //设置定位监听
    mLocationClient.setLocationListener(myLocationListener);
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
    mLocationOption.setInterval(5000);

    //设置定位参数
    mLocationClient.setLocationOption(mLocationOption);
    // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
    // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
    // 在定位结束后，在合适的生命周期调用onDestroy()方法
    // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    //启动定位
    mLocationClient.startLocation();
  }

  private AMapLocationListener myLocationListener = aMapLocation -> {
    if (aMapLocation != null) {
      //定位成功回调信息，设置相关消息
      if (aMapLocation.getErrorCode() == 0) {
        //获取当前定位结果来源，如网络定位结果，详见定位类型表
        aMapLocation.getLocationType();
        //获取纬度
        aMapLocation.getLatitude();
        //获取经度
        aMapLocation.getLongitude();
        //获取精度信息
        aMapLocation.getAccuracy();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = new Date(aMapLocation.getTime());
        //定位时间
        df.format(date);
        Log.e(TAG, aMapLocation.getLocationType() + "," + aMapLocation.getLatitude() + "," + aMapLocation
            .getLongitude());
        String location = /*aMapLocation.getAddress()+*/aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() +
            aMapLocation.getStreet()+aMapLocation.getStreetNum();
        Log.e(TAG, aMapLocation.getCountry() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation
            .getStreet());
        EventBus.getDefault().postSticky(location, ConstantUtils.MAP_LOCATION);

        Intent intent = new Intent();
        intent.setAction(ConstantUtils.MAP_RECEIVER_FILTER);
        intent.putExtra(ConstantUtils.MAP_LOCATION, location);
        sendBroadcast(intent);

      } else {
        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
        Log.e("TAG", "location Error, ErrCode:"
            + aMapLocation.getErrorCode() + ", errInfo:"
            + aMapLocation.getErrorInfo());
        if (aMapLocation.getErrorCode() == 4) {
          String msg = "请连接网络";
          Intent intent = new Intent();
          intent.setAction(ConstantUtils.MAP_RECEIVER_FILTER);
          intent.putExtra(ConstantUtils.MAP_LOCATION, msg);
          sendBroadcast(intent);
        }

      }
    }
  };
}
