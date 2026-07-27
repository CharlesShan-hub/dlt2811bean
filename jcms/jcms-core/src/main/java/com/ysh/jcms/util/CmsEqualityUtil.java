package com.ysh.jcms.util;

import com.ysh.jcms.core.CmsTypeOld;
import java.util.Arrays;
import java.util.List;

/**
 * Static helpers for equality and hashCode of CmsType trees.
 */
public class CmsEqualityUtil {

    private CmsEqualityUtil() {
    }

    /**
     * Deep equality check. Container types compare children recursively; leaf types
     * compare native memory bytes directly.
     *
     * <p>
     * CmsUint8Array and its subclasses (CmsSubReference, CmsObjectReference,
     * CmsObjectName, CmsEntryId, CmsBitString) compare by data content rather than
     * exact class, so array elements decoded as the wrong subclass still compare
     * equal.
     * </p>
     */
    public static boolean equals(CmsTypeOld a, Object b) {
        if (a == b)
            return true;
        if (b == null)
            return false;
        // CmsUint8Array hierarchy: compare by content, not by exact class
        if (a instanceof com.ysh.jcms.data.string.CmsUint8Array && b instanceof com.ysh.jcms.data.string.CmsUint8Array) {
            return Arrays.equals(((com.ysh.jcms.data.string.CmsUint8Array) a).value(),
                    ((com.ysh.jcms.data.string.CmsUint8Array) b).value());
        }
        if (a.getClass() != b.getClass())
            return false;
        CmsTypeOld other = (CmsTypeOld) b;

        List<? extends CmsTypeOld> kids = a.children();
        List<? extends CmsTypeOld> otherKids = other.children();

        if (!kids.isEmpty()) {
            if (kids.size() != otherKids.size())
                return false;
            for (int i = 0; i < kids.size(); i++) {
                if (!kids.get(i).equals(otherKids.get(i)))
                    return false;
            }
            return true;
        }

        // Leaf type: compare native memory bytes (zero allocation for scalar sizes)
        if (a.nativeSize != other.nativeSize)
            return false;
        switch (a.nativeSize) {
            case 1 :
                return a.nativePtr.getByte(0) == other.nativePtr.getByte(0);
            case 2 :
                return a.nativePtr.getShort(0) == other.nativePtr.getShort(0);
            case 4 :
                return a.nativePtr.getInt(0) == other.nativePtr.getInt(0);
            case 8 :
                return a.nativePtr.getLong(0) == other.nativePtr.getLong(0);
            default :
                return Arrays.equals(a.nativePtr.getByteArray(0, a.nativeSize), other.nativePtr.getByteArray(0, other.nativeSize));
        }
    }

    /**
     * Hash code for a CmsType. Container types combine children hashes; leaf types
     * hash native memory bytes.
     */
    public static int hashCode(CmsTypeOld a) {
        List<? extends CmsTypeOld> kids = a.children();
        if (!kids.isEmpty()) {
            int h = 1;
            for (CmsTypeOld child : kids)
                h = 31 * h + (child != null ? child.hashCode() : 0);
            return h;
        }
        // Leaf: compare using correct width
        if (a.nativeSize <= 8) {
            long v;
            switch (a.nativeSize) {
                case 1 :
                    v = a.nativePtr.getByte(0);
                    break;
                case 2 :
                    v = a.nativePtr.getShort(0);
                    break;
                case 4 :
                    v = a.nativePtr.getInt(0);
                    break;
                default :
                    v = a.nativePtr.getLong(0);
                    break;
            }
            return (int) (v ^ (v >>> 32));
        }
        return Arrays.hashCode(a.nativePtr.getByteArray(0, a.nativeSize));
    }
}
