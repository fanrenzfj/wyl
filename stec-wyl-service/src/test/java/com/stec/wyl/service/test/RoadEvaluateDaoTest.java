package com.stec.wyl.service.test;

import com.stec.masterdata.entity.wyl.EvaluateReportItem;
import com.stec.masterdata.handler.wyl.RoadEvaluateReportHandler;
import com.stec.masterdata.service.wyl.EvaluateReportItemService;
import com.stec.masterdata.service.wyl.RoadEvaluateReportService;
import com.stec.wyl.ServiceApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/6 0006
 * Time: 15:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceApp.class)
public class RoadEvaluateDaoTest {

    @Autowired
    private RoadEvaluateReportService roadEvaluateReportService;
    @Autowired
    private EvaluateReportItemService evaluateReportItemService;

    @Test
    public void testState() {
        List<Map<String, Object>> retList = roadEvaluateReportService.indexState(3L, "pci");
        System.out.println("retList = " + retList);
    }

    @Test
    public void getSomeThing() {

        EvaluateReportItem item=new EvaluateReportItem();
        item.setId(526l);
        item.setEvaluateReportId(1l);
        EvaluateReportItem dbitem=evaluateReportItemService.selectEntity(item);
        List<EvaluateReportItem> list=findParent(dbitem,new ArrayList<>());
        for (EvaluateReportItem item1:list){
            System.out.println("+++++"+item1.getTreeId());
        }
    }
    public List<EvaluateReportItem> findParent(EvaluateReportItem evaluateReportItem,List<EvaluateReportItem> items){
        if (evaluateReportItem.getTreeId().length()<6){
            return items;
        }else{
            items.add(evaluateReportItem);
            EvaluateReportItem item=new EvaluateReportItem();
            item.setId(evaluateReportItem.getParentId());
            EvaluateReportItem dbitem=evaluateReportItemService.selectEntity(item);
            findParent(dbitem,items);
        }
        return items;
    }
    @Test
    public void queryParent(){
        List<EvaluateReportItem> list=evaluateReportItemService.selectParentList(526l);
        for (EvaluateReportItem item1:list){
            System.out.println("+++++"+item1.getTreeId());
        }
    }
}
