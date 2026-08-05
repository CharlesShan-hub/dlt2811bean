<template>
  <UiCard title="命令与返回" icon="🔄" fill class="cmd-result-card">
    <template #header>
      <div class="json-opt">
        <span class="json-opt-label">--json</span>
        <UiSwitch :model-value="jsonMode" @update:model-value="$emit('update:jsonMode', $event)" />
      </div>
    </template>
    <div class="cmd-preview">
      <code class="preview-line">
        <span class="preview-text" v-html="highlightedCmd"></span>
        <button type="button" class="copy-icon-btn copy-inline" :title="copied ? '已复制' : '复制命令'" @click="copyCmd" v-html="copied ? checkIcon : clipIcon"></button>
      </code>
    </div>
    <!-- 上次执行的命令标题栏（固定，不滚动） -->
    <div v-if="result" class="term-title-bar">
      <span class="term-time">{{ result.time }}</span>
      <span class="term-cmd"><span class="dollar">$</span> <span v-html="highlightedResultCmd"></span></span>
      <span class="term-title-actions">
        <button type="button" class="copy-icon-btn copy-inline" :title="copiedCmdResult ? '已复制' : '复制命令'" @click="copyCmdResult" v-html="copiedCmdResult ? checkIcon : clipIcon"></button>
      </span>
    </div>
    <div class="cmd-result-scroll">
      <div v-if="!result" class="empty">执行后在此显示返回结果。</div>
      <div v-else-if="jsonFormat && formattedJson" class="json-window">
        <div class="json-body">
          <pre class="json-pre"><code v-html="formattedJson"></code></pre>
          <button type="button" class="copy-icon-btn copy-inline copy-out-json" :title="copiedOutput ? '已复制' : '复制输出'" @click="copyOutput" v-html="copiedOutput ? checkIcon : clipIcon"></button>
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
import { ref } from 'vue'
import UiCard from './ui/UiCard.vue'
import UiSwitch from './ui/UiSwitch.vue'
import { parseAnsi } from '../terminalLog.js'
import { clipIcon, checkIcon } from '../utils/cmdFormat.js'

const props = defineProps({
  result: { type: Object, default: null },
  jsonMode: Boolean,
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

defineEmits(['update:jsonMode', 'update:jsonFormat'])

const copied = ref(false)
const copiedOutput = ref(false)
const copiedCmdResult = ref(false)
let copyTimer = 0
let copyOutTimer = 0
let copyCmdResultTimer = 0

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

/* ── JSON 开关（卡片头部右侧） ── */
.json-opt {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 6px;
}
.json-opt-label {
  font-size: 12px;
  color: var(--text-muted);
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
}
.preview-text code {
  word-break: break-all;
  font-family: var(--font-mono);
}

/* ── 终端标题栏（已执行命令 + 时间 + 复制按钮） ── */
.term-title-bar {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 8px 14px;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.06);
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

/* 复制图标按钮 */
.copy-icon-btn {
  margin-left: 6px;
  padding: 4px;
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  color: var(--text-muted);
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.2s;
  flex-shrink: 0;
}
.copy-icon-btn:hover {
  color: var(--text-primary);
  background: rgba(255, 255, 255, 0.08);
  border-color: rgba(255, 255, 255, 0.15);
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.2);
}

.cmd-preview .copy-icon-btn {
  margin-left: 8px;
  padding: 6px;
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 6px;
}

/* 代码行内的复制按钮更紧凑 */
.copy-inline {
  margin-left: 8px;
  padding: 4px;
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 4px;
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  color: var(--text-muted);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  flex-shrink: 0;
}
.copy-inline:hover {
  color: var(--text-primary);
  background: rgba(255, 255, 255, 0.08);
  border-color: rgba(255, 255, 255, 0.15);
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

/* JSON 输出右上角叠加的复制按钮 */
.copy-out-json {
  position: absolute;
  top: 6px;
  right: 10px;
  background: rgba(255, 255, 255, 0.04);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.06);
  padding: 5px;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}
.copy-out-json:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.15);
}

.term-line:hover {
  background: rgba(255, 255, 255, 0.03);
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
.json-pre {
  margin: 0;
  font-family: var(--font-mono);
  font-size: 12px;
  line-height: 1.6;
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