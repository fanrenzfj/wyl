package com.stec.wyl.web.test;

import com.stec.wyl.web.WebApp;
import com.stec.wyl.web.utils.CoodinateCovertor;
import com.stec.wyl.web.utils.LngLat;
import com.stec.wyl.web.wrapper.LocationWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import stec.framework.excel.ExcelTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/6 0006
 * Time: 14:00
 */
public class CoorTransTest {

    @Test
    public void transFromExcel() throws Exception {

        String[] cs = coors.split("\n");
        for (String coor : cs) {
            String[] point = coor.split("\t");

            Map<String, Double> map = CoodinateCovertor.convertMC2LL(Double.parseDouble(point[0]), Double.parseDouble(point[1]));
            Double lng = map.get("lng");
            Double lat = map.get("lat");
            LngLat lngLat_bd = new LngLat(lng, lat);
            LngLat lngLat = CoodinateCovertor.bd_decrypt(lngLat_bd);
            System.out.println(lngLat.getLantitude() +"\t"+lngLat.getLongitude());
        }
    }

    private static String coors = "13374520.787600 \t3519742.191050 \n" +
            "13374405.206100 \t3519737.798950 \n" +
            "13374289.624600 \t3519733.406840 \n" +
            "13374174.109600 \t3519727.589350 \n" +
            "13374058.606900 \t3519721.521310 \n" +
            "13373943.012200 \t3519717.489090 \n" +
            "13373827.497200 \t3519711.615320 \n" +
            "13373711.990400 \t3519705.571810 \n" +
            "13373596.505400 \t3519699.123390 \n" +
            "13373481.003000 \t3519693.008790 \n" +
            "13373365.471700 \t3519687.449760 \n" +
            "13373249.922700 \t3519682.371700 \n" +
            "13373134.295900 \t3519679.399310 \n" +
            "13373018.693000 \t3519675.713440 \n" +
            "13372903.190200 \t3519669.676280 \n" +
            "13372787.727000 \t3519662.847790 \n" +
            "13372672.369400 \t3519654.434570 \n" +
            "13372556.998000 \t3519646.264370 \n" +
            "13372441.461700 \t3519640.811300 \n" +
            "13372326.106200 \t3519632.681840 \n" +
            "13372210.885500 \t3519622.555350 \n" +
            "13372095.512400 \t3519614.343780 \n" +
            "13371980.091600 \t3519606.846270 \n" +
            "13371864.653000 \t3519599.614080 \n" +
            "13371749.251200 \t3519591.817480 \n" +
            "13371633.849300 \t3519584.020870 \n" +
            "13371518.447400 \t3519576.224270 \n" +
            "13371403.026700 \t3519568.720510 \n" +
            "13371287.578300 \t3519561.646490 \n" +
            "13371172.129900 \t3519554.572480 \n" +
            "13371057.152700 \t3519543.122760 \n" +
            "13370942.189300 \t3519531.374880 \n" +
            "13370826.786000 \t3519523.599510 \n" +
            "13370711.257900 \t3519518.644960 \n" +
            "13370595.858100 \t3519513.703050 \n" +
            "13370481.319700 \t3519498.482770 \n" +
            "13370366.002600 \t3519489.520350 \n" +
            "13370250.538200 \t3519482.713850 \n" +
            "13370134.952800 \t3519480.920990 \n" +
            "13370019.402900 \t3519484.414070 \n" +
            "13369904.160600 \t3519494.292010 \n" +
            "13369788.615700 \t3519497.429440 \n" +
            "13369672.951800 \t3519497.915350 \n" +
            "13369557.737300 \t3519507.999160 \n" +
            "13369442.412600 \t3519516.636380 \n" +
            "13369326.918400 \t3519522.918540 \n" +
            "13369211.313300 \t3519525.659370 \n" +
            "13369095.652300 \t3519526.619220 \n" +
            "13368980.198200 \t3519520.094200 \n" +
            "13368864.702800 \t3519514.403560 \n" +
            "13368749.048800 \t3519512.813790 \n" +
            "13368633.395500 \t3519511.173490 \n" +
            "13368519.802900 \t3519503.958000 \n" +
            "13368461.594500 \t3519501.312160 \n";
}
