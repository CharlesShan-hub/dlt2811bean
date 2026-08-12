<template>
  <UiCard title="命令与返回" icon="🔄" fill class="cmd-result-card">
    <template #header>
    </template>
    <div class="cmd-preview">
      <code class="preview-line">
        <template v-if="editing">
          <input
            ref="editInput"
            v-model="editValue"
            class="cmd-edit-input"
            @keydown.enter="confirmEdit"
            @keydown.escape="cancelEdit"
            @blur="confirmEdit"
          />
        </template>
        <template v-else>
          <span class="preview-text" v-html="highlightedCmd" @dblclick="startEdit" title="双击编辑命令"></span>
        </template>
        <button type="button" class="glass copy-icon-btn copy-inline" :title="copied ? '已复制' : '复制命令'" @click="copyCmd" v-html="copied ? checkIcon : clipIcon"></button>
      </code>
    </div>
    <div v-if="result" class="glass term-title-bar">
      <span class="term-time">{{ result.time }}</span>
      <span class="term-cmd"><span class="dollar">$</span> <span v-html="highlightedResultCmd"></span></span>
      <span class="term-title-actions">
        <button
          v-if="formattedJson"
          type="button"
          class="glass view-toggle-btn"
          :class="{ active: jsonFormat }"
          :title="jsonFormat ? '切换为原始输出' : '切换为格式化 JSON'"
          @click="$emit('update:jsonFormat', !jsonFormat)"
        ><span v-html="jsonFormat ? jsonIcon : termIcon"></span></button>
        <button type="button" class="glass copy-icon-btn copy-inline" :title="copiedCmdResult ? '已复制' : '复制命令'" @click="copyCmdResult" v-html="copiedCmdResult ? checkIcon : clipIcon"></button>
      </span>
    </div>
    <div class="cmd-result-scroll">
      <div v-if="!result" class="empty">执行后在此显示返回结果。</div>
      <div v-else-if="jsonFormat && formattedJson" class="json-window">
        <div class="json-body">
          <pre class="json-pre"><code v-html="formattedJson"></code></pre>
          <button type="button" class="glass copy-icon-btn copy-inline copy-out-json" :title="copiedOutput ? '已复制' : '复制输出'" @click="copyOutput" v-html="copiedOutput ? checkIcon : clipIcon"></button>
        </div>
      </div>
      <div v-else class="term-window">
        <div class="term-body">
          <div v-for="(line, i) in outputLines" :key="i" class="term-line">
            <span class="term-ln">{{ i + 1 }}</span>
            <span class="term-text">
              <span
                v-for="(seg, j) in parseAnsi(line)"
                :key="j"
                :style="seg.style"
              >{{ seg.text }}</span>
            </span>
            <span v-if="i === 0" class="term-line-actions">
              <button type="button" class="copy-icon-btn copy-inline" :title="copiedOutput ? '已复制' : '复制输出'" @click="copyOutput" v-html="copiedOutput ? checkIcon : clipIcon"></button>
            </span>
          </div>
        </div>
      </div>
    </div>
  </UiCard>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'
import UiCard from './ui/UiCard.vue'
import { parseAnsi } from '../terminalLog.js'
import { clipIcon, checkIcon } from '../utils/cmdFormat.js'

/** 格式化 JSON 视图图标：大括号 */
const jsonIcon = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M7 4h-.5a2.5 2.5 0 0 0-2.5 2.5v3.5a2 2 0 0 1-2 2 2 2 0 0 1 2 2v3.5a2.5 2.5 0 0 0 2.5 2.5H7"/><path d="M17 4h.5a2.5 2.5 0 0 1 2.5 2.5v3.5a2 2 0 0 0 2 2 2 2 0 0 0-2 2v3.5a2.5 2.5 0 0 1-2.5 2.5H17"/></svg>'
/** 终端原始输出视图图标：终端符号 */
const termIcon = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="4 17 10 11 4 5"/><line x1="12" y1="19" x2="20" y2="19"/></svg>'

const props = defineProps({
  result: { type: Object, default: null },
  jsonFormat: Boolean,
  highlightedCmd: { type: String, default: '' },
  highlightedResultCmd: { type: String, default: '' },
  formattedJson: { type: String, default: '' },
  outputLines: { type: Array, default: () => [] },
  /** 原始命令字符串（用于复制） */
  previewCmd: { type: String, default: '' },
  /** 上次执行的原始命令字符串（用于复制） */
  resultCmd: { type: String, default: '' },
})

const emit = defineEmits(['update:jsonFormat', 'edit'])

const copied = ref(false)
const copiedOutput = ref(false)
const copiedCmdResult = ref(false)
let copyTimer = 0
let copyOutTimer = 0
let copyCmdResultTimer = 0

// ── 双击编辑 ──
const editing = ref(false)
const editValue = ref('')
const editInput = ref(null)

watch(() => props.previewCmd, (val) => {
  editValue.value = val
})

function startEdit() {
  editValue.value = props.previewCmd
  editing.value = true
  nextTick(() => {
    editInput.value?.focus()
    editInput.value?.select()
  })
}

function confirmEdit() {
  if (!editing.value) return
  editing.value = false
  if (editValue.value !== props.previewCmd) {
    emit('edit', editValue.value)
  }
}

function cancelEdit() {
  editing.value = false
  editValue.value = props.previewCmd
}

async function copyCmd() {
  try {
    await navigator.clipboard.writeText(props.previewCmd)
    copied.value = true
    clearTimeout(copyTimer)
    copyTimer = setTimeout(() => (copied.value = false), 1500)
  } catch { /* 剪贴板不可用时忽略 */ }
}

async function copyOutput() {
  try {
    const text = props.result?.output?.replace(/\x1b\[[\d;]*m/g, '') ?? ''
    await navigator.clipboard.writeText(text)
    copiedOutput.value = true
    clearTimeout(copyOutTimer)
    copyOutTimer = setTimeout(() => (copiedOutput.value = false), 1500)
  } catch { /* 剪贴板不可用时忽略 */ }
}

async function copyCmdResult() {
  try {
    await navigator.clipboard.writeText(props.resultCmd)
    copiedCmdResult.value = true
    clearTimeout(copyCmdResultTimer)
    copyCmdResultTimer = setTimeout(() => (copiedCmdResult.value = false), 1500)
  } catch { /* 剪贴板不可用时忽略 */ }
}
</script>

<style scoped>
/* 命令与返回卡片：预览固定，结果区域滚动 */
.cmd-result-card :deep(.ui-card__body) {
  display: flex;
  flex-direction: column;
  overflow-y: visible;
}
.cmd-result-card .cmd-preview {
  flex-shrink: 0;
}
.cmd-result-card .term-title-bar {
  flex-shrink: 0;
}
.cmd-result-card .cmd-result-scroll {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

/* ── 命令预览行 ── */
.cmd-preview {
  padding: 10px 14px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  margin-bottom: 8px;
}
.preview-line {
  display: flex;
  align-items: center;
  gap: 6px;
  font-family: var(--font-mono);
  font-size: 13px;
  line-height: 1.5;
  word-break: break-all;
}
.preview-text {
  flex: 1;
  min-width: 0;
  cursor: default;
}
.preview-text:hover {
  outline: 1px dashed var(--border);
  border-radius: 3px;
}
.preview-text code {
  word-break: break-all;
  font-family: var(--font-mono);
}
.cmd-edit-input {
  flex: 1;
  min-width: 0;
  font-family: var(--font-mono);
  font-size: 13px;
  padding: 4px 8px;
  border: 1px solid var(--accent);
  border-radius: 4px;
  background: var(--bg-primary);
  color: var(--text-primary);
  outline: none;
}

/* ── 终端标题栏（已执行命令 + 时间 + 复制按钮） ── */
.term-title-bar {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 8px 14px;
  border-radius: 8px 8px 0 0;
  margin-bottom: 0;
  font-size: 12px;
  font-family: var(--font-mono);
}
.term-time {
  color: var(--text-muted);
  flex-shrink: 0;
  font-size: 11px;
}
.term-cmd {
  flex: 1;
  min-width: 0;
  color: var(--text-secondary);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.term-cmd .dollar {
  color: var(--green);
  margin-right: 4px;
}
.term-title-actions {
  flex-shrink: 0;
  display: flex;
  align-items: center;
}

/* ── 终端输出窗口 ── */
.term-window {
  border: 1px solid var(--border);
  border-radius: 10px;
  overflow: hidden;
  background: #0a0b10;
  font-family: var(--font-mono);
  font-size: 13px;
}
.term-body {
  padding: 12px 0;
  max-height: none;
  overflow-y: auto;
}

/* 复制图标按钮（glass 风格由全局 .glass 类提供） */
.copy-icon-btn {
  margin-left: 6px;
  padding: 4px;
  border-radius: 5px;
  color: var(--text-muted);
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.copy-icon-btn:hover {
  color: var(--text-primary);
}

.cmd-preview .copy-icon-btn {
  margin-left: 8px;
  padding: 6px;
  border-radius: 6px;
}

/* 代码行内的复制按钮更紧凑 */
.copy-inline {
  margin-left: 8px;
  padding: 4px;
  border-radius: 4px;
  color: var(--text-muted);
  flex-shrink: 0;
}
.copy-inline:hover {
  color: var(--text-primary);
}

.term-line {
  display: flex;
  gap: 12px;
  padding: 1px 14px;
  line-height: 1.7;
}

/* 输出首行右侧的复制按钮 */
.term-line-actions {
  margin-left: auto;
  flex-shrink: 0;
  display: flex;
  align-items: center;
}

/* 视图切换按钮（JSON / 终端原始输出） */
.view-toggle-btn {
  padding: 5px;
  border-radius: 5px;
  color: var(--text-muted);
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: color 0.15s, background 0.15s;
}
.view-toggle-btn:hover {
  color: var(--text-primary);
}
.view-toggle-btn.active {
  color: var(--accent);
  background: rgba(99, 143, 255, 0.1);
}

/* JSON 输出右上角叠加的复制按钮 */
.copy-out-json {
  position: absolute;
  top: 6px;
  right: 10px;
  padding: 5px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}
.copy-out-json:hover {
  background: var(--glass-hover-bg);
  border-color: var(--glass-hover-border);
}

.term-line:hover {
  background: var(--glass-bg);
}

.term-ln {
  color: var(--text-muted);
  flex-shrink: 0;
  width: 32px;
  text-align: right;
  font-size: 11px;
  user-select: none;
  opacity: 0.5;
  font-family: var(--font-mono);
}

.term-text {
  flex: 1;
  min-width: 0;
  font-family: var(--font-mono) !important;
  font-size: 13px !important;
  color: #d4d4d4;
  white-space: pre-wrap;
  word-break: break-all;
}

/* JSON 窗口 */
.json-window {
  border: 1px solid var(--border);
  border-radius: 10px;
  overflow: hidden;
  background: #0d1117;
  position: relative;
}
.json-body {
  padding: 12px 14px;
  position: relative;
}
.json-body::before {
  content: 'JSON';
  display: inline-block;
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 0.5px;
  color: var(--text-muted);
  background: var(--glass-bg);
  padding: 1px 8px;
  border-radius: 4px;
  margin-bottom: 10px;
}
.json-pre {
  margin: 0;
  font-family: var(--font-mono);
  font-size: 13px;
  line-height: 1.7;
  white-space: pre-wrap;
  word-break: break-all;
  color: #d4d4d4;
}
/* 内层 <code> 也会被浏览器默认 monospace 覆盖，需显式设置 */
.json-pre code {
  font-family: var(--font-mono);
}

/* JSON 语法高亮颜色 */
.json-pre :deep(.json-key)   { color: #7cb8f0; }
.json-pre :deep(.json-string){ color: #98c379; }
.json-pre :deep(.json-bool)  { color: #d19a66; }
.json-pre :deep(.json-null)  { color: #d19a66; }
.json-pre :deep(.json-num)   { color: #d19a66; }

.empty {
  color: var(--text-muted);
  font-size: 13px;
  padding: 10px 0;
}
</style>