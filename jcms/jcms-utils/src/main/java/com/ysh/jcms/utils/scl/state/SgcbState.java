package com.ysh.jcms.utils.scl.state;

import com.ysh.jcms.data.choice.CmsData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * SGCB 运行时状态。
 */
public class SgcbState {

    private int numOfSG;
    private int actSG;
    private int editSG;
    private boolean cnfEdit;
    private long actTm;
    private int resvTms;
    private final Map<Integer, Map<String, String>> sgValues = new ConcurrentHashMap<>();
    private final Map<String, CmsData> editValues = new ConcurrentHashMap<>();

    public SgcbState() {
        this.numOfSG = 4;
        this.actSG = 1;
        this.editSG = 1;
        this.cnfEdit = true;
        this.actTm = System.currentTimeMillis() / 1000;
        this.resvTms = 0;
    }

    public SgcbState(int numOfSG) {
        this.numOfSG = numOfSG;
        this.actSG = 1;
        this.editSG = 1;
        this.cnfEdit = true;
        this.actTm = System.currentTimeMillis() / 1000;
        this.resvTms = 0;
    }

    public int getNumOfSG() {
        return numOfSG;
    }
    public void setNumOfSG(int numOfSG) {
        this.numOfSG = numOfSG;
    }

    public int getActSG() {
        return actSG;
    }
    public void setActSG(int actSG) {
        this.actSG = actSG;
    }

    public int getEditSG() {
        return editSG;
    }
    public void setEditSG(int editSG) {
        this.editSG = editSG;
    }

    public boolean isCnfEdit() {
        return cnfEdit;
    }
    public void setCnfEdit(boolean cnfEdit) {
        this.cnfEdit = cnfEdit;
    }

    public long getActTm() {
        return actTm;
    }
    public void setActTm(long actTm) {
        this.actTm = actTm;
    }

    public int getResvTms() {
        return resvTms;
    }
    public void setResvTms(int resvTms) {
        this.resvTms = resvTms;
    }

    public Map<String, String> getSgValues(int sgNum) {
        return sgValues.computeIfAbsent(sgNum, k -> new ConcurrentSkipListMap<>());
    }

    public void setSgValue(int sgNum, String daName, String value) {
        getSgValues(sgNum).put(daName, value);
    }

    public String getSgValue(int sgNum, String daName) {
        Map<String, String> values = sgValues.get(sgNum);
        return values != null ? values.get(daName) : null;
    }

    public Map<String, CmsData> getEditValues() {
        return editValues;
    }
}
