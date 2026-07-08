<template>
  <div class="terminal-page">
    <h1 class="page-title">终端</h1>
    <p class="page-desc">直接输入 CMS 命令，支持 --json 参数</p>

    <div class="terminal-box">
      <div ref="outputRef" class="terminal-output">
        <div v-for="(line, i) in output" :key="i" class="line" :class="line.type">
          <span class="line-num">{{ i + 1 }}</span>
          <span class="line-text">{{ line.text }}</span>
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
import { ref, nextTick } from 'vue'
import { executeCommand } from '../api/cms.js'

const input = ref('')
const output = ref([{ type: 'info', text: '// CMS Console — 输入命令开始' }])
const outputRef = ref(null)
const inputRef = ref(null)

async function send() {
  const cmd = input.value.trim()
  if (!cmd) return

  output.value.push({ type: 'cmd', text: `$ ${cmd}` })
  input.value = ''

  const result = await executeCommand(cmd)
  for (const line of result.split('\n')) {
    if (line.trim()) {
      const type = line.startsWith('ERR') ? 'err' : 'info'
      output.value.push({ type, text: line })
    }
  }

  await nextTick()
  outputRef.value.scrollTop = outputRef.value.scrollHeight
}
</script>

<style scoped>
.terminal-page {
  padding: 32px;
  height: 100%;
  display: flex;
  flex-direction: column;
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
  font-family: 'JetBrains Mono', 'Cascadia Code', 'Fira Code', 'Consolas', monospace;
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

.line.cmd .line-text {
  color: var(--accent);
}

.line.info .line-text {
  color: var(--text-secondary);
}

.line.err .line-text {
  color: var(--red);
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
