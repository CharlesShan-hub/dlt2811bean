package com.ysh.jcms.utils.scl.convert;

import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.instance.SclSDI;
import com.ysh.jcms.utils.scl.model.SclVal;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.navigate.TypeChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据值写入器（SetDataValues 服务）。
 * <p>
 * 按引用路径查找 DAI（必要时虚拟创建），验证并设值。
 */
public final class DataWriterResolver {
    private static final Logger log = LoggerFactory.getLogger(DataWriterResolver.class);

    private DataWriterResolver() {
    }

    /**
     * 设置数据值。如果 DAI/DOI/SDI 不存在则虚拟创建。
     *
     * @param nav
     *            已导航到目标点的 Navigator（需包含 LN）
     * @param value
     *            字符串值
     * @return CmsServiceError.NO_ERROR，或错误码
     */
    public static int setValue(Navigator nav, String value) {
        if (!nav.isValid() || nav.ln() == null)
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        if (nav.ref().isLnLevel())
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;

        // 确保 DOI 存在
        SclDOI doi = nav.doi();
        if (doi == null) {
            doi = new SclDOI().name(nav.ref().doName());
            nav.ln().addDoi(doi);
        }

        if (nav.ref().isDoLevel()) {
            // DO 级别：找第一个 DA
            String firstDaName = findFirstDaName(nav);
            if (firstDaName == null)
                firstDaName = "stVal";
            SclDAI dai = doi.findDaiByName(firstDaName);
            if (dai == null) {
                dai = new SclDAI().name(firstDaName);
                doi.addDai(dai);
            }
            dai.vals().clear();
            dai.addVal(new SclVal().value(value));
            return CmsServiceError.NO_ERROR;
        }

        // DA / SDI.BDA 级别：确保 SDI 链存在
        SclSDI currentSdi = null;
        for (String sdiName : nav.ref().sdiChain()) {
            SclSDI next = (currentSdi == null) ? doi.findSdiByName(sdiName) : currentSdi.findSdiByName(sdiName);
            if (next == null) {
                next = new SclSDI().name(sdiName);
                if (currentSdi == null)
                    doi.addSdi(next);
                else
                    currentSdi.addSdi(next);
            }
            currentSdi = next;
        }

        // 找或创建 DAI
        SclDAI dai = (currentSdi != null) ? currentSdi.findDaiByName(nav.ref().daName()) : doi.findDaiByName(nav.ref().daName());
        if (dai == null) {
            dai = new SclDAI().name(nav.ref().daName());
            if (currentSdi != null)
                currentSdi.addDai(dai);
            else
                doi.addDai(dai);
        }

        // 验证并设值
        String bType = resolveBType(nav);
        dai.vals().clear();
        if (bType != null) {
            String validated = validateAndConvert(value, bType);
            if (validated == null) {
                return CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT;
            }
            dai.addVal(new SclVal().value(validated));
        } else {
            dai.addVal(new SclVal().value(value));
        }
        return CmsServiceError.NO_ERROR;
    }

    private static String findFirstDaName(Navigator nav) {
        if (nav.document().dataTypeTemplates() == null)
            return null;
        TypeChain.DoTypeStep step = TypeChain.of(nav.document().dataTypeTemplates()).from(nav.ln().lnType()).doDef(nav.ref().doName());
        if (step.doType() == null || step.doType().das().isEmpty())
            return null;
        return step.doType().das().get(0).name();
    }

    private static String resolveBType(Navigator nav) {
        if (nav.document().dataTypeTemplates() == null)
            return null;
        StringBuilder ref = new StringBuilder(nav.ref().doName());
        for (String sdi : nav.ref().sdiChain())
            ref.append(".").append(sdi);
        ref.append(".").append(nav.ref().daName());
        return TypeChain.of(nav.document().dataTypeTemplates()).resolveBType(nav.ln().lnType(), ref.toString());
    }

    /** 按 bType 验证并规范化值。无效返回 null。 */
    static String validateAndConvert(String value, String bType) {
        if (value == null)
            return null;
        switch (bType.toUpperCase()) {
            case "BOOLEAN" :
                if ("true".equalsIgnoreCase(value) || "1".equals(value))
                    return "true";
                if ("false".equalsIgnoreCase(value) || "0".equals(value))
                    return "false";
                return null;
            case "INT8" :
                try {
                    Byte.parseByte(value);
                    return value;
                } catch (NumberFormatException e) {
                    return null;
                }
            case "INT16" :
                try {
                    Short.parseShort(value);
                    return value;
                } catch (NumberFormatException e) {
                    return null;
                }
            case "INT32" :
            case "ENUM" :
            case "ENUMERATED" :
            case "CODED_ENUM" :
                try {
                    Integer.parseInt(value);
                    return value;
                } catch (NumberFormatException e) {
                    return null;
                }
            case "INT64" :
                try {
                    Long.parseLong(value);
                    return value;
                } catch (NumberFormatException e) {
                    return null;
                }
            case "INT8U" :
                try {
                    int v = Short.parseShort(value);
                    if (v >= 0 && v <= 255)
                        return Integer.toString(v);
                    return null;
                } catch (NumberFormatException e) {
                    return null;
                }
            case "INT16U" :
                try {
                    int v = Integer.parseInt(value);
                    if (v >= 0 && v <= 65535)
                        return Integer.toString(v);
                    return null;
                } catch (NumberFormatException e) {
                    return null;
                }
            case "INT32U" :
                try {
                    long v = Long.parseLong(value);
                    if (v >= 0 && v <= 0xFFFFFFFFL)
                        return Long.toString(v);
                    return null;
                } catch (NumberFormatException e) {
                    return null;
                }
            case "FLOAT32" :
                try {
                    Float.parseFloat(value);
                    return value;
                } catch (NumberFormatException e) {
                    return null;
                }
            case "FLOAT64" :
                try {
                    Double.parseDouble(value);
                    return value;
                } catch (NumberFormatException e) {
                    return null;
                }
            default :
                return value;
        }
    }
}
