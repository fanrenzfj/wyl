package com.stec.masterdata.service.wyl.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stec.framework.metadata.exceptions.ServiceException;
import com.stec.framework.service.mybatis.AdvSqlDaoImpl;
import com.stec.masterdata.entity.wyl.RoadEvaluateDetail;
import com.stec.masterdata.entity.wyl.RoadEvaluateItem;
import com.stec.masterdata.entity.wyl.RoadEvaluateReport;
import com.stec.masterdata.entity.wyl.RoadEvaluateTemplate;
import com.stec.masterdata.service.wyl.*;
import com.stec.utils.MapUtils;
import com.stec.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:37
 */
@Service
public class RoadEvaluateReportServiceImpl extends AdvSqlDaoImpl<RoadEvaluateReport, String> implements RoadEvaluateReportService {

    @Autowired
    private RoadEvaluateTemplateService roadEvaluateTemplateService;

    @Autowired
    private RoadEvaluateItemService roadEvaluateItemService;

    @Autowired
    private RoadEvaluateDetailService roadEvaluateDetailService;

    @Autowired
    private AnchorPointService anchorPointService;

    @Override
    public List<Map<String, Object>> evaluateDetails(Long id) throws ServiceException {
        RoadEvaluateItem param = new RoadEvaluateItem();
        param.setLeaf(true);
        param.setReportId(id);
        List<RoadEvaluateItem> leafs = roadEvaluateItemService.selectEntities(param);
        List<Map<String, Object>> restList = new ArrayList<>(leafs.size());
        for (RoadEvaluateItem item : leafs) {
            Map<String, Object> retMap = new HashMap<>(2);
            retMap.put("item", item);
            RoadEvaluateDetail detailParam = new RoadEvaluateDetail();
            detailParam.setItemId(item.getId());
            List<RoadEvaluateDetail> details = roadEvaluateDetailService.selectEntities(detailParam);
            List<Map<String, Object>> detailList = new ArrayList<>(details.size());
            for (RoadEvaluateDetail detail : details) {
                Map<String, Object> detailMap = new HashMap<>(2);
                detailMap.put("detail", detail);
                detailMap.put("points", anchorPointService.queryPoints(item.getCode(), detail.getStart(), detail.getEnd()));
                detailList.add(detailMap);
            }
            retMap.put("details", detailList);
            restList.add(retMap);
        }
        return restList;
    }

    @Override
    public List<Map<String, Object>> indexState(Long id, String index) throws ServiceException {
        Map<String, Object> param = MapUtils.newHashMap("reportId", id);
        param.put("index", index);
        return this.selectMapperList("com.stec.masterdata.mapper.wyl.RoadEvaluateMapper.indexState", param);
    }

    @Override
    public void sdEvaluate(RoadEvaluateReport report, JSONObject evaluateJson) throws ServiceException {

        List<RoadEvaluateDetail> saveDetails = new ArrayList<>();
        JSONObject message = evaluateJson.getJSONObject("Message");
        String levelCode = message.getString("code");
        JSONObject levelResult = message.getJSONObject("result");
        String[] levels = new String[8];
        levelResult.getJSONArray("level").toArray(levels);
        BigDecimal[] values = new BigDecimal[8];
        levelResult.getJSONArray("value").toArray(values);
        RoadEvaluateItem root = saveItem(null, report, levelCode, levels, values);

        JSONArray lines = message.getJSONArray("lines");

        for (int i = 0; i < lines.size(); i++) {
            JSONObject line = lines.getJSONObject(i);
            String lineCode = line.getString("code");
            JSONObject lineResult = line.getJSONObject("result");
            String[] lineLevels = new String[8];
            BigDecimal[] lineValues = new BigDecimal[8];

            lineResult.getJSONArray("level").toArray(lineLevels);
            lineResult.getJSONArray("value").toArray(lineValues);
            RoadEvaluateItem lineItem = saveItem(root, report, lineCode, lineLevels, lineValues);

            JSONArray lanes = line.getJSONArray("lanes");

            for (int j = 0; j < lanes.size(); j++) {
                JSONObject lane = lanes.getJSONObject(j);
                String laneCode = lane.getString("code");
                JSONObject laneResult = lane.getJSONObject("result");
                String[] laneLevels = new String[8];
                BigDecimal[] laneValues = new BigDecimal[8];

                laneResult.getJSONArray("level").toArray(laneLevels);
                laneResult.getJSONArray("value").toArray(laneValues);
                RoadEvaluateItem laneItem = saveItem(lineItem, report, laneCode, laneLevels, laneValues);

                JSONArray sections = lane.getJSONArray("sections");
                for (int k = 0; k < sections.size(); k++) {
                    JSONObject section = sections.getJSONObject(k);
                    Double start = section.getDouble("start");
                    Double end = section.getDouble("end");


                    JSONObject sectionResult = section.getJSONObject("result");
                    String[] sectionLevels = new String[8];
                    BigDecimal[] sectionValues = new BigDecimal[8];

                    sectionResult.getJSONArray("level").toArray(sectionLevels);
                    sectionResult.getJSONArray("value").toArray(sectionValues);
                    saveDetails.add(makeDetail(laneItem, start, end, sectionLevels, sectionValues));
                }
            }
            roadEvaluateDetailService.save(saveDetails);
        }

        report.setDownload(true);
        save(report);

    }

    private RoadEvaluateDetail makeDetail(RoadEvaluateItem item, Double start, Double end, String[] levels, BigDecimal[] values){
        RoadEvaluateDetail detail = new RoadEvaluateDetail();
        detail.setItemId(item.getId());
        detail.setStart(start);
        detail.setEnd(end);
        detail = roadEvaluateDetailService.selectEntity(detail);
        if(ObjectUtils.isNull(detail)) {
            detail = new RoadEvaluateDetail();
            detail.setReportId(item.getReportId());
            detail.setItemId(item.getId());
            detail.setStart(start);
            detail.setEnd(end);
            detail.setStartDate(item.getStartDate());
            detail.setEndDate(item.getEndDate());
        }
        detail.setCode(item.getCode());
        detail.setRqi(decimalValue(values[0]));
        detail.setRqiLevel(levels[0]);

        detail.setRdi(decimalValue(values[1]));
        detail.setRdiLevel(levels[1]);

        detail.setPci(decimalValue(values[2]));
        detail.setPciLevel(levels[2]);

        detail.setSri(decimalValue(values[3]));
        detail.setSriLevel(levels[3]);

        detail.setPssi(decimalValue(values[4]));
        detail.setPssiLevel(levels[4]);

        detail.setPqi(decimalValue(values[5]));
        detail.setPqiLevel(levels[5]);

        detail.setSci(decimalValue(values[6]));
        detail.setSciLevel(levels[6]);

        detail.setMqi(decimalValue(values[7]));
        detail.setMqiLevel(levels[7]);
        return detail;

    }

    private RoadEvaluateItem saveItem(RoadEvaluateItem parent, RoadEvaluateReport report, String code, String[] levels, BigDecimal[] values) {
        RoadEvaluateItem item = new RoadEvaluateItem();
        item.setReportId(report.getId());
        item.setCode(code);
        item = roadEvaluateItemService.selectEntity(item);
        if(ObjectUtils.isNull(item)) {
            item = new RoadEvaluateItem();
            RoadEvaluateTemplate template = roadEvaluateTemplateService.selectEntity(MapUtils.newHashMap("code", code));
            item.setTemplateId(template.getId());
            item.setCode(template.getCode());
            item.setName(template.getName());
            item.setReportId(report.getId());
            item.setStartDate(report.getStartDate());
            item.setEndDate(report.getEndDate());
            if(parent != null) {
                item.setParentId(parent.getId());
            }
        }
        item.setRqi(decimalValue(values[0]));
        item.setRqiLevel(levels[0]);

        item.setRdi(decimalValue(values[1]));
        item.setRdiLevel(levels[1]);

        item.setPci(decimalValue(values[2]));
        item.setPciLevel(levels[2]);

        item.setSri(decimalValue(values[3]));
        item.setSriLevel(levels[3]);

        item.setPssi(decimalValue(values[4]));
        item.setPssiLevel(levels[4]);

        item.setPqi(decimalValue(values[5]));
        item.setPqiLevel(levels[5]);

        item.setSci(decimalValue(values[6]));
        item.setSciLevel(levels[6]);

        item.setMqi(decimalValue(values[7]));
        item.setMqiLevel(levels[7]);
        return roadEvaluateItemService.save(item);
    }

    private Double decimalValue(BigDecimal value) {
        if(ObjectUtils.isNotNull(value)){
            return value.doubleValue();
        }
        return null;
    }
}
