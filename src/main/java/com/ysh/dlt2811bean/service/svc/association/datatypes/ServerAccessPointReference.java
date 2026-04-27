package com.ysh.dlt2811bean.service.svc.association.datatypes;

import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;

/**
 * Server Access Point Reference — IEDName.AccessPoint.
 *
 * <p>Per Table 19, the server access point reference is a VisibleString(SIZE(0..129))
 * formatted as {@code IEDName.AccessPoint}, where the dot ({@code .}) separates
 * the IED name from the access point name.
 *
 * <p>This class encapsulates the concatenation/splitting logic so that callers
 * can work with the IED name and access point independently.
 */
public class ServerAccessPointReference extends CmsVisibleString {

    private static final char SEPARATOR = '.';

    public ServerAccessPointReference() {
        super("ServerAccessPointReference", "");
        max(129);
    }

    public ServerAccessPointReference(String iedName, String accessPoint) {
        super("ServerAccessPointReference", iedName + SEPARATOR + accessPoint);
        max(129);
        validateNoSeparator(iedName, "iedName");
        validateNoSeparator(accessPoint, "accessPoint");
    }

    public ServerAccessPointReference(String fullReference) {
        super("ServerAccessPointReference", fullReference);
        max(129);
        validateSeparator();
    }

    @Override
    public ServerAccessPointReference set(String value) {
        super.set(value);
        validateSeparator();
        return this;
    }

    public void setIedName(String iedName) {
        validateNoSeparator(iedName, "iedName");
        set(iedName + SEPARATOR + getAccessPoint());
    }

    public void setAccessPoint(String accessPoint) {
        validateNoSeparator(accessPoint, "accessPoint");
        set(getIedName() + SEPARATOR + accessPoint);
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

    @Override
    public ServerAccessPointReference copy() {
        ServerAccessPointReference clone = new ServerAccessPointReference();
        clone.set(get());
        return clone;
    }
}
