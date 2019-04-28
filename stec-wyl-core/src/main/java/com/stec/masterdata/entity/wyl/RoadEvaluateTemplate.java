package com.stec.masterdata.entity.wyl;

import com.stec.masterdata.entity.common.MasterTreeEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/12/4 0004
 * Time: 20:24
 */
@Entity
@Table(name = "road_evaluate_template")
public class RoadEvaluateTemplate extends MasterTreeEntity<String> {
    private static final long serialVersionUID = 4769084129063559473L;
}