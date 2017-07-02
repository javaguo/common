package com.tgw.utils.file;

import com.tgw.exception.PlatformException;
import com.tgw.utils.math.PlatformMathUtils;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zjg on 2017/7/1.
 */
public class PlatformFileUtils {

    /**
     * 重命名文件名。
     * 文件名由年月日时分秒毫秒+3位随即数组成
     * @param orginFileName
     * @return
     */
    public static String renameFileNameByTimeRandom(String orginFileName){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        String random = PlatformMathUtils.createRandString999();

        if(StringUtils.isBlank( orginFileName )){
            throw new PlatformException("文件名参数错误！");
        }

        int startIndex = orginFileName.lastIndexOf(".");
        String suffix = orginFileName.substring( startIndex );

        return sdf.format( new Date() )+random+suffix;
    }
}
