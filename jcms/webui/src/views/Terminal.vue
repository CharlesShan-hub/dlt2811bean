<template>
  <div class="terminal-page" :class="{ embedded }">
    <template v-if="!embedded">
      <h1 class="page-title">终端</h1>
      <p class="page-desc">直接输入 CMS 命令，支持 --json 参数</p>
    </template>

    <div class="terminal-box">
      <div ref="outputRef" class="terminal-output" @scroll="onScroll">
        <div v-for="(line, i) in output" :key="i" class="line">
          <span class="line-num">{{ i + 1 }}</span>
          <span class="line-text">
            <span
              v-for="(seg, j) in parseAnsi(line)"
              :key="j"
              :style="seg.style"
            >{{ seg.text }}</span>
          </span>
        </div>
      </div>

      <div class="terminal-input-row">
        <span class="prompt">cms&gt;</span>
        <input
          ref="inputRef"
          v-model="input"
          type="text"
          class="terminal-input"
          placeholder="输入命令，如 connect --ap C_B5041X/S1"
          @keydown.enter="send"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, watch } from 'vue'
import { executeCommand } from '../api/cms.js'
import { terminalLog, clearTerminal } from '../terminalLog.js'

defineProps({
  /** 嵌入式（底部面板）：隐藏标题头、去掉页面留白 */
  embedded: Boolean,
})

const input = ref('')
const output = terminalLog
const outputRef = ref(null)
const inputRef = ref(null)

const ansiStyles = {
  '0': {},                                          // reset
  '1': { fontWeight: 'bold' },
  '30': { color: '#333' },
  '31': { color: '#e5555a' },                        // red
  '32': { color: '#4caf7d' },                        // green
  '33': { color: '#e5b955' },                        // yellow
  '34': { color: '#5b8def' },
  '35': { color: '#c975dd' },
  '36': { color: '#5bc0de' },                        // cyan
  '37': { color: '#e1e3ec' },
  '90': { color: '#5c6078' },                        // gray
  '91': { color: '#e5555a' },
  '92': { color: '#4caf7d' },
  '93': { color: '#e5b955' },
  '94': { color: '#7aa3ff' },
  '95': { color: '#c975dd' },
  '96': { color: '#5bc0de' },
}

function parseAnsi(text) {
  const parts = []
  const regex = /\x1b\[(\d+)m/g
  let lastIdx = 0
  let currentStyle = {}

  while (true) {
    const match = regex.exec(text)
    if (!match) break

    // text before this code
    if (match.index > lastIdx) {
      parts.push({ text: text.slice(lastIdx, match.index), style: { ...currentStyle } })
    }

    const code = match[1]
    if (code === '0') {
      currentStyle = {}
    } else if (ansiStyles[code]) {
      currentStyle = { ...currentStyle, ...ansiStyles[code] }
    }

    lastIdx = regex.lastIndex
  }

  // remaining text
  if (lastIdx < text.length) {
    parts.push({ text: text.slice(lastIdx), style: { ...currentStyle } })
  }

  return parts.length ? parts : [{ text, style: {} }]
}

async function send() {
  const cmd = input.value.trim()
  if (!cmd) return

  // 本地命令：clear/cls 清空屏幕（不发往服务器）
  if (cmd === 'clear' || cmd === 'cls') {
    clearTerminal()
    input.value = ''
    return
  }

  output.push(`$ ${cmd}`)
  input.value = ''

  const result = await executeCommand(cmd)
  const lines = result.split('\n').filter(l => l.trim())

  // trim terminal control sequences (can interfere with ANSI parsing)
  output.push(...lines)
}

// 贴底跟随：默认用户在底部。上翻离开底部时暂停跟随，滚回底部时恢复。
// 仅当 follow 为 true 时，新日志到达自动滚到最新。
const follow = ref(true)

function onScroll() {
  const el = outputRef.value
  if (!el) return
  follow.value = el.scrollHeight - el.scrollTop - el.clientHeight < 8
}

watch(terminalLog, async () => {
  if (!follow.value) return
  const el = outputRef.value
  if (!el) return
  await nextTick()
  el.scrollTop = el.scrollHeight
})
</script>

<style scoped>
.terminal-page {
  padding: 32px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.terminal-page.embedded {
  padding: 0;
}

.terminal-page.embedded .terminal-box {
  border: none;
  border-radius: 0;
}

.page-title {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 4px;
}

.page-desc {
  color: var(--text-secondary);
  font-size: 13px;
  margin-bottom: 16px;
}

.terminal-box {
  flex: 1;
  background: #0a0b10;
  border: 1px solid var(--border);
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  font-family: var(--font-mono);
  font-size: 13px;
}

.terminal-output {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.line {
  display: flex;
  gap: 12px;
  padding: 1px 0;
  line-height: 1.6;
}

.line-num {
  color: var(--text-muted);
  min-width: 32px;
  text-align: right;
  user-select: none;
  font-size: 12px;
}

.line-text {
  white-space: pre-wrap;
  word-break: break-all;
}

.terminal-input-row {
  display: flex;
  align-items: center;
  border-top: 1px solid var(--border);
  padding: 10px 16px;
  gap: 8px;
  background: var(--bg-primary);
}

.prompt {
  color: var(--green);
  font-weight: 600;
  flex-shrink: 0;
}

.terminal-input {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  color: var(--text-primary);
  font-family: inherit;
  font-size: 13px;
}

.terminal-input::placeholder {
  color: var(--text-muted);
}
</style>
