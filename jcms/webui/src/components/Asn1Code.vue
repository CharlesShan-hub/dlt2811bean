<template>
  <pre class="asn1-code"><code v-html="html"></code></pre>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  code: { type: String, default: '' },
})

/** ASN.1 关键字（结构关键词） */
const KEYWORDS = new Set([
  'DEFINITIONS', 'AUTOMATIC', 'EXPLICIT', 'IMPLICIT', 'BEGIN', 'END',
  'EXPORTS', 'IMPORTS', 'FROM', 'SEQUENCE', 'SET', 'OF', 'CHOICE',
  'ENUMERATED', 'INTEGER', 'REAL', 'BOOLEAN', 'BIT', 'STRING', 'OCTET',
  'NULL', 'OPTIONAL', 'DEFAULT', 'SIZE', 'CONTAINING', 'TRUE', 'FALSE',
  'MIN', 'MAX',
])

/** ASN.1 内置类型 / 标准类型名 */
const BUILTIN_TYPES = new Set([
  'VisibleString', 'VisibleString129', 'OCTETSTRING', 'OCTETSTRING64',
  'ServiceError', 'no-error',
])

function esc(s) {
  return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
}

// 注释(--) > 数字 > 标识符 > 字符串 > 单字符（含换行）
const TOKEN_RE = /(--[^\n]*)|(\d+)|([A-Za-z_][A-Za-z0-9_-]*)|("[^"]*")|([\s\S])/g

const html = computed(() => {
  let out = ''
  let m
  TOKEN_RE.lastIndex = 0
  while ((m = TOKEN_RE.exec(props.code))) {
    const [, comment, num, ident, str, other] = m
    if (comment) {
      out += `<span class="tok-comment">${esc(comment)}</span>`
    } else if (num) {
      out += `<span class="tok-number">${num}</span>`
    } else if (ident) {
      const cls = KEYWORDS.has(ident)
        ? 'tok-keyword'
        : BUILTIN_TYPES.has(ident) || /^[A-Z]/.test(ident)
          ? 'tok-type'
          : 'tok-ident'
      out += `<span class="${cls}">${esc(ident)}</span>`
    } else if (str) {
      out += `<span class="tok-string">${esc(str)}</span>`
    } else {
      out += esc(other)
    }
  }
  return out
})
</script>

<style scoped>
.asn1-code {
  margin: 0;
  background: var(--bg-primary);
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 14px;
  overflow-x: auto;
  color: var(--text-secondary);
}

/* 覆盖浏览器对 <code> 的默认 monospace/smaller 样式，与命令输出字体保持一致 */
.asn1-code code {
  font-family: var(--font-mono);
  font-size: 13px;
  line-height: 1.7;
}

.asn1-code :deep(.tok-comment) {
  color: var(--text-muted);
  font-style: italic;
}

.asn1-code :deep(.tok-keyword) {
  color: var(--green);
  font-weight: 600;
}

.asn1-code :deep(.tok-type) {
  color: var(--accent);
}

.asn1-code :deep(.tok-number) {
  color: var(--yellow);
}

.asn1-code :deep(.tok-string) {
  color: var(--green);
}

.asn1-code :deep(.tok-ident) {
  color: var(--text-secondary);
}
</style>
