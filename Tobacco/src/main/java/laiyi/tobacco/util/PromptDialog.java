package laiyi.tobacco.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import butterknife.ButterKnife;
import butterknife.InjectView;
import feifei.library.util.Tools;
import laiyi.tobacco.R;


public class PromptDialog extends Dialog
{
    public static final int TYPE_CONFIRM = 0;
    public static final int TYPE_CONFIRM_CANCEL = 1;

    @InjectView(R.id.prompt_title)
    public TextView titleTV;

    @InjectView(R.id.prompt_content)
    public TextView contentTV;

    @InjectView(R.id.prompt_vertical_divider)
    public View btnDivider;

    @InjectView(R.id.prompt_cancel)
    public Button cancelBtn;

    @InjectView(R.id.prompt_confirm)
    public Button confirmBtn;

    public PromptDialog(Context context)
    {
        super(context);
    }

    public PromptDialog(Context context, int theme)
    {
        super(context, theme);
    }

    public static PromptDialog create(Context context, String title,
                                      String content, int type)
    {
        PromptDialog d = new PromptDialog(context, R.style.dialog_loading_style);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_prompt, null);
        ButterKnife.inject(d,view);
//		d.setContentView (view);
        d.setContentView(
                view,
                new ViewGroup.LayoutParams((int) (Tools.getWidth(context) * 0.8),
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        d.setType(type);
        d.setTitle(title);
        d.setContent(content);
        d.setCancelable(false);

        return d;
    }

    public PromptDialog setConfirmButton(String text,
                                         View.OnClickListener onClickListener)
    {
        confirmBtn.setText(text);
        confirmBtn.setOnClickListener(onClickListener);
        return this;
    }

    public PromptDialog setLeft()
    {
        contentTV.setGravity(Gravity.LEFT);
        return this;
    }

    public PromptDialog setCancelButton(String text,
                                        View.OnClickListener onClickListener)
    {
        cancelBtn.setText(text);
        cancelBtn.setOnClickListener(onClickListener);
        return this;
    }

    public void setType(int type)
    {
        if (TYPE_CONFIRM == type)
        {
            btnDivider.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
            confirmBtn.setBackgroundResource(R.drawable.asheet_bt_bottom2);
        } else
        {
            btnDivider.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
            confirmBtn.setBackgroundResource(R.drawable.asheet_bt_right);
            cancelBtn.setBackgroundResource(R.drawable.asheet_bt_left);
        }
    }

    public void setTitle(String title)
    {
        titleTV.setText(title);
    }

    public void setContent(String content)
    {
        contentTV.setText(content);
    }

    @Override
    public void show()
    {
        if (contentTV.getText().length() > 12)
        {
            contentTV.setGravity(Gravity.LEFT);
        }
        super.show();
    }
}
