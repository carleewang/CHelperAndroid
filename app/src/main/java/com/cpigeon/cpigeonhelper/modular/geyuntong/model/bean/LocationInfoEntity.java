package com.cpigeon.cpigeonhelper.modular.geyuntong.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 位置信息实体类
 * Created by Administrator on 2017/10/12.
 */

public class LocationInfoEntity {


    /*
    {id=27673.0, time=2017-10-12 19:00:51, weather={windPower=5, weather=多云, temperature=17.0, windDirction=西, time=2017-10-12 19:00:00}
    */

    /**
     * speed : 0
     * lo : 104.032236
     * time : 2017-06-07 15:36:30
     * id : 1
     * la : 30.66864
     */

    private int speed;//速度
    private double lo;//经度
    private String time;//时间
    private int id;
    private double la;//纬度
    /**
     * weather : {"windPower":5,"weather":"多云","temperature":25,"windDirction":"北","time":"2017-09-25 16: 00: 00"}
     * lo : 104.032424
     * lc : 0
     * la : 30.668606
     */

    private WeatherBean weather;
    //    @SerializedName("lo")
//    private transient double loX;
    private double lc;//里程
//    @SerializedName("la")
//    private transient double laX;

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getLo() {
        return lo;
    }

    public void setLo(double lo) {
        this.lo = lo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLa() {
        return la;
    }

    public void setLa(double la) {
        this.la = la;
    }

    public WeatherBean getWeather() {
        return weather;
    }

    public void setWeather(WeatherBean weather) {
        this.weather = weather;
    }

//    public double getLoX() {
//        return loX;
//    }
//
//    public void setLoX(double loX) {
//        this.loX = loX;
//    }

    public double getLc() {
        return lc;
    }

    public void setLc(double lc) {
        this.lc = lc;
    }

//    public double getLaX() {
//        return laX;
//    }
//
//    public void setLaX(double laX) {
//        this.laX = laX;
//    }

    public static class WeatherBean {
        /**
         * windPower : 5
         * weather : 多云
         * temperature : 25
         * windDirction : 北
         * time : 2017-09-25 16: 00: 00
         */

        private String windPower;//风力
        private String weather;//天气
        private String temperature;//温度
        private String windDirction;//风向
        @SerializedName("time")
        private String timeX;

        public String getWindPower() {
            return windPower;
        }

        public void setWindPower(String windPower) {
            this.windPower = windPower;
        }

        public String getWeather() {
            return weather;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getWindDirction() {
            return windDirction;
        }

        public void setWindDirction(String windDirction) {
            this.windDirction = windDirction;
        }

        public String getTimeX() {
            return timeX;
        }

        public void setTimeX(String timeX) {
            this.timeX = timeX;
        }
    }
}
