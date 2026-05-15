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

    /**
     * Filters a list of strings to return only entries after the given reference.
     * Used for referenceAfter (分页) logic in directory services.
     *
     * @param entries the full list of entries
     * @param after   the reference after which to return entries, or null/empty for all
     * @return sublist after the reference, or null if the reference is not found
     */
    public static List<String> filterAfter(List<String> entries, String after) {
        if (after == null || after.isEmpty()) return entries;
        int idx = entries.indexOf(after);
        if (idx < 0) return null;
        return entries.subList(idx + 1, entries.size());
    }

    /**
     * Filters a list of objects to return only entries after the given reference.
     * The reference is matched against a key extracted from each entry.
     *
     * @param <T>     the entry type
     * @param entries the full list of entries
     * @param after   the reference after which to return entries, or null/empty for all
     * @param keyExtractor function to extract the reference key from an entry
     * @return sublist after the reference, or null if the reference is not found
     */
    public static <T> List<T> filterAfter(List<T> entries, String after, java.util.function.Function<T, String> keyExtractor) {
        if (after == null || after.isEmpty()) return entries;
        for (int i = 0; i < entries.size(); i++) {
            if (keyExtractor.apply(entries.get(i)).equals(after)) {
                return entries.subList(i + 1, entries.size());
            }
        }
        return null;
    }
}
