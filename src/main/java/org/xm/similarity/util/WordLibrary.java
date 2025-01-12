package org.xm.similarity.util;

/**
 * @ClassName WordLibrary
 * @Description SogouUtil解析后保存的实体
 * @Author Hu
 * @Date 2021/4/27 10:21
 * @Version 1.0
 **/
public class WordLibrary
{
    private int count = 1;
    private String[] pinYin;
    private String pinYinString = "";
    private String word;
    public WordLibrary(String word , String pinYinString){
        this.word = word ;
        this.pinYinString = pinYinString ;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String[] getPinYin() {
        return pinYin;
    }
    public void setPinYin(String[] pinYin) {
        this.pinYin = pinYin;
    }
    public String getPinYinString() {
        return pinYinString;
    }
    public void setPinYinString(String pinYinString) {
        this.pinYinString = pinYinString;
    }
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }

}