package www.mp5a5.com.weardemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.Toast;

public class MainActivity extends WearableActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setAmbientEnabled();
    initListener();
  }

  private void initListener() {

    findViewById(R.id.rl_me_setting).setOnClickListener(v ->
        startActivity(new Intent(this, MapLocationActivity.class))
    );

    UiUtils.findViewById(this, R.id.rl_me_phone).setOnClickListener(v ->
        Toast.makeText(this, "拨打电话", Toast.LENGTH_SHORT).show()
    );
  }
}
