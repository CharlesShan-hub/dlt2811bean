package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFileDirectory;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class FileDirHandler extends AbstractServiceHandler {

    public FileDirHandler(CliContext ctx) { super(ctx, ServiceInfo.GET_FILE_DIRECTORY); }
    public String getDescription() { return "列文件目录"; }
    public List<Param> getParams() {
        return List.of(
            new Param("path", "路径", "/"),
            new Param("after", "参考文件名 (可选)", "")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) throws Exception {
        requireConnected(client);

        String path = values.get("path");
        String after = values.get("after");
        CmsGetFileDirectory asdu = new CmsGetFileDirectory(MessageType.REQUEST);
        if (!path.isEmpty()) asdu.pathName(path);
        if (!after.isEmpty()) asdu.fileAfter(after);
        CmsApdu response = sendAndVerify(client, asdu);

        CmsGetFileDirectory dir = (CmsGetFileDirectory) response.getAsdu();
        System.out.println("  Files: " + dir.fileEntry.size());
        for (int i = 0; i < dir.fileEntry.size(); i++) {
            var entry = dir.fileEntry.get(i);
            System.out.println("    [" + i + "] " + entry.fileName.get()
                + " (" + entry.fileSize.get() + " bytes)");
        }
    }
}
