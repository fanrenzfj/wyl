package com.stec.wyl.web.test;

import com.stec.utils.ImageRotateUtils;
import org.junit.Test;

import java.io.File;


/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/10/16 0016
 * Time: 8:58
 */
public class ImageTest {

    @Test
    public void rotateImage() {
        String src = "e:/img.jpg";
//        String src = "e:/stec-wyl.sql";
        //获取图片旋转角度
        ImageRotateUtils.judgeRotate(src);
    }
}
