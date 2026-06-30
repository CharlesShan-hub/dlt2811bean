package com.ysh.jcms.app.console;

import com.ysh.jcms.app.handler.console.client.*;
import com.ysh.jcms.app.handler.console.TracePduHandler;
import com.ysh.jcms.app.handler.data.getDataValues.GetDataValuesClient;
import com.ysh.jcms.app.handler.data.getDataValues.GetDataValuesConsole;
import com.ysh.jcms.app.handler.data.setDataValues.SetDataValuesClient;
import com.ysh.jcms.app.handler.data.setDataValues.SetDataValuesConsole;
import com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory.LdDirClient;
import com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory.LdDirConsole;
import com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory.LnDirClient;
import com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory.LnDirConsole;
import com.ysh.jcms.app.handler.directory.getAllCbValues.AllCbValuesClient;
import com.ysh.jcms.app.handler.directory.getAllCbValues.AllCbValuesConsole;
import com.ysh.jcms.app.handler.directory.getAllDataDefinition.AllDataDefClient;
import com.ysh.jcms.app.handler.directory.getAllDataDefinition.AllDataDefConsole;
import com.ysh.jcms.app.handler.directory.getAllDataValues.AllDataValuesClient;
import com.ysh.jcms.app.handler.directory.getAllDataValues.AllDataValuesConsole;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirClient;
import com.ysh.jcms.app.handler.directory.getServerDirectory.SvrDirConsole;
import com.ysh.jcms.app.handler.connection.release.ReleaseClient;
import com.ysh.jcms.app.handler.connection.release.ReleaseConsole;
import com.ysh.jcms.app.handler.connection.abort.AbortClient;
import com.ysh.jcms.app.handler.connection.abort.AbortConsole;
import com.ysh.jcms.app.handler.connection.associate.AssociateClient;
import com.ysh.jcms.app.handler.connection.associate.AssociateConsole;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateClient;
import com.ysh.jcms.app.handler.negotiate.negotiate.NegotiateConsole;
import com.ysh.jcms.app.handler.test.test.TestClient;
import com.ysh.jcms.app.handler.test.test.TestConsole;

/**
 * Client-side CMS console.
 *
 * <p>Connects to a remote CMS server and issues commands interactively.
 */
public class CmsClientConsole extends CmsConsole {

    public CmsClientConsole() {
        super(false);
        registerClients();
    }

    private void registerClients() {
        registerClient(new NegotiateClient(this));
        registerClient(new AssociateClient(this));
        registerClient(new ReleaseClient(this));
        registerClient(new AbortClient(this));
        registerClient(new TestClient(this));
        registerClient(new SvrDirClient(this));
        registerClient(new LnDirClient(this));
        registerClient(new LdDirClient(this));
        registerClient(new AllDataValuesClient(this));
        registerClient(new AllDataDefClient(this));
        registerClient(new AllCbValuesClient(this));
        registerClient(new GetDataValuesClient(this));
        registerClient(new SetDataValuesClient(this));
    }

    @Override
    protected void registerHandlers() {
        register(new HelpHandler(this));
        register(new ConnectHandler());
        register(new DisconnectHandler());
        register(new SvrDirConsole());
        register(new LdDirConsole());
        register(new LnDirConsole());
        register(new AllDataValuesConsole());
        register(new AllDataDefConsole());
        register(new AllCbValuesConsole());
        register(new GetDataValuesConsole());
        register(new SetDataValuesConsole());
        register(new TracePduHandler());
        register(new ReleaseConsole());
        register(new AssociateConsole());
        register(new AbortConsole());
        register(new NegotiateConsole());
        register(new TestConsole());
    }

    public static void main(String[] args) {
        new CmsClientConsole().run();
    }
}
