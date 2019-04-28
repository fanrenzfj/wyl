package com.stec.wyl.web.test;

import com.stec.utils.DataTimeUtils;
import com.stec.utils.StringUtils;
import com.stec.utils.TimeUtil;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/8/22 0022
 * Time: 14:43
 */
public class TimeTest {

    @Test
    public void test(){
        String year = "2018";
        try {
            Date date = TimeUtil.parseDate(year + "-01-02");
            Date start = DataTimeUtils.ceilDate(date, DataTimeUtils.YEAR);
            Date end = DataTimeUtils.floorDate(date, DataTimeUtils.YEAR);

            System.out.println();
        } catch (ParseException e) {
        }
    }

    @Test
    public void testDurant() throws ParseException {
        String dateStr = "2018-02-28";
        String dateType = "day";
        Date start, end;
        String datePatter = "";
        String period = "";
        if(StringUtils.equals("year", dateType)) {
            datePatter = "yyyy";
            period = DataTimeUtils.YEAR;
        }
        else if(StringUtils.equals("month", dateType)) {
            datePatter = "yyyy-MM";
            period = DataTimeUtils.MONTH;
        }
        else if(StringUtils.equals("day", dateType)) {
            datePatter = "yyyy-MM-dd";
            period = DataTimeUtils.DAY;
        }
        start = TimeUtil.parseDate(dateStr, datePatter);
        end = DataTimeUtils.next(start, period);
        System.out.println(TimeUtil.format(start) + "####" + TimeUtil.format(end));


    }
}
