package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.SclVal;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.instance.SclSDI;
import com.ysh.jcms.utils.scl.model.template.SclBDA;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.navigate.TypeChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Data value resolver.
 * <p>
 * Based on the {@link Navigator} + {@link TypeChain} building blocks, looks up
 * the DAI value by reference path and traces the bType. Usage:
 *
 * <pre>
 * {
 *     &#64;code
 *     DataValueEntry dv = DataValueResolver.resolve(doc, "E1Q1SB1/C1/LPHD1.Proxy.stVal");
 *     // dv.val() → "false", dv.bType() → "BOOLEAN"
 * }
 * </pre>
 */
public final class DataValueResolver {

    private static final Logger log = LoggerFactory.getLogger(DataValueResolver.class);

    private DataValueResolver() {
    }

    /**
     * Resolves a data value by a full reference.
     */
    public static DataValueEntry resolve(SclDocument doc, String ref) {
        return resolve(doc, ref, null);
    }

    /**
     * Resolves a data value by a full reference with FC filtering.
     */
    public static DataValueEntry resolve(SclDocument doc, String ref, String fc) {
        if (doc == null || ref == null)
            return null;
        Navigator nav = Navigator.go(doc, ref);
        return resolve(nav, fc, ref);
    }

    /** Resolves a data value based on an already created Navigator. */
    public static DataValueEntry resolve(Navigator nav, String fc) {
        if (nav == null || !nav.isValid())
            return null;
        return resolve(nav, fc, nav.ref() != null ? nav.ref().fullReference() : "");
    }

    /** Internal implementation */
    private static DataValueEntry resolve(Navigator nav, String fc, String ref) {
        if (nav == null || !nav.isValid() || nav.ln() == null) {
            log.debug("resolve ref={}: nav invalid or ln=null (valid={})", ref, nav != null ? nav.isValid() : false);
            return null;
        }

        if (nav.ref().isLnLevel())
            return null;

        if (nav.ref().isDaLevel()) {
            String bType = resolveBType(nav);
            if (nav.dai() != null) {
                String daiVal = firstVal(nav.dai());
                if (daiVal != null)
                    return new DataValueEntry(ref, daiVal, bType);
            }
            // Instance DAI has no value → fall back to the template DA/BDA default value
            // (in real SCD files, common values are written in the DOType template)
            String tpl = templateDefault(nav);
            if (tpl != null)
                return new DataValueEntry(ref, tpl, bType);
            log.debug("resolve ref={}: no instance nor template value", ref);
            return null;
        }

        return resolveDoLevel(nav, ref, fc);
    }

    /**
     * DO level value lookup (FC filtering is needed to determine which DA to take)
     */
    private static DataValueEntry resolveDoLevel(Navigator nav, String ref, String fc) {
        SclDOI doi = nav.doi();
        if (doi == null)
            return null;

        boolean filterByFc = fc != null && !fc.isEmpty() && !"XX".equalsIgnoreCase(fc);

        if (!filterByFc) {
            for (SclDAI dai : doi.dais()) {
                String val = firstVal(dai);
                if (val != null) {
                    String bType = resolveDaBType(nav, dai.name());
                    return new DataValueEntry(ref, val, bType);
                }
                // Instance DAI has no value → fall back to the template DA default value
                String tpl = templateDefaultByName(nav, dai.name());
                if (tpl != null) {
                    String bType = resolveDaBType(nav, dai.name());
                    return new DataValueEntry(ref, tpl, bType);
                }
            }
            return null;
        }

        String lnTypeId = nav.ln().lnType();
        if (lnTypeId == null || nav.document().dataTypeTemplates() == null)
            return null;

        TypeChain chain = TypeChain.of(nav.document().dataTypeTemplates());
        TypeChain.DoTypeStep doTypeStep = chain.from(lnTypeId).doDef(nav.ref().doName());
        SclDOType doType = doTypeStep.doType();
        if (doType == null)
            return null;

        for (SclDA da : doType.das()) {
            if (!fc.equalsIgnoreCase(da.fc()))
                continue;

            SclDAI dai = doi.findDaiByName(da.name());
            if (dai != null) {
                String val = firstVal(dai);
                if (val != null) {
                    String bType = resolveDaBType(nav, da.name());
                    return new DataValueEntry(ref, val, bType);
                }
            }
            // Instance DAI has no value → fall back to the template default value of the DA
            // in DOType
            String tpl = firstTemplateVal(da.vals());
            if (tpl != null) {
                String bType = resolveDaBType(nav, da.name());
                return new DataValueEntry(ref, tpl, bType);
            }

            SclSDI sdi = doi.findSdiByName(da.name());
            if (sdi != null) {
                for (SclDAI sdai : sdi.dais()) {
                    String val = firstVal(sdai);
                    if (val != null) {
                        String bType = resolveSdiBdaBType(nav, da.name(), sdai.name());
                        return new DataValueEntry(ref, val, bType);
                    }
                    // The DAI of an instance SDI has no value → fall back to the BDA template
                    // default value in DAType
                    String bdaTpl = templateBdaDefault(nav, da.name(), sdai.name());
                    if (bdaTpl != null) {
                        String bType = resolveSdiBdaBType(nav, da.name(), sdai.name());
                        return new DataValueEntry(ref, bdaTpl, bType);
                    }
                }
            }
        }

        return null;
    }

    private static String firstVal(SclDAI dai) {
        if (dai == null || dai.vals().isEmpty())
            return null;
        String v = dai.vals().get(0).value();
        return (v != null && !v.isEmpty()) ? v : null;
    }

    /** Takes the first valid value from the template DA/BDA default value list. */
    private static String firstTemplateVal(List<SclVal> vals) {
        if (vals == null || vals.isEmpty())
            return null;
        String v = vals.get(0).value();
        return (v != null && !v.isEmpty()) ? v : null;
    }

    /**
     * DA level template default value fallback: follows the TypeChain along the
     * full reference to find the default Val of the DOType's DA (or the DAType's
     * BDA). Supports two paths — DO.DA without SDI and DO.SDI.BDA with a
     * single-level SDI (consistent with the existing type resolution).
     */
    private static String templateDefault(Navigator nav) {
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null)
            return null;
        String doName = nav.ref().doName();
        String daName = nav.ref().daName();
        if (doName == null || daName == null)
            return null;
        TypeChain chain = TypeChain.of(nav.document().dataTypeTemplates());
        if (nav.ref().sdiChain() == null || nav.ref().sdiChain().isEmpty()) {
            SclDA da = chain.from(nav.ln().lnType()).doDef(doName).daDef(daName).da();
            return da != null ? firstTemplateVal(da.vals()) : null;
        }
        TypeChain.DaStep step = chain.from(nav.ln().lnType()).doDef(doName).daDef(nav.ref().sdiChain().get(0));
        SclBDA bda = step.daType().bdaDef(daName);
        return bda != null ? firstTemplateVal(bda.vals()) : null;
    }

    /**
     * DO level (without FC filtering): looks up the template default value by DA
     * name.
     */
    private static String templateDefaultByName(Navigator nav, String daName) {
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null || nav.ref().doName() == null)
            return null;
        TypeChain chain = TypeChain.of(nav.document().dataTypeTemplates());
        SclDA da = chain.from(nav.ln().lnType()).doDef(nav.ref().doName()).daDef(daName).da();
        return da != null ? firstTemplateVal(da.vals()) : null;
    }

    /**
     * Fallback to the BDA template default value when the DAI inside an SDI has no
     * value.
     */
    private static String templateBdaDefault(Navigator nav, String sdiName, String bdaName) {
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null)
            return null;
        TypeChain chain = TypeChain.of(nav.document().dataTypeTemplates());
        TypeChain.DaStep step = chain.from(nav.ln().lnType()).doDef(nav.ref().doName()).daDef(sdiName);
        SclBDA bda = step.daType().bdaDef(bdaName);
        return bda != null ? firstTemplateVal(bda.vals()) : null;
    }

    private static String resolveDaBType(Navigator nav, String daName) {
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null)
            return null;
        String ref = nav.ref().doName() + "." + daName;
        return TypeChain.of(nav.document().dataTypeTemplates()).resolveBType(nav.ln().lnType(), ref);
    }

    private static String resolveSdiBdaBType(Navigator nav, String sdiName, String bdaName) {
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null)
            return null;
        String ref = nav.ref().doName() + "." + sdiName + "." + bdaName;
        return TypeChain.of(nav.document().dataTypeTemplates()).resolveBType(nav.ln().lnType(), ref);
    }

    private static String resolveBType(Navigator nav) {
        if (!nav.ref().isDaLevel())
            return null;
        StringBuilder ref = new StringBuilder(nav.ref().doName());
        for (String sdi : nav.ref().sdiChain())
            ref.append(".").append(sdi);
        ref.append(".").append(nav.ref().daName());
        if (nav.document().dataTypeTemplates() == null || nav.ln().lnType() == null)
            return null;
        return TypeChain.of(nav.document().dataTypeTemplates()).resolveBType(nav.ln().lnType(), ref.toString());
    }
}
