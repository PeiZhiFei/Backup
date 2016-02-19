// Generated code from Butter Knife. Do not modify!
package laiyi.tobacco.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ListViewActivity$$ViewInjector {
  public static void inject(Finder finder, final laiyi.tobacco.activity.ListViewActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427435, "field 'list'");
    target.list = (feifei.library.view.swipmenu.SwipeMenuListView) view;
  }

  public static void reset(laiyi.tobacco.activity.ListViewActivity target) {
    target.list = null;
  }
}
