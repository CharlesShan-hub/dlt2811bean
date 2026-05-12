package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFileAttributeValues;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class FileAttrHandler extends AbstractServiceHandler {

    public FileAttrHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_FILE_ATTRIBUTE_VALUES); }
    public List<Param> getParams() {
        return List.of(
            new Param("fileName", "文件路径", "/README.txt")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String fileName = values.get("fileName");
        CmsGetFileAttributeValues asdu = new CmsGetFileAttributeValues(MessageType.REQUEST).fileName(fileName);
        CmsApdu response = sendAndVerify(client, asdu);

        CmsGetFileAttributeValues attr = (CmsGetFileAttributeValues) response.getAsdu();
        System.out.println("  File: " + attr.fileEntry.fileName.get());
        System.out.println("  Size: " + attr.fileEntry.fileSize.get() + " bytes");
        System.out.println("  CRC32: " + Long.toHexString(attr.fileEntry.checkSum.get()));
    }
}
