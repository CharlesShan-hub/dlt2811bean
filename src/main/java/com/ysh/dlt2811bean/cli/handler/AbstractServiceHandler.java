package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.service.info.LnInfo;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.datatypes.numeric.CmsBoolean;
import com.ysh.dlt2811bean.scl.SclReader;
import com.ysh.dlt2811bean.scl.SclTypeResolver;
import com.ysh.dlt2811bean.scl.SclDocument;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl.model.SclIED;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractServiceHandler implements CommandHandler {

    protected final CliContext ctx;
    private final ServiceInfo serviceInfo;
    protected CmsConfig config;

    protected AbstractServiceHandler(CliContext ctx, ServiceInfo serviceInfo) {
        this.ctx = ctx;
        this.serviceInfo = serviceInfo;
    }

    @Override
    public String getName() { return serviceInfo.getCliName(); }

    @Override
    public String getDescription() { return serviceInfo.getDescription(); }

    @Override
    public CmsConfig config() { return CmsConfigLoader.load(); }

    @Override
    public List<Param> updateConfigAndGetParams() {
        config = CmsConfigLoader.load();
        return getParams();
    }

    public List<Param> getParams() { return List.of(); }

    protected void requireConnected(CmsClient client) {
        if (!client.isConnected()) {
            throw new IllegalStateException("Not connected. Type 'connect' first.");
        }
    }

    protected CmsApdu sendAndVerify(CmsClient client, CmsAsdu<?> asdu) throws Exception {
        CmsApdu response = ctx.sendAndPrint(client, asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            throw new IllegalStateException("Request failed");
        }
        return response;
    }

    protected String lnClassName(String lnRef) {
        String className = lnRef.substring(lnRef.lastIndexOf("/") + 1, lnRef.lastIndexOf("/") + 5);
        LnInfo info = LnInfo.byName(className);
        return info != null ? " - " + info.getChineseName() : "";
    }

    protected void printRequestPdu(Object pdu) {
        ctx.printGrayPdu("  >> Request PDU:", pdu);
    }

    protected void printResponsePdu(Object pdu) {
        ctx.printGrayPdu("  << Response PDU:", pdu);
    }

    protected void printGray(String text) {
        System.out.println(CmsColor.gray(text));
    }

    protected void printRed(String text) {
        System.out.println(CmsColor.red(text));
    }

    protected void printGreen(String text) {
        System.out.println(CmsColor.green(text));
    }

    protected void printCyan(String text) {
        System.out.println(CmsColor.cyan(text));
    }

    protected String cyan(String text) {
        return CmsColor.cyan(text);
    }

    protected void printMoreFollows(boolean moreFollows) {
        if (moreFollows) {
            printGray("  (more data available)");
        }
    }

    protected boolean printIfEmpty(boolean isEmpty) {
        if (isEmpty) {
            printGray("  无数据");
            return true;
        }
        return false;
    }

    protected <T> void printList(String title, List<T> items, Function<T, String> formatter) {
        if (printIfEmpty(items.isEmpty())) return;
        printGreen("  " + title + ":");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("    " + cyan("[" + i + "]") + " " + formatter.apply(items.get(i)));
        }
    }

    protected CmsType<?> parseControlValue(String ref, String value) {
        try {
            String sclPath = config.getServer().getSclFile();
            SclDocument doc = new SclReader().read(sclPath);
            SclIED.SclServer server = findFirstServer(doc);
            SclDataTypeTemplates templates = doc != null ? doc.getDataTypeTemplates() : null;
            if (server == null || templates == null) {
                return new CmsBoolean(value.equalsIgnoreCase("true"));
            }
            String bType = resolveBType(ref, server, templates);
            if (bType != null) {
                return SclTypeResolver.createTypedValue(bType, value);
            }
        } catch (Exception e) {
            // fall through
        }
        return new CmsBoolean(value.equalsIgnoreCase("true"));
    }

    private SclIED.SclServer findFirstServer(SclDocument sclDocument) {
        if (sclDocument == null || sclDocument.getIeds() == null) return null;
        for (SclIED ied : sclDocument.getIeds()) {
            if (ied.getAccessPoints() != null) {
                for (SclIED.SclAccessPoint ap : ied.getAccessPoints()) {
                    if (ap.getServer() != null) {
                        return ap.getServer();
                    }
                }
            }
        }
        return null;
    }

    private String resolveBType(String ref, SclIED.SclServer server, SclDataTypeTemplates templates) {
        if (server == null || templates == null) return null;
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return null;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");
        if (parts.length < 2) return null;
        String lnName = parts[0];
        String doName = parts[1];
        if (parts.length >= 3) {
            return SclTypeResolver.resolveBType(server, templates, ldName, lnName, doName, parts[2]);
        }
        var das = SclTypeResolver.listDasFromType(server, templates, ldName, lnName, doName);
        if (das != null && !das.isEmpty()) {
            return SclTypeResolver.resolveBType(server, templates, ldName, lnName, doName, das.get(0).getName());
        }
        return null;
    }
}
