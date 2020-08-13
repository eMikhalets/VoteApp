package com.ntech.fourtop;

import android.app.Application;

import com.ntech.fourtop.data.AppRepository;

import timber.log.Timber;

public class FourTopApplication extends Application {

    private AppRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());
    }

    public AppRepository getRepository() {
        return repository;
    }
}
