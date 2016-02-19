package feifei.dataanalysis.base;

import com.duowan.mobile.netroid.AuthFailureError;
import com.duowan.mobile.netroid.Listener;
import com.duowan.mobile.netroid.request.StringRequest;

import java.util.Map;

public class PostRequest extends StringRequest
{
    private Map<String, String> mParams;

    // 传入Post参数的Map集合
    public PostRequest(String url, Map<String, String> params, Listener<String> listener)
    {
        super(Method.POST, url, listener);
        mParams = params;
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError
    {
        return mParams;
    }
}