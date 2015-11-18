package com.team2052.frckrawler.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.team2052.frckrawler.activities.HasComponent;
import com.team2052.frckrawler.database.DBManager;
import com.team2052.frckrawler.database.consumer.DataComsumer;
import com.team2052.frckrawler.database.subscribers.BaseDataSubscriber;
import com.team2052.frckrawler.di.FragmentComponent;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Acorp on 11/17/2015.
 */
public abstract class BaseDataFragment
        <T, V, S extends BaseDataSubscriber<T, V>, B extends DataComsumer<V>>
        extends Fragment {
    @Inject
    S subscriber;
    @Inject
    B binder;
    protected FragmentComponent mComponent;
    protected DBManager dbManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() instanceof HasComponent){
            mComponent = ((HasComponent) getActivity()).getComponent();
        }
        mComponent.dbManager();
        inject();
        dbManager = mComponent.dbManager();
        subscriber.setConsumer(binder);
        binder.setActivity(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getObservable().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(subscriber);
    }

    public abstract void inject();

    protected abstract Observable<? extends T> getObservable();
}