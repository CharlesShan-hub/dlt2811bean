package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.cli.handler.command.HelpHandler;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.info.DataTypeInfo;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CmsCliCompleter implements Completer {

    private final Map<String, CommandHandler> handlers;
    private final CliContext ctx;
    private final Set<String> cachedLds;
    private final Set<String> cachedValues;
    private final Set<String> cachedRefs;

    public CmsCliCompleter(Map<String, CommandHandler> handlers, CliContext ctx,
                            Set<String> cachedLds, Set<String> cachedValues,
                            Set<String> cachedRefs) {
        this.handlers = handlers;
        this.ctx = ctx;
        this.cachedLds = cachedLds;
        this.cachedValues = cachedValues;
        this.cachedRefs = cachedRefs;
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

        int paramIdx = parts.length - 1;
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

        Collection<String> pool = poolForType(param.getType());
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

    private Collection<String> poolForType(Param.Type type) {
        return switch (type) {
            case LD_NAME -> cachedLds;
            case LN_REF -> ctx.getCachedLnRefs();
            case DA_REF -> cachedValues;
            case DA_TARGET -> ctx.getCachedDaRefs();
            case REFERENCE -> cachedRefs;
            case ENUM, PLAIN -> null;
        };
    }
}
