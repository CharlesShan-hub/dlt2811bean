#include "c_gen.h"
#include <stdlib.h>
#include <string.h>

/* ---- helpers ---- */

static const char *def_kind_to_c_type(def_kind_t k) {
    switch (k) {
        case DEF_BOOLEAN:     return "int";
        case DEF_INTEGER:     return "int64_t";
        case DEF_REAL:        return "double";
        case DEF_BIT_STRING:  return "uint8_t *";
        case DEF_OCTET_STRING:return "uint8_t *";
        case DEF_VISIBLE_STRING:
        case DEF_UTF8STRING:  return "char *";
        default: return "";
    }
}

static int is_primitive(def_kind_t k) {
    return k == DEF_BOOLEAN || k == DEF_INTEGER || k == DEF_REAL
        || k == DEF_BIT_STRING || k == DEF_OCTET_STRING
        || k == DEF_VISIBLE_STRING || k == DEF_UTF8STRING;
}

static void write_indent(FILE *out, int depth) {
    for (int i = 0; i < depth; i++) fprintf(out, "    ");
}

/* safe_name writes to a caller-provided buffer (no static buffer) */
static void safe_name_buf(const char *name, char *out, int maxlen) {
    int j = 0;
    for (int i = 0; name[i] && j < maxlen - 1; i++) {
        if (name[i] == '-') out[j++] = '_';
        else out[j++] = name[i];
    }
    out[j] = '\0';
}

/* Returns the C type string for a type_name. Writes to caller-provided buf.
 * Since all types use typedef, just return the bare type name (no struct/enum prefix). */
static const char *type_ref_to_c_type_r(asn1_module_t *mod, const char *type_name,
                                        char *buf, int maxlen) {
    asn1_def_t *d = asn1_find_def(mod, type_name);
    if (d) {
        if (is_primitive(d->kind)) {
            return def_kind_to_c_type(d->kind);
        }
        /* For SEQUENCE/CHOICE/ENUMERATED: bare typedef name */
    }
    safe_name_buf(type_name, buf, maxlen);
    return buf;
}

/* get C type string for a type expression — uses caller-provided buffer */
static void write_c_type_r(FILE *out, asn1_module_t *mod, asn1_type_t *t, char *tmp, int tmpmax) {
    switch (t->kind) {
        case DEF_BOOLEAN:     fprintf(out, "int"); break;
        case DEF_INTEGER:     fprintf(out, "int64_t"); break;
        case DEF_REAL:        fprintf(out, "double"); break;
        case DEF_BIT_STRING:  fprintf(out, "uint8_t *"); break;
        case DEF_OCTET_STRING:fprintf(out, "uint8_t *"); break;
        case DEF_VISIBLE_STRING:
        case DEF_UTF8STRING:  fprintf(out, "char *"); break;
        case DEF_ENUMERATED:  fprintf(out, "int"); break;
        case DEF_TYPE_REF:    fprintf(out, "%s", type_ref_to_c_type_r(mod, t->type_name, tmp, tmpmax)); break;
        case DEF_SEQUENCE:
        case DEF_CHOICE:      fprintf(out, "%s", type_ref_to_c_type_r(mod, t->type_name, tmp, tmpmax)); break;
    }
}

/* Forward declaration */
static asn1_def_t *resolve_ref(asn1_module_t *mod, const char *name);

/* Check if a field type is a variable-length OCTET STRING or BIT STRING (needs _len field) */
static int needs_len_field(asn1_module_t *mod, asn1_type_t *t) {
    if (!t) return 0;
    if (t->kind == DEF_OCTET_STRING || t->kind == DEF_BIT_STRING) {
        return !(t->has_lower && t->lower_bound == t->upper_bound);
    }
    if (t->kind == DEF_TYPE_REF) {
        asn1_def_t *d = resolve_ref(mod, t->type_name);
        if (d && (d->kind == DEF_OCTET_STRING || d->kind == DEF_BIT_STRING)) {
            return !(d->has_lower && d->lower_bound == d->upper_bound);
        }
    }
    return 0;
}

/* ---- header generation ---- */

static void gen_header_for_def(FILE *out, asn1_module_t *mod, asn1_def_t *d) {
    char sn[256];
    safe_name_buf(d->name, sn, sizeof(sn));

    switch (d->kind) {
        case DEF_ENUMERATED: {
            fprintf(out, "typedef enum %s {\n", sn);
            for (int i = 0; i < d->enum_count; i++) {
                fprintf(out, "    %s_%s = %lld", sn, d->enum_values[i].name, (long long)d->enum_values[i].value);
                if (i < d->enum_count - 1) fprintf(out, ",");
                fprintf(out, "\n");
            }
            fprintf(out, "} %s;\n\n", sn);
            break;
        }
        case DEF_SEQUENCE: {
            fprintf(out, "typedef struct %s {\n", sn);
            for (int i = 0; i < d->field_count; i++) {
                asn1_field_t *f = d->fields[i];
                if (!f || !f->type) continue;
                /* SEQUENCE OF — generate pointer + count */
                if (f->type->is_sequence_of) {
                    asn1_type_t *elem = f->type->fields[0]->type;
                    char tmp[256];
                    char fn[256];
                    safe_name_buf(f->name, fn, sizeof(fn));
                    fprintf(out, "    ");
                    write_c_type_r(out, mod, elem, tmp, sizeof(tmp));
                    fprintf(out, " *%s", fn);
                    if (f->is_optional) fprintf(out, ";\n    int _has_%s", fn);
                    fprintf(out, ";\n");
                    fprintf(out, "    int %s_count;\n", fn);
                    continue;
                }
                char tmp[256];
                fprintf(out, "    ");
                write_c_type_r(out, mod, f->type, tmp, sizeof(tmp));
                char fn[256];
                safe_name_buf(f->name, fn, sizeof(fn));
                fprintf(out, " %s", fn);
                if (f->is_optional) fprintf(out, ";\n    int _has_%s", fn);
                fprintf(out, ";\n");
                if (needs_len_field(mod, f->type)) {
                    fprintf(out, "    int %s_len;\n", fn);
                }
            }
            fprintf(out, "} %s;\n\n", sn);
            break;
        }
        case DEF_CHOICE: {
            fprintf(out, "typedef struct %s {\n", sn);
            fprintf(out, "    int _choice;\n");
            fprintf(out, "    union {\n");
            for (int i = 0; i < d->field_count; i++) {
                asn1_field_t *f = d->fields[i];
                if (!f || !f->type) continue;
                /* Skip SEQUENCE OF fields in CHOICE */
                if (f->type->is_sequence_of) continue;
                char tmp[256], fn[256];
                safe_name_buf(f->name, fn, sizeof(fn));
                fprintf(out, "        ");
                write_c_type_r(out, mod, f->type, tmp, sizeof(tmp));
                fprintf(out, " %s;\n", fn);
            }
            fprintf(out, "    } u;\n");
            fprintf(out, "} %s;\n\n", sn);
            break;
        }
        case DEF_INTEGER: {
            fprintf(out, "typedef int64_t %s;\n\n", sn);
            break;
        }
        case DEF_BOOLEAN: {
            fprintf(out, "typedef int %s;\n\n", sn);
            break;
        }
        case DEF_BIT_STRING: {
            if (d->has_lower && d->lower_bound == d->upper_bound)
                fprintf(out, "typedef uint8_t %s[%lld];\n\n", sn, (long long)((d->upper_bound + 7) / 8));
            else
                fprintf(out, "typedef struct { uint8_t *data; int len; } %s;\n\n", sn);
            break;
        }
        case DEF_OCTET_STRING: {
            if (d->has_lower && d->lower_bound == d->upper_bound)
                fprintf(out, "typedef uint8_t %s[%lld];\n\n", sn, (long long)d->upper_bound);
            else
                fprintf(out, "typedef struct { uint8_t *data; int len; } %s;\n\n", sn);
            break;
        }
        case DEF_VISIBLE_STRING:
        case DEF_UTF8STRING: {
            fprintf(out, "typedef char *%s;\n\n", sn);
            break;
        }
        case DEF_REAL: {
            fprintf(out, "typedef double %s;\n\n", sn);
            break;
        }
        case DEF_TYPE_REF: {
            char tmp[256];
            fprintf(out, "typedef %s %s;\n\n", type_ref_to_c_type_r(mod, d->ref_name, tmp, sizeof(tmp)), sn);
            break;
        }
    }
}

static void gen_encode_decode_decls(FILE *out, asn1_module_t *mod, asn1_def_t *d) {
    char sn[256];
    safe_name_buf(d->name, sn, sizeof(sn));
    if (d->kind == DEF_SEQUENCE || d->kind == DEF_CHOICE) {
        fprintf(out, "int encode_%s(per_stream_t *s, const %s *v);\n", sn, sn);
        fprintf(out, "int decode_%s(per_stream_t *s, %s *v);\n", sn, sn);
    } else if (d->kind == DEF_ENUMERATED) {
        fprintf(out, "int encode_%s(per_stream_t *s, int v);\n", sn);
        fprintf(out, "int decode_%s(per_stream_t *s, int *v);\n", sn);
    }
}

/* Check if a definition directly references another definition by name in its fields */
static int def_depends_on(asn1_def_t *d, const char *ref_name) {
    /* TYPE_REF depends on the type it references */
    if (d->kind == DEF_TYPE_REF && strcmp(d->ref_name, ref_name) == 0)
        return 1;
    /* SEQUENCE/CHOICE may reference other types in their fields */
    for (int i = 0; i < d->field_count; i++) {
        asn1_field_t *f = d->fields[i];
        if (!f || !f->type) continue;
        if (f->type->is_sequence_of) {
            asn1_type_t *elem = f->type->fields[0]->type;
            if (elem && elem->kind == DEF_TYPE_REF && strcmp(elem->type_name, ref_name) == 0)
                return 1;
            continue;
        }
        if (f->type->kind == DEF_TYPE_REF && strcmp(f->type->type_name, ref_name) == 0)
            return 1;
    }
    return 0;
}

/* Topological sort of definitions so that dependencies come first.
 * Uses Kahn's algorithm. Only SEQUENCE, CHOICE, and TYPE_REF are sorted;
 * primitive types (INTEGER, BOOLEAN, etc.) have no dependencies and are placed at the end. */
static void topo_sort_defs(asn1_module_t *mod, int *order) {
    int n = mod->def_count;
    int *indeg = calloc(n, sizeof(int));
    int *queue = malloc(n * sizeof(int));
    int qh = 0, qt = 0;
    int written = 0;

    /* Mark all as unvisited */
    for (int i = 0; i < n; i++) order[i] = -1;

    /* Compute in-degree for all non-primitive defs */
    for (int i = 0; i < n; i++) {
        asn1_def_t *d = mod->defs[i];
        if (d->kind != DEF_SEQUENCE && d->kind != DEF_CHOICE && d->kind != DEF_TYPE_REF) continue;
        indeg[i] = 0;
        for (int j = 0; j < n; j++) {
            if (i == j) continue;
            asn1_def_t *other = mod->defs[j];
            if (other->kind != DEF_SEQUENCE && other->kind != DEF_CHOICE && other->kind != DEF_TYPE_REF) continue;
            if (def_depends_on(d, other->name)) {
                indeg[i]++;
            }
        }
        if (indeg[i] == 0) {
            queue[qt++] = i;
        }
    }

    while (qh < qt) {
        int idx = queue[qh++];
        order[written++] = idx;
        /* Decrease in-degree of all defs that depend on this one */
        for (int j = 0; j < n; j++) {
            asn1_def_t *other = mod->defs[j];
            if (other->kind != DEF_SEQUENCE && other->kind != DEF_CHOICE && other->kind != DEF_TYPE_REF) continue;
            if (def_depends_on(other, mod->defs[idx]->name)) {
                indeg[j]--;
                if (indeg[j] == 0) {
                    int already = 0;
                    for (int k = qh; k < qt; k++) {
                        if (queue[k] == j) { already = 1; break; }
                    }
                    if (!already) queue[qt++] = j;
                }
            }
        }
    }

    /* Append remaining sorted-type defs (cycles or unvisited) */
    for (int i = 0; i < n; i++) {
        asn1_def_t *d = mod->defs[i];
        if (d->kind != DEF_SEQUENCE && d->kind != DEF_CHOICE && d->kind != DEF_TYPE_REF) continue;
        int found = 0;
        for (int j = 0; j < written; j++) {
            if (order[j] == i) { found = 1; break; }
        }
        if (!found) order[written++] = i;
    }

    /* Append primitive defs (INTEGER, BOOLEAN, etc.) */
    for (int i = 0; i < n; i++) {
        asn1_def_t *d = mod->defs[i];
        if (d->kind == DEF_SEQUENCE || d->kind == DEF_CHOICE || d->kind == DEF_TYPE_REF) continue;
        order[written++] = i;
    }

    free(indeg);
    free(queue);
}

void gen_header(FILE *out, asn1_module_t *mod, const char *module_name) {
    fprintf(out, "#ifndef GEN_%s_H\n", module_name);
    fprintf(out, "#define GEN_%s_H\n\n", module_name);
    fprintf(out, "#include <stdint.h>\n");
    fprintf(out, "#include \"cmsper/cmsper.h\"\n\n");

    /* Forward declarations for SEQUENCE/CHOICE types (needed for cross-refs) */
    for (int i = 0; i < mod->def_count; i++) {
        asn1_def_t *d = mod->defs[i];
        char sn[256];
        safe_name_buf(d->name, sn, sizeof(sn));
        if (d->kind == DEF_SEQUENCE || d->kind == DEF_CHOICE) {
            fprintf(out, "typedef struct %s %s;\n", sn, sn);
        }
    }
    fprintf(out, "\n");

    /* Topological sort to satisfy dependencies */
    int *order = malloc(mod->def_count * sizeof(int));
    topo_sort_defs(mod, order);

    /* Output enums first (they have no dependencies), then everything else in topo order */
    for (int phase = 0; phase < 2; phase++) {
        for (int i = 0; i < mod->def_count; i++) {
            asn1_def_t *d = mod->defs[order[i]];
            if (phase == 0 && d->kind != DEF_ENUMERATED) continue;
            if (phase == 1 && d->kind == DEF_ENUMERATED) continue;
            gen_header_for_def(out, mod, d);
        }
    }

    free(order);

    for (int i = 0; i < mod->def_count; i++) {
        gen_encode_decode_decls(out, mod, mod->defs[i]);
    }

    fprintf(out, "#endif\n");
}

/* ---- source generation ---- */

/* Resolve TYPE_REF chain to the actual definition */
static asn1_def_t *resolve_ref(asn1_module_t *mod, const char *name) {
    asn1_def_t *d = asn1_find_def(mod, name);
    if (!d) return NULL;
    if (d->kind == DEF_TYPE_REF) return resolve_ref(mod, d->ref_name);
    return d;
}

static void gen_encode_field(FILE *out, asn1_module_t *mod, asn1_field_t *f, int depth) {
    write_indent(out, depth);
    if (!f || !f->type) { fprintf(out, "/* skip null field */\n"); return; }
    char fn[256];
    safe_name_buf(f->name, fn, sizeof(fn));

    if (f->is_optional) {
        fprintf(out, "if (v->_has_%s) {\n", fn);
        depth++;
        write_indent(out, depth);
        fprintf(out, "per_encode_boolean(s, 1); /* present */\n");
    }

    /* SEQUENCE OF — encode length + each element */
    if (f->type->is_sequence_of) {
        asn1_type_t *elem = f->type->fields[0]->type;
        char ef[256];
        safe_name_buf(elem->type_name, ef, sizeof(ef));
        /* Check if element type is a struct/choice type */
        asn1_def_t *elem_def = resolve_ref(mod, elem->type_name);
        fprintf(out, "per_encode_length(s, v->%s_count);\n", fn);
        fprintf(out, "    for (int _i = 0; _i < v->%s_count; _i++) {\n", fn);
        if (elem_def && (elem_def->kind == DEF_SEQUENCE || elem_def->kind == DEF_CHOICE)) {
            fprintf(out, "        encode_%s(s, &v->%s[_i]);\n", ef, fn);
        } else {
            fprintf(out, "        /* TODO: encode %s element */\n", ef);
        }
        fprintf(out, "    }\n");
        goto end_encode_field;
    }

    switch (f->type->kind) {
        case DEF_BOOLEAN:
            fprintf(out, "per_encode_boolean(s, v->%s);\n", fn);
            break;
        case DEF_TYPE_REF: {
            asn1_def_t *ref = resolve_ref(mod, f->type->type_name);
            if (ref) {
                if (ref->kind == DEF_ENUMERATED || ref->kind == DEF_INTEGER || ref->kind == DEF_BOOLEAN) {
                    fprintf(out, "per_encode_constrained_int(s, v->%s", fn);
                    if (ref->kind == DEF_ENUMERATED)
                        fprintf(out, ", 0, %d", ref->enum_count - 1);
                    else if (ref->has_lower)
                        fprintf(out, ", %lld, %lld", (long long)ref->lower_bound, (long long)ref->upper_bound);
                    else
                        fprintf(out, ", 0, 255");
                    fprintf(out, ");\n");
                } else if (ref->kind == DEF_VISIBLE_STRING) {
                    fprintf(out, "per_encode_visible_string(s, v->%s, %lld);\n",
                            fn, (long long)(ref->has_upper ? ref->upper_bound : 255));
                } else if (ref->kind == DEF_OCTET_STRING) {
                    if (ref->has_lower && ref->lower_bound == ref->upper_bound)
                        fprintf(out, "per_encode_octet_string_fixed(s, v->%s, %lld);\n",
                                fn, (long long)ref->upper_bound);
                    else
                        fprintf(out, "per_encode_octet_string(s, v->%s, v->%s_len, %lld);\n",
                                fn, fn, (long long)(ref->has_upper ? ref->upper_bound : 65535));
                } else if (ref->kind == DEF_BIT_STRING) {
                    fprintf(out, "per_encode_bit_string_fixed(s, v->%s, %lld);\n",
                            fn, (long long)(ref->has_upper ? ref->upper_bound : 16));
                } else if (ref->kind == DEF_SEQUENCE) {
                    char rn[256];
                    safe_name_buf(ref->name, rn, sizeof(rn));
                    fprintf(out, "encode_%s(s, &v->%s);\n", rn, fn);
                } else if (ref->kind == DEF_CHOICE) {
                    fprintf(out, "per_encode_small_non_negative(s, v->%s._choice);\n", fn);
                }
            }
            break;
        }
        case DEF_VISIBLE_STRING:
            fprintf(out, "per_encode_visible_string(s, v->%s, %lld);\n",
                    fn, (long long)(f->type->has_upper ? f->type->upper_bound : 255));
            break;
        case DEF_OCTET_STRING:
            if (f->type->has_lower && f->type->lower_bound == f->type->upper_bound)
                fprintf(out, "per_encode_octet_string_fixed(s, v->%s, %lld);\n",
                        fn, (long long)f->type->upper_bound);
            else
                fprintf(out, "per_encode_octet_string(s, v->%s, v->%s_len, %lld);\n",
                        fn, fn, (long long)(f->type->has_upper ? f->type->upper_bound : 65535));
            break;
        default:
            fprintf(out, "/* TODO: encode %s */\n", fn);
    }

end_encode_field:
    if (f->is_optional) {
        depth--;
        write_indent(out, depth);
        fprintf(out, "} else {\n");
        write_indent(out, depth + 1);
        fprintf(out, "per_encode_boolean(s, 0); /* absent */\n");
        write_indent(out, depth);
        fprintf(out, "}\n");
    }
}

static void gen_decode_field(FILE *out, asn1_module_t *mod, asn1_field_t *f, int depth) {
    write_indent(out, depth);
    if (!f || !f->type) { fprintf(out, "/* skip null field */\n"); return; }
    char fn[256];
    safe_name_buf(f->name, fn, sizeof(fn));

    if (f->is_optional) {
        fprintf(out, "{\n");
        write_indent(out, depth + 1);
        fprintf(out, "bool _b;\n");
        write_indent(out, depth + 1);
        fprintf(out, "per_decode_boolean(s, &_b);\n");
        write_indent(out, depth + 1);
        fprintf(out, "v->_has_%s = _b ? 1 : 0;\n", fn);
        write_indent(out, depth);
        fprintf(out, "}\n");
        write_indent(out, depth);
        fprintf(out, "if (v->_has_%s) {\n", fn);
        depth++;
    }

    /* SEQUENCE OF — decode length + each element */
    if (f->type->is_sequence_of) {
        asn1_def_t *elem_def = resolve_ref(mod, f->type->fields[0]->type->type_name);
        if (!elem_def || (elem_def->kind != DEF_SEQUENCE && elem_def->kind != DEF_CHOICE)) {
            fprintf(out, "/* TODO: decode SEQUENCE OF %s */\n", f->type->fields[0]->type->type_name);
            goto end_decode_field;
        }
        asn1_type_t *elem = f->type->fields[0]->type;
        char ef[256];
        safe_name_buf(elem->type_name, ef, sizeof(ef));
        fprintf(out, "{\n");
        write_indent(out, depth + 1);
        fprintf(out, "uint32_t _count;\n");
        write_indent(out, depth + 1);
        fprintf(out, "per_decode_length(s, &_count);\n");
        write_indent(out, depth + 1);
        fprintf(out, "v->%s_count = (int)_count;\n", fn);
        write_indent(out, depth + 1);
        fprintf(out, "v->%s = calloc(_count, sizeof(*v->%s));\n", fn, fn);
        write_indent(out, depth + 1);
        fprintf(out, "for (uint32_t _i = 0; _i < _count; _i++) {\n");
        write_indent(out, depth + 2);
        fprintf(out, "decode_%s(s, &v->%s[_i]);\n", ef, fn);
        write_indent(out, depth + 1);
        fprintf(out, "}\n");
        write_indent(out, depth);
        fprintf(out, "}\n");
        goto end_decode_field;
    }

    switch (f->type->kind) {
        case DEF_BOOLEAN:
            fprintf(out, "{\n");
            write_indent(out, depth + 1);
            fprintf(out, "bool _b;\n");
            write_indent(out, depth + 1);
            fprintf(out, "per_decode_boolean(s, &_b);\n");
            write_indent(out, depth + 1);
            fprintf(out, "v->%s = _b ? 1 : 0;\n", fn);
            write_indent(out, depth);
            fprintf(out, "}\n");
            break;
        case DEF_INTEGER:
            fprintf(out, "per_decode_constrained_int(s, &v->%s, %lld, %lld);\n",
                    fn, (long long)f->type->lower_bound, (long long)f->type->upper_bound);
            break;
        case DEF_TYPE_REF: {
            asn1_def_t *ref = resolve_ref(mod, f->type->type_name);
            if (ref) {
                if (ref->kind == DEF_ENUMERATED || ref->kind == DEF_INTEGER || ref->kind == DEF_BOOLEAN) {
                    fprintf(out, "{\n");
                    write_indent(out, depth + 1);
                    fprintf(out, "int64_t _tmp;\n");
                    write_indent(out, depth + 1);
                    fprintf(out, "per_decode_constrained_int(s, &_tmp");
                    if (ref->kind == DEF_ENUMERATED)
                        fprintf(out, ", 0, %d", ref->enum_count - 1);
                    else if (ref->has_lower)
                        fprintf(out, ", %lld, %lld", (long long)ref->lower_bound, (long long)ref->upper_bound);
                    else
                        fprintf(out, ", 0, 255");
                    fprintf(out, ");\n");
                    write_indent(out, depth + 1);
                    fprintf(out, "v->%s = (int)_tmp;\n", fn);
                    write_indent(out, depth);
                    fprintf(out, "}\n");
                } else if (ref->kind == DEF_VISIBLE_STRING) {
                    fprintf(out, "{\n");
                    write_indent(out, depth + 1);
                    fprintf(out, "char _buf[%lld];\n", (long long)(ref->has_upper ? ref->upper_bound + 1 : 256));
                    write_indent(out, depth + 1);
                    fprintf(out, "per_decode_visible_string(s, _buf, %lld);\n",
                            (long long)(ref->has_upper ? ref->upper_bound : 255));
                    write_indent(out, depth + 1);
                    fprintf(out, "v->%s = strdup(_buf);\n", fn);
                    write_indent(out, depth);
                    fprintf(out, "}\n");
                } else if (ref->kind == DEF_SEQUENCE) {
                    char rn[256];
                    safe_name_buf(ref->name, rn, sizeof(rn));
                    fprintf(out, "decode_%s(s, &v->%s);\n", rn, fn);
                }
            }
            break;
        }
        case DEF_VISIBLE_STRING: {
            fprintf(out, "{\n");
            write_indent(out, depth + 1);
            fprintf(out, "char _buf[%lld];\n", (long long)(f->type->has_upper ? f->type->upper_bound + 1 : 256));
            write_indent(out, depth + 1);
            fprintf(out, "per_decode_visible_string(s, _buf, %lld);\n",
                    (long long)(f->type->has_upper ? f->type->upper_bound : 255));
            write_indent(out, depth + 1);
            fprintf(out, "v->%s = strdup(_buf);\n", fn);
            write_indent(out, depth);
            fprintf(out, "}\n");
            break;
        }
        case DEF_UTF8STRING: {
            fprintf(out, "/* decode UTF8String %s */\n", fn);
            break;
        }
        default:
            fprintf(out, "/* TODO: decode %s */\n", fn);
    }

end_decode_field:
    if (f->is_optional) {
        depth--;
        write_indent(out, depth);
        fprintf(out, "}\n");
    }
}

static void gen_encode_for_def(FILE *out, asn1_module_t *mod, asn1_def_t *d) {
    char sn[256];
    safe_name_buf(d->name, sn, sizeof(sn));

    if (d->kind == DEF_SEQUENCE) {
        fprintf(out, "int encode_%s(per_stream_t *s, const %s *v) {\n", sn, sn);
        for (int i = 0; i < d->field_count; i++) {
            gen_encode_field(out, mod, d->fields[i], 1);
        }
        fprintf(out, "    return 0;\n}\n\n");
    } else if (d->kind == DEF_CHOICE) {
        fprintf(out, "int encode_%s(per_stream_t *s, const %s *v) {\n", sn, sn);
        fprintf(out, "    per_encode_small_non_negative(s, v->_choice);\n");
        fprintf(out, "    switch (v->_choice) {\n");
        for (int i = 0; i < d->field_count; i++) {
            fprintf(out, "        case %d: break; /* %s */\n", i, d->fields[i]->name);
        }
        fprintf(out, "    }\n");
        fprintf(out, "    return 0;\n}\n\n");
    } else if (d->kind == DEF_ENUMERATED) {
        fprintf(out, "int encode_%s(per_stream_t *s, int v) {\n", sn);
        fprintf(out, "    return per_encode_constrained_int(s, v, 0, %d);\n", d->enum_count - 1);
        fprintf(out, "}\n\n");
        fprintf(out, "int decode_%s(per_stream_t *s, int *v) {\n", sn);
        fprintf(out, "    int64_t _tmp;\n");
        fprintf(out, "    int err = per_decode_constrained_int(s, &_tmp, 0, %d);\n", d->enum_count - 1);
        fprintf(out, "    if (err) return err;\n");
        fprintf(out, "    *v = (int)_tmp;\n");
        fprintf(out, "    return 0;\n}\n\n");
    }
}

static void gen_decode_for_def(FILE *out, asn1_module_t *mod, asn1_def_t *d) {
    char sn[256];
    safe_name_buf(d->name, sn, sizeof(sn));

    if (d->kind == DEF_SEQUENCE) {
        fprintf(out, "int decode_%s(per_stream_t *s, %s *v) {\n", sn, sn);
        for (int i = 0; i < d->field_count; i++) {
            gen_decode_field(out, mod, d->fields[i], 1);
        }
        fprintf(out, "    return 0;\n}\n\n");
    } else if (d->kind == DEF_CHOICE) {
        fprintf(out, "int decode_%s(per_stream_t *s, %s *v) {\n", sn, sn);
        fprintf(out, "    uint32_t _idx;\n");
        fprintf(out, "    per_decode_small_non_negative(s, &_idx);\n");
        fprintf(out, "    v->_choice = (int)_idx;\n");
        fprintf(out, "    switch (v->_choice) {\n");
        for (int i = 0; i < d->field_count; i++) {
            fprintf(out, "        case %d: break; /* %s */\n", i, d->fields[i]->name);
        }
        fprintf(out, "    }\n");
        fprintf(out, "    return 0;\n}\n\n");
    }
}

void gen_source(FILE *out, asn1_module_t *mod, const char *module_name) {
    fprintf(out, "#include \"gen_%s.h\"\n", module_name);
    fprintf(out, "#include <stdlib.h>\n");
    fprintf(out, "#include <string.h>\n\n");

    for (int i = 0; i < mod->def_count; i++) {
        gen_encode_for_def(out, mod, mod->defs[i]);
        gen_decode_for_def(out, mod, mod->defs[i]);
    }
}
