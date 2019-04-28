package com.stec.wyl.web.utils;

import java.lang.reflect.Field;

public class JavaBeanToString {
    /**
     * 对象转string
     */
    public static String toString(Object o) {
        if (o != null) {//判断传过来的对象是否为空
            StringBuilder sb = new StringBuilder("[");//定义一个保存数据的变量
            Class cs = o.getClass();//获取对象的类
            Field[] fields = cs.getDeclaredFields();//反射获取该对象里面的所有变量
            for (Field f : fields) {//遍历变量
                f.setAccessible(true);//强制允许访问私有变量
                Object value = null;//获取传递过来的对象 对应 f 的类型
                try {
                    value = f.get(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                value = value == null ? "null" : value;//判断获取到的变量是否为空，如果为空就赋值字符串，否则下面代码会异常
                sb.append(f.getName() + ":\"" + value.toString() + "\" ");// f.getName()：获取变量名；value.toString()：变量值装String
            }
            sb.append("]");
            System.out.println(sb.toString());
            return sb.toString();
        } else {
            System.out.println("null");
            return "null";
        }
    }

}
