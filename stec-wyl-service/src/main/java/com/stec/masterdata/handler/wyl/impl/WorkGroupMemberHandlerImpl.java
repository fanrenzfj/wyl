package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.masterdata.entity.wyl.WorkGroupMember;
import com.stec.masterdata.handler.wyl.WorkGroupMemberHandler;
import com.stec.masterdata.service.wyl.WorkGroupMemberService;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/21
 * Time: 10:40
 */
@Service
@Component
public class WorkGroupMemberHandlerImpl extends AdvMySqlHandlerService<WorkGroupMemberService, WorkGroupMember, Long> implements WorkGroupMemberHandler {
}
