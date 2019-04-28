package com.stec.masterdata.handler.wyl.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.stec.framework.handler.service.AdvMySqlHandlerService;
import com.stec.framework.metadata.exceptions.DataServiceException;
import com.stec.masterdata.entity.auth.SysUser;
import com.stec.masterdata.entity.basic.DocAttach;
import com.stec.masterdata.entity.project.Device;
import com.stec.masterdata.entity.project.Structure;
import com.stec.masterdata.entity.wyl.Defect;
import com.stec.masterdata.handler.auth.SysUserHandler;
import com.stec.masterdata.handler.basic.DocAttachHandler;
import com.stec.masterdata.handler.project.DeviceHandler;
import com.stec.masterdata.handler.project.StructureHandler;
import com.stec.masterdata.handler.wyl.DefectHandler;
import com.stec.masterdata.service.wyl.DefectService;
import com.stec.utils.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author li.peng
 * Date: 2018/8/28
 * Time: 14:12
 */
@Service
@Component
public class DefectHandlerImpl extends AdvMySqlHandlerService<DefectService, Defect, Long> implements DefectHandler {

    @Reference
    private SysUserHandler sysUserHandler;
    @Reference
    private StructureHandler structureHandler;
    @Reference
    private DeviceHandler deviceHandler;
    @Reference
    private DocAttachHandler docAttachHandler;

    @Override
    public boolean matchStatus(Long defectId, String status) throws DataServiceException {
        return this.privateService.matchStatus(defectId, status);
    }

    @Override
    public void createWorkOrder(Long defectId) throws DataServiceException {
        this.privateService.createWorkOrder(defectId);
    }

    @Override
    public void removeDeviceId(Long defectId) throws DataServiceException {
        this.privateService.removeDeviceId(defectId);
    }

    //封装路劲字段Defect实体
    @Override
    public List<Defect> getFilePath(List<Defect> list) {
        for (Defect defect : list) {
            makeDefect(defect);
            DocAttach attach = new DocAttach();
            attach.setDocInfoId(defect.getDocId());
            List<DocAttach> docAttaches = docAttachHandler.selectEntities(attach);
            String remark = "";
            for (DocAttach docAttach : docAttaches) {
                remark = " http://183.129.193.154:6280/stec-platform-doc/img/" + remark + docAttach.getFileName() + "；";
            }
            defect.setRemark(remark);
        }
        return list;
    }

    private Defect makeDefect(Defect defect) {
        if (ObjectUtils.isNotNull(defect)) {
            SysUser sysUser = sysUserHandler.selectByPrimaryKey(defect.getDiscoveryUserId());
            defect.setDiscoveryUserName(sysUser.getName());
            Structure structure = structureHandler.selectByPrimaryKey(defect.getStructureId());
            if (ObjectUtils.isNull(structure)) {
                defect.setStructureName("");
            } else {
                defect.setStructureName(structure.getName());
            }
            Device device = deviceHandler.selectByPrimaryKey(defect.getDeviceId());
            if (ObjectUtils.isNull(device)) {
                defect.setDeviceName("");
            } else {
                defect.setDeviceName(device.getName());
            }
        }
        return defect;
    }
}
