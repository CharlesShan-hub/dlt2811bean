package com.ysh.dlt2811bean.scl2.query;

import com.ysh.dlt2811bean.scl2.model.*;

import java.util.List;
import java.util.Optional;

public class DataTypeQuery {

    private final SclDataTypeTemplates templates;

    public DataTypeQuery(SclDataTypeTemplates templates) {
        this.templates = templates;
    }

    public SclDataTypeTemplates templates() { return templates; }

    public Optional<SclLNodeType> lNodeType(String id) {
        return Optional.ofNullable(templates.findLNodeTypeById(id));
    }

    public Optional<SclDOType> doType(String id) {
        return Optional.ofNullable(templates.findDoTypeById(id));
    }

    public Optional<SclDAType> daType(String id) {
        return Optional.ofNullable(templates.findDaTypeById(id));
    }

    public Optional<SclEnumType> enumType(String id) {
        return Optional.ofNullable(templates.findEnumTypeById(id));
    }

    public List<SclLNodeType> allLNodeTypes() { return templates.getLNodeTypes(); }

    public List<SclDOType> allDoTypes() { return templates.getDoTypes(); }

    public List<SclDAType> allDaTypes() { return templates.getDaTypes(); }

    public List<SclEnumType> allEnumTypes() { return templates.getEnumTypes(); }

    public Optional<String> resolveEnumValue(String enumTypeId, int ord) {
        return enumType(enumTypeId)
            .map(et -> et.findEnumValByOrdAsString(ord));
    }

    public Optional<SclDOType> resolveDoTypeByLnType(String lnTypeId, String doName) {
        return lNodeType(lnTypeId)
            .map(lnt -> lnt.findDoByName(doName))
            .map(SclDO::getType)
            .flatMap(this::doType);
    }

    public Optional<SclDA> resolveDaInDoType(String doTypeId, String daName) {
        return doType(doTypeId)
            .map(dot -> dot.findDaByName(daName));
    }

    public Optional<String> resolveBTypeInDoType(String doTypeId, String daName) {
        return resolveDaInDoType(doTypeId, daName)
            .map(SclDA::getBType);
    }

    public List<SclDA> findDasByFc(String doTypeId, String fc) {
        return doType(doTypeId)
            .map(dot -> dot.findDaByFc(fc))
            .orElse(List.of());
    }
}
