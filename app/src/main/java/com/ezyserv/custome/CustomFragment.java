package com.ezyserv.custome;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by android3 on 05-Oct-16.
 */
public class CustomFragment extends Fragment implements View.OnClickListener {
//    private ResponseCallback responseCallback;
    public CustomActivity parent;

    //    private Bundle arg;
    protected CustomFragment THIS;
    public View root;

    public View onCreateView(LayoutInflater inflater, int layout) {
        View v = inflater.inflate(layout, null);
        parent = (CustomActivity) getActivity();
        THIS = this;

        root = v;
        return v;
    }

    public View findViewById(int id) {
        return root.findViewById(id);
    }

    public View setTouchNClick(int id) {
        View v = findViewById(id);
        if (v == null)
            return null;
        v.setOnClickListener(this);
        v.setOnTouchListener(CustomActivity.TOUCH);
        return v;
    }

    public View setClick(int id) {
        View v = findViewById(id);
        if (v == null)
            return null;
        v.setOnClickListener(this);
        return v;
    }

//    public void setResponseListener(ResponseCallback responseCallback) {
//        this.responseCallback = responseCallback;
//    }

//    public void postCallJsonObject(Context c, String url, JSONObject params, String loadingMsg, final int callNumber) {
//        Log.d("URl:", url);
//        Log.d("Request:", params.toString());
//        StringEntity entity = null;
//        try {
//            entity = new StringEntity(params.toString());
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.setTimeout(30000);
//        client.post(c, url, entity, "application/json", new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
//                responseCallback.onJsonObjectResponseReceived(response, callNumber);
//                Log.d("Response:", response.toString());
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {
//                MyApp.spinnerStop();
//                responseCallback.onErrorReceived(getString(R.string.something_wrong));
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                responseCallback.onErrorReceived(getString(R.string.something_wrong));
//            }
//        });
//    }
//
//    public void postCall(Context c, String url, String loadingMsg, final int callNumber) {
//        if (!TextUtils.isEmpty(loadingMsg))
//            Log.d("URl:", url);
//        Log.d("Request:", "no Params");
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.setTimeout(30000);
//        client.post(url, new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
//                responseCallback.onJsonObjectResponseReceived(response, callNumber);
//                Log.d("Response:", response.toString());
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                responseCallback.onErrorReceived(getString(R.string.something_wrong));
//            }
//        });
//    }

    @Override
    public void onClick(View view) {

    }

//    public interface ResponseCallback {
//        void onJsonObjectResponseReceived(JSONObject o, int callNumber);
//
//        void onJsonArrayResponseReceived(JSONArray a, int callNumber);
//
//        void onErrorReceived(String error);
//
//    }

}
