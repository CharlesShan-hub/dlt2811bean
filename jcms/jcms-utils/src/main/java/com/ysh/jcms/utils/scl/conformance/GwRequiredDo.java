package com.ysh.jcms.utils.scl.conformance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Mandatory (M) data objects per logical node class, from Q/GDW 1396-2012
 * Appendix A (220 kV and above) and Appendix B (110 kV and below).
 * <p>
 * Every LN class in the appendices carries the common MDO set
 * {@code Mod/Beh/Health/NamPlt}; protection/measurement/control classes add
 * their own mandatory DOs (e.g. {@code Str}/{@code Op} for protections,
 * {@code Amp} for TCTR, {@code Rel} for RSYN). The table merges classes that
 * appear in both appendices (their M sets are identical).
 * <p>
 * Per §7.1.5 the LNodeType of an instance must contain at least these DOs, so
 * the checker reports an ERROR when a required DO is missing from the
 * referenced LNodeType.
 */
public enum GwRequiredDo {

    // 公用逻辑节点信息 (common MDO set)
    LLN0("Mod", "Beh", "Health", "NamPlt"), SCAS("Mod", "Beh", "Health", "NamPlt"), YPTR("Mod", "Beh", "Health", "NamPlt"),

    // 保护逻辑节点（附录 A）
    PSCH("Mod", "Beh", "Health", "NamPlt", "ProTx", "ProRx", "Str", "Op"), PTRC("Mod", "Beh", "Health", "NamPlt"), PPDP("Mod", "Beh",
            "Health", "NamPlt", "Op"), PDIF("Mod", "Beh", "Health", "NamPlt", "Str", "Op"), PDIS("Mod", "Beh", "Health", "NamPlt", "Str",
                    "Op"), PTOC("Mod", "Beh", "Health", "NamPlt", "Str", "Op"), PVOC("Mod", "Beh", "Health", "NamPlt", "Str",
                            "Op"), PTOV("Mod", "Beh", "Health", "NamPlt", "Str", "Op"), PVPH("Mod", "Beh", "Health", "NamPlt", "Str",
                                    "Op"), RBRF("Mod", "Beh", "Health", "NamPlt", "Str"), RRTC("Mod", "Beh", "Health", "NamPlt", "Str",
                                            "Op"), RPSB("Mod", "Beh", "Health", "NamPlt"), RREC("Mod", "Beh", "Health", "NamPlt", "Op",
                                                    "AutoRecSt"), RBZT("Mod", "Beh", "Health", "NamPlt", "Str", "Op"),

    // 过程层 / 互感器 / 联闭锁 / 录波（附录 A）
    TCTR("Mod", "Beh", "Health", "NamPlt", "Amp"), TVTR("Mod", "Beh", "Health", "NamPlt", "Vol"), CILO("Mod", "Beh", "Health", "NamPlt",
            "EnaOpn", "EnaCls"), ATCC("Mod", "Beh", "Health", "NamPlt", "Loc", "TapChg", "ParOp", "CtlV"), RDRE("Mod", "Beh", "Health",
                    "NamPlt", "RcdMade",
                    "FltNum"), RADR("Mod", "Beh", "Health", "NamPlt", "ChTrg"), RBDR("Mod", "Beh", "Health", "NamPlt", "ChTrg"),

    // 附录 B 独有
    PTTR("Mod", "Beh", "Health", "NamPlt", "Op"), PTUF("Mod", "Beh", "Health", "NamPlt", "Str", "Op"), PTUV("Mod", "Beh", "Health",
            "NamPlt", "Str", "Op"), PSDE("Mod", "Beh", "Health", "NamPlt", "Str", "Op"), RFLO("Mod", "Beh", "Health", "NamPlt", "FltZ",
                    "FltDiskm"), RSYN("Mod", "Beh", "Health", "NamPlt", "Rel"), ZMOT("Mod", "Beh", "Health", "NamPlt", "DExt");

    private static final Map<String, GwRequiredDo> BY_CLASS;

    static {
        Map<String, GwRequiredDo> index = new HashMap<>();
        for (GwRequiredDo req : values()) {
            index.put(req.name(), req);
        }
        BY_CLASS = Collections.unmodifiableMap(index);
    }

    private final String[] dos;

    GwRequiredDo(String... dos) {
        this.dos = dos;
    }

    /** Mandatory DO names for this LN class. */
    public String[] dos() {
        return dos;
    }

    /**
     * Look up the mandatory-DO rule for an LN class.
     *
     * @param lnClass
     *            LN class name (e.g. "PDIF")
     * @return the rule, or null when the class is not covered by the appendices
     */
    public static GwRequiredDo byLnClass(String lnClass) {
        return lnClass == null ? null : BY_CLASS.get(lnClass);
    }
}
