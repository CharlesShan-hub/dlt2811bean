package com.ysh.jcms.app.tool;

import com.ysh.jcms.app.handler.connection.associate.AssociateDao;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateDao;
import com.ysh.jcms.app.handler.connection.release.ReleaseDao;
import com.ysh.jcms.app.handler.data.getDataValues.GetDataValuesClient;
import com.ysh.jcms.app.handler.data.getDataValues.GetDataValuesDao;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirClient;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirDao;
import com.ysh.jcms.app.handler.support.CmsContent;
import com.ysh.jcms.app.node.CmsClient;
import com.ysh.jcms.core.util.CmsPrinter;

import java.util.Arrays;

/**
 * Demonstration of using {@link CmsClient} as a pure API — no CLI, no JLine.
 *
 * <p>
 * Shows the typical workflow:
 * {@code create → connect → associate → execute requests → release → close}.
 * Each DAO is a fluent data object; execute via {@link CmsClient#execute} or
 * {@link CmsClient#getClient getClient(...).executeResult(content)}.
 */
public class CmsClientDemo {

    public static void main(String[] args) throws Exception {
        // 1. Create client
        // CmsClient client = new CmsClient("config/sample-scd-full.scd");
        // 1. Create client with scd file path in config
        CmsClient client = new CmsClient();

        // 2. TCP connect to the CMS server
        System.out.println("Connecting to 127.0.0.1:8102 ...");
        client.connect("127.0.0.1", 8102);

        // 3. Negotiate with the server: apduSize\asduSize\protocolVersion from config
        System.out.println("Negotiating with the server ...");
        //client.execute(new NegotiateDao());
        client.execute(new NegotiateDao().apduSize(1024).asduSize(1024).protocolVersion(1));

        // 3. Associate with an access point (auto-negotiates)
        System.out.println("Associating with C_B5041X/S1 ...");
        client.execute(new AssociateDao().sapRef("C_B5041X/S1"));

        // 4. Execute a request and read the response via CmsContent.
        //    Use getClient(clazz).executeResult(content) when you need the response.
        System.out.println("Fetching server directory ...");
        CmsContent<SvrDirDao> sd = new CmsContent<>(new SvrDirDao());
        client.getClient(SvrDirClient.class).executeResult(sd);
        System.out.println("Logical devices:");
        CmsPrinter.outputJson(sd.res());

        // 5. Read data values
        System.out.println("Reading data values ...");
        CmsContent<GetDataValuesDao> dv = new CmsContent<>(
                new GetDataValuesDao()
                        .refs(Arrays.asList("LD0/LLN0.Beh", "LD0/LLN0.NamPlt"))
                        .fcs(Arrays.asList("ST", "ST")));
        client.getClient(GetDataValuesClient.class).executeResult(dv);
        System.out.println("Data values:");
        CmsPrinter.outputJson(dv.res());

        // 6. Normal release (close association + TCP)
        client.execute(new ReleaseDao());
        client.close();

        System.out.println("Done.");
    }
}
