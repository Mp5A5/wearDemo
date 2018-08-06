package www.mp5a5.com.weardemo.utils;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author ：mp5a5 on 2018/8/5 19：11
 * @describe
 * @email：wwb199055@126.com
 */
public class UiUtils {

  public static <T extends View> T findViewById(Activity activity, @IdRes int id) {
    return activity.findViewById(id);
  }

  public static String getEditTextString(EditText editText) {
    return editText.getText().toString().trim();
  }

  public static String getTextViewString(TextView textView) {
    return textView.getText().toString().trim();
  }

  private static long lastClickTime = 0;

  /**
   * 返回true 可以点击
   */
  public static boolean isFastDoubleClick() {
    long time = System.currentTimeMillis();
    long endTime = time - lastClickTime;
    lastClickTime = time;
    return endTime > 2000;
  }
}
