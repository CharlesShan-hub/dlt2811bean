package com.ysh.dlt2811bean.scl2.query;

import com.ysh.dlt2811bean.scl2.model.*;

import java.util.List;
import java.util.Optional;

public class IedQuery {

    private final SclIED ied;

    public IedQuery(SclIED ied) {
        this.ied = ied;
    }

    public SclIED ied() { return ied; }

    public String name() { return ied.getName(); }

    public List<SclAccessPoint> accessPoints() { return ied.getAccessPoints(); }

    public Optional<SclAccessPoint> accessPoint(String name) {
        return Optional.ofNullable(ied.findAccessPointByName(name));
    }

    public Optional<SclServer> server() {
        return ied.getAccessPoints().stream()
            .map(SclAccessPoint::getServer)
            .filter(s -> s != null)
            .findFirst();
    }

    public List<SclLDevice> lDevices() {
        return server()
            .map(SclServer::getLDevices)
            .orElse(List.of());
    }

    public Optional<SclLDevice> lDevice(String inst) {
        return server()
            .map(s -> s.findLDeviceByInst(inst));
    }

    public List<String> lDeviceNames() {
        return lDevices().stream()
            .map(SclLDevice::getInst)
            .toList();
    }

    public List<SclLN> lns() {
        return lDevices().stream()
            .flatMap(ld -> ld.getLns().stream())
            .toList();
    }

    public List<SclLN> lnsByClass(String lnClass) {
        return lDevices().stream()
            .flatMap(ld -> ld.findLnsByClass(lnClass).stream())
            .toList();
    }

    public List<String> lnNames() {
        return lns().stream()
            .map(SclLN::getFullName)
            .toList();
    }

    public Optional<SclLN> ln(String fullName) {
        return lDevices().stream()
            .map(ld -> ld.findLnByFullName(fullName))
            .filter(ln -> ln != null)
            .findFirst();
    }

    public Optional<SclLN> ln(String ldInst, String lnFullName) {
        return lDevice(ldInst)
            .map(ld -> ld.findLnByFullName(lnFullName));
    }

    public SclServices services() { return ied.getServices(); }
}
