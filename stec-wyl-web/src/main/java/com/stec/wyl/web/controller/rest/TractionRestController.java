package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.auth.SysUser;
import com.stec.masterdata.entity.basic.DocAttach;
import com.stec.masterdata.entity.basic.SysDic;
import com.stec.masterdata.entity.wyl.Car;
import com.stec.masterdata.entity.wyl.EmergencyEvent;
import com.stec.masterdata.entity.wyl.Traction;
import com.stec.masterdata.handler.auth.SysUserHandler;
import com.stec.masterdata.handler.basic.SysDicHandler;
import com.stec.masterdata.handler.wyl.CarHandler;
import com.stec.masterdata.handler.wyl.TractionHandler;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import com.stec.wyl.web.controller.BaseController;
import com.stec.wyl.web.utils.ExcelResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import stec.framework.excel.ExcelTemplate;
import stec.framework.excel.handler.ExcelHeader;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述：牵引管理
 *
 * @author Li.peng
 * @create 2018-08-29 18:49
 */
@Api(value = "牵引信息controller", tags = {"牵引信息"})
@RestController
@RequestMapping(value = "/rest/traction", method = RequestMethod.POST)
public class TractionRestController extends BaseController {
    @Reference
    private TractionHandler tractionHandler;
    @Reference
    private SysUserHandler sysUserHandler;
    @Reference
    private CarHandler carHandler;
    @Autowired
    private ExcelTemplate excelTemplate;
    @Reference
    private SysDicHandler sysDicHandler;

    @ApiOperation("牵引信息新增/修改")
    @RequestMapping("/save")
    public ResultForm save(@ApiParam(value = "{carId:carId,startTime:startTime,\nendTime:endTime,money:money,type:type,\nposition:position,detailPosition:detailPosition,\nuserId;userId,userName:userName,mobile;mobile,\nreason;reason}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("牵引信息新增/修改", jsonRequestBody);
        Traction entity = jsonRequestBody.tryGet(Traction.class);
        // entity.getMoney() 收费金额
        if (!ObjectUtils.allIsNotNull(entity.getCarId(), entity.getStartTime(), entity.getEndTime(), entity.getType(), entity.getPosition(), entity.getDetailPosition(), entity.getUserId())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, " 牵引车车牌、开始时间、结束时间、车辆类型、方位、详细部位、记录人不能为空！");
        }
        if (ObjectUtils.isNotNull(entity.getCarId())) {
            Car car = carHandler.selectByPrimaryKey(entity.getCarId());
            if (car != null) {
                entity.setCode(car.getCode());
            }
        }
        return ResultForm.createSuccessResultForm(tractionHandler.save(entity), "新增牵引信息成功！");
    }

    @ApiOperation("牵引信息分页列表查询")
    @RequestMapping(value = "/list")
    public ResultForm list(@ApiParam(value = "{code,reason,position,startTime,endTime,[pageForm properties]}") @RequestBody JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();
        String code = jsonRequestBody.getString("code");
        String reason = jsonRequestBody.getString("reason");
        String position = jsonRequestBody.getString("position");
        Date startTime = jsonRequestBody.getDate("startTime");
        Date endTime = jsonRequestBody.getDate("endTime");


        EntityWrapper<Traction> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(code)) {
            wrapper.like("code", code);
        }
        if (StringUtils.isNotEmpty(reason)) {
            wrapper.eq("reason", reason);
        }
        if (StringUtils.isNotEmpty(position)) {
            wrapper.eq("position", position);
        }
        if (ObjectUtils.allIsNotNull(startTime, endTime)) {
            wrapper.le("end_time", endTime).and().ge("start_time", startTime);
        }
        DataPaging<Traction> dataPaging = tractionHandler.selectEntities(wrapper, pageForm);
        List<Traction> list = dataPaging.getList();
        for (Traction traction : list) {
            if (traction.getCarId() != null) {
                Car car = carHandler.selectByPrimaryKey(traction.getCarId());
                if (car != null) {
                    traction.setCode(car.getCode());
                }
                SysUser sysUser = sysUserHandler.selectByPrimaryKey(traction.getUserId());
                if (sysUser != null) {
                    traction.setUserName(sysUser.getName());
                }
            }

        }
        return ResultForm.createSuccessResultForm(dataPaging, "牵引信息表查询成功！");
    }

    @ApiOperation("牵引信息预案")
    @RequestMapping("/get")
    public ResultForm get(@ApiParam(value = "{id:id 牵引信息id}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "牵引信息ID不能为空");
        }
        Traction traction = tractionHandler.selectByPrimaryKey(id);
        Car car = carHandler.selectByPrimaryKey(traction.getCarId());
        traction.setCode(car.getCode());
        SysUser sysUser = sysUserHandler.selectByPrimaryKey(traction.getUserId());
        traction.setUserName(sysUser.getName());
        return ResultForm.createSuccessResultForm(traction, "牵引信息获取成功");
    }

    @ApiOperation("删除牵引信息")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id 牵引信息id}") JsonRequestBody jsonRequestBody) {
        logger("删除牵引信息", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "牵引信息id不能为空");
        }
        return ResultForm.createSuccessResultForm(tractionHandler.deleteByPrimaryKey(id));
    }

    @ApiOperation("牵引信息导出Excel")
    @RequestMapping("/downLoad")
    public ResultForm downLoad(@ApiParam(value = "{code,reason,position,startTime,endTime}") @RequestBody JsonRequestBody jsonRequestBody, HttpServletResponse response) {
        //response响应
        try {
            ExcelResponse.responseHeader(response, "牵引信息");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //获取牵引信息 参数

        Traction traction = jsonRequestBody.tryGet(Traction.class);
        traction.getReason();
        EntityWrapper<Traction> wrapper = new EntityWrapper<>();
        if (ObjectUtils.isNotNull(traction)) {
            wrapper.setEntity(traction);
        }
        wrapper.orderBy("start_time", false);
        List<Traction> tractions = tractionHandler.selectEntities(wrapper);
        //循环tractions
        for (Traction traction1 : tractions) {
            packagingTraction(traction1);
        }
        //映射excel表 把字段与表字段映射
        List<ExcelHeader> headers = packagingExcel();
        try {
            excelTemplate.exportObjects2Excel(tractions, headers, "应急事件", true, response.getOutputStream());
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, e.getMessage());
        }
        return ResultForm.createSuccessResultForm(null, "下载成功！");
    }

    //封装db查出来的EmergencyEvent对象
    private void packagingTraction(Traction traction1) {
        //向数据字典 转换方位
        traction1.setPosition(dBConvertSysDic(traction1.getPosition()));
        //向数据字典 转换车辆类型
        traction1.setType(dBConvertSysDic(traction1.getType()));
        //向数据字典 转换牵引原因
        traction1.setReason(dBConvertSysDic(traction1.getReason()));
    }

    //封装excel表映射字段
    private List<ExcelHeader> packagingExcel() {
        //创建excel Header对象
        List<ExcelHeader> headers = new ArrayList<>();
        headers.add(new ExcelHeader("序号", true));
        headers.add(new ExcelHeader("开始时间", 1, "startTime", Date.class, "yyyy年MM月dd日 hh:mm:ss"));
        headers.add(new ExcelHeader("结束时间", 1, "endTime", Date.class, "yyyy年MM月dd日 hh:mm:ss"));
        headers.add(new ExcelHeader("车辆类型", "type", String.class));
        headers.add(new ExcelHeader("方位", "position", String.class));
        headers.add(new ExcelHeader("详细部位", "detailPosition", String.class));
        headers.add(new ExcelHeader("牵引原因", "reason", String.class));
        headers.add(new ExcelHeader("记录人", "userName", String.class));
        headers.add(new ExcelHeader("手机号码", "mobile", String.class));
        headers.add(new ExcelHeader("牵引描述", "tractionDescribe", String.class));
        return headers;
    }
    //数据库code 转换成数据字典字段
    public String dBConvertSysDic(String dbCode) {
        if (StringUtils.isNotEmpty(dbCode)) {
            SysDic sysDic = new SysDic();
            sysDic.setCode(dbCode);
            if (ObjectUtils.isNotNull(sysDic)) {
                SysDic dbSysDic = sysDicHandler.selectEntity(sysDic);
                if (ObjectUtils.isNotNull(dbSysDic)) {
                    dbCode = dbSysDic.getName();
                }
            }
        }
        return dbCode;
    }

}
