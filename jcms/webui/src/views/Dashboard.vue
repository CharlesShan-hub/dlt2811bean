<template>
  <div class="connect-page">
    <header class="page-head">
      <div class="title-row">
        <div class="title-left">
          <h1 class="page-title">连接管理</h1>
        </div>
        <div class="title-right">
          <code class="cmd-chip">connect</code>
          <span class="desc-text">TCP → 协商 → 关联</span>
          <span class="sep">·</span>
          <code class="cmd-chip">disconnect</code>
          <span class="desc-text">断开 TCP 连接</span>
        </div>
      </div>
    </header>

    <div class="columns">
      <!-- ── 左栏：操作 ── -->
      <div class="col-left">
        <UiCard title="连接设置" icon="🔌" fill>
          <ConnectForm :busy="busy" submit-label="连接" @submit="onConnect" @update:cmd="connectCmd = $event">
            <template #extra>
              <UiButton v-if="connected" @click="disconnect">断开</UiButton>
            </template>
          </ConnectForm>

          <transition name="fade">
            <div v-if="message" class="msg" :class="msgOk ? 'ok' : 'err'">{{ message }}</div>
          </transition>
        </UiCard>
      </div>

      <!-- ── 右栏：说明 ── -->
      <div class="col-right">
        <UiCard title="连接流程" icon="⛓">
          <StateDiagram :states="connectFlow.states" :edges="connectFlow.edges" :active="activeState" />
          <p class="tip">💡 <code>connect --ap</code> 自动完成上述三步；关联建立后即可使用各服务页面。</p>
        </UiCard>

        <UiCard title="命令与返回" icon="🔄">
          <div class="cmd-preview">
            <code class="preview-line">{{ connectCmd || 'connect --ap …' }}</code>
            <UiButton variant="ghost" @click="copyCmd">
              {{ copied ? '✓ 已复制' : '复制命令' }}
            </UiButton>
          </div>
          <div v-if="!result" class="empty">连接 / 断开后在此显示返回结果。</div>
          <div v-else class="hist-item">
            <div class="hist-cmd">
              <span class="hist-time">{{ result.time }}</span>
              <code class="hist-line">$ {{ result.cmd }}</code>
            </div>
            <pre class="hist-out">{{ result.output }}</pre>
          </div>
        </UiCard>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import ConnectForm from '../components/ConnectForm.vue'
import StateDiagram from '../components/StateDiagram.vue'
import UiCard from '../components/ui/UiCard.vue'
import UiButton from '../components/ui/UiButton.vue'
import { executeCommand } from '../api/cms.js'
import { CONNECT_FLOW } from '../cmddefs/connect.js'
import { pushTerminal } from '../terminalLog.js'

const props = defineProps({
  connected: Boolean,
  tcpConnected: Boolean,
})

const connectFlow = CONNECT_FLOW

/** 根据实时状态高亮状态图中的当前状态。 */
const activeState = computed(() => {
  if (props.connected) return 'assoc'
  if (props.tcpConnected) return 'tcp'
  return 'init'
})

const busy = ref(false)
const message = ref('')
const msgOk = ref(true)

// ── 命令与返回（与 CommandDebug 子页面统一：只保留最新一条） ──
const connectCmd = ref('')
const result = ref(null)
const copied = ref(false)
let copyTimer

async function copyCmd() {
  try {
    await navigator.clipboard.writeText(connectCmd.value)
    copied.value = true
    clearTimeout(copyTimer)
    copyTimer = setTimeout(() => (copied.value = false), 1500)
  } catch {
    // 剪贴板不可用时忽略
  }
}

/** 从原始响应中提取 JSON 结果（与 executeJson 同逻辑，避免重复发命令）。 */
function parseResult(text) {
  const clean = text.replace(/\x1b\[\d+m/g, '').trim()
  const jsonStart = clean.indexOf('{')
  if (jsonStart >= 0) {
    try {
      return JSON.parse(clean.slice(jsonStart))
    } catch {
      // fall through
    }
  }
  return { success: false, error: clean }
}

/** 执行命令：返回内容同步推送到终端，结果写入"命令与返回"卡片。 */
async function runSync(cmd) {
  busy.value = true
  try {
    const text = await executeCommand(cmd)
    const clean = text.replace(/\x1b\[\d+m/g, '').trim()
    result.value = { cmd, output: clean, time: new Date().toLocaleTimeString() }
    pushTerminal([`$ ${cmd}`, text.trim()])
    return parseResult(text)
  } catch (e) {
    result.value = { cmd, output: String(e), time: new Date().toLocaleTimeString() }
    pushTerminal([`$ ${cmd}`, 'ERR ' + e])
    return { success: false, error: String(e) }
  } finally {
    busy.value = false
  }
}

/** ConnectForm 提交：执行 connect 命令并显示结果。 */
async function onConnect(cmd) {
  message.value = ''
  const res = await runSync(`${cmd} --json`)
  msgOk.value = !!res.success
  message.value = res.success ? (res.message || '连接成功') : (res.error || '连接失败')
}

async function disconnect() {
  message.value = ''
  const res = await runSync('disconnect --json')
  if (!res.success) {
    msgOk.value = false
    message.value = res.error || '断开失败'
  }
}
</script>

<style scoped>
.connect-page {
  height: 100%;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  padding: 24px 32px;
  overflow: hidden;
}

.page-head {
  margin-bottom: 20px;
  flex-shrink: 0;
}

.page-title {
  font-size: 22px;
  font-weight: 700;
}

/* ── 页头标题行：左标题 + 右命令简介（与 CommandDebug 子页面统一） ── */
.title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 6px;
}

.title-left {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.title-right {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  flex-wrap: wrap;
  text-align: right;
  color: var(--text-secondary);
  font-size: 13px;
  min-width: 0;
}

.cmd-chip {
  font-family: var(--font-mono);
  font-size: 12px;
  color: var(--text-primary);
  background: var(--bg-tertiary);
  border: 1px solid var(--border);
  border-radius: 5px;
  padding: 1px 8px;
}

.sep {
  color: var(--text-muted);
}

.desc-text {
  color: var(--text-secondary);
}

.columns {
  flex: 1;
  min-height: 0;
  display: grid;
  grid-template-columns: minmax(340px, 1fr) minmax(400px, 1.3fr);
  /* 行高受容器约束，内容超高时由列内滚动条承接（否则会溢出被底部终端面板盖住） */
  grid-auto-rows: minmax(0, 1fr);
  gap: 20px;
}

.col-left,
.col-right {
  min-height: 0;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding-right: 6px;
}

/* ── 深色滚动条 ── */
.col-left::-webkit-scrollbar,
.col-right::-webkit-scrollbar {
  width: 8px;
}

.col-left::-webkit-scrollbar-track,
.col-right::-webkit-scrollbar-track {
  background: transparent;
}

.col-left::-webkit-scrollbar-thumb,
.col-right::-webkit-scrollbar-thumb {
  background: var(--border);
  border-radius: 4px;
}

.col-left::-webkit-scrollbar-thumb:hover,
.col-right::-webkit-scrollbar-thumb:hover {
  background: var(--text-muted);
}

/* ── 连接结果 ── */
.msg {
  margin-top: 16px;
  padding: 10px 14px;
  border-radius: 8px;
  font-size: 13px;
}

.msg.ok {
  color: var(--green);
  background: var(--green-bg);
  border: 1px solid rgba(76, 175, 125, 0.3);
}

.msg.err {
  color: var(--red);
  background: var(--red-bg);
  border: 1px solid rgba(229, 85, 90, 0.3);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* ── 命令与返回 ── */
.cmd-preview {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}

.preview-line {
  flex: 1;
  min-width: 0;
  overflow-x: auto;
  white-space: nowrap;
  background: var(--bg-primary);
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 10px 12px;
  font-family: var(--font-mono);
  font-size: 13px;
  color: var(--accent);
}

.empty {
  color: var(--text-muted);
  font-size: 13px;
}

.hist-item {
  border: 1px solid var(--border);
  border-radius: 8px;
  overflow: hidden;
}

.hist-cmd {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 10px;
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border);
}

.hist-time {
  font-size: 11px;
  color: var(--text-muted);
  flex-shrink: 0;
}

.hist-line {
  font-family: var(--font-mono);
  font-size: 13px;
  color: var(--accent);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.hist-out {
  margin: 0;
  padding: 10px;
  font-family: var(--font-mono);
  font-size: 13px;
  line-height: 1.7;
  color: var(--text-secondary);
  white-space: pre-wrap;
  word-break: break-all;
}

.tip {
  margin: 12px 0 0;
  font-size: 12px;
  color: var(--text-muted);
}

.tip code {
  color: var(--accent);
}
</style>
