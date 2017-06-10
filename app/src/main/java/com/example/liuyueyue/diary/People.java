package com.example.liuyueyue.diary;

/**
 * Created by liuyueyue on 2017/6/9.
 */

public class People {
    public int ID = -1;
    public String Diary;
    public String Time;
    public String toString()
    {
        String result =this.ID+","+
                "时间:"+this.Time+","
                +"日记内容:"+this.Diary;
        return result;
    }
}
