package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.protocol.DeviceType;
import com.stec.masterdata.handler.protocol.DeviceTypeHandler;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import com.stec.wyl.web.controller.BaseController;
import com.stec.masterdata.entity.protocol.Manufacturer;
import com.stec.masterdata.entity.protocol.Product;
import com.stec.masterdata.handler.protocol.ManufacturerHandler;
import com.stec.masterdata.handler.protocol.ProductHandler;
import com.stec.masterdata.handler.protocol.ProtocolHandler;
import com.stec.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author liweigao
 * Date: 2018-07-25
 * Time: 16:46
 */
@Api(value = "供应商controller", tags = "供应商管理")
@RestController
@RequestMapping(value = "/rest/manufacturer", method = RequestMethod.POST)
public class ManufacturerRestController extends BaseController {
    @Reference
    private ManufacturerHandler manufacturerHandler;
    @Reference
    private ProductHandler productHandler;
    @Reference
    private ProtocolHandler protocolHandler;

    @Reference
    private DeviceTypeHandler deviceTypeHandler;

    @ApiOperation(value = "供应商保存和修改")
    @RequestMapping("/save")
    public ResultForm save(@RequestBody @ApiParam(value = "{code:code 供应商编码,\nname:name 供应商名称,\nstate:state 状态,\naddress:address 地址,\ncityId:cityId 城市id，" +
            "\ncountyId:countyId 县id,\nstreetId:streetId 乡镇id,\ndetailAddress:detailAddress 详细地址,\nlatitude:latitude 纬度,\nlongitude:longitude 经度,\nprovinceId:provinceId 省id," +
            "\ncontact:contact 联系人,\nphone:phone 联系电话}") JsonRequestBody jsonRequestBody) {
        logger("供应商保存和修改", jsonRequestBody);
        Manufacturer entity = jsonRequestBody.tryGet(Manufacturer.class);
        if (!StringUtils.isAllNotBlank(entity.getName(), entity.getCode())) {
            ResultForm.createErrorResultForm(jsonRequestBody, "供应商名称，供应商编码不能为空！");
        }
        return ResultForm.createSuccessResultForm(manufacturerHandler.save(entity), "供应商保存成功！");
    }

    @ApiOperation(value = "供应商分页列表查询")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody @ApiParam(value = "{分页参数}") JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();
        Manufacturer entity = jsonRequestBody.tryGet(Manufacturer.class);
        EntityWrapper<Manufacturer> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(entity.getCode())) {
            wrapper.like("code", entity.getCode());
        }
        if (StringUtils.isNotEmpty(entity.getName())) {
            wrapper.like("name", entity.getName());
        }
        return ResultForm.createSuccessResultForm(manufacturerHandler.selectEntities(wrapper, pageForm), "供应商分页查询成功！");
    }
    
    @ApiOperation(value = "删除供应商")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id 供应商id}") JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
        if (id == null) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "供应商id不能为空！");
        }
        return ResultForm.createSuccessResultForm(manufacturerHandler.deleteByPrimaryKey(id), "供应商删除成功！");
    }

    @ApiOperation(value = "产品型号列表查询")
    @RequestMapping("/productList")
    public ResultForm productList(@RequestBody @ApiParam(value = "{manufacturerId:manufacturerId 供应商id,\ndeviceTypeId:deviceTypeId 设备类型id," +
            "\nname:name 产品名称,\ncode:code 产品编码}") JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();
        Product entity = jsonRequestBody.tryGet(Product.class);
        DataPaging<Product> dataPaging =protocolHandler.selectProduct(entity,pageForm);
        List<Product> list=dataPaging.getList();
        List<Map<String, Object>> retList = new ArrayList<>(list.size());
        for (Product product : list) {
            Map<String, Object> productMap;
            try {
                productMap = MapUtils.toMapIgnoreNull(product);
            } catch (Exception e) {
                return ResultForm.createErrorResultForm(jsonRequestBody, e.getMessage());
            }
            DeviceType deviceType =deviceTypeHandler.selectByPrimaryKey(product.getDeviceTypeId());
            DeviceType deviceParentType =deviceTypeHandler.selectByPrimaryKey(product.getDeviceParentTypeId());
            if(ObjectUtils.isNotNull(deviceType)){
                productMap.put("deviceTypeName",deviceType.getName());
            }else{
                productMap.put("deviceTypeName","");
            }
            if(ObjectUtils.isNotNull(deviceParentType)){
                productMap.put("deviceParentTypeName",deviceParentType.getName());
            }else{
                productMap.put("deviceParentTypeName","");
            }
            retList.add(productMap);
        }
        return ResultForm.createSuccessResultForm(new DataPaging<>(retList, dataPaging.getTotal().intValue(), dataPaging.getOffset(), dataPaging.getLimit()), "产品型号查询成功！");
    }
    @ApiOperation(value = "产品型号保存和修改")
    @RequestMapping("/saveProduct")
    public ResultForm saveProduct(@RequestBody @ApiParam(value = "{code:code 产品编码,\nname:name 产品名称,\nstate:state 状态,\nattach:attach 附件," +
            "\ndeviceTypeId:deviceTypeId 设备类型id,\nmanufacturerId:manufacturerId 供应商id,\nwarrantyPeriod:warrantyPeriod 保修期}") JsonRequestBody jsonRequestBody){
        logger("产品保存和修改", jsonRequestBody);
        Product entity = jsonRequestBody.tryGet(Product.class);
        if (!StringUtils.isAllNotBlank(entity.getName(), entity.getCode())) {
            ResultForm.createErrorResultForm(jsonRequestBody, "产品名称，产品编码不能为空！");
        }
        return ResultForm.createSuccessResultForm(productHandler.save(entity),"产品型号保存成功");
    }
    @ApiOperation(value = "删除产品型号")
    @RequestMapping("/deleteProduct")
    public ResultForm deleteProduct(@RequestBody @ApiParam(value = "{id:id 产品型号id}") JsonRequestBody jsonRequestBody){
        Long id = jsonRequestBody.getLong("id");
        if (id == null) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "产品型号id不能为空！");
        }
        return ResultForm.createSuccessResultForm(productHandler.deleteByPrimaryKey(id), "产品型号删除成功！");
    }

    @ApiOperation(value = "供应商查询")
    @RequestMapping("/manufacturerList")
    public ResultForm manufacturerList(@RequestBody @ApiParam(value = "{deviceTypeId:deviceTypeId 设备类型deviceTypeId}") JsonRequestBody jsonRequestBody) {
        Long deviceTypeId = jsonRequestBody.getLong("deviceTypeId");
        if (deviceTypeId == null) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "设备类型deviceTypeId不能为空！");
        }
        return ResultForm.createSuccessResultForm(protocolHandler.selectManufacturer(deviceTypeId), "供应商查询成功！");
    }
}
