#include "asn1_lexer.h"
#include "asn1_parser.h"
#include "c_gen.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#ifdef _WIN32
#include <direct.h>
#define mkdir_(p) _mkdir(p)
#else
#include <sys/stat.h>
#define mkdir_(p) mkdir(p, 0755)
#endif

/* Create directory and parent directories if they don't exist */
static void ensure_dir(const char *path) {
    char tmp[512];
    strncpy(tmp, path, sizeof(tmp) - 1);
    tmp[sizeof(tmp) - 1] = '\0';
    for (char *p = tmp + 1; *p; p++) {
        if (*p == '/' || *p == '\\') {
            *p = '\0';
            mkdir_(tmp);
            *p = '\\';
        }
    }
    mkdir_(tmp);
}

static char *read_file(const char *path) {
    FILE *f = fopen(path, "rb");
    if (!f) { fprintf(stderr, "error: cannot open %s\n", path); return NULL; }
    fseek(f, 0, SEEK_END);
    long len = ftell(f);
    rewind(f);
    char *buf = malloc((size_t)len + 1);
    if (!buf) { fclose(f); return NULL; }
    fread(buf, 1, (size_t)len, f);
    buf[len] = '\0';
    fclose(f);
    return buf;
}

static void base_name(const char *path, char *out, int maxlen) {
    const char *p = strrchr(path, '/');
    if (!p) p = strrchr(path, '\\');
    if (!p) p = path; else p++;
    const char *dot = strchr(p, '.');
    if (!dot) dot = p + strlen(p);
    int len = (int)(dot - p);
    if (len >= maxlen) len = maxlen - 1;
    strncpy(out, p, len);
    out[len] = '\0';
    /* replace hyphens */
    for (int i = 0; out[i]; i++) if (out[i] == '-') out[i] = '_';
}

int main(int argc, char **argv) {
    if (argc < 2) {
        fprintf(stderr, "usage: cmsgen <input.asn> [output_dir]\n");
        return 1;
    }

    const char *input_path = argv[1];
    const char *output_dir = argv[2] ? argv[2] : ".";
    fprintf(stderr, "reading %s\n", input_path);

    char *input = read_file(input_path);
    if (!input) return 1;

    char modname[128];
    base_name(input_path, modname, sizeof(modname));

    /* Parse all modules in the file */
    parser_t parser;
    parser_init(&parser, input);

    asn1_module_t *mod = calloc(1, sizeof(asn1_module_t));
    int cap = 256;
    mod->defs = calloc(cap, sizeof(asn1_def_t *));
    mod->def_count = 0;

    int module_count = 0;
    while (parser.cur.kind != TOK_EOF) {
        asn1_module_t *m = parser_parse_module(&parser);
        if (parser.error) {
            fprintf(stderr, "parse error: %s\n", parser.error_msg);
            asn1_module_free(mod);
            free(input);
            return 1;
        }
        if (!m || m->def_count == 0) {
            asn1_module_free(m);
            continue;
        }

        module_count++;
        fprintf(stderr, "  module %d: %d definitions, next token: '%s' (kind=%d)\n",
                module_count, m->def_count, parser.cur.text, parser.cur.kind);

        for (int i = 0; i < m->def_count; i++) {
            if (mod->def_count >= cap) {
                cap *= 2;
                mod->defs = realloc(mod->defs, cap * sizeof(asn1_def_t *));
            }
            mod->defs[mod->def_count++] = m->defs[i];
        }
        m->def_count = 0;  /* prevent double-free */
        asn1_module_free(m);
    }

    fprintf(stderr, "parsed %d definitions from %d modules\n", mod->def_count, module_count);

    /* Generate header */
    char h_path[512], c_path[512];
    snprintf(h_path, sizeof(h_path), "%s/gen_%s.h", output_dir, modname);
    snprintf(c_path, sizeof(c_path), "%s/gen_%s.c", output_dir, modname);

    ensure_dir(output_dir);
    FILE *fh = fopen(h_path, "w");
    if (!fh) { fprintf(stderr, "cannot write %s\n", h_path); return 1; }
    gen_header(fh, mod, modname);
    fprintf(stderr, "wrote %s\n", h_path);
    fclose(fh);

    FILE *fc = fopen(c_path, "w");
    if (!fc) { fprintf(stderr, "error: cannot open %s\n", c_path); return 1; }
    gen_source(fc, mod, modname);
    fprintf(stderr, "wrote %s\n", c_path);
    fclose(fc);

    asn1_module_free(mod);
    free(input);
    return 0;
}
