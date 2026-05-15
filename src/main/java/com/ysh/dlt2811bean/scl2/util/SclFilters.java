package com.ysh.dlt2811bean.scl2.util;

import com.ysh.dlt2811bean.scl2.model.*;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class SclFilters {

    private SclFilters() {}

    public static Predicate<SclIED> iedNameMatches(String pattern) {
        Pattern p = Pattern.compile(pattern.replace("*", ".*"));
        return ied -> p.matcher(ied.getName()).matches();
    }

    public static Predicate<SclIED> iedNameEquals(String name) {
        return ied -> ied.getName().equals(name);
    }

    public static Predicate<SclLN> lnClassEquals(String lnClass) {
        return ln -> ln.getLnClass().equals(lnClass);
    }

    public static Predicate<SclLN> lnClassIn(List<String> lnClasses) {
        return ln -> lnClasses.contains(ln.getLnClass());
    }

    public static Predicate<SclLN> lnPrefixEquals(String prefix) {
        return ln -> ln.getPrefix().equals(prefix);
    }

    public static Predicate<SclLDevice> ldInstEquals(String inst) {
        return ld -> ld.getInst().equals(inst);
    }

    public static Predicate<SclDA> daFcEquals(String fc) {
        return da -> da.getFc().equals(fc);
    }

    public static Predicate<SclDA> daBTypeEquals(String bType) {
        return da -> da.getBType().equals(bType);
    }

    public static Predicate<SclDOType> doCdcEquals(String cdc) {
        return dot -> dot.getCdc().equals(cdc);
    }

    public static Predicate<SclFCDA> fcdaFcEquals(String fc) {
        return fcda -> fcda.getFc().equals(fc);
    }

    public static <T> Predicate<T> alwaysTrue() {
        return t -> true;
    }
}
