package com.cpigeon.cpigeonhelper.idcard.utils;

import com.youtu.Youtu;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Zhu TingYu on 2017/11/30.
 */

public class IdCardIdentification {
    public static final int  TYPE_POSITIVE = 0;
    public static final int  TYPE_NOT_POSITIVE = 1;

    Youtu faceYoutu;

    public static final String APP_ID = "10110861";
    public static final String SECRET_ID = "AKIDngXYVRp5Uku6DGD7pIvbH0jhYGnGkkWj";
    public static final String SECRET_KEY = "HVZhtwopjjcNNH2hDxbM2uyviKsT5iYW";
    public static final String USER_ID = "2851551317";

    public IdCardIdentification(){
        faceYoutu = new Youtu(APP_ID,SECRET_ID,SECRET_KEY, Youtu.API_YOUTU_END_POINT);
    }

    public void  IdCardOcr(final String path, final int type, Consumer<JSONObject> consumer){
        Observable.<JSONObject>create(observableEmitter -> {
            try {
                JSONObject jsonObject = faceYoutu.IdCardOcr(path,type);
                observableEmitter.onNext(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);

    }
}
