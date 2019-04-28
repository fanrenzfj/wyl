package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.usage.page.PageForm;
import com.stec.masterdata.entity.auth.SysMessageText;
import com.stec.masterdata.handler.auth.MessageHandler;
import com.stec.utils.CollectionUtils;
import com.stec.utils.ObjectUtils;
import com.stec.utils.StringUtils;
import com.stec.utils.TimeUtil;
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
 * @author joe.xie
 * Date: 2018/9/27
 * Time: 11:23
 */
@Api(tags = {"站内信接口"})
@RestController
@RequestMapping(value = "/rest/message", method = RequestMethod.POST)
public class MessageRestController extends BaseController {

    @Reference
    private MessageHandler messageHandler;

    @ApiOperation("未读消息数量")
    @RequestMapping("/unreadNum")
    public ResultForm unreadNum() {
        return ResultForm.createSuccessResultForm(messageHandler.unreadNum(this.currentSysUserId()), "获取未读站内消息数量成功！");
    }

    @ApiOperation("站内信分页列表查询")
    @RequestMapping("/list")
    public ResultForm list(@ApiParam(value = "{sight:true/false,title:search key, pageForm}") @RequestBody JsonRequestBody jsonRequestBody){
        SysMessageText entity = jsonRequestBody.tryGet(SysMessageText.class);
        PageForm pageForm = jsonRequestBody.getPageForm();
        return ResultForm.createSuccessResultForm(messageHandler.messageList(entity, currentSysUserId(), pageForm), "查询成功！");
    }

    @ApiOperation("阅读消息")
    @RequestMapping("/read")
    public ResultForm read(@ApiParam(value = "{id:id}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("阅读消息", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "站内消息ID不能为空");
        }
        return ResultForm.createSuccessResultForm(messageHandler.readMessage(id, currentSysUserId()), "获取成功！");
    }

    @ApiOperation("逻辑删除站内消息")
    @RequestMapping("/delete")
    public ResultForm delete(@ApiParam(value = "{id:id}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("逻辑删除站内消息", jsonRequestBody);
        Long id = jsonRequestBody.getLong("id");
        if(ObjectUtils.isNull(id)) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "站内消息ID不能为空");
        }
        messageHandler.deleteMessage(id, currentSysUserId());
        return ResultForm.createSuccessResultForm(null, "删除成功！");
    }

    @ApiOperation("发送系统消息，无接收用户列表标识对全体有效用户")
    @RequestMapping("/sendSysMessage")
    public ResultForm sendSysMessage(@ApiParam(value = "{title*,text*,system:true/false,receiveUserIds:Long[...]}") @RequestBody JsonRequestBody jsonRequestBody) {
        logger("发送系统消息，无接收用户列表标识对全体有效用户", jsonRequestBody);
        SysMessageText entity = jsonRequestBody.tryGet(SysMessageText.class);
        if(!StringUtils.isAllNotBlank(entity.getTitle(), entity.getText())) {
            return ResultForm.createErrorResultForm(jsonRequestBody, "消息标题及消息体不能为空！");
        }
        Boolean system = jsonRequestBody.getBoolean("system");
        if(ObjectUtils.isNotNull(system) && !system) {
            entity.setSendUserId(currentSysUserId());
        }
        List<Long> receiveUserIds = jsonRequestBody.getList("receiveUserIds", Long.class);
        entity.setTime(TimeUtil.now());
        if(CollectionUtils.isNotEmpty(receiveUserIds)) {
            messageHandler.sendMessage(entity, receiveUserIds);
        }
        else {
            messageHandler.sendSystemMessage(entity);
        }
        return ResultForm.createSuccessResultForm(null, "消息发送成功！");
    }

}
