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
