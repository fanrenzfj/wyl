package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.DataPaging;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.project.Contact;
import com.stec.masterdata.entity.project.Maintenance;
import com.stec.masterdata.handler.project.ContactHandler;
import com.stec.masterdata.handler.project.MaintenanceHandler;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import com.stec.wyl.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @author liweigao
 * Date: 2018-08-06
 * Time: 9:47
 */
@Api(value = "维保商controller", tags = "维保商管理")
@RestController
@RequestMapping(value = "/rest/maintenance", method = RequestMethod.POST)
public class MaintenanceRestController extends BaseController {
    @Reference
    private MaintenanceHandler maintenanceHandler;
    @Reference
    private ContactHandler contactHandler;


    @ApiOperation(value = "维保商保存和修改")
    @RequestMapping("/save")
    public ResultForm save(@RequestBody @ApiParam(value = "{code:code 维保商编码,\nname:name 名称,\naddress:address 地址,contacts:[name:name 联系人名称,\nphone:phone 联系人电话]}") JsonRequestBody jsonRequestBody) {
        logger("维保商保存和修改", jsonRequestBody);
        Maintenance entity = jsonRequestBody.tryGet(Maintenance.class);
        if (!StringUtils.isAllNotBlank(entity.getName(), entity.getCode())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "维保商名称，维保商编码不能为空！");
        }
        List<Contact> contacts = jsonRequestBody.getList("contacts", Contact.class);
        return ResultForm.createSuccessResultForm(maintenanceHandler.save(entity, contacts), "维保商保存成功！");
    }

    @ApiOperation(value = "维保商列表,分页查询")
    @RequestMapping("/list")
    public ResultForm list(@RequestBody @ApiParam(value = "{name:name,[pageForm properties]}") JsonRequestBody jsonRequestBody) {
        PageForm pageForm = jsonRequestBody.getPageForm();
        Maintenance entity = jsonRequestBody.tryGet(Maintenance.class);
        EntityWrapper<Maintenance> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotEmpty(entity.getName())) {
            wrapper.like("name", entity.getName());
        }
        DataPaging<Maintenance> dataPaging = maintenanceHandler.selectEntities(wrapper, pageForm);
        List<Maintenance> list = dataPaging.getList();
        for (Maintenance maintenance : list) {
            Contact param = new Contact();
            param.setMaintenanceId(maintenance.getId());
            List<Contact> contactsList = contactHandler.selectEntities(param);
            maintenance.setListContact(contactsList);
        }
        return ResultForm.createSuccessResultForm(dataPaging, "维保商列表查询成功！");
    }

    @ApiOperation(value = "删除维保商")
    @RequestMapping("/delete")
    public ResultForm delete(@RequestBody @ApiParam(value = "{id:id 维保商id}") JsonRequestBody jsonRequestBody) {
        Long id = jsonRequestBody.getLong("id");
            if (ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "维保商id不能为空！");
        }
        return ResultForm.createSuccessResultForm(maintenanceHandler.deleteByPrimaryKey(id), "维保商删除成功！");
    }

    @ApiOperation(value = "获取维保商")
    @RequestMapping("/get")
    public ResultForm get(@RequestBody @ApiParam(value = "{id:id 维保商id}") JsonRequestBody jsonRequestBody){
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)){
            return ResultForm.createErrorResultForm(jsonRequestBody,"维保商id不能为空！");
        }
        Maintenance maintenance =maintenanceHandler.selectByPrimaryKey(id);
        if(ObjectUtils.isNotNull(maintenance)){
            Contact param = new Contact();
            param.setMaintenanceId(id);
            List<Contact> list=contactHandler.selectEntities(param);
            maintenance.setListContact(list);
            return ResultForm.createSuccessResultForm(maintenance,"获取维保商成功！");
        }else{
            return ResultForm.createErrorResultForm(jsonRequestBody,"没有维保商！");
        }
    }
}
