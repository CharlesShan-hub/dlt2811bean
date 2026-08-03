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
          <div class="field">
            <label class="field-label">🔀 默认 AP 加载方式</label>
            <UiSegmented
              :model-value="form.source"
              :options="sourceOptions"
              @update:model-value="onSourceChange"
            />
          </div>

          <div class="field">
            <label class="field-label">📍 默认 AP</label>
            <UiSelect
              v-model="form.ap"
              :options="apOptions"
              :loading="loadingAps"
              placeholder="请选择 AP"
            />
          </div>

          <div class="field switch-field">
            <span class="field-label">🔒 安全连接（TLS）</span>
            <UiSwitch v-model="form.secure" />
          </div>

          <div class="divider"></div>

          <div class="field">
            <label class="field-label">🤝 协商参数</label>
            <div class="neg-grid">
              <div class="neg-item">
                <label>APDU 大小</label>
                <UiInput v-model.number="form.neg.apduSize" type="number" />
              </div>
              <div class="neg-item">
                <label>ASDU 大小</label>
                <UiInput v-model.number="form.neg.asduSize" type="number" />
              </div>
              <div class="neg-item">
                <label>协议版本</label>
                <UiInput v-model.number="form.neg.protocolVersion" type="number" />
              </div>
              <div class="neg-item">
                <label>模型版本</label>
                <UiInput v-model="form.neg.modelVersion" readonly />
                <span class="neg-hint">服务器协商返回</span>
              </div>
            </div>
          </div>

          <div class="actions">
            <UiButton variant="primary" :loading="busy" @click="connect">
              {{ busy ? '连接中' : '连接' }}
            </UiButton>
            <UiButton v-if="connected" @click="disconnect">断开</UiButton>
          </div>

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
          <ol class="flow-steps">
            <li v-for="step in flowSteps" :key="step.title">
              <span class="step-title">{{ step.title }}</span>
              <span class="step-desc">{{ step.desc }}</span>
            </li>
          </ol>
          <p class="tip">💡 <code>connect --ap</code> 自动完成上述三步；关联建立后即可使用各服务页面。</p>
        </UiCard>

        <UiCard title="关联报文 · Associate (§8.2.1)" icon="📨">
          <pre class="pdu"><code>Request:
  serverAccessPointReference [0..1]  VisibleString129
  authenticationParameter  [0..1]  OCTETSTRING

Response+:
  associationId                OCTETSTRING64
  result                       ServiceError = no-error
  authenticationParameter      OCTETSTRING

Response-:
  serviceError                 ServiceError</code></pre>
          <p class="tip">💡 访问点格式 <code>IEDName.AccessPoint</code>，如 <code>C_B5041X/S1</code>。</p>
        </UiCard>

        <UiCard title="协商报文 · Negotiate" icon="🤝">
          <pre class="pdu"><code>Request:
  apduSize         INTEGER (0..65535)
  asduSize         INTEGER (0..65531)
  protocolVersion  INTEGER

Response:
  apduSize         INTEGER (0..65535)
  asduSize         INTEGER (0..65531)
  protocolVersion  INTEGER
  modelVersion     VisibleString</code></pre>
          <p class="tip">💡 响应中 <code>apduSize &gt; asduSize</code> 表示支持分帧。</p>
        </UiCard>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import UiCard from '../components/ui/UiCard.vue'
import UiButton from '../components/ui/UiButton.vue'
import UiSegmented from '../components/ui/UiSegmented.vue'
import UiSelect from '../components/ui/UiSelect.vue'
import UiSwitch from '../components/ui/UiSwitch.vue'
import UiInput from '../components/ui/UiInput.vue'
import UiCollapse from '../components/ui/UiCollapse.vue'
import { executeJson } from '../api/cms.js'

defineProps({
  connected: Boolean,
})

const sourceOptions = [
  { value: 'scd', label: '从 SCD 文件读' },
  { value: 'list', label: '从静态列表读' },
]

const cmdRows = [
  {
    name: 'connect',
    usage: 'connect [--ip addr] [--ap IED/AP] [--secure] [--apdu N] [--asdu N] [--version N]',
    desc: 'TCP → 协商 → 关联',
    detail: '最常用命令，自动完成 TCP 连接、参数协商与关联三步。指定 --ap 时同时协商并关联到该访问点。',
  },
  {
    name: 'associate',
    usage: 'associate [--ap IED/AP] [--secure]',
    desc: '手动关联',
    detail: '纯 TCP 连接后手动关联，或关联后更换访问点（先 release 再 associate）。--secure 为应用层证书认证。',
  },
  {
    name: 'release',
    usage: 'release',
    desc: '释放关联',
    detail: '释放当前应用层关联，保持 TCP 连接不变，可继续 associate 其他访问点。',
  },
  {
    name: 'abort',
    usage: 'abort',
    desc: '中止关联',
    detail: '直接中止当前关联，与 release 相比更主动，通常用于异常场景。',
  },
  {
    name: 'negotiate',
    usage: 'negotiate [--apdu N] [--asdu N] [--version N]',
    desc: '手动协商',
    detail: '单独执行参数协商（connect --ap 已自动包含）。协商必须在关联之前完成。',
  },
  {
    name: 'disconnect',
    usage: 'disconnect',
    desc: '断开',
    detail: '断开与服务器的 TCP 连接，需先释放/中止关联。',
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

const flowSteps = [
  { title: '① TCP 连接', desc: '建立到 127.0.0.1:8102 的传输层连接（可 TLS）' },
  { title: '② 协商 Negotiate', desc: '交换 APDU/ASDU 大小与协议版本' },
  { title: '③ 关联 Associate', desc: '指定 ServerAccessPointReference 建立应用层关联' },
]

const form = ref({ source: 'scd', ap: '', secure: false, neg: { apduSize: 65535, asduSize: 65531, protocolVersion: 1, modelVersion: '1.0' } })
const apOptions = ref([])
const loadingAps = ref(false)
const busy = ref(false)
const message = ref('')
const msgOk = ref(true)

/** 读取当前 ap-cfg / neg-cfg 配置并回填表单。 */
async function loadConfig() {
  try {
    const res = await executeJson('ap-cfg --json')
    if (res.success && res.data) {
      form.value.source = res.data.fromScd ? 'scd' : 'list'
    }
  } catch {
    // 配置读取失败时保留默认值
  }
  try {
    const neg = await executeJson('neg-cfg --json')
    if (neg.success && neg.data) {
      form.value.neg.apduSize = neg.data.apduSize
      form.value.neg.asduSize = neg.data.asduSize
      form.value.neg.protocolVersion = neg.data.protocolVersion
      form.value.neg.modelVersion = neg.data.modelVersion
    }
  } catch {
    // 协商参数读取失败时保留默认值
  }
  await refreshAps()
}

/** 按当前来源刷新 AP 下拉选项。 */
async function refreshAps() {
  const prev = form.value.ap
  loadingAps.value = true
  let options = []
  try {
    if (form.value.source === 'list') {
      // list 模式：ap-cfg --json 返回 defaultAps
      const res = await executeJson('ap-cfg --json')
      if (res.success && res.data) {
        options = res.data.defaultAps || []
      }
    } else {
      // scd 模式：ap-dir --json 返回 [{ied, aps}]，拍平为 IED/AP 引用
      const res = await executeJson('ap-dir --json')
      if (res.success && Array.isArray(res.data)) {
        options = res.data.flatMap((d) => (d.aps || []).map((ap) => `${d.ied}/${ap}`))
      }
    }
  } catch {
    // 拉取失败时保持空选项
  }
  apOptions.value = options
  form.value.ap = options.includes(prev) ? prev : (options[0] || '')
  loadingAps.value = false
}

/** 切换加载方式：赋值 → 保存 → 刷新下拉。 */
async function onSourceChange(s) {
  if (form.value.source === s) {
    return
  }
  form.value.source = s
  await executeJson(`ap-cfg --source ${s} --json`)
  await refreshAps()
}

async function connect() {
  busy.value = true
  message.value = ''
  try {
    const ap = form.value.ap.trim()
    const secure = form.value.secure ? ' --secure' : ''
    const neg =
      ` --apdu ${form.value.neg.apduSize} --asdu ${form.value.neg.asduSize} --version ${form.value.neg.protocolVersion}`
    const cmd = ap ? `connect --ap ${ap}${secure}${neg} --json` : `connect${secure}${neg} --json`
    const res = await executeJson(cmd)
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

onMounted(loadConfig)
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

/* ── 左栏表单 ── */
.field {
  margin-bottom: 20px;
}

.field-label {
  display: block;
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 8px;
}

.switch-field {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.switch-field .field-label {
  margin-bottom: 0;
}

.divider {
  height: 1px;
  background: var(--border);
  margin: 4px 0 20px;
}

.neg-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
}

.neg-item label {
  display: block;
  font-size: 12px;
  color: var(--text-muted);
  margin-bottom: 6px;
}

.neg-item .neg-hint {
  display: block;
  margin-top: 4px;
  font-size: 11px;
  color: var(--text-muted);
}

.actions {
  display: flex;
  gap: 10px;
  margin-top: 24px;
}

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

.flow-steps {
  list-style: none;
  padding: 0;
  margin: 0;
  counter-reset: none;
}

.flow-steps li {
  display: flex;
  align-items: baseline;
  gap: 8px;
  padding: 8px 0;
  border-bottom: 1px dashed var(--border);
}

.flow-steps li:last-child {
  border-bottom: none;
}

.step-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-primary);
  white-space: nowrap;
}

.step-desc {
  font-size: 12px;
  color: var(--text-secondary);
}

.pdu {
  background: var(--bg-primary);
  border: 1px solid var(--border);
  border-radius: 8px;
  padding: 14px;
  overflow-x: auto;
  font-size: 12px;
  line-height: 1.7;
  color: var(--text-secondary);
  margin: 0;
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
