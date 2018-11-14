package com.cpigeon.cpigeonhelper.message.ui.modifysign;


import com.cpigeon.cpigeonhelper.R;
import com.cpigeon.cpigeonhelper.common.db.AssociationData;
import com.cpigeon.cpigeonhelper.common.network.ApiConstants;
import com.cpigeon.cpigeonhelper.common.network.ApiResponse;
import com.cpigeon.cpigeonhelper.common.network.RetrofitHelper;
import com.cpigeon.cpigeonhelper.entity.PersonInfoEntity;
import com.cpigeon.cpigeonhelper.message.GXYHttpUtil;
import com.cpigeon.cpigeonhelper.utils.CommonUitls;
import com.cpigeon.cpigeonhelper.utils.StringValid;
import com.cpigeon.cpigeonhelper.utils.http.HttpUtil;
import com.cpigeon.cpigeonhelper.utils.http.LogUtil;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Zhu TingYu on 2017/11/30.
 */

public class PersonSignModel {

    public static Observable<ApiResponse<PersonInfoEntity>> personSignInfo(int userId) {
        return GXYHttpUtil.<ApiResponse<PersonInfoEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<PersonInfoEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_sign_info)
                .request();
    }

    public static Observable<ApiResponse<PersonInfoEntity>> personInfo(int userId) {
        return GXYHttpUtil.<ApiResponse<PersonInfoEntity>>build()
                .setToJsonType(new TypeToken<ApiResponse<PersonInfoEntity>>() {
                }.getType())
                .setType(HttpUtil.TYPE_POST)
                .url(R.string.api_person_info)
                .request();
    }


    public static Observable<ApiResponse> modifySign(int userId, String sign, String IdCardP, String IdCardN
            , String license, String name, String sex, String familyName, String address, String idCardNumber
            , String organization, String idCardDate) {

        File imgIdCardP = null;
        File imgIdCardN = null;
        File imgLicense = null;

        if (StringValid.isStringValid(IdCardP)) {
            imgIdCardP = new File(IdCardP);
        }

        if (StringValid.isStringValid(IdCardN)) {
            imgIdCardN = new File(IdCardN);
        }

        if (StringValid.isStringValid(license)) {
            imgLicense = new File(license);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("uid", String.valueOf(AssociationData.getUserId()));
        map.put("qm", sign);
        map.put("xingming", name);
        map.put("xingbie", sex);
        map.put("minzu", familyName);
        map.put("zhuzhi", address);
        map.put("haoma", idCardNumber);
        map.put("qianfajiguan", organization);
        map.put("youxiaoqi", idCardDate);

        Map<String, RequestBody> files = new HashMap<>();

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))
                .addFormDataPart("qm", getString(sign))
                .addFormDataPart("xingming", getString(name))
                .addFormDataPart("xingbie", getString(sex))
                .addFormDataPart("minzu", getString(familyName))
                .addFormDataPart("zhuzhi", getString(address))
                .addFormDataPart("haoma", getString(idCardNumber))
                .addFormDataPart("qianfajiguan", getString(organization))
                .addFormDataPart("youxiaoqi", getString(idCardDate));

        if (imgIdCardP != null) {
            files.put("sfzzm", RequestBody.create(MediaType.parse("image/*"), imgIdCardP));
            builder.addPart(MultipartBody.Part.createFormData("sfzzm", imgIdCardP.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgIdCardP)));
        }

        if (imgIdCardN != null) {
            files.put("sfzbm", RequestBody.create(MediaType.parse("image/*"), imgIdCardN));
            builder.addPart(MultipartBody.Part.createFormData("sfzbm", imgIdCardN.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgIdCardN)));

        }

        if (imgLicense != null) {
            files.put("gswj", RequestBody.create(MediaType.parse("image/*"), imgLicense));
            builder.addPart(MultipartBody.Part.createFormData("gswj", imgLicense.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgLicense)));
        }

        RequestBody requestBody = builder.build();
        return RetrofitHelper.getApi().modifySign(
                AssociationData.getUserToken(),
                CommonUitls.getApiSign(System.currentTimeMillis() / 1000, map),
                System.currentTimeMillis() / 1000,
                requestBody);

    }


    public static Observable<ApiResponse> modifyPersonInfo(int userId, String IdCardP, String IdCardN
            , String license, String name, String sex, String familyName, String address, String idCardNumber
            , String organization, String idCardDate, String personName, String personPhoneNumber, String personWork, String sign) {

        File imgIdCardP = null;
        File imgIdCardN = null;
        File imgLicense = null;

        if (StringValid.isStringValid(IdCardP)) {
            imgIdCardP = new File(IdCardP);
        }

        if (StringValid.isStringValid(IdCardN)) {
            imgIdCardN = new File(IdCardN);
        }

        if (StringValid.isStringValid(license)) {
            imgLicense = new File(license);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("uid", String.valueOf(AssociationData.getUserId()));
        map.put("xingming", name);
        map.put("xingbie", sex);
        map.put("minzu", familyName);
        map.put("zhuzhi", address);
        map.put("haoma", idCardNumber);
        map.put("qianfajiguan", organization);
        map.put("youxiaoqi", idCardDate);

        map.put("lianxiren", personName);
        map.put("sjhm", personPhoneNumber);
        map.put("dwmc", personWork);
        map.put("qianming", sign);


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))
                .addFormDataPart("xingming", getString(name))
                .addFormDataPart("xingbie", getString(sex))
                .addFormDataPart("minzu", getString(familyName))
                .addFormDataPart("zhuzhi", getString(address))
                .addFormDataPart("haoma", getString(idCardNumber))
                .addFormDataPart("qianfajiguan", getString(organization))
                .addFormDataPart("youxiaoqi", getString(idCardDate))
                .addFormDataPart("lianxiren", getString(personName))
                .addFormDataPart("sjhm", getString(personPhoneNumber))
                .addFormDataPart("qianming", getString(sign))
                .addFormDataPart("dwmc", getString(personWork));

        if (imgIdCardP != null) {
            builder.addPart(MultipartBody.Part.createFormData("sfzzm", imgIdCardP.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgIdCardP)));
        }

        if (imgIdCardN != null) {
            builder.addPart(MultipartBody.Part.createFormData("sfzbm", imgIdCardN.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgIdCardN)));

        }

        if (imgLicense != null) {
            builder.addPart(MultipartBody.Part.createFormData("gswj", imgLicense.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgLicense)));
        }

        LogUtil.print("{\n");
        for (String key : map.keySet()) {
            LogUtil.print(key + ": " + map.get(key) + "\n");
        }

        if (imgIdCardP != null) {
            LogUtil.print("sfzzm" + ": " + imgIdCardP.getPath() + "\n");
        }

        if (imgIdCardN != null) {
            LogUtil.print("sfzbm" + ": " + imgIdCardN.getPath() + "\n");
        }

        if (imgLicense != null) {
            LogUtil.print("gswj" + ": " + imgLicense.getPath() + "\n");
        }

        LogUtil.print("}");

        LogUtil.print(ApiConstants.BASE_URL
                + "/CHAPI/V1/" + "GXT_XGGRXX");

        RequestBody requestBody = builder.build();


        return RetrofitHelper.getApi().modifyPersonInfo(
                AssociationData.getUserToken(),
                CommonUitls.getApiSign(System.currentTimeMillis() / 1000, map),
                System.currentTimeMillis() / 1000,
                requestBody);

    }


    public static Observable<ApiResponse> uploadPersonInfo(int userId, String IdCardP, String IdCardN
            , String license, String name, String sex, String familyName, String address, String idCardNumber
            , String organization, String idCardDate, String personName, String personPhoneNumber, String personWork, String sign) {

        File imgIdCardP = null;
        File imgIdCardN = null;
        File imgLicense = null;

        if (StringValid.isStringValid(IdCardP)) {
            imgIdCardP = new File(IdCardP);
        }

        if (StringValid.isStringValid(IdCardN)) {
            imgIdCardN = new File(IdCardN);
        }

        if (StringValid.isStringValid(license)) {
            imgLicense = new File(license);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("uid", String.valueOf(AssociationData.getUserId()));
        map.put("xingming", name);
        map.put("xingbie", sex);
        map.put("minzu", familyName);
        map.put("zhuzhi", address);
        map.put("haoma", idCardNumber);
        map.put("qianfajiguan", organization);
        map.put("youxiaoqi", idCardDate);
        map.put("lianxiren", personName);
        map.put("sjhm", personPhoneNumber);
        map.put("dwmc", personWork);
        map.put("qianming", sign);


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uid", String.valueOf(AssociationData.getUserId()))
                .addFormDataPart("xingming", getString(name))
                .addFormDataPart("xingbie", getString(sex))
                .addFormDataPart("minzu", getString(familyName))
                .addFormDataPart("zhuzhi", getString(address))
                .addFormDataPart("haoma", getString(idCardNumber))
                .addFormDataPart("qianfajiguan", getString(organization))
                .addFormDataPart("youxiaoqi", getString(idCardDate))
                .addFormDataPart("lianxiren", getString(personName))
                .addFormDataPart("sjhm", getString(personPhoneNumber))
                .addFormDataPart("dwmc", getString(personWork))
                .addFormDataPart("qianming", getString(sign));

        if (imgIdCardP != null) {
            builder.addPart(MultipartBody.Part.createFormData("sfzzm", imgIdCardP.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgIdCardP)));
        }

        if (imgIdCardN != null) {
            builder.addPart(MultipartBody.Part.createFormData("sfzbm", imgIdCardN.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgIdCardN)));

        }

        if (imgLicense != null) {
            builder.addPart(MultipartBody.Part.createFormData("gswj", imgLicense.getName(),
                    RequestBody.create(MediaType.parse("image/*"), imgLicense)));
        }

        LogUtil.print("{\n");
        for (String key : map.keySet()) {
            LogUtil.print(key + ": " + map.get(key) + "\n");
        }

        if (imgIdCardP != null) {
            LogUtil.print("sfzzm" + ": " + imgIdCardP.getPath() + "\n");
        }

        if (imgIdCardN != null) {
            LogUtil.print("sfzbm" + ": " + imgIdCardN.getPath() + "\n");
        }

        if (imgLicense != null) {
            LogUtil.print("gswj" + ": " + imgLicense.getPath() + "\n");
        }

        LogUtil.print("}");

        LogUtil.print(ApiConstants.BASE_URL
                + "/CPAPI/V1/" + "GXT_TJGRXX");

        RequestBody requestBody = builder.build();


        return RetrofitHelper.getApi().uploadPersonInfo(
                AssociationData.getUserToken(),
                CommonUitls.getApiSign(System.currentTimeMillis() / 1000, map),
                System.currentTimeMillis() / 1000,
                requestBody);

    }


    private static String getString(String s) {
        return StringValid.isStringValid(s) ? s : "";
    }

}
