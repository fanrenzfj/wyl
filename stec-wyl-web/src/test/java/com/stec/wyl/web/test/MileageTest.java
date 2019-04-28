package com.stec.wyl.web.test;

import com.stec.utils.MileageUtils;
import com.stec.utils.StringUtils;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/9/13
 * Time: 14:02
 */
public class MileageTest {

    @Test
    public void transStr(){
        String[]  lines = mileage.split("\n");

        for (String line : lines) {
            String[] subs = line.split("\t");
            String start = "";
            String end = "";
            if(subs.length == 3) {
                String head = subs[0];
                start = conbine(head, subs[1]);
                end = conbine(head, subs[2]);
            }
            System.out.println(start + "\t" + end );
        }
    }

    private String conbine(String head, String mileage) {
        String ret = "";
        if(StringUtils.isNotEmpty(mileage)) {
            float value = Float.valueOf(mileage);
            ret = MileageUtils.format(value, head);
        }
        return ret;
    }

    private String mileage = "NK/SK\t1040\t1249\n" +
            "SK\t1269.228\t3021.794\n" +
            "NK\t1270\t3105\n" +
            "NK/SK\t3128.545\t3683.686\n" +
            "SK\t3706.794\t5454.012\n" +
            "NK\t3706.241\t5453.351\n" +
            "NK/SK\t5473.536\t5720.575\n" +
            "NK/SK\t\t\n" +
            "NK/SK\t1040\t1062\n" +
            "NK/SK\t1062\t1125\n" +
            "NK/SK\t1125\t1155\n" +
            "NK/SK\t1155\t1185\n" +
            "NK/SK\t1185\t1214\n" +
            "NK/SK\t1214\t1235\n" +
            "NK/SK\t1235\t1249\n" +
            "NK/SK\t1251\t1270\n" +
            "NK\t1132.95\t1160.884\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t1269.5\t1370\n" +
            "SK\t1370\t1470\n" +
            "SK\t1470\t1570\n" +
            "SK\t1570\t1670\n" +
            "SK\t1670\t1770\n" +
            "SK\t1770\t1870\n" +
            "SK\t1870\t1970\n" +
            "SK\t1970\t2070\n" +
            "SK\t2070\t2170\n" +
            "SK\t2170\t2270\n" +
            "SK\t2270\t2370\n" +
            "SK\t2370\t2470\n" +
            "SK\t2470\t2570\n" +
            "SK\t2570\t2670\n" +
            "SK\t2670\t2770\n" +
            "SK\t2770\t2870\n" +
            "SK\t2870\t3022\n" +
            "NK\t1270\t1370\n" +
            "NK\t1370\t1470\n" +
            "NK\t1470\t1570\n" +
            "NK\t1570\t1670\n" +
            "NK\t1670\t1770\n" +
            "NK\t1770\t1870\n" +
            "NK\t1870\t1970\n" +
            "NK\t1970\t2070\n" +
            "NK\t2070\t2170\n" +
            "NK\t2170\t2270\n" +
            "NK\t2270\t2370\n" +
            "NK\t2370\t2470\n" +
            "NK\t2470\t2570\n" +
            "NK\t2570\t2670\n" +
            "NK\t2670\t2770\n" +
            "NK\t2770\t2870\n" +
            "NK\t2870\t2970\n" +
            "NK\t2970\t3106\n" +
            "NK\t\t\n" +
            "SK\t\t\n" +
            "NK\t1040\t1062\n" +
            "SK\t1040\t1061.5\n" +
            "NK\t1040\t1062\n" +
            "NK\t\t\n" +
            "NK/SK\t1251\t1270\n" +
            "NK/SK\t1251\t1270\n" +
            "NK/SK\t1251\t1270\n" +
            "NK/SK\t1251\t1270\n" +
            "NK/SK\t1251\t1270\n" +
            "NK/SK\t1251\t1270\n" +
            "NK/SK\t1251\t1270\n" +
            "NK/SK\t1251\t1270\n" +
            "NK/SK\t1251\t1270\n" +
            "SK\t1270\t1370\n" +
            "SK\t1270\t1370\n" +
            "SK\t1270\t1370\n" +
            "SK\t1270\t1370\n" +
            "SK\t1270\t1370\n" +
            "SK\t1270\t1370\n" +
            "SK\t1370\t1470\n" +
            "SK\t1370\t1470\n" +
            "SK\t1370\t1470\n" +
            "SK\t1370\t1470\n" +
            "SK\t1370\t1470\n" +
            "SK\t1370\t1470\n" +
            "NK\t1270\t1370\n" +
            "NK\t1270\t1370\n" +
            "NK\t1270\t1370\n" +
            "NK\t1270\t1370\n" +
            "NK\t1270\t1370\n" +
            "NK\t1270\t1370\n" +
            "NK\t1370\t1470\n" +
            "NK\t1370\t1470\n" +
            "NK\t1370\t1470\n" +
            "NK\t1370\t1470\n" +
            "NK\t1370\t1470\n" +
            "NK\t1370\t1470\n" +
            "NK\t1470\t1570\n" +
            "NK\t1470\t1570\n" +
            "NK\t1470\t1570\n" +
            "NK\t1470\t1570\n" +
            "NK\t1470\t1570\n" +
            "NK\t1470\t1570\n" +
            "NK\t1570\t1670\n" +
            "NK\t1570\t1670\n" +
            "NK\t1570\t1670\n" +
            "NK\t1570\t1670\n" +
            "NK\t1570\t1670\n" +
            "NK\t1570\t1670\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t1040\t1062\n" +
            "NK\t1040\t1062\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "\t\t\n" +
            "SK\t1270\t1370\n" +
            "SK\t1270\t1370\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "NK\t1270\t1370\n" +
            "NK\t1270\t1370\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t1040\t1062\n" +
            "NK\t1040\t1062\n" +
            "NK\t1040\t1062\n" +
            "NK\t1040\t1062\n" +
            "NK\t1040\t1062\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "NK\t\t\n" +
            "SK\t1270\t1370\n" +
            "SK\t1270\t1370\n" +
            "SK\t1270\t1370\n" +
            "SK\t1270\t1370\n" +
            "SK\t1270\t1370\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t\n" +
            "SK\t\t";
}
