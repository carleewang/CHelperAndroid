/**
 * created by jiang, 16/2/2
 * Copyright (c) 2016, jyuesong@gmail.com All Rights Reserved.
 * *                #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 */

package com.cpigeon.cpigeonhelper.message.ui.selectPhoneNumber.model;



import com.cpigeon.cpigeonhelper.message.ui.selectPhoneNumber.widget.Indexable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiang on 16/2/2.
 */
public class ContactModel {


    private List<MembersEntity> members = new ArrayList<>();

    public void setMembers(List<MembersEntity> members) {
        this.members = members;
    }



    public List<MembersEntity> getMembers() {
        return members;
    }

    public static class MembersEntity implements Indexable {

        private String id;

        private String username;

        private String mobile;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public boolean isChooseVisible;

        public boolean isChoose;

        public String getSortLetters() {
            return sortLetters;
        }

        @Override
        public String getIndex()
        {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        private String sortLetters;

        public void setId(String id) {
            this.id = id;
        }

        public void setUsername(String username) {
            this.username = username;
        }



        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }


    }
}
