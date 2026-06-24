package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.data.block.CmsSgcb;
import com.ysh.jcms.svc.directory.CmsCbValueChoice;
import com.ysh.jcms.utils.config.CmsConfig;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.ied.SclLNBase;

/**
 * SGCB 构建器
 */
public class SgcbBuilder {

    private final CmsSgcb sgcb = new CmsSgcb();

    public SgcbBuilder numOfSG(int numOfSG) {
        sgcb.numOfSG(numOfSG);
        return this;
    }

    public SgcbBuilder actSG(int actSG) {
        sgcb.actSG(actSG);
        return this;
    }

    public SgcbBuilder editSG(int editSG) {
        sgcb.editSG(editSG);
        return this;
    }

    public CmsCbValueChoice build() {
        CmsCbValueChoice result = new CmsCbValueChoice();
        result.choice(CmsCbValueChoice.SGECB);
        result.altSgecb = sgcb;
        return result;
    }

    public static CmsCbValueChoice defaultSgcb() {
        CmsConfig.Setting setting = CmsConfigLoader.load().getSetting();
        return new SgcbBuilder()
                .numOfSG(setting.getNumOfSG())
                .actSG(1)
                .editSG(1)
                .build();
    }

    /**
     * 构建 SGCB entry reference，如果 SGCB 未启用则返回 null。
     */
    public static String buildEntryRef(SclLNBase ln) {
        CmsConfig.Setting setting = CmsConfigLoader.load().getSetting();
        if (!setting.isSgDefaultEnabled()) return null;
        return ln.getFullName() + "." + setting.getSgDefaultName();
    }
}
