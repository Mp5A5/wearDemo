package www.mp5a5.com.weardemo.utils;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * @author ：mp5a5 on 2018/8/5 19：11
 * @describe
 * @email：wwb199055@126.com
 */
public class UiUtils {

  public static <T extends View> T findViewById(Activity activity, @IdRes int id) {
    return activity.findViewById(id);
  }
}
