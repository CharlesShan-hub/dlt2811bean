package com.ysh.jcms.utils.scl.ref;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SCL reference parser.
 * <p>
 * Supported formats:
 * <ul>
 * <li>{@code LD/LN} — LN level</li>
 * <li>{@code LD/LN.DO[.SDI]...[.DA][FC]} — DO/DA level</li>
 * <li>{@code IED/LD/LN[.DO[.SDI]...[.DA]][FC]} — with IED prefix</li>
 * </ul>
 */
public final class SclRefParser {

    // Without IED: LD/LN[.X[.Y]...][FC] — capture LD/LN, parse the rest manually
    private static final Pattern REF_PATTERN = Pattern.compile("([^/]+)/([^.]+)(?:\\..+)?(?:\\[([^\\]]+)\\])?");

    // With IED: IED/LD/LN[.X[.Y]...][FC]
    private static final Pattern FULL_REF_PATTERN = Pattern.compile("([^/]+)/([^/]+)/([^.]+)(?:\\..+)?(?:\\[([^\\]]+)\\])?");

    private SclRefParser() {
    }

    public static SclRef parse(String ref) {
        if (ref == null || ref.trim().isEmpty()) {
            throw new IllegalArgumentException("SCL reference cannot be null or blank");
        }

        String trimmed = ref.trim();

        String iedName = null;
        String ldInst;
        String lnName;
        String fc = null;

        // Try the full format first
        Matcher fullMatcher = FULL_REF_PATTERN.matcher(trimmed);
        if (fullMatcher.matches()) {
            iedName = fullMatcher.group(1);
            ldInst = fullMatcher.group(2);
            lnName = fullMatcher.group(3);
            fc = fullMatcher.group(4);
        } else {
            Matcher matcher = REF_PATTERN.matcher(trimmed);
            if (matcher.matches()) {
                ldInst = matcher.group(1);
                lnName = matcher.group(2);
                fc = matcher.group(3);
            } else {
                throw new IllegalArgumentException("Invalid SCL reference format: " + ref);
            }
        }

        // Manually parse the DO/SDI/DA parts
        // Remove the [IED/]LD/LN part from trimmed, leaving ".DO[.SDI...[.DA]][FC]"
        int prefixLen = (iedName != null ? iedName.length() + 1 : 0) + ldInst.length() + 1 + lnName.length();
        String rest = trimmed.substring(prefixLen); // ".DO.SDI.DA[FC]" or ""

        String doName = null;
        List<String> sdiChain = new ArrayList<>();
        String daName = null;

        if (!rest.isEmpty()) {
            // Strip the leading "."
            String dotPart = rest.startsWith(".") ? rest.substring(1) : rest;
            // Strip the trailing "[FC]"
            String fcPart = null;
            int bracketStart = dotPart.indexOf('[');
            if (bracketStart >= 0) {
                fcPart = dotPart.substring(bracketStart + 1, dotPart.length() - 1);
                dotPart = dotPart.substring(0, bracketStart);
                if (fc == null)
                    fc = fcPart;
            }
            // Split by "."
            String[] parts = dotPart.split("\\.");
            if (parts.length >= 1 && !parts[0].isEmpty()) {
                doName = parts[0];
            }
            if (parts.length >= 3) {
                // DO.SDI...[.DA] — everything in the middle is SDI
                for (int i = 1; i < parts.length - 1; i++) {
                    sdiChain.add(parts[i]);
                }
                daName = parts[parts.length - 1];
            } else if (parts.length == 2) {
                daName = parts[1];
            }
        }

        return new SclRef(iedName, ldInst, lnName, doName, sdiChain.isEmpty() ? null : sdiChain, daName, fc);
    }

    public static boolean isValid(String ref) {
        if (ref == null || ref.trim().isEmpty())
            return false;
        String trimmed = ref.trim();
        return FULL_REF_PATTERN.matcher(trimmed).matches() || REF_PATTERN.matcher(trimmed).matches();
    }

    public static String extractLnReference(String ref) {
        return parse(ref).lnReference();
    }

    public static String extractDoReference(String ref) {
        return parse(ref).doReference();
    }
}
