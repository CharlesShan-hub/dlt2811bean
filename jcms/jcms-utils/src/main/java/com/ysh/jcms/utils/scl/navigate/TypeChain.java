package com.ysh.jcms.utils.scl.navigate;

import com.ysh.jcms.utils.scl.model.template.*;

/**
 * Type chain tracer.
 * <p>
 * Starting from the LN's lnType, traces along the {@code LNodeType → DO → DOType → DA → bType} chain,
 * supporting nested SDI/BDA tracing.
 * <p>
 * Usage:
 *
 * <pre>
 * {
 *     &#64;code
 *     String bType = TypeChain.of(templates).from(lnTypeId).doDef("Mod").daDef("stVal").bType();
 *     // → "Enum"
 * }
 * </pre>
 */
public class TypeChain {

    private final SclDataTypeTemplates templates;

    private TypeChain(SclDataTypeTemplates templates) {
        this.templates = templates;
    }

    public static TypeChain of(SclDataTypeTemplates templates) {
        if (templates == null)
            throw new IllegalArgumentException("templates must not be null");
        return new TypeChain(templates);
    }

    // ==================== Step Builder ====================

    /** First step: start from the LNodeType. Returns the DO selector. */
    public DoStep from(String lnTypeId) {
        SclLNodeType lnt = templates.findLNodeTypeById(lnTypeId);
        return new DoStep(lnt);
    }

    // ==================== Step classes ====================

    /** DO selection step */
    public class DoStep {
        private final SclLNodeType lnt;
        DoStep(SclLNodeType lnt) {
            this.lnt = lnt;
        }

        /** Finds a DO definition by name */
        public DoTypeStep doDef(String doName) {
            if (lnt == null)
                return new DoTypeStep(null);
            SclDO doDef = lnt.findDoByName(doName);
            if (doDef == null || doDef.type() == null)
                return new DoTypeStep(null);
            SclDOType doType = templates.findDoTypeById(doDef.type());
            return new DoTypeStep(doType);
        }
    }

    /** DOType selection step */
    public class DoTypeStep {
        private final SclDOType doType;
        DoTypeStep(SclDOType doType) {
            this.doType = doType;
        }

        public SclDOType doType() {
            return doType;
        }
        public String cdc() {
            return doType != null ? doType.cdc() : null;
        }

        /** Finds a DA definition by name */
        public DaStep daDef(String daName) {
            if (doType == null)
                return new DaStep(null);
            SclDA da = doType.findDaByName(daName);
            return new DaStep(da);
        }
    }

    /** DA selection step */
    public class DaStep {
        private final SclDA da;
        DaStep(SclDA da) {
            this.da = da;
        }

        public SclDA da() {
            return da;
        }
        public String bType() {
            return da != null ? da.bType() : null;
        }
        public String fc() {
            return da != null ? da.fc() : null;
        }

        /** If the bType is Struct, continue tracing the DAType */
        public BdaStep daType() {
            if (da != null && "Struct".equals(da.bType()) && da.type() != null) {
                SclDAType dat = templates.findDaTypeById(da.type());
                return new BdaStep(dat);
            }
            return new BdaStep(null);
        }
    }

    /** BDA selection step (inside DAType) */
    public class BdaStep {
        private final SclDAType dat;
        BdaStep(SclDAType dat) {
            this.dat = dat;
        }

        public SclDAType dat() {
            return dat;
        }

        /** Finds a BDA by name */
        public SclBDA bdaDef(String bdaName) {
            if (dat == null)
                return null;
            return dat.findBdaByName(bdaName);
        }

        /** bType of the first BDA (for single-field structures) */
        public String firstBdaBType() {
            if (dat == null || dat.bdas().isEmpty())
                return null;
            return dat.bdas().get(0).bType();
        }
    }

    // ==================== Quick methods ====================

    /**
     * Quick resolution: starting from lnType, resolves a reference to bType in one step.
     * <p>
     * Reference formats: {@code DO.DA}, {@code DO.SDI.BDA} or {@code DO.SDO[.SDO...].DA}
     *
     * @param lnTypeId
     *            the id of the LNodeType
     * @param ref
     *            a DO-level reference (e.g. "Mod.stVal" or "PPV.phsAB.cVal")
     * @return the bType string, or null when unresolvable
     */
    public String resolveBType(String lnTypeId, String ref) {
        if (ref == null)
            return null;
        String[] parts = ref.split("\\.");
        if (parts.length < 1)
            return null;

        SclLNodeType lnt = templates.findLNodeTypeById(lnTypeId);
        if (lnt == null)
            return null;

        // DO
        SclDO doDef = lnt.findDoByName(parts[0]);
        if (doDef == null || doDef.type() == null)
            return null;
        SclDOType doType = templates.findDoTypeById(doDef.type());
        if (doType == null)
            return null;

        // Only DO part
        if (parts.length == 1) {
            return doType.cdc();
        }

        // DO.DA (2 parts)
        if (parts.length == 2) {
            SclDA da = doType.findDaByName(parts[1]);
            return da != null ? da.bType() : null;
        }

        // 3+ parts: DO.SDO[.SDO...].DA or DO.SDI.BDA
        // Walk intermediate parts: try SDO first, then DA-with-Struct (SDI)
        SclDOType currentDoType = doType;
        for (int i = 1; i < parts.length - 1; i++) {
            String name = parts[i];
            // Try SDO path first
            SclSDO sdo = currentDoType.findSdoByName(name);
            if (sdo != null && sdo.type() != null) {
                SclDOType sdoType = templates.findDoTypeById(sdo.type());
                if (sdoType == null)
                    return null;
                currentDoType = sdoType;
                continue;
            }
            // Fall back to SDI path (DA with Struct bType → DAType)
            SclDA sdiDa = currentDoType.findDaByName(name);
            if (sdiDa == null || !"Struct".equals(sdiDa.bType()) || sdiDa.type() == null)
                return null;
            // For SDI path, subsequent parts are BDA within the DAType
            // Only the last part is the BDA name
            SclDAType dat = templates.findDaTypeById(sdiDa.type());
            if (dat == null)
                return null;
            String bdaName = parts[parts.length - 1];
            SclBDA bda = dat.findBdaByName(bdaName);
            return bda != null ? bda.bType() : null;
        }

        // Last part is the DA name in the final DOType (SDO path)
        SclDA da = currentDoType.findDaByName(parts[parts.length - 1]);
        return da != null ? da.bType() : null;
    }
}
