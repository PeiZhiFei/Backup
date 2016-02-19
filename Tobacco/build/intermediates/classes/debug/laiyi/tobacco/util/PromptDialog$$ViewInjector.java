// Generated code from Butter Knife. Do not modify!
package laiyi.tobacco.util;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PromptDialog$$ViewInjector {
  public static void inject(Finder finder, final laiyi.tobacco.util.PromptDialog target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427472, "field 'titleTV'");
    target.titleTV = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427473, "field 'contentTV'");
    target.contentTV = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427475, "field 'btnDivider'");
    target.btnDivider = view;
    view = finder.findRequiredView(source, 2131427474, "field 'cancelBtn'");
    target.cancelBtn = (android.widget.Button) view;
    view = finder.findRequiredView(source, 2131427476, "field 'confirmBtn'");
    target.confirmBtn = (android.widget.Button) view;
  }

  public static void reset(laiyi.tobacco.util.PromptDialog target) {
    target.titleTV = null;
    target.contentTV = null;
    target.btnDivider = null;
    target.cancelBtn = null;
    target.confirmBtn = null;
  }
}
