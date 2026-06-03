package com.ysh.dlt2811bean.service.svc.directory.datatypes;

import com.ysh.dlt2811bean.datatypes.compound.CmsBRCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsGoCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsLCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsMSVCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsSGCB;
import com.ysh.dlt2811bean.datatypes.compound.CmsURCB;
import com.ysh.dlt2811bean.datatypes.type.AbstractCmsChoice;

public class CmsCBValue extends AbstractCmsChoice<CmsCBValue> {

    public CmsBRCB brcb = new CmsBRCB();
    public CmsURCB urcb = new CmsURCB();
    public CmsLCB lcb = new CmsLCB();
    public CmsSGCB sgb = new CmsSGCB();
    public CmsGoCB gocb = new CmsGoCB();
    public CmsMSVCB msvcb = new CmsMSVCB();

    public CmsCBValue() {
        super("CBValue", 0);
        registerAlternative("brcb");
        registerAlternative("urcb");
        registerAlternative("lcb");
        registerAlternative("sgb");
        registerAlternative("gocb");
        registerAlternative("msvcb");
    }

    public CmsCBValue selectBrcb() { select(0); return this; }
    public CmsCBValue selectUrcb() { select(1); return this; }
    public CmsCBValue selectLcb()  { select(2); return this; }
    public CmsCBValue selectSgb()  { select(3); return this; }
    public CmsCBValue selectGocb() { select(4); return this; }
    public CmsCBValue selectMsvcb(){ select(5); return this; }

    @Override
    public CmsCBValue copy() {
        CmsCBValue clone = new CmsCBValue();
        clone.selectedIndex = this.selectedIndex;
        clone.brcb = this.brcb.copy();
        clone.urcb = this.urcb.copy();
        clone.lcb = this.lcb.copy();
        clone.sgb = this.sgb.copy();
        clone.gocb = this.gocb.copy();
        clone.msvcb = this.msvcb.copy();
        return clone;
    }
}