package www.mp5a5.com.weardemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import www.mp5a5.com.weardemo.utils.ConstantUtils;

public class LocationReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {

    String location = intent.getStringExtra(ConstantUtils.MAP_LOCATION);
    Log.e("-->", location);
  }
}
