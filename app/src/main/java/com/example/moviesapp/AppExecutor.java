package com.example.moviesapp;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutor {
    //singleton Pattern
    private static AppExecutor instance;
    //getter.


    public static AppExecutor getInstance() {
        if (instance == null){
            instance = new AppExecutor();
        }
        return instance;
    }

    private final ScheduledExecutorService mNetWork = Executors.newScheduledThreadPool(3);

    public ScheduledExecutorService netWorkIo(){
        return mNetWork;
    }



}
