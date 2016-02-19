// Generated code from Butter Knife. Do not modify!
package laiyi.tobacco.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class DetailActivity$$ViewInjector {
  public static void inject(Finder finder, final laiyi.tobacco.activity.DetailActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427439, "field 'etName'");
    target.etName = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427440, "field 'etAddress'");
    target.etAddress = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427441, "field 'etArea'");
    target.etArea = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131427442, "field 'etHaiba'");
    target.etHaiba = (feifei.library.view.ClearEditText) view;
    view = finder.findRequiredView(source, 2131427445, "field 'spSoilType'");
    target.spSoilType = (android.widget.Spinner) view;
    view = finder.findRequiredView(source, 2131427446, "field 'spLandFeature'");
    target.spLandFeature = (android.widget.Spinner) view;
    view = finder.findRequiredView(source, 2131427444, "field 'spLandType'");
    target.spLandType = (android.widget.Spinner) view;
    view = finder.findRequiredView(source, 2131427447, "field 'spSoilfertility'");
    target.spSoilfertility = (android.widget.Spinner) view;
    view = finder.findRequiredView(source, 2131427448, "field 'spTobaccotype'");
    target.spTobaccotype = (android.widget.Spinner) view;
    view = finder.findRequiredView(source, 2131427449, "field 'spTobaccobreed'");
    target.spTobaccobreed = (android.widget.Spinner) view;
    view = finder.findRequiredView(source, 2131427450, "field 'cbWaterexist'");
    target.cbWaterexist = (android.widget.CheckBox) view;
    view = finder.findRequiredView(source, 2131427451, "field 'cbWaterstate'");
    target.cbWaterstate = (android.widget.CheckBox) view;
    view = finder.findRequiredView(source, 2131427443, "field 'tvGPS'");
    target.tvGPS = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131427438, "field 'recyclerView'");
    target.recyclerView = (android.support.v7.widget.RecyclerView) view;
    view = finder.findRequiredView(source, 2131427437, "field 'button' and method 'select'");
    target.button = (android.widget.Button) view;
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.select();
        }
      });
  }

  public static void reset(laiyi.tobacco.activity.DetailActivity target) {
    target.etName = null;
    target.etAddress = null;
    target.etArea = null;
    target.etHaiba = null;
    target.spSoilType = null;
    target.spLandFeature = null;
    target.spLandType = null;
    target.spSoilfertility = null;
    target.spTobaccotype = null;
    target.spTobaccobreed = null;
    target.cbWaterexist = null;
    target.cbWaterstate = null;
    target.tvGPS = null;
    target.recyclerView = null;
    target.button = null;
  }
}
