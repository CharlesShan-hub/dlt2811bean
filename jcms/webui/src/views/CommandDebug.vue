<template>
  <div class="cmd-debug">
    <header class="page-head">
      <div class="title-row">
        <div class="title-left">
          <span v-if="section" class="sec-badge">✦ {{ section }}</span>
          <h1 class="page-title">{{ isConnect ? '连接管理' : shortTitle }}</h1>
        </div>
        <div class="title-right">
          <button v-if="def.doc" type="button" class="doc-btn" title="协议说明" @click="docOpen = true">📖</button>
          <template v-if="isConnect">
            <code class="cmd-chip">connect</code>
            <span class="desc-text">TCP → 协商 → 关联</span>
            <span class="sep">·</span>
            <code class="cmd-chip">disconnect</code>
            <span class="desc-text">断开 TCP 连接</span>
          </template>
          <template v-else>
            <code class="cmd-chip">{{ props.cmd }}</code>
            <span class="sep">·</span>
            <span class="desc-text">{{ def.desc }}</span>
          </template>
        </div>
      </div>
    </header>

    <UiModal v-model="docOpen" wide title="协议说明">
      <div v-if="def.doc" class="doc-md" v-html="docHtml"></div>
      <template v-else>
        <pre v-if="def.asn1" class="doc-asn1">{{ def.asn1 }}</pre>
        <p v-else class="doc-desc muted">该命令没有独立的协议说明。</p>
      </template>
    </UiModal>

    <div class="debug-grid">
      <!-- ── 左栏：参数 ── -->
      <UiCard :title="isConnect ? '连接设置' : '参数'" icon="⚙" fill>
        <!-- connect 专属：使用共享连接表单 + 断开按钮 + 结果消息 -->
        <ConnectForm v-if="isConnect" :busy="busy" submit-label="连接" @submit="runCmd" @update:cmd="connectCmd = $event">
          <template #extra>
            <UiButton v-if="connected" @click="disconnect">断开</UiButton>
          </template>
        </ConnectForm>
        <transition name="fade">
          <div v-if="isConnect && connMsg" class="msg" :class="connMsgOk ? 'ok' : 'err'">{{ connMsg }}</div>
        </transition>

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
                  :disabled="p.disabled"
                />
                <UiSelect
                  v-else-if="p.type === 'ld-select'"
                  v-model="form[p.key]"
                  :options="p.required ? ldCache : ['', ...ldCache]"
                  :placeholder="p.placeholder"
                  empty-label="（不选）"
                />
                <!-- 级联二选：LD → LN，生成 --ln/--after "LD/LN"（ld-dir 的 after 与上面 ld 联动） -->
                <div v-else-if="p.type === 'ln-cascade'" class="cascade-pair">
                  <UiSelect
                    v-model="form[p.key].ld"
                    :options="ldCache"
                    :disabled="cascadeLdDisabled(p)"
                    placeholder="LD"
                    empty-label="（不选）"
                    @update:modelValue="onCascadeLd(p.key)"
                  />
                  <UiSelect
                    v-model="form[p.key].ln"
                    :options="cascadeLns(form[p.key])"
                    placeholder="LN"
                    empty-label="（不选）"
                  />
                </div>
                <UiSelect
                  v-else-if="p.type === 'ln-ref-select'"
                  v-model="form[p.key]"
                  :options="['', ...refOptions]"
                  :placeholder="p.placeholder"
                  empty-label="（不选）"
                />
                <!-- 动态引用列表：加号增行、叉号删行，命令拼接为 --refs "r1 r2 ..." -->
                <div v-else-if="p.type === 'refs-list'" class="refs-list">
                  <!-- 级联三选：LD → LN → DO，逐层下钻引用 -->
                  <template v-if="p.cascade">
                    <div v-for="(r, i) in form[p.key]" :key="i" class="refs-row">
                      <UiSelect
                        v-model="r.ld"
                        :options="ldCache"
                        placeholder="LD"
                        empty-label="（不选）"
                        @update:modelValue="onRowLd(r)"
                      />
                      <UiSelect
                        v-model="r.ln"
                        :options="r.ld ? ldLns[r.ld] || [] : []"
                        placeholder="LN"
                        empty-label="（不选）"
                        @update:modelValue="onRowLn(r)"
                      />
                      <UiSelect
                        v-model="r.do"
                        :options="rowDoOptions(r)"
                        placeholder="DO"
                        empty-label="（不选）"
                        @update:modelValue="onRowDo(r)"
                      />
                      <UiSelect
                        v-model="r.da"
                        :options="rowDaOptions(r)"
                        placeholder="DA"
                        empty-label="（不选）"
                      />
                      <button type="button" class="refs-del" title="删除该引用" @click="removeRefs(i)">✕</button>
                    </div>
                  </template>
                  <template v-else>
                    <div v-for="(r, i) in form[p.key]" :key="i" class="refs-row">
                      <UiSelect
                        v-model="form[p.key][i]"
                        :options="['', ...refsListOptions]"
                        :placeholder="p.placeholder"
                        empty-label="（不选）"
                      />
                      <button type="button" class="refs-del" title="删除该引用" @click="removeRefs(i)">✕</button>
                    </div>
                  </template>
                  <button type="button" class="refs-add" @click="addRefs">＋ 添加引用</button>
                </div>
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
          <StateDiagram v-if="isConnect" :states="connectFlow.states" :edges="connectFlow.edges" :active="activeState" />
          <p v-if="isConnect" class="tip">💡 <code>connect --ap</code> 自动完成上述三步；关联建立后即可使用各服务页面。</p>
          <template v-else>
            <Asn1Code v-if="def.asn1" :code="def.asn1" />
            <p v-else-if="def.desc" class="svc-note">{{ def.desc }}</p>
            <p v-if="def.note" class="svc-note">{{ def.note }}</p>
          </template>
        </UiCard>

        <UiCard title="命令与返回" icon="🔄">
          <template #header>
            <div class="json-opt">
              <span class="json-opt-label">--json</span>
              <UiSwitch v-model="jsonMode" />
            </div>
          </template>
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
import UiModal from '../components/ui/UiModal.vue'
import { executeCommand, executeJson } from '../api/cms.js'
import { marked } from 'marked'
import { CMD_DEFS } from '../cmddefs/index.js'
import { CONNECT_FLOW } from '../cmddefs/connect.js'
import { pushTerminal } from '../terminalLog.js'
import { ldCache, ldLns, allLnRefs, lnDirRefs, allDefRefs, allCbRefs, ensureLdLns, ensureAllLnRefs, ensureLnDirRefs, ensureAllDefRefs, ensureAllCbRefs } from '../ldCache.js'

const props = defineProps({
  cmd: String,
  /** 连接管理页：当前是否已关联 / 已 TCP 连接（用于状态图高亮） */
  connected: { type: Boolean, default: false },
  tcpConnected: { type: Boolean, default: false },
})

const connectFlow = CONNECT_FLOW

const def = computed(() => CMD_DEFS[props.cmd] || { title: props.cmd, desc: '', params: [] })

/** connect 状态图高亮：关联 > TCP 连接 > 未连接。 */
const activeState = computed(() => {
  if (props.connected) return 'assoc'
  if (props.tcpConnected) return 'tcp'
  return 'init'
})

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

/** ln-cascade 参数（key=ln）当前拼出的 LD/LN 引用（未选时为空），驱动 after 下拉懒加载。 */
const lnRef = computed(() => {
  const p = def.value.params.find((x) => x.key === 'ln' && x.type === 'ln-cascade')
  const o = p ? form[p.key] : null
  return o && o.ld ? (o.ln ? `${o.ld}/${o.ln}` : o.ld) : ''
})

/** ln-cascade 的 LN 选项：选中 LD 后取该 LD 的 LN 列表。 */
function cascadeLns(o) {
  return o && o.ld ? ldLns[o.ld] || [] : []
}

/** ln-cascade 的 LD 变化：清空 LN 并懒加载该 LD 的 LN 列表。 */
function onCascadeLd(key) {
  const o = form[key]
  if (!o) return
  o.ln = ''
  if (o.ld) ensureLdLns(o.ld)
}

/** ld-dir 的 after 级联：上面已选 ld 时，after 的 LD 跟随上面并禁用（只选 LN，单设备模式不带前缀）。 */
function cascadeLdDisabled(p) {
  return props.cmd === 'ld-dir' && p.key === 'after' && !!form.ld
}

/** after 下拉选项：ln-dir 用该 LN 的子引用；all-data/all-def 用 all-def 的 DO 引用；all-cb 用 CB 引用。 */
const refOptions = computed(() => {
  if (props.cmd === 'ln-dir') return lnDirRefs
  if (props.cmd === 'all-cb') return allCbRefs
  return allDefRefs
})

/** 动态引用列表（refs-list）选项：全量 LD/LN 完整引用。 */
const refsListOptions = computed(() => allLnRefs)

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
const docOpen = ref(false)
/** 连接管理页：执行 connect/disconnect 的结果消息。 */
const connMsg = ref('')
const connMsgOk = ref(true)

/** 全局 JSON 输出开关：命令自动追加 --json（localStorage 持久化，各子页面共享）。 */
const jsonMode = ref(localStorage.getItem('cms-json-mode') === '1')
watch(jsonMode, (v) => localStorage.setItem('cms-json-mode', v ? '1' : '0'))

/** 协议说明 doc 的 markdown 渲染结果。 */
const docHtml = computed(() => marked.parse(def.value.doc || ''))
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
      const first = p.options[0]
      // 选项可能是对象 { value, label, color }（带颜色），默认取 value
      form[p.key] = first && typeof first === 'object' ? first.value : (first || '')
    } else if (p.type === 'ap-select') {
      form[p.key] = ''
    } else if (p.type === 'ld-select') {
      // 必填的 LD 默认选中缓存第一个，避免空值
      form[p.key] = p.required && ldCache.length ? ldCache[0] : ''
    } else if (p.type === 'ln-cascade') {
      // 级联二选：LD → LN
      form[p.key] = { ld: '', ln: '' }
    } else if (p.type === 'refs-list') {
      // 动态引用列表：初始一行空引用（级联模式为 LD/LN/DO/DA 对象）
      form[p.key] = p.cascade ? [{ ld: '', ln: '', do: '', da: '' }] : ['']
    } else {
      form[p.key] = p.default ?? (p.type === 'switch' ? false : '')
    }
  }
}

/** 动态引用列表：追加一行。 */
function addRefs() {
  const p = def.value.params.find((x) => x.type === 'refs-list')
  if (!p) return
  form[p.key].push(p.cascade ? { ld: '', ln: '', do: '', da: '' } : '')
}

/** 动态引用列表：删除指定行。 */
function removeRefs(i) {
  const key = def.value.params.find((p) => p.type === 'refs-list')?.key
  if (key) form[key].splice(i, 1)
}

/** 级联引用行的 DO 选项缓存：key = "LD/LN|fc"（经 all-def 轻量查询）。 */
const rowDoRefs = reactive({})
function rowDoKey(row) {
  const fc = String(form.fc || '').split(':')[0] || 'XX'
  return `${row.ld}/${row.ln}|${fc}`
}
function rowDoOptions(row) {
  if (!row.ld || !row.ln) return []
  return rowDoRefs[rowDoKey(row)] || []
}
async function loadRowDo(row) {
  if (!row.ld || !row.ln) return
  const key = rowDoKey(row)
  if (rowDoRefs[key]) return
  const fc = String(form.fc || '').split(':')[0] || 'XX'
  try {
    const res = await executeJson(`all-def --ln ${row.ld}/${row.ln} --fc ${fc} --json`)
    rowDoRefs[key] = res.success && Array.isArray(res.data) ? res.data.map((d) => d.ref).filter(Boolean) : []
  } catch {
    rowDoRefs[key] = []
  }
}
/** LD 变化：清空下级并加载该 LD 的 LN 列表。 */
function onRowLd(row) {
  row.ln = ''
  row.do = ''
  row.da = ''
  if (row.ld) ensureLdLns(row.ld)
}
/** LN 变化：清空 DO/DA 并懒加载 DO 列表。 */
function onRowLn(row) {
  row.do = ''
  row.da = ''
  loadRowDo(row)
}
/** DO 变化：清空 DA 并懒加载 DA 列表。 */
function onRowDo(row) {
  row.da = ''
  loadRowDa(row)
}

/** 级联引用行的 DA 选项缓存：key = "LD/LN.DO"（经 data-dir 轻量查询）。 */
const rowDaRefs = reactive({})
function rowDaKey(row) {
  if (!row.ld || !row.ln || !row.do) return ''
  return `${row.ld}/${row.ln}.${row.do}`
}
function rowDaOptions(row) {
  const key = rowDaKey(row)
  if (!key) return []
  return rowDaRefs[key] || []
}
async function loadRowDa(row) {
  const key = rowDaKey(row)
  if (!key || rowDaRefs[key]) return
  try {
    const res = await executeJson(`data-dir --ref ${key} --json`)
    rowDaRefs[key] = res.success && Array.isArray(res.data)
      ? res.data.map((s) => String(s).replace(/^\[[A-Z]+\]\s+/, '')).filter(Boolean)
      : []
  } catch {
    rowDaRefs[key] = []
  }
}

// 级联引用：fc 变化时重新加载各行的 DO 选项（缓存按 fc 分键）
watch(() => form.fc, () => {
  const p = def.value.params.find((x) => x.type === 'refs-list' && x.cascade)
  const rows = p ? form[p.key] : null
  if (!Array.isArray(rows)) return
  for (const row of rows) {
    if (row && row.ld && row.ln) loadRowDo(row)
  }
})

/** ln 必填的命令：进入页面时预加载全量 LN 引用并默认选中第一个。 */
const LN_REQUIRED_CMDS = ['ln-dir', 'all-data', 'all-def', 'all-cb']

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
    const p = def.value.params.find((x) => x.key === 'ln' && x.type === 'ln-cascade')
    const o = p ? form[p.key] : null
    if (o && !o.ld && ldCache.length) {
      o.ld = ldCache[0]
      const lns = ldLns[o.ld] || []
      if (lns.length) o.ln = lns[0]
    }
  }
}, { immediate: true })

// ld-dir：选中 LD 后预加载其 LN 列表；未选 LD 时预加载全量完整引用（供 after 下拉）
// 同时监听 ldCache 长度：直接进入页面时缓存可能还没填充，填充后自动补拼全量引用
// after 级联联动：上面 ld 变化时同步 after.ld（单设备模式 after 只带 LN）
watch([() => form.ld, () => ldCache.length], async ([ld]) => {
  if (props.cmd !== 'ld-dir') return
  if (ld) {
    await ensureLdLns(ld)
    const o = form.after
    if (o) {
      o.ld = ld
      o.ln = ''
    }
  } else {
    await ensureAllLnRefs()
    const o = form.after
    if (o && !o.ln) o.ld = ''
  }
}, { immediate: true })

// ln 必填命令：等 LD 缓存就绪后自动选中第一个 LD + 其下第一个 LN（覆盖连接后才进入/缓存在填充中的情况）
watch([() => allLnRefs.length, () => ldCache.length], async () => {
  if (!LN_REQUIRED_CMDS.includes(props.cmd)) return
  const p = def.value.params.find((x) => x.key === 'ln' && x.type === 'ln-cascade')
  const o = p ? form[p.key] : null
  if (!o || o.ld) return
  await ensureAllLnRefs()
  if (ldCache.length && !o.ld) {
    o.ld = ldCache[0]
    const lns = ldLns[o.ld] || []
    if (lns.length) o.ln = lns[0]
  }
})

// ln-dir / all-cb：ln / acsi 变化时懒加载引用列表（供 after 下拉）
watch([lnRef, () => form.acsi], async () => {
  if (!lnRef.value) return
  if (props.cmd === 'ln-dir') {
    const acsi = String(form.acsi || '').split(':')[0] || '1'
    await ensureLnDirRefs(lnRef.value, acsi)
  } else if (props.cmd === 'all-cb') {
    const acsi = String(form.acsi || '').split(':')[0] || 'brcb'
    await ensureAllCbRefs(lnRef.value, acsi)
  }
}, { immediate: true })

// all-data / all-def：ln / fc 变化时懒加载该 LN 下的 DO 引用（经轻量 all-def 查询，供 after 下拉）
watch([lnRef, () => form.fc], async () => {
  if (!['all-data', 'all-def'].includes(props.cmd) || !lnRef.value) return
  const fc = String(form.fc || '').split(':')[0] || 'XX'
  await ensureAllDefRefs(lnRef.value, fc)
}, { immediate: true })

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
      // disabled 参数（如协议固定值）仅展示，不拼入命令
      if (p.disabled) continue
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
    } else if (p.type === 'ln-cascade') {
      const o = v || {}
      if (o.ld) {
        // ld-dir 的 after：上面已选 ld 时按单设备模式只带 LN（服务端拒绝带前缀），否则带完整 LD/LN
        const ref = props.cmd === 'ld-dir' && p.key === 'after' && form.ld
          ? o.ln
          : (o.ln ? `${o.ld}/${o.ln}` : o.ld)
        if (ref) {
          parts.push(`--${p.key}`, ref)
        }
      }
    } else if (p.type === 'ln-ref-select') {
      if (v) {
        parts.push(`--${p.key}`, String(v))
      }
    } else if (p.type === 'refs-list') {
      const rows = (v || []).filter(Boolean)
      let refs
      if (p.cascade) {
        // 级联行 → "LD/LN[.DO[.DA]]" 完整引用
        refs = rows.map((row) => {
          if (!row.ld || !row.ln) return ''
          let ref = `${row.ld}/${row.ln}`
          if (row.do) ref += `.${row.do}`
          if (row.da) ref += `.${row.da}`
          return ref
        }).filter(Boolean)
      } else {
        refs = rows.filter((r) => typeof r === 'string' && r)
      }
      if (refs.length) {
        // 多个引用用引号包裹成单参数（后端 --refs "r1 r2 ..."），避免空格拆分
        parts.push(`--${p.key}`, `"${refs.join(' ')}"`)
      }
    } else if (v !== '' && v !== null && v !== undefined) {
      parts.push(`--${p.key}`, String(v))
    }
  }
  if (jsonMode.value) {
    parts.push('--json')
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
    // 连接管理页：按 --json 开关决定是否补 --json（开关开才解析结构化消息）
    const final = isConnect.value && jsonMode.value && !cmdLine.includes('--json') ? `${cmdLine} --json` : cmdLine
    const text = await executeCommand(final)
    const clean = text.replace(/\x1b\[\d+m/g, '').trim()
    result.value = {
      cmd: final,
      output: clean,
      time: new Date().toLocaleTimeString(),
    }
    // 终端里保留 ANSI 颜色（由 Terminal.vue 的 parseAnsi 渲染）
    pushTerminal([`$ ${final}`, text.trim()])
    if (isConnect.value) {
      const res = parseResult(clean)
      if (res) {
        connMsg.value = res.success ? (res.message || '操作成功') : (res.error || '操作失败')
        connMsgOk.value = !!res.success
      } else {
        connMsg.value = '命令已执行（未启用 --json）'
        connMsgOk.value = true
      }
    }
  } catch (e) {
    result.value = {
      cmd: cmdLine,
      output: String(e),
      time: new Date().toLocaleTimeString(),
    }
    pushTerminal([`$ ${cmdLine}`, 'ERR ' + e])
    if (isConnect.value) {
      connMsg.value = String(e)
      connMsgOk.value = false
    }
  } finally {
    busy.value = false
  }
}

/** 从原始响应中提取 JSON 结果（连接管理页解析 connect/disconnect 成败），无 JSON 时返回 null。 */
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
  return null
}

/** 连接管理页：断开 TCP 连接。 */
async function disconnect() {
  await runCmd('disconnect')
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

.doc-btn {
  flex-shrink: 0;
  width: 26px;
  height: 26px;
  border: none;
  border-radius: 6px;
  background: transparent;
  font-size: 15px;
  line-height: 1;
  cursor: pointer;
  transition: background 0.12s, transform 0.12s;
}

.doc-btn:hover {
  background: var(--bg-hover);
  transform: scale(1.12);
}

.doc-desc {
  margin: 0 0 12px;
  font-size: 13px;
  line-height: 1.6;
  color: var(--text-secondary);
}

.doc-desc.muted {
  color: var(--text-muted);
}

.doc-asn1 {
  margin: 0;
  padding: 12px 14px;
  background: var(--bg-primary);
  border: 1px solid var(--border);
  border-radius: 8px;
  font-size: 12px;
  line-height: 1.6;
  color: var(--text-secondary);
  overflow-x: auto;
  white-space: pre;
}

/* ── 协议说明：markdown 渲染（v-html 内容需 :deep 才生效） ── */
.doc-md {
  font-size: 13px;
  line-height: 1.8;
  color: var(--text-secondary);
}

/* 章节花纹：每个二级标题一种主题色，顶部有彩色渐变花纹线 + 花纹字符 */
.doc-md :deep(h2) {
  --doc-color: var(--accent);
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 34px 0 14px;
  padding: 11px 16px;
  font-size: 15px;
  font-weight: 700;
  letter-spacing: 0.5px;
  color: var(--doc-color);
  border-radius: 10px;
  border: 1px solid color-mix(in srgb, var(--doc-color) 35%, transparent);
  background: color-mix(in srgb, var(--doc-color) 9%, transparent);
}

.doc-md :deep(h2:nth-of-type(1)) { --doc-color: #7c8cf8; }
.doc-md :deep(h2:nth-of-type(2)) { --doc-color: #4caf7d; }
.doc-md :deep(h2:nth-of-type(3)) { --doc-color: #e5b955; }
.doc-md :deep(h2:nth-of-type(4)) { --doc-color: #e56a7f; }
.doc-md :deep(h2:nth-of-type(5)) { --doc-color: #5b8def; }
.doc-md :deep(h2:nth-of-type(6)) { --doc-color: #8a5ce0; }

/* 花纹字符（章节前缀） */
.doc-md :deep(h2::before) {
  content: '❖';
  flex-shrink: 0;
  font-size: 12px;
  color: var(--doc-color);
  text-shadow: 0 0 8px var(--doc-color);
}

/* 小节标题：绿色菱形点缀 */
.doc-md :deep(h3) {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 20px 0 8px;
  font-size: 13.5px;
  font-weight: 700;
  color: var(--text-primary);
}

.doc-md :deep(h3::before) {
  content: '◆';
  flex-shrink: 0;
  font-size: 9px;
  color: var(--green);
  text-shadow: 0 0 6px var(--green);
}

.doc-md :deep(p) {
  margin: 8px 0;
}

/* 列表：彩色项目符号 / 编号 */
.doc-md :deep(ul),
.doc-md :deep(ol) {
  margin: 8px 0;
  padding-left: 24px;
}

.doc-md :deep(li) {
  margin: 4px 0;
}

.doc-md :deep(ul li::marker) {
  color: var(--accent);
}

.doc-md :deep(ol li::marker) {
  color: var(--green);
  font-weight: 700;
}

/* 加粗：暖金高亮 */
.doc-md :deep(strong) {
  color: #ffc163;
  font-weight: 600;
}

/* 行内代码 */
.doc-md :deep(code) {
  font-family: var(--font-mono);
  font-size: 12px;
  background: var(--accent-muted);
  border: 1px solid color-mix(in srgb, var(--accent) 35%, transparent);
  border-radius: 5px;
  padding: 1px 6px;
  color: var(--accent-hover);
}

.doc-md :deep(pre) {
  background: var(--bg-tertiary);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 12px 14px;
  overflow-x: auto;
  font-size: 12px;
  line-height: 1.6;
}

.doc-md :deep(pre code) {
  background: none;
  border: none;
  padding: 0;
}

/* 表格：居中 + 圆角 + 渐变彩色表头 + 斑马纹 */
.doc-md :deep(table) {
  width: fit-content;
  max-width: 100%;
  margin: 14px auto;
  border-collapse: separate;
  border-spacing: 0;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.35);
}

.doc-md :deep(th) {
  padding: 9px 16px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.5px;
  text-align: left;
  color: #fff;
  background: linear-gradient(135deg, #5b8def, #7a5ce0);
  border: none;
}

.doc-md :deep(td) {
  padding: 8px 16px;
  font-size: 12px;
  border: none;
  border-bottom: 1px solid var(--border);
  color: var(--text-secondary);
}

.doc-md :deep(tbody tr:nth-child(even)) {
  background: rgba(255, 255, 255, 0.025);
}

.doc-md :deep(tbody tr:hover) {
  background: var(--bg-hover);
}

.doc-md :deep(td:first-child) {
  color: var(--text-primary);
  font-weight: 600;
}

.doc-md :deep(tr:last-child td) {
  border-bottom: none;
}

/* 引用块：绿色侧边 */
.doc-md :deep(blockquote) {
  margin: 10px 0;
  padding: 8px 14px;
  border-radius: 0 8px 8px 0;
  background: var(--green-bg);
  border-left: 3px solid var(--green);
  color: var(--text-secondary);
}

.doc-md :deep(hr) {
  border: none;
  height: 2px;
  margin: 18px 0;
  border-radius: 2px;
  background: linear-gradient(90deg, transparent, var(--accent), transparent);
}

.doc-md :deep(a) {
  color: var(--accent-hover);
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

/* ── 动态引用列表（refs-list） ── */
.refs-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.refs-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.refs-row .ui-select {
  flex: 1;
  min-width: 0;
}

/* ── 级联二选（ln-cascade：LD → LN） ── */
.cascade-pair {
  display: flex;
  align-items: center;
  gap: 8px;
}

.cascade-pair .ui-select {
  flex: 1;
  min-width: 0;
}

.refs-del {
  flex-shrink: 0;
  width: 26px;
  height: 26px;
  border: none;
  border-radius: 6px;
  background: transparent;
  color: var(--text-muted);
  font-size: 12px;
  cursor: pointer;
  transition: background 0.12s, color 0.12s;
}

.refs-del:hover {
  background: var(--red-bg);
  color: var(--red);
}

.refs-add {
  align-self: flex-start;
  border: 1px dashed var(--border);
  background: transparent;
  color: var(--text-secondary);
  border-radius: 8px;
  padding: 6px 12px;
  font-size: 12px;
  cursor: pointer;
  transition: border-color 0.12s, color 0.12s;
}

.refs-add:hover {
  border-color: var(--accent);
  color: var(--accent);
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

/* ── 连接管理页：结果消息 ── */
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

/* ── 连接流程提示 ── */
.tip {
  margin: 12px 0 0;
  font-size: 12px;
  color: var(--text-muted);
}

.tip code {
  color: var(--accent);
}

/* ── 命令预览 ── */
.json-opt {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 8px;
}

.json-opt-label {
  font-size: 12px;
  color: var(--text-secondary);
}

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
