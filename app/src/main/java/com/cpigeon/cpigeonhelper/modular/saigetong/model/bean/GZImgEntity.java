package com.cpigeon.cpigeonhelper.modular.saigetong.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class GZImgEntity implements Serializable{


    /**
     * title : 2017年12月01日
     * image : http://118.123.244.89:818/uploadfiles/APP/SGT/thumb/i_11104377_c3c7c3f64789637e513386676ad8ed4a.jpg
     * imglist : [{"sj":"2017/12/19:58:38","imgurl":"http://118.123.244.89:818/uploadfiles/APP/SGT/11104377_c3c7c3f64789637e513386676ad8ed4a.jpg","slturl":"http://118.123.244.89:818/uploadfiles/APP/SGT/thumb/i_11104377_c3c7c3f64789637e513386676ad8ed4a.jpg","tag":"赛鸽入棚"}]
     */

    /**
     * {"status":true,"errorCode":0,"msg":"","data":[{
     * "title":"2017年12月01日",//标题
     * "image":"http://118.123.244.89:818/uploadfiles/APP/SGT/thumb/i_11104377_c3c7c3f64789637e513386676ad8ed4a.jpg",//封面图片
     * "imglist":[{
     * "sj":"2017/12/19:58:38",//创建时间
     * "imgurl":"http://118.123.244.89:818/uploadfiles/APP/SGT/11104377_c3c7c3f64789637e513386676ad8ed4a.jpg",//原图
     * "slturl":"http://118.123.244.89:818/uploadfiles/APP/SGT/thumb/i_11104377_c3c7c3f64789637e513386676ad8ed4a.jpg",//缩略图
     * "tag":"赛鸽入棚"//标签
     * }]//点击进入后图片列表
     * }]}
     */
    
    private String title;
    private String image;
    private List<ImglistBean> imglist;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ImglistBean> getImglist() {
        return imglist;
    }

    public void setImglist(List<ImglistBean> imglist) {
        this.imglist = imglist;
    }

    public static class ImglistBean implements Serializable{
        /**
         * sj : 2017/12/19:58:38
         * imgurl : http://118.123.244.89:818/uploadfiles/APP/SGT/11104377_c3c7c3f64789637e513386676ad8ed4a.jpg
         * slturl : http://118.123.244.89:818/uploadfiles/APP/SGT/thumb/i_11104377_c3c7c3f64789637e513386676ad8ed4a.jpg
         * tag : 赛鸽入棚
         */

        private String sj;
        private String imgurl;
        private String slturl;
        private String tag;

        public String getSj() {
            return sj;
        }

        public void setSj(String sj) {
            this.sj = sj;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getSlturl() {
            return slturl;
        }

        public void setSlturl(String slturl) {
            this.slturl = slturl;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
