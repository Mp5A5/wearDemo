package www.mp5a5.com.weardemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import www.mp5a5.com.weardemo.utils.ConstantUtils;
import www.mp5a5.com.weardemo.utils.SpUtil;
import www.mp5a5.com.weardemo.utils.UiUtils;

public class SettingActivity extends WearableActivity {

  private EditText etInputPhone;
  private EditText etInputTime;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
    setAmbientEnabled();
    initView();
    initListener();
  }

  private void initView() {
    etInputPhone = UiUtils.findViewById(this, R.id.et_input_phone);
    etInputTime = UiUtils.findViewById(this, R.id.et_input_time);
    long contractNumber = SpUtil.getLong(ConstantUtils.SETTING_CONTRACT);
    int locationTime = SpUtil.getInt(ConstantUtils.SETTING_TIME);

    if (contractNumber == -1) {
      etInputPhone.setText(getString(R.string.setting_phone));
    } else {
      etInputPhone.setText(String.valueOf(contractNumber));
    }

    if (locationTime == -1) {
      etInputTime.setText(getString(R.string.setting_location));
    } else {
      etInputTime.setText(String.valueOf(locationTime));
    }
  }

  private void initListener() {
    UiUtils.findViewById(this, R.id.btn_sure).setOnClickListener(v -> {

      if (isMobileNum(UiUtils.getEditTextString(etInputPhone))) {
        SpUtil.setLong(ConstantUtils.SETTING_CONTRACT, Long.parseLong(UiUtils.getEditTextString(etInputPhone)));
      } else {
        Toast.makeText(this, "请输入正确的电话号码！", Toast.LENGTH_SHORT).show();
      }

      if (isNum(UiUtils.getEditTextString(etInputTime))) {
        SpUtil.setInt(ConstantUtils.SETTING_TIME, Integer.parseInt(UiUtils.getEditTextString(etInputTime)));
      }

    });
  }

  /**
   * 判断字符串是否符合手机号码格式
   * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
   * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
   * 电信号段: 133,149,153,170,173,177,180,181,189
   *
   * @return 待检测的字符串
   */
  private static boolean isMobileNum(String mobileNums) {
    String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
    // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
    return !TextUtils.isEmpty(mobileNums) && mobileNums.matches(telRegex);
  }

  private static boolean isNum(@NonNull String num) {
    String telRegex = "^[0-9]*[1-9][0-9]*$";
    return num.matches(telRegex);
  }
}
