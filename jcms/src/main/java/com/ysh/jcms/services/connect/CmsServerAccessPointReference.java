package com.ysh.jcms.services.connect;

import com.ysh.jcms.datatypes.string.CmsVisibleString;
import com.ysh.jcms.datatypes.type.AbstractCmsString.Mode;

/**
 * ServerAccessPointReference — format {@code IEDName.AccessPoint}.
 *
 * <p>ASN.1: VisibleString (SIZE(0..129)).
 *
 * <p>Used by client to specify which access point to associate with.
 * When omitted, the server uses a default access point or selects one
 * based on the client address.
 *
 * <p>Thread-safety: not guaranteed.
 */
public class CmsServerAccessPointReference extends CmsVisibleString {

    public static final int MAX_LEN = 129;
    private static final char SEPARATOR = '.';

    public CmsServerAccessPointReference() {
        super();
        max(MAX_LEN);
    }

    public CmsServerAccessPointReference(String iedName, String accessPoint) {
        super(iedName + SEPARATOR + accessPoint);
        max(MAX_LEN);
        validateNoSeparator(iedName, "iedName");
        validateNoSeparator(accessPoint, "accessPoint");
    }

    public CmsServerAccessPointReference(String fullReference) {
        super(fullReference);
        max(MAX_LEN);
        validateSeparator();
    }

    /**
     * Set the full reference string (chainable convenience, not an override).
     */
    public CmsServerAccessPointReference setValue(String value) {
        super.set(value);
        validateSeparator();
        return this;
    }

    public void setIedName(String iedName) {
        validateNoSeparator(iedName, "iedName");
        super.set(iedName + SEPARATOR + getAccessPoint());
    }

    public void setAccessPoint(String accessPoint) {
        validateNoSeparator(accessPoint, "accessPoint");
        super.set(getIedName() + SEPARATOR + accessPoint);
    }

    public String getIedName() {
        String value = get();
        int dot = value.indexOf(SEPARATOR);
        return dot >= 0 ? value.substring(0, dot) : "";
    }

    public String getAccessPoint() {
        String value = get();
        int dot = value.indexOf(SEPARATOR);
        return dot >= 0 ? value.substring(dot + 1) : "";
    }

    public static CmsServerAccessPointReference decode(byte[] encoded) {
        CmsVisibleString vs = CmsVisibleString.decode(encoded, Mode.VARIABLE, MAX_LEN);
        return new CmsServerAccessPointReference(vs.get());
    }

    @Override
    public CmsServerAccessPointReference copy() {
        CmsServerAccessPointReference clone = new CmsServerAccessPointReference();
        clone.setValue(get());
        return clone;
    }

    private void validateSeparator() {
        String value = get();
        if (value.indexOf(SEPARATOR) < 0) {
            throw new IllegalArgumentException(
                "ServerAccessPointReference must contain '" + SEPARATOR + "' separator: " + value);
        }
    }

    private static void validateNoSeparator(String part, String name) {
        if (part.indexOf(SEPARATOR) >= 0) {
            throw new IllegalArgumentException(
                name + " must not contain '" + SEPARATOR + "' separator: " + part);
        }
    }
}
