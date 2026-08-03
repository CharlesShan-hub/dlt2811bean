<template>
  <div class="connect-page">
    <header class="page-head">
      <h1 class="page-title">连接管理</h1>
      <p class="page-desc">配置 AP 来源与连接参数，发起 / 断开与 CMS 服务器的关联</p>
    </header>

    <div class="columns">
      <!-- ── 左栏：操作 ── -->
      <div class="col-left">
        <UiCard title="连接设置" icon="🔌">
          <ConnectForm :busy="busy" submit-label="连接" @submit="onConnect">
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
        <UiCard title="命令速览" icon="⌘">
          <div class="cmd-list">
            <UiCollapse v-for="cmd in cmdRows" :key="cmd.name">
              <template #title>
                <code class="cmd-name">{{ cmd.name }}</code>
                <span class="cmd-short">{{ cmd.desc }}</span>
              </template>
              <div class="cmd-detail">
                <div class="cmd-usage"><code>{{ cmd.usage }}</code></div>
                <p class="cmd-desc">{{ cmd.detail }}</p>
              </div>
            </UiCollapse>
          </div>
        </UiCard>

        <UiCard title="连接流程" icon="⛓">
          <StateDiagram :states="connectFlow.states" :edges="connectFlow.edges" :active="activeState" />
          <p class="tip">💡 <code>connect --ap</code> 自动完成上述三步；关联建立后即可使用各服务页面。</p>
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
import UiCollapse from '../components/ui/UiCollapse.vue'
import { executeJson } from '../api/cms.js'
import { CONNECT_FLOW } from '../cmddefs/connect.js'

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

const cmdRows = [
  {
    name: 'connect',
    usage: 'connect [--ip addr] [--ap IED/AP] [--secure] [--apdu N] [--asdu N] [--version N]',
    desc: 'TCP → 协商 → 关联',
    detail: '最常用命令，自动完成 TCP 连接、参数协商与关联三步。指定 --ap 时同时协商并关联到该访问点。',
  },
  {
    name: 'disconnect',
    usage: 'disconnect',
    desc: '断开',
    detail: '断开与服务器的 TCP 连接，需先释放/中止关联。',
  },
  {
    name: 'negotiate',
    usage: 'negotiate [--apdu N] [--asdu N] [--version N]',
    desc: '手动协商 (8.15)',
    detail: '单独执行参数协商（connect --ap 已自动包含）。协商必须在关联之前完成。',
  },
  {
    name: 'associate',
    usage: 'associate [--ap IED/AP] [--secure]',
    desc: '手动关联 (8.2.1)',
    detail: '纯 TCP 连接后手动关联，或关联后更换访问点（先 release 再 associate）。--secure 为应用层证书认证。',
  },
  {
    name: 'release',
    usage: 'release',
    desc: '释放关联 (8.2.2)',
    detail: '释放当前应用层关联，保持 TCP 连接不变，可继续 associate 其他访问点。',
  },
  {
    name: 'abort',
    usage: 'abort',
    desc: '中止关联 (8.2.3)',
    detail: '直接中止当前关联，与 release 相比更主动，通常用于异常场景。',
  },
  {
    name: 'ap-dir',
    usage: 'ap-dir [--scd path] [--ied name]',
    desc: '列出可用 AP',
    detail: '本地配置命令，无需连接。从 SCD 文件或 defaultAps 静态列表列出所有可用访问点，供 connect --ap 使用。',
  },
  {
    name: 'ap-cfg',
    usage: 'ap-cfg [--source scd|list]',
    desc: '切换 AP 来源',
    detail: '运行时切换 AP 来源（SCD 文件 / 静态列表），立即生效，无需重启。',
  },
]

const busy = ref(false)
const message = ref('')
const msgOk = ref(true)

/** ConnectForm 提交：执行 connect 命令并显示结果。 */
async function onConnect(cmd) {
  busy.value = true
  message.value = ''
  try {
    const res = await executeJson(`${cmd} --json`)
    msgOk.value = !!res.success
    message.value = res.success ? (res.message || '连接成功') : (res.error || '连接失败')
  } catch (e) {
    msgOk.value = false
    message.value = String(e)
  } finally {
    busy.value = false
  }
}

async function disconnect() {
  await executeJson('disconnect --json')
  message.value = ''
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
  margin-bottom: 4px;
}

.page-desc {
  color: var(--text-secondary);
  font-size: 13px;
}

.columns {
  flex: 1;
  min-height: 0;
  display: grid;
  grid-template-columns: minmax(340px, 1fr) minmax(400px, 1.3fr);
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

/* ── 右栏文档 ── */
.cmd-list {
  display: flex;
  flex-direction: column;
}

.cmd-name {
  font-size: 13px;
  color: var(--accent);
  background: var(--accent-muted);
  border-radius: 4px;
  padding: 2px 8px;
  flex-shrink: 0;
}

.cmd-short {
  font-size: 12px;
  color: var(--text-muted);
}

.cmd-detail {
  padding: 2px 2px 0;
}

.cmd-usage {
  background: var(--bg-primary);
  border: 1px solid var(--border);
  border-radius: 6px;
  padding: 8px 10px;
  overflow-x: auto;
}

.cmd-usage code {
  font-size: 12px;
  color: var(--text-secondary);
}

.cmd-desc {
  margin: 8px 0 0;
  font-size: 12px;
  line-height: 1.7;
  color: var(--text-secondary);
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
