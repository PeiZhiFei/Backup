// Generated code from Butter Knife. Do not modify!
package laiyi.tobacco.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MapManageActivity$$ViewInjector {
  public static void inject(Finder finder, final laiyi.tobacco.activity.MapManageActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427435, "field 'list'");
    target.list = (android.widget.ListView) view;
    view = finder.findRequiredView(source, 2131427462, "method 'down'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.down();
        }
      });
  }

  public static void reset(laiyi.tobacco.activity.MapManageActivity target) {
    target.list = null;
  }
}
