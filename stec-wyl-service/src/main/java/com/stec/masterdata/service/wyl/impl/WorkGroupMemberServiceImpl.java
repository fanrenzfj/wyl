package com.stec.masterdata.service.wyl.impl;

import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.WorkGroupMember;
import com.stec.masterdata.service.wyl.WorkGroupMemberService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/21
 * Time: 10:41
 */
@Service
public class WorkGroupMemberServiceImpl extends AdvSqlDaoImpl<WorkGroupMember, String> implements WorkGroupMemberService {
}
