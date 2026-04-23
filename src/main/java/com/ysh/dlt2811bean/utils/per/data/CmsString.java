package com.ysh.dlt2811bean.utils.per.data;

/**
 * Interface for all CMS string types.
 * String types include OCTET STRING, VISIBLE STRING, UTF8String, and BIT STRING.
 *
 * <p>This interface defines the common API for all string-like types
 * defined in DL/T 2811 §7.1.5 (Table 6).
 *
 * <p>String types support two constraint modes:
 * <ul>
 *   <li><b>FIXED</b> (SIZE(n)): fixed length, padded/trimmed as needed</li>
 *   <li><b>VARIABLE</b> (SIZE(0..max)): variable length with length prefix</li>
 * </ul>
 *
 * @param <T> the concrete string type implementing this interface
 * @param <V> the value type (String, byte[], etc.)
 */
public interface CmsString<T extends CmsString<T, V>, V> extends CmsScalar<T, V> {

    T size(int size);

    T max(int max);
}
