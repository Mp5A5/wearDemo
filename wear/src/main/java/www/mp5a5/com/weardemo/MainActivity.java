package www.mp5a5.com.weardemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.RelativeLayout;

public class MainActivity extends WearableActivity {

  private RelativeLayout rlMeSetting;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    rlMeSetting = findViewById(R.id.rl_me_setting);
    setAmbientEnabled();
    initListener();
  }

  private void initListener() {
    findViewById(R.id.rl_me_setting).setOnClickListener(v ->
        startActivity(new Intent(this, FirstActivity.class))
    );
  }
}
