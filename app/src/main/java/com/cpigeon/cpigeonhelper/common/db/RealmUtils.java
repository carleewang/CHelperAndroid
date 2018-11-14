package com.cpigeon.cpigeonhelper.common.db;


import com.cpigeon.cpigeonhelper.MyApplication;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GYTService;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.GeYunTongs;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.MyLocation;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.OfflineFileEntity;
import com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean.ServiceType;
import com.cpigeon.cpigeonhelper.modular.guide.model.bean.IsLoginAppBean;
import com.cpigeon.cpigeonhelper.modular.menu.model.bean.BulletinEntity;
import com.cpigeon.cpigeonhelper.modular.orginfo.model.bean.OrgInfo;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UserBean;
import com.cpigeon.cpigeonhelper.modular.usercenter.model.bean.UserLoginEntity;
import com.cpigeon.cpigeonhelper.utils.http.LogUtil;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.rx.RealmObservableFactory;

/**
 * 数据库帮助类
 * Created by Administrator on 2017/5/16.
 */

public class RealmUtils {
    public static final String DB_NAME = "cpigeonhelper.realm";
    private Realm mRealm;
    private static RealmUtils instance;

    private static String TABLE_OFFLINE_FILE = "TABLE_OFFLINE_FILE";

    private RealmUtils() {
    }

    public static RealmUtils getInstance() {
        if (instance == null) {
            synchronized (RealmUtils.class) {
                if (instance == null)
                    instance = new RealmUtils();
            }
        }
        return instance;
    }

    private Realm getRealm() {
        try {
            try {
                if (mRealm == null || mRealm.isClosed())
                    mRealm = Realm.getDefaultInstance();
            } catch (Exception e) {
                getRealm().init(MyApplication.getContext());
                RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                        .name(RealmUtils.DB_NAME)
                        .schemaVersion(1)
                        .rxFactory(new RealmObservableFactory())
                        .deleteRealmIfMigrationNeeded()
                        .build();
                Realm.setDefaultConfiguration(realmConfiguration);
                mRealm = Realm.getDefaultInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mRealm;
    }

    public void close() {
        if (mRealm != null) {
            mRealm.close();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 登录相关
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 添加用户信息
     *
     * @param userBean
     */
    public void insertUserInfo(UserBean userBean) {
        getRealm().beginTransaction();//开启事务
        getRealm().copyToRealm(userBean);
        getRealm().commitTransaction();
    }


    /**
     * 查询用户信息
     *
     * @return
     */
    public RealmResults<UserBean> queryUserInfo() {
        return getRealm().where(UserBean.class).findAll();
    }


    /**
     * 查询用户信息
     *
     * @return
     */
    public UserBean queryUserInfoData() {
        RealmResults<UserBean> results = getRealm().where(UserBean.class).findAll();
        if (results != null && results.size() == 1) {
            return getRealm().where(UserBean.class).findAll().get(0);
        }
        return null;
    }

    /**
     * 判断是否存在用户信息
     *
     * @return
     */
    public boolean existUserInfo() {
        RealmResults<UserBean> results = getRealm().where(UserBean.class).findAll();
        if (results != null && results.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 删除用户信息
     */
    public void deleteUserInfo() {
        RealmResults<UserBean> results = getRealm().where(UserBean.class).findAll();
        getRealm().executeTransaction(realm -> results.deleteAllFromRealm());
    }

    /**
     * 删除所有用户信息
     */
    public void deleteUserAllInfo() {
        //删除鸽运通服务
        if (RealmUtils.getInstance().existGYTInfo()) {
            RealmUtils.getInstance().deleteGYTInfo();
        }

        //删除用户信息
        if (RealmUtils.getInstance().existUserInfo()) {
            RealmUtils.getInstance().deleteUserInfo();
        }

        //删除单条鸽运通内容
        if (RealmUtils.getInstance().existGYTBeanInfo()) {
            RealmUtils.getInstance().deleteGYTBeanInfo();
        }

        //删除组织信息
        if (RealmUtils.getInstance().existOrgInfo()) {
            RealmUtils.getInstance().deleteXieHuiInfo();
        }

        //删除公告通知数据
        if (RealmUtils.getInstance().existBulletinEntity()) {
            RealmUtils.getInstance().deleteBulletinEntity();
        }

//        if (RealmUtils.getInstance().existLocation()) {
//            RealmUtils.getInstance().deleteLocation();
//        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 单条鸽车监控的数据
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 添加单条鸽车监控的数据
     *
     * @param gytBean
     */
    public void insertGYTBeanInfo(GeYunTongs gytBean) {
        getRealm().beginTransaction();//开启事务
        getRealm().copyToRealm(gytBean);
        getRealm().commitTransaction();
    }

    /**
     * 查询用户信息
     *
     * @return
     */
    public RealmResults<GeYunTongs> queryGYTBeanInfo() {

        return getRealm().where(GeYunTongs.class).findAll();
    }

    /**
     * 判断是否存在用户信息
     *
     * @return
     */
    public boolean existGYTBeanInfo() {
        RealmResults<GeYunTongs> results = getRealm().where(GeYunTongs.class).findAll();
        if (results != null && results.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 删除用户信息
     */
    public void deleteGYTBeanInfo() {
        RealmResults<GeYunTongs> results = getRealm().where(GeYunTongs.class).findAll();
        getRealm().executeTransaction(realm -> results.deleteAllFromRealm());
    }


    ///////////////////////////////////////////////////////////////////////////
    // 鸽运通服务信息
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 查询鸽运通服务
     *
     * @return
     */
    public RealmResults<GYTService> queryGTYInfo() {
        return getRealm().where(GYTService.class).findAll();
    }

    /**
     * 添加鸽运通服务
     *
     * @param gytService
     */
    public void insertGYTService(GYTService gytService) {
        getRealm().beginTransaction();//开启事务
        getRealm().copyToRealm(gytService);
        getRealm().commitTransaction();
    }

    /**
     * 是否存在鸽运通服务
     *
     * @return
     */
    public boolean existGYTInfo() {
        RealmResults<GYTService> results = getRealm().where(GYTService.class).findAll();
        if (results != null && results.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 删除鸽运通服务
     */
    public void deleteGYTInfo() {
        RealmResults<GYTService> results = getRealm().where(GYTService.class).findAll();
        getRealm().executeTransaction(realm -> results.deleteAllFromRealm());
    }


    ///////////////////////////////////////////////////////////////////////////
    // 坐标相关
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 插入坐标
     *
     * @param location
     */
    public void insertLocation(MyLocation location) {
        getRealm().beginTransaction();
        getRealm().copyToRealm(location);
        getRealm().commitTransaction();
    }


    /**
     * 查询坐标
     */
    public RealmResults<MyLocation> queryLocation() {
        return getRealm().where(MyLocation.class).findAll();
    }

    /**
     * 删除所有坐标
     */
    public void deleteLocation() {
        RealmResults<MyLocation> results = getRealm().where(MyLocation.class).findAll();
        getRealm().executeTransaction(realm -> results.deleteAllFromRealm());
    }

    /**
     * 是否存在坐标数据
     *
     * @return
     */
    public boolean existLocation() {
        RealmResults<MyLocation> results = getRealm().where(MyLocation.class).findAll();
        if (results != null && results.size() > 0) {
            return true;
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 协会信息
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 插入协会信息
     *
     * @param orgInfo
     */
    public void insertXieHuiInfo(OrgInfo orgInfo) {
        getRealm().beginTransaction();
        getRealm().copyToRealmOrUpdate(orgInfo);
        getRealm().commitTransaction();
    }

    /**
     * 删除用户关联的协会信息
     */
    public void deleteXieHuiInfo() {
        RealmResults<OrgInfo> results = getRealm().where(OrgInfo.class).findAll();
        getRealm().executeTransaction(realm -> results.deleteAllFromRealm());
    }

    /**
     * 查询用户关联的协会信息
     *
     * @param uid
     * @return
     */
    public RealmResults<OrgInfo> queryOrgInfo(int uid) {
        return getRealm().where(OrgInfo.class).equalTo("uid", uid).findAll();
    }


    /**
     * 获取协会名称
     *
     * @return
     */
    public OrgInfo queryOrgInfoName() {
        RealmResults<OrgInfo> results = getRealm().where(OrgInfo.class).findAll();
        for (OrgInfo orgInfo : results) {
            return orgInfo;
        }

        return null;
    }

    /**
     * 是否存在协会信息
     *
     * @return
     */
    public boolean existOrgInfo() {
        RealmResults<OrgInfo> results = getRealm().where(OrgInfo.class).findAll();
        if (results != null && results.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 保存组织信息
     */

    public static void preservationOrgInfo(OrgInfo orgInfo) {
        if (RealmUtils.getInstance().existOrgInfo()) {
            RealmUtils.getInstance().deleteXieHuiInfo();
            RealmUtils.getInstance().insertXieHuiInfo(orgInfo);
        } else {
            RealmUtils.getInstance().insertXieHuiInfo(orgInfo);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 服务类型（鸽运通，鸽训通）
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 插入 服务类型（鸽运通，鸽训通）
     *
     * @param serviceType
     */
    public void insertServiceType(ServiceType serviceType) {
        getRealm().beginTransaction();
        getRealm().copyToRealmOrUpdate(serviceType);
        getRealm().commitTransaction();
    }

    /**
     * 删除 服务类型（鸽运通，鸽训通）
     */
    public void deleteServiceType() {
        RealmResults<ServiceType> results = getRealm().where(ServiceType.class).findAll();
        getRealm().executeTransaction(realm -> results.deleteAllFromRealm());
    }


    /**
     * 是否存在 服务类型（鸽运通，鸽训通）
     *
     * @return
     */
    public boolean existServiceType() {
        RealmResults<ServiceType> results = getRealm().where(ServiceType.class).findAll();
        if (results != null && results.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询 服务类型（鸽运通，鸽训通）
     *
     * @return
     */
    public RealmResults<ServiceType> queryServiceType() {
        return getRealm().where(ServiceType.class).findAll();
    }

    /**
     * 保存  服务类型（鸽运通，鸽训通）
     */
    public static void preservationServiceType(ServiceType serviceType) {
        if (RealmUtils.getInstance().existServiceType()) {
            RealmUtils.getInstance().deleteServiceType();
            RealmUtils.getInstance().insertServiceType(serviceType);
        } else {
            RealmUtils.getInstance().insertServiceType(serviceType);
        }
    }

    public static RealmResults<ServiceType> infos = RealmUtils.getInstance().queryServiceType();

    /**
     * 获取  服务类型（鸽运通，鸽训通(geyuntong,xungetong)）
     *
     * @return
     */
    public static String getServiceType() {
        try {
            for (ServiceType mServiceType : infos) {
                if (!mServiceType.getServiceType().equals("")) {
                    return mServiceType.getServiceType();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    ///////////////////////////////////////////////////////////////////////////
    // 公告通知
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 插入 公告通知
     *
     * @param mBulletinEntity
     */
    public void insertBulletinEntity(BulletinEntity mBulletinEntity) {
        getRealm().beginTransaction();
        getRealm().copyToRealmOrUpdate(mBulletinEntity);
        getRealm().commitTransaction();
    }

    /**
     * 删除 公告通知
     */
    public void deleteBulletinEntity() {
        RealmResults<BulletinEntity> results = getRealm().where(BulletinEntity.class).findAll();
        getRealm().executeTransaction(realm -> results.deleteAllFromRealm());
    }


    /**
     * 是否存在 公告通知
     *
     * @return
     */
    public boolean existBulletinEntity() {
        RealmResults<BulletinEntity> results = getRealm().where(BulletinEntity.class).findAll();
        if (results != null && results.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询 公告通知
     *
     * @return
     */
    public RealmResults<BulletinEntity> queryBulletinEntity() {
        return getRealm().where(BulletinEntity.class).findAll();
    }


    /**
     * 保存  公告通知
     */
    public static void preservationBulletinEntity(BulletinEntity mBulletinEntity) {
        if (RealmUtils.getInstance().existBulletinEntity()) {
            RealmUtils.getInstance().deleteBulletinEntity();
            RealmUtils.getInstance().insertBulletinEntity(mBulletinEntity);
        } else {
            RealmUtils.getInstance().insertBulletinEntity(mBulletinEntity);
        }
    }

    public static RealmResults<BulletinEntity> infosBulletinEntity = RealmUtils.getInstance().queryBulletinEntity();

    /**
     * 获取  公告通知 时间
     *
     * @return
     */
    public static String getBulletinEntityTime() {
        for (BulletinEntity mBulletinEntity : infosBulletinEntity) {
            if (!mBulletinEntity.getTime().equals("")) {
                return mBulletinEntity.getTime();
            }
        }
        return "";
    }


    ///////////////////////////////////////////////////////////////////////////
    // 是否第一次登录app
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 插入 是否第一次登录app
     *
     * @param mIsLoginAppBean
     */
    public void insertIsLoginAppEntity(IsLoginAppBean mIsLoginAppBean) {
        getRealm().beginTransaction();
        getRealm().copyToRealmOrUpdate(mIsLoginAppBean);
        getRealm().commitTransaction();
    }

    /**
     * 删除 是否第一次登录app
     */
    public void deleteIsLoginAppEntity() {
        RealmResults<IsLoginAppBean> results = getRealm().where(IsLoginAppBean.class).findAll();
        getRealm().executeTransaction(realm -> results.deleteAllFromRealm());
    }


    /**
     * 是否存在 是否第一次登录app
     *
     * @return
     */
    public boolean existIsLoginAppEntity() {
        RealmResults<IsLoginAppBean> results = getRealm().where(IsLoginAppBean.class).findAll();
        if (results != null && results.size() > 0) {
            return true;
        }
        return false;
    }


    ///////////////////////////////////////////////////////////////////////////
    // 离线缓存
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 插入 离线文件
     *
     * @param offlineFileEntity
     */
    public void insertOfflineFileEntity(OfflineFileEntity offlineFileEntity) {

        OfflineFileEntity offlineFileEntity1 = findOfflineFileEntity(offlineFileEntity.getUserId()
                , offlineFileEntity.getRid(), offlineFileEntity.getFt());

        if (offlineFileEntity1 == null) {
            getRealm().beginTransaction();
            getRealm().copyToRealm(offlineFileEntity);
            getRealm().commitTransaction();
        }
    }

    /**
     * 删除 离线文件
     */
    public void deleteOfflineFileEntity(OfflineFileEntity offlineFileEntity) {

        if (offlineFileEntity == null) {
            return;
        }

        RealmResults<OfflineFileEntity> userList = getRealm().where(OfflineFileEntity.class)
                .equalTo("userId", offlineFileEntity.getUserId())
                .equalTo("rid", offlineFileEntity.getRid())
                .equalTo("ft", offlineFileEntity.getFt()).findAll();

        for (OfflineFileEntity offlineFileEntity1 : userList) {
            LogUtil.print(offlineFileEntity1.toString());
        }

        LogUtil.print("userList size: " + userList.size());

        if (userList.size() > 0) {
            getRealm().executeTransaction(realm -> {
                userList.get(0).deleteFromRealm();
            });
        }
    }

    public OfflineFileEntity findOfflineFileEntity(String userId, String matchId, String fileType) {

        RealmResults<OfflineFileEntity> userList = getRealm().where(OfflineFileEntity.class)
                .equalTo("userId", userId)
                .equalTo("rid", matchId)
                .equalTo("ft", fileType).findAll();

        for (OfflineFileEntity offlineFileEntity : userList) {
            LogUtil.print(offlineFileEntity.toString());
        }

        LogUtil.print("userList size: " + userList.size());

        if (userList.size() == 0) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 用户登录
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 插入 用户登录信息
     */
    public void insertUserLoginEntity(UserLoginEntity mUserLoginEntity) {

        if (existUserLoginEntity()) {
            deleteUserLoginEntity();
        }
        getRealm().beginTransaction();
        getRealm().copyToRealmOrUpdate(mUserLoginEntity);
        getRealm().commitTransaction();
    }

    /**
     * 删除 用户登录信息
     */
    public void deleteUserLoginEntity() {
        RealmResults<UserLoginEntity> results = getRealm().where(UserLoginEntity.class).findAll();
        getRealm().executeTransaction(realm -> results.deleteAllFromRealm());
    }


    /**
     * 是否存在 用户登录信息
     *
     * @return
     */
    public boolean existUserLoginEntity() {
        RealmResults<UserLoginEntity> results = getRealm().where(UserLoginEntity.class).findAll();
        if (results != null && results.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询用户信息
     *
     * @return
     */
    public RealmResults<UserLoginEntity> queryUserLoginEntity() {
        return getRealm().where(UserLoginEntity.class).findAll();
    }


}
