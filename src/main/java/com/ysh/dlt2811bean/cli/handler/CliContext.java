package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataDefinition;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataValues;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetServerDirectory;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsObjectClass;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.Map;
import java.util.Set;

public class CliContext {

    private final CmsConfig config;
    private final Map<String, CommandHandler> handlers;
    private final Set<String> cachedRefs;
    private final Set<String> cachedLds;
    private final Set<String> cachedValues;

    public CliContext(CmsConfig config, Map<String, CommandHandler> handlers,
                      Set<String> cachedRefs, Set<String> cachedLds,
                      Set<String> cachedValues) {
        this.config = config;
        this.handlers = handlers;
        this.cachedRefs = cachedRefs;
        this.cachedLds = cachedLds;
        this.cachedValues = cachedValues;
    }

    public CmsConfig getConfig() { return config; }
    public Map<String, CommandHandler> getHandlers() { return handlers; }
    public Set<String> getCachedRefs() { return cachedRefs; }
    public Set<String> getCachedLds() { return cachedLds; }
    public Set<String> getCachedValues() { return cachedValues; }

    public CmsApdu sendAndPrint(CmsClient client, CmsAsdu<?> asdu) throws Exception {
        if (config.getCli().isTracePdu()) {
            printGray("  >> Request PDU:\n" + asdu.toString().indent(4).stripTrailing());
        }
        CmsApdu response = client.send(asdu);
        if (config.getCli().isTracePdu()) {
            printGray("  << Response PDU:\n" + response.toString().indent(4).stripTrailing());
        }
        return response;
    }

    private void printGray(String text) {
        for (String line : text.split("\n")) {
            System.out.println(CmsColor.gray(line));
        }
    }

    public void printGrayPdu(String label, Object pdu) {
        if (config.getCli().isTracePdu()) {
            printGray(label + "\n" + pdu.toString().indent(4).stripTrailing());
        }
    }

    public String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public String bytesToHex(byte[] bytes, int len) {
        if (bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(len, bytes.length); i++) {
            sb.append(String.format("%02X", bytes[i]));
        }
        return sb.toString();
    }

    public void discoverAfterConnect(CmsClient client) {
        try {
            CmsGetServerDirectory asdu = new CmsGetServerDirectory(MessageType.REQUEST)
                    .objectClass(new CmsObjectClass(CmsObjectClass.LOGICAL_DEVICE));
            CmsApdu response = client.send(asdu);
            if (response != null && response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                CmsGetServerDirectory dir = (CmsGetServerDirectory) response.getAsdu();
                cachedRefs.clear();
                cachedLds.clear();
                System.out.println(CmsColor.gray("  Discovered devices:"));
                for (int i = 0; i < dir.reference().size(); i++) {
                    String ldRef = dir.reference().get(i).get();
                    cachedRefs.add(ldRef);
                    cachedLds.add(ldRef);
                    System.out.println(CmsColor.gray("    [" + i + "] LD: " + ldRef));
                    discoverLnRefs(client, ldRef);
                }
            }
        } catch (Exception ignored) {}
    }

    private void discoverLnRefs(CmsClient client, String ldName) {
        try {
            CmsGetLogicalDeviceDirectory req = new CmsGetLogicalDeviceDirectory(MessageType.REQUEST);
            req.ldName(ldName);
            CmsApdu response = client.send(req);
            if (response != null && response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                CmsGetLogicalDeviceDirectory asdu = (CmsGetLogicalDeviceDirectory) response.getAsdu();
                for (int i = 0; i < asdu.lnReference().size(); i++) {
                    String lnRef = ldName + "/" + asdu.lnReference().get(i).get();
                    cachedRefs.add(lnRef);
                    System.out.println(CmsColor.gray("      LN[" + i + "] " + lnRef));
                    discoverDataRefs(client, lnRef);
                }
            }
        } catch (Exception ignored) {}
    }

    private void discoverDataRefs(CmsClient client, String lnRef) {
        try {
            CmsGetAllDataDefinition req = new CmsGetAllDataDefinition(MessageType.REQUEST);
            req.lnReference(lnRef);
            CmsApdu response = client.send(req);
            if (response != null && response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                CmsGetAllDataDefinition asdu = (CmsGetAllDataDefinition) response.getAsdu();
                for (int i = 0; i < asdu.data().size(); i++) {
                    String dataRef = lnRef + "." + asdu.data().get(i).reference().get();
                    cachedRefs.add(dataRef);
                }
            }
        } catch (Exception ignored) {}
        try {
            CmsGetAllDataValues reqVal = new CmsGetAllDataValues(MessageType.REQUEST);
            reqVal.lnReference(lnRef);
            CmsApdu response = client.send(reqVal);
            if (response != null && response.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                CmsGetAllDataValues asdu = (CmsGetAllDataValues) response.getAsdu();
                for (int i = 0; i < asdu.data().size(); i++) {
                    cachedValues.add(lnRef + "." + asdu.data().get(i).reference().get());
                }
            }
        } catch (Exception ignored) {}
    }
}
