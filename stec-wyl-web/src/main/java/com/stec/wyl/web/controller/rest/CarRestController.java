package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.auth.SysOrg;
import com.stec.masterdata.entity.basic.DocInfo;
import com.stec.masterdata.entity.wyl.Car;
import com.stec.masterdata.entity.wyl.CarCategory;
import com.stec.masterdata.entity.wyl.CarUsage;
import com.stec.masterdata.handler.auth.SysOrgHandler;
import com.stec.masterdata.handler.wyl.CarCategoryHandler;
import com.stec.masterdata.handler.wyl.CarHandler;
import com.stec.masterdata.handler.wyl.CarUsageHandler;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import com.stec.wyl.web.controller.BaseController;
import com.stec.wyl.web.service.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/22
 * Time: 17:58
 */
@Api(value = "车辆管理controller", tags = {"车辆管理"})
@RestController
@RequestMapping(value = "/rest/car", method = RequestMethod.POST)
public class CarRestController extends BaseController {
    @Autowired
    private DocumentService documentService;
    @Reference
    private CarHandler carHandler;
    @Reference
    private SysOrgHandler sysOrgHandler;
    @Reference
    private CarUsageHandler carUsageHandler;
    @Reference
    private CarCategoryHandler carCategoryHandler;

    @ApiOperation("车辆管理新增/修改")
    @RequestMapping("/save")
    public ResultForm save(MultipartRequest multipartRequest, Long[] delIds, Car entity, String[] carUsage, String[] carCategory) {
        logger("", JSONObject.toJSONString(entity));
        if (!ObjectUtils.allIsNotNull(entity.getCode(), entity.getType(), entity.getOrgId(), entity.getUseStatus())) {
            return ResultForm.createErrorResultForm(entity, "车牌号码、车辆类型、归属单位、使用状态不能为空");
        }
        if (carUsage.length < 1) {
            return ResultForm.createErrorResultForm(entity, "车牌用途不能为空");
        }
        if (carCategory.length < 1) {
            return ResultForm.createErrorResultForm(entity, "车牌类别不能为空");
        }
        //上传车辆图片
        DocInfo docInfo;
        try {
            docInfo = documentService.uploadFiles(multipartRequest, "file", entity.getDocId(), delIds);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(null, "文档保存失败!");
        }
        entity.setDocId(docInfo.getId());
        return ResultForm.createSuccessResultForm(carHandler.saveEntity(entity, carUsage, carCategory), "保存成功！");
    }

    @ApiOperation("车辆分页列表查询")
    @RequestMapping("/list")
    public ResultForm list(@ApiParam(value = "{code:code,useStatus:[],carUsage;carUsage,[pageForm properties]}") @RequestBody JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();
        String code = jsonRequestBody.getString("code");
        EntityWrapper<Car> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(code)) {
            wrapper.like("code", code);
        }
        String useStatus = "useStatus";
        if (jsonRequestBody.containsKey(useStatus)) {
            List<String> useStatusList = jsonRequestBody.getList("useStatus", String.class);
            if (ObjectUtils.isNotNull(useStatusList)) {
                wrapper.in("use_status", useStatusList);
            }
        }
        String carUsageStr = "carUsage";
        if (jsonRequestBody.containsKey(carUsageStr)) {
            String carUsage = jsonRequestBody.getString("carUsage");
            if (StringUtils.isNotEmpty(carUsage)) {
                CarUsage carUsage1 = new CarUsage();
                carUsage1.setCode(carUsage);
                List<CarUsage> carUsageList = carUsageHandler.selectEntities(carUsage1);
                List<Long> carIds = new ArrayList<>();
                for (CarUsage usage : carUsageList) {
                    carIds.add(usage.getCarId());
                }
                wrapper.in("id", carIds);
            }
        }
        DataPaging<Car> dataPaging = carHandler.selectEntities(wrapper, pageForm);
        List<Car> list = dataPaging.getList();
        List<Map<String, Object>> retList = new ArrayList<>(list.size());
        for (Car car : list) {
            SysOrg sysOrg = sysOrgHandler.selectByPrimaryKey(car.getOrgId());
            if (sysOrg == null) {
                return ResultForm.createErrorResultForm(null, "车辆所挂组织异常！");
            }
            car.setOrgName(sysOrg.getName());
            Map<String, Object> carMap;
            try {
                carMap = MapUtils.toMapIgnoreNull(car);
            } catch (Exception e) {
                return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
            }
            retList.add(carMap);
            List<CarUsage> carUsageList = carUsageHandler.selectEntities(MapUtils.newHashMap("carId", car.getId()));
            carMap.put("carUsage", carUsageList);
            List<CarCategory> carCategoryList = carCategoryHandler.selectEntities(MapUtils.newHashMap("carId", car.getId()));
            carMap.put("carCategory", carCategoryList);
        }
        return ResultForm.createSuccessResultForm(new DataPaging<>(retList, dataPaging.getTotal().intValue(), dataPaging.getOffset(), dataPaging.getLimit()), "车辆列表查询成功！");
    }

    @ApiOperation("获取车辆")
    @RequestMapping("/get")
    public ResultForm get(@ApiParam(value = "{id:id 车辆id}") @RequestBody JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "车辆ID不能为空");
        }
        Car car = carHandler.selectByPrimaryKey(id);
        SysOrg sysOrg = sysOrgHandler.selectByPrimaryKey(car.getOrgId());
        car.setOrgName(sysOrg.getName());
        Map<String, Object> carMap;
        try {
            carMap = MapUtils.toMapIgnoreNull(car);
        } catch (Exception e) {
            return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
        }
        if (ObjectUtils.isNotNull(car)) {
            List<CarUsage> carUsageList = carUsageHandler.selectEntities(MapUtils.newHashMap("carId", car.getId()));
            carMap.put("carUsage", carUsageList);
            List<CarCategory> carCategoryList = carCategoryHandler.selectEntities(MapUtils.newHashMap("carId", car.getId()));
            carMap.put("carCategory", carCategoryList);
            return ResultForm.createSuccessResultForm(carMap, "车辆信息获取成功！");
        } else {
            return ResultForm.createErrorResultForm(jsonRequestBody, "没有车辆信息！");
        }
    }

    @ApiOperation("删除车辆")
    @RequestMapping("/delete")
    public ResultForm delete(@ApiParam(value = "{id:id 车辆id}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("删除车辆", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "车辆id不能为空");
        }
        return ResultForm.createSuccessResultForm(carHandler.deleteEntity(id));
    }

}
