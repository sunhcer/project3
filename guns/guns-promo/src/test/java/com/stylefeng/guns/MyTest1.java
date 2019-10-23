package com.stylefeng.guns;

import com.stylefeng.guns.rest.util.RandomUtils;
import org.junit.Test;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/18
 * @Time 20:26
 */
public class MyTest1 {

    @Test
    public void testRandomUtils(){
        String nowDateString = RandomUtils.getNowDateString();
        String random6 = RandomUtils.getLenNumRandom(6);
        String random16 = RandomUtils.getLenNumRandom(16);
        String randomMix6 = RandomUtils.getLenMixRandom(6);
        String randomMix16 = RandomUtils.getLenMixRandom(16);

        System.out.println(nowDateString);
        System.out.println(random6);
        System.out.println(random16);
        System.out.println(randomMix6);
        System.out.println(randomMix16);
    }
}
