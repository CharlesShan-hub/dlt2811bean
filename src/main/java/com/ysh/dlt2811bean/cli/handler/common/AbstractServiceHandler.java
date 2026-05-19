package com.ysh.dlt2811bean.cli.handler.common;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import java.util.List;
import java.util.Map;

public abstract class AbstractServiceHandler implements CommandHandler {

    protected final CliContext ctx;
    private final ServiceInfo serviceInfo;
    private final boolean requireConnected;
    protected CmsConfig config;
    protected List<Param> cachedParams;
    protected CmsApdu response;
    protected Object result;
    protected int resultExtra;

    protected AbstractServiceHandler(CliContext ctx, ServiceInfo serviceInfo) {
        this(ctx, serviceInfo, true);
    }

    protected AbstractServiceHandler(CliContext ctx, ServiceInfo serviceInfo, boolean requireConnected) {
        this.ctx = ctx;
        this.serviceInfo = serviceInfo;
        this.requireConnected = requireConnected;
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

    public List<Param> getParams() {
        if (cachedParams == null) {
            cachedParams = setParams();
        }
        return cachedParams;
    }

    protected List<Param> setParams() { return List.of(); }

    protected void resolveParams(Map<String, String> rawValues) {
        for (Param param : getParams()) {
            String raw = rawValues.get(param.getName());
            if (raw == null || raw.isEmpty()) {
                raw = param.getDefaultValue();
            }
            param.setValue(convert(param, raw));
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T val(String name) {
        for (Param param : getParams()) {
            if (param.getName().equals(name)) {
                return (T) param.getValue();
            }
        }
        throw new IllegalArgumentException("Unknown parameter: " + name);
    }

    protected boolean booleanVal(String name) {
        return val(name);
    }

    protected int intVal(String name) {
        Object v = val(name);
        if (v instanceof Number) {
            return ((Number) v).intValue();
        }
        return Integer.parseInt(v.toString());
    }

    protected String stringVal(String name) {
        String v = val(name);
        return v != null ? v : "";
    }

    private Object convert(Param param, String raw) {
        if (raw == null || raw.isEmpty()) {
            return null;
        }
        if (!param.getEnumChoices().isEmpty()) {
            boolean valid = param.getEnumChoices().stream()
                .anyMatch(c -> c.getValue().equals(raw));
            if (!valid) {
                throw new IllegalArgumentException(
                    "Invalid value '" + raw + "' for " + param.getName()
                    + ", expected: " + param.getEnumChoices().stream()
                        .map(c -> c.getValue() + "(" + c.getLabel() + ")")
                        .collect(java.util.stream.Collectors.joining(", ")));
            }
            return raw;
        }
        return switch (param.getValueType()) {
            case BOOLEAN -> Boolean.parseBoolean(raw);
            case INTEGER -> Integer.parseInt(raw);
            case STRING -> raw;
        };
    }

    @Override
    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        resolveParams(values);
        if (requireConnected) {
            requireConnected(client);
        }
        doExecute(client, values);
        afterExecute(client, values);
    }

    protected void doExecute(CmsClient client, Map<String, String> values) throws Exception {
    }

    protected void afterExecute(CmsClient client, Map<String, String> values) throws Exception {
    }

    protected void requireConnected(CmsClient client) {
        if (!client.isConnected()) {
            throw new IllegalStateException("Not connected. Type 'connect' first.");
        }
    }

    protected CmsApdu sendAndVerify(CmsClient client, CmsAsdu<?> asdu) throws Exception {
        CmsApdu response = client.send(asdu);
        if (response.getMessageType() != MessageType.RESPONSE_POSITIVE) {
            throw new IllegalStateException("Request failed");
        }
        return response;
    }
}
