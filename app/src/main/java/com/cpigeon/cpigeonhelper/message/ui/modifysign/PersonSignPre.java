package com.cpigeon.cpigeonhelper.message.ui.modifysign;

import android.app.Activity;

import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.commonstandard.model.dao.IBaseDao;
import com.cpigeon.cpigeonhelper.commonstandard.presenter.BasePresenter;
import com.cpigeon.cpigeonhelper.entity.OrderInfoEntity;
import com.cpigeon.cpigeonhelper.entity.PersonInfoEntity;
import com.cpigeon.cpigeonhelper.idcard.utils.IntentBuilder;
import com.cpigeon.cpigeonhelper.message.ui.order.ui.OrderModel;
import com.cpigeon.cpigeonhelper.message.ui.person.PersonInfoFragment;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StringValid;

import io.reactivex.functions.Consumer;

/**
 * Created by Zhu TingYu on 2017/11/30.
 */

public class PersonSignPre extends BasePresenter {

    int userId;//：用户ID
    public String sign;//签名
    public String idCardP;//身份证正面图片，data
    public String idCardN;//身份证背面图片，data
    public String license;//营业执照图片，data
    public String name;//身份证信息：姓名 
    public String sex;//身份证信息：性别 
    public String familyName;//身份证信息：民族
    public String address;//身份证信息：住址
    public String idCardNumber;//身份证信息：身份证号码
    public String organization;//身份证信息：签发机关
    public String idCardDate;//身份证信息：有效期

    public String personName;
    public String personPhoneNumber;
    public String personWork;

    public int type;

    public PersonSignPre(Activity activity) {
        super(activity);
        userId = AssociationData.getUserId();
        type = getActivity().getIntent().getIntExtra(IntentBuilder.KEY_TYPE, 0);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void modifySign(Consumer<ApiResponse> consumer) {
        submitRequestThrowError(PersonSignModel.modifySign(userId, sign, idCardP, idCardN, license, name
                , sex, familyName, address, idCardNumber, organization, idCardDate).map(r -> {
            return r;
        }), consumer);
    }

    public void modifyPersonInfo(Consumer<ApiResponse> consumer) {
        submitRequestThrowError(PersonSignModel.modifyPersonInfo(userId, idCardP, idCardN, license, name
                , sex, familyName, address, idCardNumber, organization, idCardDate, personName,personPhoneNumber, personWork, sign).map(r -> {
            return r;
        }), consumer);
    }

    public void uploadPersonInfo(Consumer<ApiResponse> consumer) {
        if(!StringValid.isStringValid(personName)){
//            ToastUtil.showLongToast(getActivity(),"请输入名字");

            CommonUitls.showSweetDialog1(getActivity(), "请输入名字", dialog -> {
                dialog.dismiss();
            });
            return;
        }

        if(!StringValid.phoneNumberValid(personPhoneNumber)){
//            ToastUtil.showLongToast(getActivity(),"手机号码无效");

            CommonUitls.showSweetDialog1(getActivity(), "手机号码无效", dialog -> {
                dialog.dismiss();
            });
            return;
        }

        if(!StringValid.isStringValid(personWork)){
//            ToastUtil.showLongToast(getActivity(),"请输入单位名称");

            CommonUitls.showSweetDialog1(getActivity(), "请输入单位名称", dialog -> {
                dialog.dismiss();
            });
            return;
        }

        if(type == PersonInfoFragment.TYPE_UPLOAD_INFO){
            if(!StringValid.isStringValid(sign)){
//                ToastUtil.showLongToast(getActivity(),"请输入签名");

                CommonUitls.showSweetDialog1(getActivity(), "请输入签名", dialog -> {
                    dialog.dismiss();
                });
                return;
            }
        }

        submitRequestThrowError(PersonSignModel.uploadPersonInfo(userId, idCardP, idCardN, license, name
                , sex, familyName, address, idCardNumber, organization, idCardDate, personName,personPhoneNumber, personWork, sign).map(r -> {
            return r;
        }), consumer);
    }

    public void getPersonInfo(Consumer<ApiResponse<PersonInfoEntity>> consumer){
        submitRequestThrowError(PersonSignModel.personInfo(userId),consumer);
    }

    public void getPersonSignInfo(Consumer<ApiResponse<PersonInfoEntity>> consumer){
        submitRequestThrowError(PersonSignModel.personSignInfo(userId),consumer);
    }
    public void getGXTOrder(Consumer<ApiResponse<OrderInfoEntity>> consumer) {
        submitRequestThrowError(OrderModel.greatServiceOrder(userId).map(r -> {
            return r;
        }), consumer);
    }


    public Consumer<String> setSign(){
        return s -> {
          sign = s.trim();
        };
    }

    public Consumer<String> setPersonName(){
        return s -> {
            personName = s.trim();
        };
    }

    public Consumer<String> setPersonPhoneNumber(){
        return s -> {
            personPhoneNumber = s.trim();
        };
    }

    public Consumer<String> setPersonWork(){
        return s -> {
            personWork = s.trim();
        };
    }

}
