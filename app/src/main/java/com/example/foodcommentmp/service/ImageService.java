package com.example.foodcommentmp.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.foodcommentmp.Config.ImageConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangweikun
 * @create: 2022-05-03 9:22
 */
public class ImageService {

    /**
     *
     * @param filePath 是文件夹+文件名，格式为："/FileDir/filename"
     * @return 获取的图片
     */
    public static Bitmap getImage(String filePath){
        File file = new File(ImageConfig.DIR + filePath);
        if(!file.exists()){
            Log.i("获取图片", "该路径图片不存在" + filePath);
            return null;
        }
        if(file.isDirectory()){
            Log.i("获取图片", "请输入文件路径而不是文件夹路径" + filePath);
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(ImageConfig.DIR + filePath);
        return bitmap;
    }

    /**
     *
     * @param fileDir 返回某文件夹的全部图片，格式为："/FileDir"
     * @return 获取的图片列表
     */
    public static List<Bitmap> getImages(String fileDir){
        File file = new File(ImageConfig.DIR + fileDir);
        if(!file.isDirectory()){
            Log.i("获取图片", "该文件夹不存在" + fileDir);
            return null;
        }

        List<Bitmap> bitmaps = new ArrayList<>();
        File[] files = file.listFiles();
        for(File temp : files){
            String path = temp.getAbsolutePath();
            bitmaps.add(BitmapFactory.decodeFile(path));
        }

        return bitmaps;
    }

}
