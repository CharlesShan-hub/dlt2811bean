package com.ysh.dlt2811bean.transport.app;

import com.ysh.dlt2811bean.service.svc.report.CmsReport;

@FunctionalInterface
public interface ReportListener {
    void onReportReceived(CmsReport report);
}