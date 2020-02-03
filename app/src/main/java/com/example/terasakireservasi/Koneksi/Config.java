package com.example.terasakireservasi.Koneksi;

public class Config {


    public static String getJsonParser() {
        String concatIp;
        JSONParser jsonParser = new JSONParser();
        concatIp=jsonParser.concat();
        return concatIp;
    }

    public static String PATH_URL_IMG = "http://"+getJsonParser()+"/terasaki/uploads/images/category/";
    public static String PATH_URL_IMG_CAT = "http://"+getJsonParser()+"/terasaki/image/FoodPics/";

}
