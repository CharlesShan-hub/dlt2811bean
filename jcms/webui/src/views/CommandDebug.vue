<template>
  <div class="cmd-debug">
    <header class="page-head">
      <h1 class="page-title">{{ title }}</h1>
      <p class="page-desc">{{ def.desc }}</p>
    </header>

    <div class="debug-grid">
      <!-- ── 左栏：参数 ── -->
      <UiCard title="参数" icon="⚙" fill>
        <!-- connect 专属：使用共享连接表单 -->
        <ConnectForm v-if="isConnect" :busy="busy" submit-label="执行 connect" @submit="runCmd" @update:cmd="connectCmd = $event" />

        <!-- 通用参数渲染（connect 由上方专属模板渲染，跳过） -->
        <template v-if="!isConnect && simpleParams.length">
          <template v-for="p in simpleParams" :key="p.key">
            <ApPicker v-if="p.type === 'ap-select'" v-model="form[p.key]" />
            <div v-else class="field" :class="{ 'switch-field': p.type === 'switch' }">
              <label class="field-label">{{ p.label }}</label>
              <UiInput
                v-if="p.type === 'text'"
                v-model="form[p.key]"
                :placeholder="p.placeholder"
              />
              <UiInput
                v-else-if="p.type === 'number'"
                v-model.number="form[p.key]"
                type="number"
                :readonly="p.readonly"
              />
              <UiSelect
                v-else-if="p.type === 'select'"
                v-model="form[p.key]"
                :options="p.options"
              />
              <UiSelect
                v-else-if="p.type === 'ld-select'"
                v-model="form[p.key]"
                :options="p.required ? ldCache : ['', ...ldCache]"
                :placeholder="p.placeholder"
                empty-label="（不选）"
              />
              <UiSelect
                v-else-if="p.type === 'ln-select'"
                v-model="form[p.key]"
                :options="['', ...(ldLns[form.ld] || [])]"
                :placeholder="p.placeholder"
                empty-label="（不选）"
              />
              <UiSwitch v-else-if="p.type === 'switch'" v-model="form[p.key]" />
            </div>
          </template>
        </template>
        <p v-if="!isConnect && simpleParams.length === 0" class="empty">该命令无需参数，直接执行。</p>

        <div v-if="!isConnect" class="actions">
          <UiButton variant="primary" :loading="busy" @click="run">执行 {{ cmd }}</UiButton>
        </div>
      </UiCard>

      <!-- ── 右栏：流程 + 命令与返回（含实时预览） ── -->
      <div class="col-right">
        <UiCard :title="rightTitle" icon="⛓" collapsible>
          <!-- connect 是便捷封装命令：显示状态图而非 ASN.1 -->
          <StateDiagram v-if="isConnect" :states="connectFlow.states" :edges="connectFlow.edges" />
          <template v-else>
            <Asn1Code v-if="def.asn1" :code="def.asn1" />
            <p v-if="def.note" class="svc-note">{{ def.note }}</p>
          </template>
        </UiCard>

        <UiCard title="命令与返回" icon="🔄">
          <div class="cmd-preview">
            <code class="preview-line">{{ previewCmd }}</code>
            <UiButton variant="ghost" @click="copyCmd">
              {{ copied ? '✓ 已复制' : '复制命令' }}
            </UiButton>
          </div>
          <div v-if="history.length === 0" class="empty">执行后在此显示返回结果。</div>
          <div v-for="(h, i) in history" :key="i" class="hist-item">
            <div class="hist-cmd">
              <span class="hist-time">{{ h.time }}</span>
              <code class="hist-line">$ {{ h.cmd }}</code>
            </div>
            <pre class="hist-out">{{ h.output }}</pre>
          </div>
        </UiCard>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, reactive, ref, watch } from 'vue'
import ConnectForm from '../components/ConnectForm.vue'
import ApPicker from '../components/ApPicker.vue'
import StateDiagram from '../components/StateDiagram.vue'
import Asn1Code from '../components/Asn1Code.vue'
import UiCard from '../components/ui/UiCard.vue'
import UiButton from '../components/ui/UiButton.vue'
import UiInput from '../components/ui/UiInput.vue'
import UiSelect from '../components/ui/UiSelect.vue'
import UiSwitch from '../components/ui/UiSwitch.vue'
import { executeCommand, executeJson } from '../api/cms.js'
import { CMD_DEFS } from '../cmddefs/index.js'
import { CONNECT_FLOW } from '../cmddefs/connect.js'
import { pushTerminal } from '../terminalLog.js'
import { ldCache, ldLns, ensureLdLns } from '../ldCache.js'

const props = defineProps({
  cmd: String,
})

const connectFlow = CONNECT_FLOW

const def = computed(() => CMD_DEFS[props.cmd] || CMD_DEFS.connect)
const title = computed(() => def.value.title)
const isConnect = computed(() => props.cmd === 'connect')
const simpleParams = computed(() => def.value.params)

/** 右侧卡片标题：connect 是流程状态图；有 ASN.1 报文则标 ASN.1，否则为服务说明。 */
const rightTitle = computed(() => {
  if (isConnect.value) return '连接流程'
  return def.value.asn1 ? 'ASN.1' : '服务说明'
})

const form = reactive({})
const busy = ref(false)
const history = ref([])
const connectCmd = ref('')
const copied = ref(false)
let copyTimer

/** 实时生成的命令：connect 由 ConnectForm 输出，其余由表单状态实时拼接 */
const previewCmd = computed(() => {
  if (isConnect.value) {
    return connectCmd.value
  }
  return buildCmd()
})

async function copyCmd() {
  try {
    await navigator.clipboard.writeText(previewCmd.value)
    copied.value = true
    clearTimeout(copyTimer)
    copyTimer = setTimeout(() => (copied.value = false), 1500)
  } catch {
    // 剪贴板不可用时忽略
  }
}

function initForm() {
  for (const k of Object.keys(form)) {
    delete form[k]
  }
  for (const p of def.value.params) {
    if (p.type === 'select') {
      form[p.key] = p.options[0] || ''
    } else if (p.type === 'ap-select') {
      form[p.key] = ''
    } else if (p.type === 'ld-select') {
      // 必填的 LD 默认选中缓存第一个，避免空值
      form[p.key] = p.required && ldCache.length ? ldCache[0] : ''
    } else {
      form[p.key] = p.default ?? (p.type === 'switch' ? false : '')
    }
  }
}

watch(() => props.cmd, async () => {
  history.value = []
  connectCmd.value = ''
  copied.value = false
  initForm()
  if (props.cmd === 'negotiate') {
    await loadNegotiateDefaults()
  }
}, { immediate: true })

// ld-dir：选中 LD 后预加载其 LN 列表，供 after 下拉选择
watch(() => form.ld, async (ld) => {
  if (props.cmd === 'ld-dir' && ld) {
    await ensureLdLns(ld)
  }
})

/** negotiate 专属：读取 neg-cfg 配置回填 APDU/ASDU/版本。 */
async function loadNegotiateDefaults() {
  try {
    const neg = await executeJson('neg-cfg --json')
    if (neg.success && neg.data) {
      form.apdu = neg.data.apduSize
      form.asdu = neg.data.asduSize
      form.version = neg.data.protocolVersion
    }
  } catch {
    // 配置读取失败时保留默认值
  }
}

function buildCmd() {
  const parts = [props.cmd]
  for (const p of def.value.params) {
    const v = form[p.key]
    if (p.type === 'switch') {
      if (v) {
        parts.push(`--${p.key}`)
      }
    } else if (p.type === 'select') {
      if (v) {
        parts.push(`--${p.key}`, String(v).split(':')[0])
      }
    } else if (p.type === 'ap-select') {
      if (v) {
        parts.push(`--${p.key}`, String(v))
      }
    } else if (p.type === 'ld-select') {
      if (v) {
        parts.push(`--${p.key}`, String(v))
      }
    } else if (p.type === 'ln-select') {
      if (v) {
        parts.push(`--${p.key}`, String(v))
      }
    } else if (v !== '' && v !== null && v !== undefined) {
      parts.push(`--${p.key}`, String(v))
    }
  }
  return parts.join(' ')
}

async function run() {
  await runCmd(buildCmd())
}

/** 执行命令并写入历史（ConnectForm 提交的 connect 命令也走这里），同时回显到共享终端。 */
async function runCmd(cmdLine) {
  busy.value = true
  try {
    const text = await executeCommand(cmdLine)
    const clean = text.replace(/\x1b\[\d+m/g, '').trim()
    history.value.unshift({
      cmd: cmdLine,
      output: clean,
      time: new Date().toLocaleTimeString(),
    })
    // 终端里保留 ANSI 颜色（由 Terminal.vue 的 parseAnsi 渲染）
    pushTerminal([`$ ${cmdLine}`, text.trim()])
  } catch (e) {
    history.value.unshift({
      cmd: cmdLine,
      output: String(e),
      time: new Date().toLocaleTimeString(),
    })
    pushTerminal([`$ ${cmdLine}`, 'ERR ' + e])
  } finally {
    busy.value = false
  }
}
</script>

<style scoped>
.cmd-debug {
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

.debug-grid {
  flex: 1;
  min-height: 0;
  display: grid;
  grid-template-columns: minmax(320px, 1fr) minmax(420px, 1.4fr);
  /* 行高受容器约束，内容超高时由列内滚动条承接 */
  grid-auto-rows: minmax(0, 1fr);
  gap: 20px;
}

.col-right {
  min-height: 0;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding-right: 6px;
}

.field {
  margin-bottom: 16px;
}

.field-label {
  display: block;
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 8px;
}

.field-label::before {
  content: '';
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--red);
  margin-right: 7px;
  vertical-align: middle;
  box-shadow: 0 0 4px rgba(229, 85, 90, 0.6);
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

.actions {
  margin-top: 20px;
}

.empty {
  color: var(--text-muted);
  font-size: 13px;
}

/* ── 命令预览 ── */
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

/* ── 服务说明（无报文的命令） ── */
.svc-note {
  margin: 0;
  font-size: 13px;
  line-height: 1.8;
  color: var(--text-secondary);
}

/* ── 会话记录 ── */
.hist-item {
  border: 1px solid var(--border);
  border-radius: 8px;
  margin-bottom: 10px;
  overflow: hidden;
}

.hist-item:last-child {
  margin-bottom: 0;
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
</style>
