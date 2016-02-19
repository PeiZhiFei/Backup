// Generated code from Butter Knife. Do not modify!
package laiyi.tobacco.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoginActivity$$ViewInjector {
  public static void inject(Finder finder, final laiyi.tobacco.activity.LoginActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427455, "field 'password'");
    target.password = (feifei.library.view.ClearEditText) view;
    view = finder.findRequiredView(source, 2131427454, "field 'username'");
    target.username = (feifei.library.view.ClearEditText) view;
    view = finder.findRequiredView(source, 2131427457, "field 'version'");
    target.version = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427437, "field 'button' and method 'veryLogin'");
    target.button = (android.widget.Button) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.veryLogin();
        }
      });
    view = finder.findRequiredView(source, 2131427456, "field 'button2' and method 'button'");
    target.button2 = (android.widget.Button) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.button();
        }
      });
  }

  public static void reset(laiyi.tobacco.activity.LoginActivity target) {
    target.password = null;
    target.username = null;
    target.version = null;
    target.button = null;
    target.button2 = null;
  }
}
