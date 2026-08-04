<template>
  <div class="cmd-debug">
    <header class="page-head">
      <div class="title-row">
        <div class="title-left">
          <span v-if="section" class="sec-badge">✦ {{ section }}</span>
          <h1 class="page-title">{{ shortTitle }}</h1>
        </div>
        <div class="title-right">
          <code class="cmd-chip">{{ props.cmd }}</code>
          <span class="sep">·</span>
          <span class="desc-text">{{ def.desc }}</span>
        </div>
      </div>
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
          </template>

          <!-- 其余参数按行渲染：inline 同组的参数并排一行（如 APDU / ASDU） -->
          <template v-for="row in paramRows" :key="row.inline || row.items[0].key">
            <div class="field-row" :class="{ single: !row.inline }">
              <div
                v-for="p in row.items"
                :key="p.key"
                class="field"
                :class="{ 'switch-field': p.type === 'switch' }"
              >
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
                  :options="p.required ? lnOptions : ['', ...lnOptions]"
                  :placeholder="p.placeholder"
                  :empty-label="p.required ? '暂无选项' : '（不选）'"
                />
                <UiSwitch v-else-if="p.type === 'switch'" v-model="form[p.key]" />
              </div>
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
          <div v-if="!result" class="empty">执行后在此显示返回结果。</div>
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
import { ldCache, ldLns, allLnRefs, ensureLdLns, ensureAllLnRefs } from '../ldCache.js'

const props = defineProps({
  cmd: String,
})

const connectFlow = CONNECT_FLOW

const def = computed(() => CMD_DEFS[props.cmd] || { title: props.cmd, desc: '', params: [] })

/** 页头拆解："关联 associate (8.2.1)" → 大标题"关联" + 章节徽章"8.2.1"。 */
const titleParts = computed(() => {
  const m = (def.value.title || '').match(/^(.*?)\s+\S+\s*\(([\d.]+)\)$/)
  return m ? { name: m[1], section: m[2] } : { name: def.value.title, section: '' }
})
const shortTitle = computed(() => titleParts.value.name)
const section = computed(() => titleParts.value.section)
const isConnect = computed(() => props.cmd === 'connect')
const simpleParams = computed(() => def.value.params)

/** 参数按行分组：inline 值相同的参数并排一行（如 APDU / ASDU）。 */
const paramRows = computed(() => {
  const rows = []
  let cur = null
  for (const p of def.value.params || []) {
    if (p.inline) {
      if (!cur || cur.inline !== p.inline) {
        cur = { inline: p.inline, items: [] }
        rows.push(cur)
      }
      cur.items.push(p)
    } else {
      rows.push({ inline: null, items: [p] })
    }
  }
  return rows
})

/** ln 下拉选项：选中 LD 时用该 LD 下的 LN，否则用全量完整引用。 */
const lnOptions = computed(() => (form.ld ? ldLns[form.ld] || [] : allLnRefs))

/** 右侧卡片标题：connect 是流程状态图；有 ASN.1 报文则标 ASN.1，否则为服务说明。 */
const rightTitle = computed(() => {
  if (isConnect.value) return '连接流程'
  return def.value.asn1 ? 'ASN.1' : '服务说明'
})

const form = reactive({})
const busy = ref(false)
const result = ref(null)
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

/** ln 必填的命令：进入页面时预加载全量 LN 引用并默认选中第一个。 */
const LN_REQUIRED_CMDS = ['ln-dir', 'all-data', 'all-def']

watch(() => props.cmd, async () => {
  result.value = null
  connectCmd.value = ''
  copied.value = false
  initForm()
  if (props.cmd === 'negotiate') {
    await loadNegotiateDefaults()
  }
  if (LN_REQUIRED_CMDS.includes(props.cmd)) {
    await ensureAllLnRefs()
    if (!form.ln && allLnRefs.length) form.ln = allLnRefs[0]
  }
}, { immediate: true })

// ld-dir：选中 LD 后预加载其 LN 列表；未选 LD 时预加载全量完整引用（供 after 下拉）
// 同时监听 ldCache 长度：直接进入页面时缓存可能还没填充，填充后自动补拼全量引用
watch([() => form.ld, () => ldCache.length], async ([ld]) => {
  if (props.cmd !== 'ld-dir') return
  if (ld) {
    await ensureLdLns(ld)
  } else {
    await ensureAllLnRefs()
  }
}, { immediate: true })

// ln 必填命令：等全量 LN 引用缓存就绪后自动选中第一个（覆盖连接后才进入/缓存在填充中的情况）
watch([() => allLnRefs.length, () => ldCache.length], async () => {
  if (!LN_REQUIRED_CMDS.includes(props.cmd)) return
  if (form.ln) return
  await ensureAllLnRefs()
  if (!form.ln && allLnRefs.length) form.ln = allLnRefs[0]
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

/** 执行命令并写入结果区（只保留最新一条），同时回显到共享终端。 */
async function runCmd(cmdLine) {
  busy.value = true
  try {
    const text = await executeCommand(cmdLine)
    const clean = text.replace(/\x1b\[\d+m/g, '').trim()
    result.value = {
      cmd: cmdLine,
      output: clean,
      time: new Date().toLocaleTimeString(),
    }
    // 终端里保留 ANSI 颜色（由 Terminal.vue 的 parseAnsi 渲染）
    pushTerminal([`$ ${cmdLine}`, text.trim()])
  } catch (e) {
    result.value = {
      cmd: cmdLine,
      output: String(e),
      time: new Date().toLocaleTimeString(),
    }
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
}

/* ── 页头标题行：左（章节徽章 + 大标题）右（命令名 + 简介）两端对齐 ── */
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

.title-left .sec-badge {
  padding: 3px 12px;
  font-size: 13px;
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

/* ── 页头副标题：章节徽章 + 命令名 + 简介 ── */
.sec-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: var(--accent-muted);
  color: var(--accent);
  border-radius: 999px;
  padding: 2px 10px;
  font-size: 12px;
  font-weight: 600;
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

/* 参数按行分组：同组并排一行（如 APDU / ASDU） */
.field-row {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.field-row .field {
  flex: 1;
  min-width: 0;
  margin-bottom: 0;
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
