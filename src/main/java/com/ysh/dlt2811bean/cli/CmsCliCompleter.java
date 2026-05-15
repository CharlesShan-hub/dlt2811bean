package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.info.DataTypeInfo;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CmsCliCompleter implements Completer {

    private final Map<String, CommandHandler> handlers;
    private final CliContext ctx;

    public CmsCliCompleter(Map<String, CommandHandler> handlers, CliContext ctx) {
        this.handlers = handlers;
        this.ctx = ctx;
    }

    @Override
    public void complete(LineReader rdr, ParsedLine parsedLine, List<Candidate> candidates) {
        String buffer = parsedLine.line();
        String word = parsedLine.word();

        if (buffer.toLowerCase().startsWith("help datatype ")) {
            completeDataType(word, candidates);
        } else if (buffer.toLowerCase().startsWith("help cdc ")) {
            completeCdc(word, candidates);
        } else if (buffer.toLowerCase().startsWith("help ")) {
            completeHelpCommand(word, candidates);
        } else if (buffer.contains(" ")) {
            completeParam(buffer, word, candidates);
        } else {
            completeCommandName(word, candidates);
        }
    }

    private void completeDataType(String word, List<Candidate> candidates) {
        for (DataTypeInfo dt : DataTypeInfo.values()) {
            if (dt.getTypeName().toLowerCase().startsWith(word.toLowerCase())) {
                candidates.add(new Candidate(dt.getTypeName()));
            }
        }
    }

    private void completeCdc(String word, List<Candidate> candidates) {
        for (CdcInfo cdc : CdcInfo.values()) {
            if (cdc.getName().toLowerCase().startsWith(word.toLowerCase())) {
                candidates.add(new Candidate(cdc.getName()));
            }
        }
    }

    private void completeHelpCommand(String word, List<Candidate> candidates) {
        for (String cmd : handlers.keySet()) {
            if (cmd.startsWith(word.toLowerCase())) {
                candidates.add(new Candidate(cmd));
            }
        }
        if ("datatype".startsWith(word.toLowerCase())) {
            candidates.add(new Candidate("datatype"));
        }
        if ("cdc".startsWith(word.toLowerCase())) {
            candidates.add(new Candidate("cdc"));
        }
    }

    private void completeCommandName(String word, List<Candidate> candidates) {
        for (String cmd : handlers.keySet()) {
            if (cmd.startsWith(word.toLowerCase())) {
                candidates.add(new Candidate(cmd));
            }
        }
    }

    private void completeParam(String buffer, String word, List<Candidate> candidates) {
        String[] parts = buffer.trim().split("\\s+");
        String cmdName = parts[0].toLowerCase();
        CommandHandler h = handlers.get(cmdName);
        if (h == null) return;

        boolean hasTrailingSpace = buffer.endsWith(" ");
        int paramIdx = hasTrailingSpace ? parts.length - 1 : parts.length - 2;
        paramIdx = Math.max(0, paramIdx);
        List<Param> params = h.updateConfigAndGetParams();
        if (paramIdx >= params.size()) return;

        Param param = params.get(paramIdx);

        if (!param.getEnumChoices().isEmpty()) {
            for (Param.EnumChoice ec : param.getEnumChoices()) {
                if (ec.value.toLowerCase().startsWith(word.toLowerCase())) {
                    candidates.add(new Candidate(ec.value));
                }
            }
            return;
        }

        if (isHierarchicalRefType(param.getType())) {
            completeHierarchicalRef(param.getType(), word, candidates);
            return;
        }

        Collection<String> pool = poolForType(param.getType(), parts);
        if (pool != null) {
            for (String ref : pool) {
                if (ref.toLowerCase().startsWith(word.toLowerCase())) {
                    candidates.add(new Candidate(ref));
                }
            }
        } else if (param.getDefaultValue() != null && !param.getDefaultValue().isEmpty()) {
            candidates.add(new Candidate(param.getDefaultValue()));
        }
    }

    private static boolean isHierarchicalRefType(Param.Type type) {
        return type == Param.Type.DA_REF || type == Param.Type.DA_TARGET
            || type == Param.Type.REFERENCE
            || type == Param.Type.LN_REF
            || type == Param.Type.BRCB_REF
            || type == Param.Type.URCB_REF;
    }

    private void completeHierarchicalRef(Param.Type type, String word, List<Candidate> candidates) {
        Map<String, Map<String, Map<String, Map<String, Object>>>> h = ctx.getCachedHierarchy();
        if (h.isEmpty()) return;

        int slashIdx = word.indexOf('/');
        int dotIdx = word.indexOf('.');

        if (slashIdx < 0) {
            for (String ld : h.keySet()) {
                if (ld.toLowerCase().startsWith(word.toLowerCase())) {
                    candidates.add(new Candidate(ld));
                }
            }
        } else if (dotIdx < 0 && (type != Param.Type.LN_REF || slashIdx >= 0)) {
            String ldPart = word.substring(0, slashIdx);
            Map<String, Map<String, Map<String, Object>>> lnMap = h.get(ldPart);
            if (lnMap != null) {
                for (String ln : lnMap.keySet()) {
                    if (type == Param.Type.BRCB_REF) {
                        Map<String, Object> brcbs = lnMap.get(ln).get("BRCB");
                        if (brcbs == null || brcbs.isEmpty()) continue;
                    } else if (type == Param.Type.URCB_REF) {
                        Map<String, Object> urcbs = lnMap.get(ln).get("URCB");
                        if (urcbs == null || urcbs.isEmpty()) continue;
                    } else if (type == Param.Type.DS_REF) {
                        Map<String, Object> datasets = lnMap.get(ln).get("DATA_SET");
                        if (datasets == null || datasets.isEmpty()) continue;
                    }
                    String candidate = ldPart + "/" + ln;
                    if (candidate.toLowerCase().startsWith(word.toLowerCase())) {
                        candidates.add(new Candidate(candidate));
                    }
                }
            }
        } else if (dotIdx >= 0 && type != Param.Type.LN_REF) {
            String ldPart = word.substring(0, slashIdx);
            String rest = word.substring(slashIdx + 1);
            int dotInRest = rest.indexOf('.');
            String lnPart = rest.substring(0, dotInRest);

            Map<String, Map<String, Map<String, Object>>> lnMap = h.get(ldPart);
            if (lnMap != null) {
                Map<String, Map<String, Object>> acs = lnMap.get(lnPart);
                if (acs != null) {
                    Set<String> acsiClasses;
                    if (type == Param.Type.DS_REF) {
                        acsiClasses = Set.of("DATA_SET");
                    } else if (type == Param.Type.DA_REF || type == Param.Type.DA_TARGET) {
                        acsiClasses = Set.of("DATA_OBJECT");
                    } else if (type == Param.Type.BRCB_REF) {
                        acsiClasses = Set.of("BRCB");
                    } else if (type == Param.Type.URCB_REF) {
                        acsiClasses = Set.of("URCB");
                    } else {
                        acsiClasses = acs.keySet();
                    }
                    for (String ac : acsiClasses) {
                        Map<String, Object> members = acs.get(ac);
                        if (members != null) {
                            for (String member : members.keySet()) {
                                String candidate = ldPart + "/" + lnPart + "." + member;
                                if (candidate.toLowerCase().startsWith(word.toLowerCase())) {
                                    candidates.add(new Candidate(candidate));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Collection<String> poolForType(Param.Type type, String[] parts) {
        Map<String, Map<String, Map<String, Map<String, Object>>>> h = ctx.getCachedHierarchy();
        return switch (type) {
            case LD_NAME -> h.keySet();
            case LN_NAME -> {
                 Set<String> refs = new java.util.LinkedHashSet<>();
                 String ldName = parts.length > 1 ? parts[1] : "";
                 if (!ldName.isEmpty()) {
                     java.util.Map<String, java.util.Map<String, java.util.Map<String, Object>>> lnMap = h.get(ldName);
                     if (lnMap != null) refs.addAll(lnMap.keySet());
                 }
                 yield refs;
             }
            case LN_REF -> {
                Set<String> refs = new java.util.LinkedHashSet<>();
                for (String ld : h.keySet()) {
                    for (String ln : h.get(ld).keySet()) {
                        refs.add(ld + "/" + ln);
                    }
                }
                yield refs;
            }
            case DA_REF -> {
                Set<String> refs = new java.util.LinkedHashSet<>();
                for (String ld : h.keySet()) {
                    for (Map.Entry<String, Map<String, Map<String, Object>>> lnEntry : h.get(ld).entrySet()) {
                        Map<String, Object> das = lnEntry.getValue().get("DATA_OBJECT");
                        if (das != null) {
                            for (String da : das.keySet()) {
                                refs.add(ld + "/" + lnEntry.getKey() + "." + da);
                            }
                        }
                    }
                }
                yield refs;
            }
            case DS_REF -> {
                Set<String> refs = new java.util.LinkedHashSet<>();
                for (String ld : h.keySet()) {
                    for (Map.Entry<String, Map<String, Map<String, Object>>> lnEntry : h.get(ld).entrySet()) {
                        Map<String, Object> datasets = lnEntry.getValue().get("DATA_SET");
                        if (datasets != null) {
                            for (String ds : datasets.keySet()) {
                                refs.add(ld + "/" + lnEntry.getKey() + "." + ds);
                            }
                        }
                    }
                }
                yield refs;
            }
            case REFERENCE -> {
                Set<String> refs = new java.util.LinkedHashSet<>();
                for (String ld : h.keySet()) {
                    refs.add(ld);
                    for (String ln : h.get(ld).keySet()) {
                        String lnRef = ld + "/" + ln;
                        refs.add(lnRef);
                        Map<String, Map<String, Object>> acs = h.get(ld).get(ln);
                        for (String ac : acs.keySet()) {
                            for (String member : acs.get(ac).keySet()) {
                                refs.add(lnRef + "." + member);
                            }
                        }
                    }
                }
                yield refs;
            }
            case DA_TARGET -> {
                Set<String> refs = new java.util.LinkedHashSet<>();
                for (String ld : h.keySet()) {
                    for (Map.Entry<String, Map<String, Map<String, Object>>> lnEntry : h.get(ld).entrySet()) {
                        Map<String, Object> das = lnEntry.getValue().get("DATA_OBJECT");
                        if (das != null) {
                            for (String da : das.keySet()) {
                                refs.add(ld + "/" + lnEntry.getKey() + "." + da);
                            }
                        }
                    }
                }
                yield refs;
            }
            case DA_NAME -> {
                 Set<String> refs = new java.util.LinkedHashSet<>();
                 String ref = parts.length > 1 ? parts[1] : "";
                 int slashIdx = ref.indexOf('/');
                 int dotIdx = ref.indexOf('.');
                 if (slashIdx >= 0 && dotIdx >= 0) {
                     String ld = ref.substring(0, slashIdx);
                     int dotInRest = ref.indexOf('.', slashIdx + 1);
                     if (dotInRest >= 0) {
                         String ln = ref.substring(slashIdx + 1, dotInRest);
                         String doName = ref.substring(dotInRest + 1);
                         Map<String, Map<String, Map<String, Object>>> lnMap = h.get(ld);
                         if (lnMap != null) {
                             Map<String, Map<String, Object>> acs = lnMap.get(ln);
                             if (acs != null) {
                                 Map<String, Object> das = acs.get("DATA_OBJECT");
                                 if (das != null) {
                                     Object doMap = das.get(doName);
                                     if (doMap instanceof Map) {
                                         refs.addAll(((Map<String, ?>) doMap).keySet());
                                     }
                                 }
                             }
                         }
                     }
                 }
                 yield refs;
             }
            case DA_NAME_NOT_NULL -> {
                 Set<String> refs = new java.util.LinkedHashSet<>();
                 String ref = parts.length > 1 ? parts[1] : "";
                 int slashIdx = ref.indexOf('/');
                 int dotIdx = ref.indexOf('.');
                 if (slashIdx >= 0 && dotIdx >= 0) {
                     String ld = ref.substring(0, slashIdx);
                     int dotInRest = ref.indexOf('.', slashIdx + 1);
                     if (dotInRest >= 0) {
                         String ln = ref.substring(slashIdx + 1, dotInRest);
                         String doName = ref.substring(dotInRest + 1);
                         Map<String, Map<String, Map<String, Object>>> lnMap = h.get(ld);
                         if (lnMap != null) {
                             Map<String, Map<String, Object>> acs = lnMap.get(ln);
                             if (acs != null) {
                                 Map<String, Object> das = acs.get("DATA_OBJECT");
                                 if (das != null) {
                                     Object doMap = das.get(doName);
                                     if (doMap instanceof Map) {
                                         for (Map.Entry<String, ?> entry : ((Map<String, ?>) doMap).entrySet()) {
                                             if (entry.getValue() instanceof Map) {
                                                 Object val = ((Map<?, ?>) entry.getValue()).get("value");
                                                 if (val != null) {
                                                     refs.add(entry.getKey());
                                                 }
                                             }
                                         }
                                     }
                                 }
                             }
                         }
                     }
                 }
                 yield refs;
             }
            case BRCB_REF -> {
                Set<String> refs = new java.util.LinkedHashSet<>();
                for (String ld : h.keySet()) {
                    for (Map.Entry<String, Map<String, Map<String, Object>>> lnEntry : h.get(ld).entrySet()) {
                        Map<String, Object> brcbs = lnEntry.getValue().get("BRCB");
                        if (brcbs != null) {
                            for (String cb : brcbs.keySet()) {
                                refs.add(ld + "/" + lnEntry.getKey() + "." + cb);
                            }
                        }
                    }
                }
                yield refs;
            }
            case URCB_REF -> {
                Set<String> refs = new java.util.LinkedHashSet<>();
                for (String ld : h.keySet()) {
                    for (Map.Entry<String, Map<String, Map<String, Object>>> lnEntry : h.get(ld).entrySet()) {
                        Map<String, Object> urcbs = lnEntry.getValue().get("URCB");
                        if (urcbs != null) {
                            for (String cb : urcbs.keySet()) {
                                refs.add(ld + "/" + lnEntry.getKey() + "." + cb);
                            }
                        }
                    }
                }
                yield refs;
            }
            case ENUM, PLAIN -> null;
        };
    }
}
