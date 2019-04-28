package com.stec.masterdata.entity.wyl;

import com.baomidou.mybatisplus.annotations.TableField;
import com.stec.masterdata.entity.common.MasterEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with STEC METADATA DESIGN.
 *
 * @author 谢娇
 * Date: 2019/04/24
 * Time: 13:57
 *
 * 里程信息 
 * 项目里程信息表
 */
@Entity
@Table(name = "mileage_info")
public class MileageInfo extends MasterEntity<String> {

    private static final long serialVersionUID = 108L;

    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_MILEAGE = "mileage";
    public static final String COLUMN_MILEAGE_VALUE = "mileage_value";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";

    public static final String ATTRIBUTE_PROJECT_ID = "projectId";
    public static final String ATTRIBUTE_POSITION = "position";
    public static final String ATTRIBUTE_MILEAGE = "mileage";
    public static final String ATTRIBUTE_MILEAGE_VALUE = "mileageValue";
    public static final String ATTRIBUTE_LONGITUDE = "longitude";
    public static final String ATTRIBUTE_LATITUDE = "latitude";

    /**
     * 项目ID by 谢娇 on 2019/04/24 13:58
     * 项目ID
     * 项目ID
     */
    @Column(name = "project_id")
    private Long projectId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    /**
     * 方位 by 谢娇 on 2019/04/24 13:59
     * 方位
     * 方位
     */
    @Column(name = "position")
    private String position;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * 里程号 by 谢娇 on 2019/04/24 13:59
     * 里程号
     * 里程号
     */
    @Column(name = "mileage")
    private String mileage;

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    /**
     * 里程值 by 谢娇 on 2019/04/24 14:00
     * 里程值
     * 里程值
     */
    @Column(name = "mileage_value")
    private Double mileageValue;

    public Double getMileageValue() {
        return mileageValue;
    }

    public void setMileageValue(Double mileageValue) {
        this.mileageValue = mileageValue;
    }

    /**
     * 经度 by 谢娇 on 2019/04/24 14:00
     * 经度
     * 经度
     */
    @Column(name = "longitude")
    private String longitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 纬度 by 谢娇 on 2019/04/24 14:01
     * 纬度
     * 纬度
     */
    @Column(name = "latitude")
    private Double latitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

}