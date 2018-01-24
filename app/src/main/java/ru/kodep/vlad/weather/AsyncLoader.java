package ru.kodep.vlad.weather;

import android.annotation.SuppressLint;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by vlad on 24.01.18
 */

public class AsyncLoader extends AsyncTaskLoader {

    @SuppressLint("WrongConstant")
    AsyncLoader(Context context, Bundle args) {
        super(context);
  
        Log.i("AsyncLoader", "AsyncLoader");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.i("AsyncLoader", "Startovala");
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.i("AsyncLoader", "Ostanovilas");
    }

    @Override
    protected void onReset() {
        super.onReset();
        Log.i("AsyncLoader", "Restart");
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        Log.i("AsyncLoader", "Force");
    }


    @Override
    public Object loadInBackground() {
        Log.i("AsyncLoader", "loadInBack");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            return null;
        }

        return "fdsf";
    }


}
