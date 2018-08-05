package www.mp5a5.com.weardemo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @author ：mp5a5 on 2018/8/1 13：56
 * @describe
 * @email：wwb199055@126.com
 */
public class MyApplication extends Application {

  private int count = 0;
  private static MyApplication application = null;

  @Override
  public void onCreate() {
    super.onCreate();
    application = this;

    registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
      @Override
      public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

      }

      @Override
      public void onActivityStarted(Activity activity) {
        count++;
      }

      @Override
      public void onActivityResumed(Activity activity) {

      }

      @Override
      public void onActivityPaused(Activity activity) {

      }

      @Override
      public void onActivityStopped(Activity activity) {
        if (count > 0) {
          count--;
        }
      }

      @Override
      public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

      }

      @Override
      public void onActivityDestroyed(Activity activity) {

      }
    });
  }

  public boolean isBackground() {
    return count <= 0;
  }


  public static MyApplication getApplication() {
    return application;
  }
}
