package com.ysh.jcms.utils.scl.model.ied;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclLDevice {

    private String inst;
    private String desc;
    private String ldName;
    /**
     * LD 配置版本号。
     * <p>
     * 注意：非 IEC 61850-6 2007B 标准属性（tLDevice 只有 inst/ldName）——系国网 Q/GDW 1396 / 厂商扩展，
     * 真实国网 SCD 常见，保留用于宽容解析。
     */
    private String confRev;
    private SclAccessControl accessControl;

    private final List<SclLN> lns = new ArrayList<>();
    private final List<SclLN> subLns = new ArrayList<>();

    public SclLDevice addLn(SclLN ln) {
        this.lns.add(ln);
        ln.parentLd(this);
        return this;
    }

    public SclLDevice addSubLn(SclLN subLn) {
        this.subLns.add(subLn);
        subLn.parentLd(this);
        return this;
    }

    public SclLN findLnByFullName(String fullName) {
        for (SclLN ln : lns) {
            if (ln.getFullName().equals(fullName)) {
                return ln;
            }
        }
        for (SclLN ln : subLns) {
            if (ln.getFullName().equals(fullName)) {
                return ln;
            }
        }
        return null;
    }

    public List<SclLN> findLnsByClass(String lnClass) {
        List<SclLN> result = new ArrayList<>();
        for (SclLN ln : lns) {
            if (ln.lnClass().equals(lnClass)) {
                result.add(ln);
            }
        }
        for (SclLN ln : subLns) {
            if (ln.lnClass().equals(lnClass)) {
                result.add(ln);
            }
        }
        return result;
    }
}
