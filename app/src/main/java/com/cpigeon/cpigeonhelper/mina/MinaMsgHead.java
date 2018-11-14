package com.cpigeon.cpigeonhelper.mina;

/**
 *
 * Mina框架的消息对象
 * Created by Administrator on 2017/7/4.
 */

public class MinaMsgHead {

    public short     event;//消息事件号 2位
    public int       bodyLen;//消息内容长度 4位
}
