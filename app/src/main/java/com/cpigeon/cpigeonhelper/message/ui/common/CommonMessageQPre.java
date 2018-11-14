package com.cpigeon.cpigeonhelper.message.ui.common;

import android.app.Activity;

import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.entity.CommonEntity;
import com.cpigeon.cpigeonhelper.message.adapter.CommonMessageAdapter;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.ToastUtil;
import com.cpigeon.cpigeonhelper.utils.http.HttpErrorException;
import com.cpigeon.cpigeonhelper.utils.http.LogUtil;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Zhu TingYu on 2017/11/23.
 */

public class CommonMessageQPre extends BasePresenter{

    public int userId;

    public String messageContent;

    public String messageId;

    public CommonMessageQPre(Activity activity) {
        super(activity);
    }


    public void getCommonList(Consumer<List<CommonEntity>> consumer){
        submitRequestThrowError(CommonModel.getCommons(userId).map(r -> {
            if (r.isOk()){
                if(r.isHaveDate()){
                    return r.data;
                }else {
                    return Lists.newArrayList();
                }
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    /*public void getCommonList1(Consumer<List<CommonEntity>> consumer){
        submitRequestThrowError(CommonModel.getCommons(userId).map(r ->{
            if(r.isHaveDate()){
                return r.data;
            }else throw new HttpErrorException(r);
        }),consumer);
    }*/

    public void addCommonMessage(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CommonModel.addCommonMessage(userId, messageContent).map(r ->{
            return r;
        }),consumer);
    }

    /*public void addCommonMessage2(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CommonModel.addCommonMessage(userId, messageContent).doOnNext(apiResponse -> {

        }),o -> {

        });
    }*/


    public void modifyMessage(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CommonModel.modifyCommonMessage(userId, messageId, messageContent).map(r -> {
            return r;
        }),consumer);
    }

    public void deleteMessage(Consumer<ApiResponse> consumer){
        submitRequestThrowError(CommonModel.deleteCommonMessage(userId, messageId).map(r -> {
            return r;
        }),consumer);
    }

    public Consumer<String> setMessageContent(){
        return s -> {
            messageContent = s;
        };
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setSelectIds(CommonMessageAdapter adapter){
        List<String> id = Lists.newArrayList();
        List<Integer> positions  = adapter.getSelectedPotion();
        for (Integer position : positions) {
            id.add(adapter.getItem(position).id);
        }
        messageId = Lists.appendStringByList(id);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }
}
