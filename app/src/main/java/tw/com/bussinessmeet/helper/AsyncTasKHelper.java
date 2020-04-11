package tw.com.bussinessmeet.helper;

import android.os.AsyncTask;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import tw.com.bussinessmeet.bean.ResponseBody;

public class AsyncTasKHelper<P, R> extends AsyncTask<P, Void, Response<ResponseBody<R>>> {

    private OnResponseListener<P, R> onResponseListener;

    public AsyncTasKHelper(OnResponseListener<P, R> onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    public static <P, R> void execute(OnResponseListener<P,R> onResponseListener, P... ps) {
        new AsyncTasKHelper<>(onResponseListener).execute(ps);
    }

    @Override
    protected Response<ResponseBody<R>> doInBackground(P... ps) {
        try {
            return onResponseListener.request(ps).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Response<ResponseBody<R>> response) {
        super.onPostExecute(response);
        if (response != null && response.isSuccessful()) {
            ResponseBody<R> body = response.body();
            if (body.getSuccess()) {
                onResponseListener.onSuccess(body.getData());
            } else {
                onResponseListener.onFail(500);
            }
        } else {
            onResponseListener.onFail(response != null ? response.code() : 500);
        }
    }



    public interface OnResponseListener<P, R> {
        Call<ResponseBody<R>> request(P... ps);

        void onSuccess(R r);

        void onFail(int status);
    }
}

