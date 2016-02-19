// Generated code from Butter Knife. Do not modify!
package laiyi.tobacco.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BlankFragment$$ViewInjector {
  public static void inject(Finder finder, final laiyi.tobacco.fragment.BlankFragment target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427477, "field 'layout1' and method 'click'");
    target.layout1 = (feifei.library.view.percent.PercentLinearLayout) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.click();
        }
      });
    view = finder.findRequiredView(source, 2131427478, "field 'layout2' and method 'click'");
    target.layout2 = (feifei.library.view.percent.PercentLinearLayout) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.click();
        }
      });
    view = finder.findRequiredView(source, 2131427479, "field 'layout3'");
    target.layout3 = (feifei.library.view.percent.PercentLinearLayout) view;
    view = finder.findRequiredView(source, 2131427480, "field 'layout4'");
    target.layout4 = (feifei.library.view.percent.PercentLinearLayout) view;
    view = finder.findRequiredView(source, 2131427481, "field 'layout5'");
    target.layout5 = (feifei.library.view.percent.PercentLinearLayout) view;
    view = finder.findRequiredView(source, 2131427482, "field 'layout6'");
    target.layout6 = (feifei.library.view.percent.PercentLinearLayout) view;
    view = finder.findRequiredView(source, 2131427483, "field 'layout7' and method 'mapManage'");
    target.layout7 = (feifei.library.view.percent.PercentLinearLayout) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.mapManage();
        }
      });
    view = finder.findRequiredView(source, 2131427484, "field 'layout8' and method 'MyTask'");
    target.layout8 = (feifei.library.view.percent.PercentLinearLayout) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.MyTask();
        }
      });
  }

  public static void reset(laiyi.tobacco.fragment.BlankFragment target) {
    target.layout1 = null;
    target.layout2 = null;
    target.layout3 = null;
    target.layout4 = null;
    target.layout5 = null;
    target.layout6 = null;
    target.layout7 = null;
    target.layout8 = null;
  }
}
