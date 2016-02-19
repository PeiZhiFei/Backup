// Generated code from Butter Knife. Do not modify!
package laiyi.tobacco.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MapActivity$$ViewInjector {
  public static void inject(Finder finder, final laiyi.tobacco.activity.MapActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427460, "method 'getGps'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.getGps();
        }
      });
  }

  public static void reset(laiyi.tobacco.activity.MapActivity target) {
  }
}
