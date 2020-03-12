package com.mmall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: fangcong
 * @date: 2020/2/11
 */
public class Wu {
    private static final String firstName = "吴";
    private static final String DIR = "E:/name/";
    private static final String PREFIX = "name_tu_huo2.txt";
//    private static String[] huo = {"之","太","天","爻","中","冉","冬","呈","利","良",
//                                    "廷","志","姊","彤","昊","来","妮","昕","炎","易","政",
//                                    "知","忠","卓","炳","帝","哚","俊","亮","南","映","秦","昱",
//                                    "昭","贞","亭","炫","朗","夏","迅","展","哲","恬"};
//
//    private static String[] tu = {"一","安","伊","亦","宇","羽","辰","廷","冶","佑","余",
//                                    "艾","宛","依","咏","昀","姚","怡","宥","玥"};
//

    private static String[] huo = {"妮","娜","昕","晓","晴","晶","乐","灵"};
    private static String[] tu = {"永","辰","亦","怡"};
    public static void main(String[] args) {
        int count = 0;
        File dir = new File(DIR);
        if(!dir.exists()){
            try {
                dir.mkdirs();
                File file = new File(DIR+PREFIX);
                if(!file.exists()){
                    file.createNewFile();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        try {
            FileWriter fileWriter = new FileWriter(DIR+PREFIX);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for(int i = 0; i < huo.length ; i ++){
                for(int j = 0; j < tu.length; j ++){
                    count++;
                    if(count % 8 == 0){
                        printWriter.println();
                        System.out.println();
                    }
                    String s = firstName+tu[j]+huo[i]+"    ";
                    printWriter.print(s);
                    printWriter.flush();
                    System.out.print(firstName+huo[i]+tu[j]+"    ");
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println(count);
    }
}
