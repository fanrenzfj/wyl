package com.stec.wyl.service.test;


import com.stec.framework.metadata.usage.bean.Area;
import com.stec.masterdata.entity.auth.SysMessageText;
import com.stec.masterdata.entity.auth.SysPrivilege;
import com.stec.masterdata.entity.common.MasterTreeEntity;
import com.stec.masterdata.entity.wyl.DefectType;
import com.stec.masterdata.entity.wyl.HistoricalDefect;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: Joe Xie
 * Date: 2017/8/19
 * Time: 16:25
 */
public class MBStrTest {

    private static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Test
    public void genResultMap() {
        Class clazz = HistoricalDefect.class;
        testColumn(clazz);
        testCondition(clazz, "d.");
    }

    private String queryFields(Class clazz) {
        StringBuilder builder = new StringBuilder("id;name;code;state");
        if (MasterTreeEntity.class.isAssignableFrom(clazz)) {
            builder.append(";parentId;leaf");
        }
        if (Area.class.isAssignableFrom(clazz)) {
            builder.append(";provinceId;cityId;countyId;streetId;detailAddress;address;longitude;latitude");
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!"serialVersionUID".equals(field.getName())) {
                builder.append(";").append(field.getName());
            }
        }
        return builder.toString();
    }

    public void testCondition(Class clazz, String shortName) {
        String columns = queryFields(clazz);
        String[] subs = columns.split(";");
        for (String property : subs) {
            String column = camelToUnderline(property);
            System.out.println("<if test=\"" + property + " != null\">and " + shortName + column + " = #{" + property + "}</if>");
        }
    }

    public void testColumn(Class clazz) {
        String columns = queryFields(clazz);
        String[] subs = columns.split(";");
        System.out.println("<resultMap id=\"resultMap\" type=\"" + clazz.getName() + "\">");
        for (String property : subs) {
            String column = camelToUnderline(property);
            if ("id".equals(property)) {
                System.out.println("\t<id column=\"" + column + "\" property=\"" + property + "\" />");
            } else {
                System.out.println("\t<result column=\"" + column + "\" property=\"" + property + "\" />");
            }
        }
        System.out.println("</resultMap>");
    }

    @Test
    public void classNamespace() {
        System.out.println(SysPrivilege.class.getName());
        System.out.println(SysPrivilege.class.getCanonicalName());
    }
}
