package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclLDevice {

    private String inst;
    private String desc;
    private final List<SclLN> lns = new ArrayList<>();
    private final List<SclLN> subLns = new ArrayList<>();

    public void addLn(SclLN ln) { this.lns.add(ln); }

    public void addSubLn(SclLN subLn) { this.subLns.add(subLn); }

    public SclLN findLnByPrefixAndClass(String prefix, String lnClass) {
        for (SclLN ln : lns) {
            if (ln.getPrefix().equals(prefix) && ln.getLnClass().equals(lnClass)) return ln;
        }
        return null;
    }

    public SclLN findLnByFullName(String fullName) {
        for (SclLN ln : lns) {
            if (ln.getFullName().equals(fullName)) return ln;
        }
        return null;
    }

    public List<SclLN> findLnsByClass(String lnClass) {
        List<SclLN> result = new ArrayList<>();
        for (SclLN ln : lns) {
            if (ln.getLnClass().equals(lnClass)) result.add(ln);
        }
        return result;
    }

    public SclLN getLn0() {
        for (SclLN ln : lns) {
            if ("LLN0".equals(ln.getLnClass())) return ln;
        }
        return null;
    }

    public List<String> getLnNames() {
        List<String> names = new ArrayList<>();
        SclLN ln0 = getLn0();
        if (ln0 != null) names.add(ln0.getFullName());
        for (SclLN ln : lns) {
            if (!"LLN0".equals(ln.getLnClass())) names.add(ln.getFullName());
        }
        return names;
    }

    public List<String> getLnNames(String after) {
        List<String> names = getLnNames();
        if (after == null || after.isEmpty()) return names;
        int idx = names.indexOf(after);
        if (idx < 0) return null;
        return names.subList(idx + 1, names.size());
    }
}
