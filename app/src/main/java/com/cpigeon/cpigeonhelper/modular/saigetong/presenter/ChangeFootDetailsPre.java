package com.cpigeon.cpigeonhelper.modular.saigetong.presenter;

import android.app.Activity;

import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.entity.ChangeFootPhotoDetailsEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.ChangeFootDetailsModel;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.FootSSEntity;
import com.cpigeon.cpigeonhelper.modular.saigetong.model.bean.SearchFootEntity;
import com.cpigeon.cpigeonhelper.utils.Lists;
import com.cpigeon.cpigeonhelper.utils.StringValid;
import com.cpigeon.cpigeonhelper.utils.http.HttpErrorException;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2018/3/15.
 */

public class ChangeFootDetailsPre extends BasePresenter {

    public String footId;
    public String tagId;
    public String tagString;
    public String errorReason;

    public List<LocalMedia> images = Lists.newArrayList();
    public SearchFootEntity searchFootEntity;

    public ChangeFootDetailsPre(Activity activity) {
        super(activity);

        searchFootEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);

        footId = String.valueOf(searchFootEntity.getId());
        tagId = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_TYPE);
        errorReason = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_CONTENT);
        tagString = getActivity().getIntent().getStringExtra("tagStr");
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getFootInfo(Consumer<List<ChangeFootPhotoDetailsEntity>> consumer){
        submitRequestThrowError(ChangeFootDetailsModel.getFootInfo(footId, tagId).map(r -> {
            if(r.status){

                ChangeFootPhotoDetailsEntity entity = r.data;
                getImages(entity);

                List<ChangeFootPhotoDetailsEntity> list = Lists.newArrayList();
                if(StringValid.isStringValid(entity.slturl)){
                     list.add(entity);
                }

                if(StringValid.isStringValid(entity.bpimgurl)){
                    list.add(entity);
                }
                return list;
            }else throw new HttpErrorException(r);
        }),consumer);
    }

    private void getImages(ChangeFootPhotoDetailsEntity entity){

        if(StringValid.isStringValid(entity.imgurl)){
            LocalMedia localMedia = new LocalMedia();
            localMedia.setPath(entity.imgurl);
            images.add(localMedia);
        }

        if(StringValid.isStringValid(entity.bpimgurl)){
            LocalMedia localMedia = new LocalMedia();
            localMedia.setPath(entity.bpimgurl);
            images.add(localMedia);
        }
    }

    public List<FootSSEntity> getFootSSEntity(){
        List<FootSSEntity> list = Lists.newArrayList();
        FootSSEntity footSSEntity = new FootSSEntity(searchFootEntity.getId()
                ,searchFootEntity.getColor(),searchFootEntity.getSex(),searchFootEntity.getAddress()
                ,searchFootEntity.getFoot(),searchFootEntity.getTel(), searchFootEntity.getEye()
                ,searchFootEntity.getSjhm(), searchFootEntity.getXingming(), searchFootEntity.getCskh()
                ,searchFootEntity.getGpmc());
        list.add(footSSEntity);
        return list;
    }

}
