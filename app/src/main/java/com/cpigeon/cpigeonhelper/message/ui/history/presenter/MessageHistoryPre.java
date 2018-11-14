package com.cpigeon.cpigeonhelper.message.ui.history.presenter;


import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.commonstandard.view.activity.IView;
import com.cpigeon.cpigeonhelper.entity.MessageEntity;
import com.cpigeon.cpigeonhelper.message.ui.history.MessageHistoryModel;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.http.HttpErrorException;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/27.
 */

public class MessageHistoryPre extends BasePresenter {

    int userId;
    public int date = 1;
    List<MessageEntity> data;

    public MessageHistoryPre(IView mView) {
        super(mView);
        userId = AssociationData.getUserId();
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getMessageHistoryList(Consumer<List<MessageEntity>> consumer) {
        submitRequestThrowError(MessageHistoryModel.MessageHistory(userId, date).map(r -> {
            if (r.isOk()) {
                if (r.isHaveDate()) {
                    this.data = r.data;
                    return data;
                } else {
                    this.data = Lists.newArrayList();
                    return data;
                }
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public int getSendMessageCountInDate() {
        int count = 0;
        if (data != null) {
            for (MessageEntity messageEntity : data) {
                count = count + messageEntity.fscount;
            }
        }
        return count;
    }


}
