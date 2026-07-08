package com.ysh.jcms.utils.scl2.ref;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SCL 引用解析器。
 * <p>
 * 支持格式：
 * <ul>
 *   <li>{@code LD/IN} — LN 级别</li>
 *   <li>{@code LD/LN.DO} — DO 级别</li>
 *   <li>{@code LD/LN.DO.DA} — DA 级别</li>
 *   <li>{@code LD/LN.DO.DA[FC]} — 带功能约束</li>
 * </ul>
 * 其中 {@code DO} 和 {@code DA} 可选，{@code [FC]} 只在 DA 级别出现。
 */
public final class SclRefParser {

    // LD/LN[.DO[.DA]][FC]
    private static final Pattern REF_PATTERN = Pattern.compile(
            "([^/]+)/([^.]+)(?:\\.([^.]+)(?:\\.([^\\[\\]]+))?)?(?:\\[([^\\]]+)\\])?"
    );

    private SclRefParser() {
    }

    /**
     * 解析引用字符串。
     *
     * @param ref 引用字符串，不可为 null 或空白
     * @return SclRef 实例
     * @throws IllegalArgumentException 格式无效时抛出
     */
    public static SclRef parse(String ref) {
        if (ref == null || ref.trim().isEmpty()) {
            throw new IllegalArgumentException("SCL reference cannot be null or blank");
        }

        String trimmed = ref.trim();
        Matcher matcher = REF_PATTERN.matcher(trimmed);
        if (matcher.matches()) {
            String ldName = matcher.group(1);
            String lnName = matcher.group(2);
            String doName = matcher.group(3);
            String daName = matcher.group(4);
            String fc = matcher.group(5);
            return new SclRef(ldName, lnName, doName, daName, fc, trimmed);
        }

        throw new IllegalArgumentException("Invalid SCL reference format: " + ref);
    }

    /**
     * 判断引用字符串格式是否有效。
     */
    public static boolean isValid(String ref) {
        if (ref == null || ref.trim().isEmpty()) return false;
        return REF_PATTERN.matcher(ref.trim()).matches();
    }

    /**
     * 提取 LN 级别引用（LD/LN）。
     */
    public static String extractLnReference(String ref) {
        return parse(ref).lnReference();
    }

    /**
     * 提取 DO 级别引用（LD/LN.DO）。
     */
    public static String extractDoReference(String ref) {
        return parse(ref).doReference();
    }
}
