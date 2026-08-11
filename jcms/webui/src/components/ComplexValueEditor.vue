<template>
  <teleport to="body">
    <transition name="ui-modal">
      <div v-if="visible" class="cve-overlay" @click.self="cancel">
        <div class="cve-panel">
          <div class="cve-head">
            <span class="cve-title">编辑值 — {{ typeLabel }}</span>
            <span class="cve-type-badge">{{ type }}</span>
            <button type="button" class="cve-close" title="关闭" @click="cancel">✕</button>
          </div>
          <div class="cve-body">
            <!-- 简单类型：文本输入 -->
            <template v-if="isSimple">
              <div class="cve-field">
                <label class="cve-label">值</label>
                <UiInput
                  v-if="isNumeric"
                  v-model="localValue"
                  type="number"
                  placeholder="输入数值"
                  class="cve-input"
                />
                <UiInput
                  v-else
                  v-model="localValue"
                  placeholder="输入值"
                  class="cve-input"
                />
              </div>
            </template>

            <!-- quality 结构化表单 -->
            <template v-else-if="type === 'quality'">
              <div class="cve-field">
                <label class="cve-label">validity</label>
                <UiSelect
                  v-model="qualityValidity"
                  :options="[
                    { value: '0', label: 'good (0)' },
                    { value: '1', label: 'invalid (1)' },
                    { value: '2', label: 'reserved (2)' },
                    { value: '3', label: 'questionable (3)' },
                  ]"
                  class="cve-input"
                />
              </div>
              <div class="cve-field cve-checkbox-field">
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="qualityBits.overflow" />
                  <span>overflow</span>
                </label>
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="qualityBits.outOfRange" />
                  <span>outOfRange</span>
                </label>
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="qualityBits.badReference" />
                  <span>badReference</span>
                </label>
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="qualityBits.oscillatory" />
                  <span>oscillatory</span>
                </label>
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="qualityBits.failure" />
                  <span>failure</span>
                </label>
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="qualityBits.oldData" />
                  <span>oldData</span>
                </label>
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="qualityBits.inconsistent" />
                  <span>inconsistent</span>
                </label>
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="qualityBits.inaccurate" />
                  <span>inaccurate</span>
                </label>
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="qualityBits.substituted" />
                  <span>substituted</span>
                </label>
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="qualityBits.test" />
                  <span>test</span>
                </label>
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="qualityBits.operatorBlocked" />
                  <span>operatorBlocked</span>
                </label>
              </div>
            </template>

            <!-- utc-time 结构化表单 -->
            <template v-else-if="type === 'utc-time'">
              <div class="cve-field">
                <label class="cve-label">secondsSinceEpoch (Unix 秒)</label>
                <UiInput v-model.number="utcSeconds" type="number" placeholder="例如 1893456000" class="cve-input" />
              </div>
              <div class="cve-field">
                <label class="cve-label">fractionOfSecond (微秒)</label>
                <UiInput v-model.number="utcFraction" type="number" placeholder="0–999999" class="cve-input" />
              </div>
              <div class="cve-section-title">TimeQuality</div>
              <div class="cve-field cve-checkbox-field">
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="utcTq.leap_seconds_known" />
                  <span>leap_seconds_known</span>
                </label>
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="utcTq.clock_failure" />
                  <span>clock_failure</span>
                </label>
                <label class="cve-checkbox">
                  <input type="checkbox" v-model="utcTq.clock_not_synchronized" />
                  <span>clock_not_synchronized</span>
                </label>
              </div>
              <div class="cve-field">
                <label class="cve-label">precision (5-bit)</label>
                <UiInput v-model.number="utcTq.precision" type="number" placeholder="0–31" class="cve-input" />
              </div>
              <div class="cve-actions cve-actions--inline">
                <UiButton variant="ghost" @click="fillNow">填充当前时间</UiButton>
              </div>
            </template>

            <!-- binary-time 结构化表单 -->
            <template v-else-if="type === 'binary-time'">
              <div class="cve-field">
                <label class="cve-label">msOfDay (自午夜毫秒数)</label>
                <UiInput v-model.number="btMsOfDay" type="number" placeholder="0–86399999" class="cve-input" />
              </div>
              <div class="cve-field">
                <label class="cve-label">daysSince1984 (自1984-01-01天数)</label>
                <UiInput v-model.number="btDays" type="number" placeholder="例如 15543" class="cve-input" />
              </div>
              <div class="cve-actions cve-actions--inline">
                <UiButton variant="ghost" @click="fillNowBinary">填充当前时间</UiButton>
              </div>
            </template>
          </div>
          <div class="cve-foot">
            <span class="cve-preview">预览: {{ previewValue }}</span>
            <div class="cve-foot-actions">
              <UiButton variant="ghost" @click="cancel">取消</UiButton>
              <UiButton variant="primary" @click="confirm">确定</UiButton>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </teleport>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import UiInput from './ui/UiInput.vue'
import UiSelect from './ui/UiSelect.vue'
import UiButton from './ui/UiButton.vue'

const props = defineProps({
  visible: { type: Boolean, default: false },
  /** 当前值字符串 */
  modelValue: { type: String, default: '' },
  /** 类型，如 'int32', 'quality', 'utc-time', 'binary-time' */
  type: { type: String, default: '' },
})

const emit = defineEmits(['update:modelValue', 'update:visible', 'confirm'])

// ── 简单类型列表 ──
const simpleTypes = ['boolean', 'int8', 'int16', 'int32', 'int64', 'int8u', 'int16u', 'int32u', 'int64u',
  'float32', 'float64', 'visible-string', 'unicode-string', 'octet-string', 'bit-string',
  'INT8', 'INT16', 'INT32', 'INT64', 'INT8U', 'INT16U', 'INT32U', 'INT64U',
  'FLOAT32', 'FLOAT64', 'BOOLEAN', 'ENUM', 'ENUMERATED', 'CODED_ENUM',
  'dbpos', 'tcmd', 'check']

const numericTypes = ['int8', 'int16', 'int32', 'int64', 'int8u', 'int16u', 'int32u', 'int64u',
  'float32', 'float64', 'INT8', 'INT16', 'INT32', 'INT64', 'INT8U', 'INT16U', 'INT32U', 'INT64U',
  'FLOAT32', 'FLOAT64', 'ENUM', 'ENUMERATED', 'CODED_ENUM']

const isSimple = computed(() => simpleTypes.includes(props.type))
const isNumeric = computed(() => numericTypes.includes(props.type))

const typeLabel = computed(() => {
  const map = {
    'quality': 'Quality',
    'utc-time': 'UtcTime',
    'binary-time': 'BinaryTime',
  }
  return map[props.type] || props.type
})

// ── 本地编辑状态 ──
const localValue = ref('')

// quality
const qualityValidity = ref('0')
const qualityBits = ref({
  overflow: false,
  outOfRange: false,
  badReference: false,
  oscillatory: false,
  failure: false,
  oldData: false,
  inconsistent: false,
  inaccurate: false,
  substituted: false,
  test: false,
  operatorBlocked: false,
})

// utc-time
const utcSeconds = ref(0)
const utcFraction = ref(0)
const utcTq = ref({
  leap_seconds_known: false,
  clock_failure: false,
  clock_not_synchronized: false,
  precision: 24,
})

// binary-time
const btMsOfDay = ref(0)
const btDays = ref(0)

// ── 从当前值初始化 ──
function initFromValue(val) {
  if (!val) {
    // 默认值
    qualityValidity.value = '0'
    Object.keys(qualityBits.value).forEach(k => qualityBits.value[k] = false)
    utcSeconds.value = 0
    utcFraction.value = 0
    utcTq.value = { leap_seconds_known: false, clock_failure: false, clock_not_synchronized: false, precision: 24 }
    btMsOfDay.value = 0
    btDays.value = 0
    localValue.value = ''
    return
  }

  // 尝试解析 JSON
  let parsed = null
  try {
    if (val.startsWith('{') || val.startsWith('[')) {
      parsed = JSON.parse(val)
    }
  } catch { /* not JSON */ }

  if (parsed && typeof parsed === 'object') {
    // JSON 格式
    if (props.type === 'quality') {
      qualityValidity.value = String(parsed.validity ?? 0)
      qualityBits.value.overflow = !!parsed.overflow
      qualityBits.value.outOfRange = !!parsed.outOfRange
      qualityBits.value.badReference = !!parsed.badReference
      qualityBits.value.oscillatory = !!parsed.oscillatory
      qualityBits.value.failure = !!parsed.failure
      qualityBits.value.oldData = !!parsed.oldData
      qualityBits.value.inconsistent = !!parsed.inconsistent
      qualityBits.value.inaccurate = !!parsed.inaccurate
      qualityBits.value.substituted = !!parsed.substituted
      qualityBits.value.test = !!parsed.test
      qualityBits.value.operatorBlocked = !!parsed.operatorBlocked
    } else if (props.type === 'utc-time') {
      utcSeconds.value = parsed.secondsSinceEpoch ?? 0
      utcFraction.value = parsed.fractionOfSecond ?? 0
      const tq = parsed.timeQuality || {}
      utcTq.value = {
        leap_seconds_known: !!tq.leap_seconds_known,
        clock_failure: !!tq.clock_failure,
        clock_not_synchronized: !!tq.clock_not_synchronized,
        precision: tq.precision ?? 24,
      }
    } else if (props.type === 'binary-time') {
      btMsOfDay.value = parsed.msOfDay ?? 0
      btDays.value = parsed.daysSince1984 ?? 0
    } else {
      localValue.value = val
    }
  } else {
    // 简单值或 hex 格式
    localValue.value = val
  }
}

watch(() => props.visible, (v) => {
  if (v) {
    initFromValue(props.modelValue)
  }
})

// ── 预览 ──
const previewValue = computed(() => {
  if (isSimple.value) {
    return localValue.value || '(空)'
  }
  if (props.type === 'quality') {
    return JSON.stringify({
      validity: parseInt(qualityValidity.value),
      ...Object.fromEntries(Object.entries(qualityBits.value).map(([k, v]) => [k, v])),
    })
  }
  if (props.type === 'utc-time') {
    return JSON.stringify({
      secondsSinceEpoch: utcSeconds.value,
      fractionOfSecond: utcFraction.value,
      timeQuality: { ...utcTq.value },
    })
  }
  if (props.type === 'binary-time') {
    return JSON.stringify({
      msOfDay: btMsOfDay.value,
      daysSince1984: btDays.value,
    })
  }
  return localValue.value || '(空)'
})

// ── 填充当前时间 ──
function fillNow() {
  const now = Date.now()
  utcSeconds.value = Math.floor(now / 1000)
  utcFraction.value = (now % 1000) * 1000
  utcTq.value = {
    leap_seconds_known: false,
    clock_failure: false,
    clock_not_synchronized: false,
    precision: 24,
  }
}

function fillNowBinary() {
  const now = new Date()
  btMsOfDay.value = now.getHours() * 3600000 + now.getMinutes() * 60000 + now.getSeconds() * 1000 + now.getMilliseconds()
  // 1984-01-01 to now
  const ref = new Date(1984, 0, 1)
  btDays.value = Math.floor((now.getTime() - ref.getTime()) / 86400000)
}

// ── 确认 / 取消 ──
function confirm() {
  let result
  if (isSimple.value) {
    result = localValue.value
  } else if (props.type === 'quality') {
    result = JSON.stringify({
      validity: parseInt(qualityValidity.value),
      ...Object.fromEntries(Object.entries(qualityBits.value).map(([k, v]) => [k, v])),
    })
  } else if (props.type === 'utc-time') {
    result = JSON.stringify({
      secondsSinceEpoch: utcSeconds.value,
      fractionOfSecond: utcFraction.value,
      timeQuality: { ...utcTq.value },
    })
  } else if (props.type === 'binary-time') {
    result = JSON.stringify({
      msOfDay: btMsOfDay.value,
      daysSince1984: btDays.value,
    })
  } else {
    result = localValue.value
  }
  emit('update:modelValue', result)
  emit('confirm', result)
  emit('update:visible', false)
}

function cancel() {
  emit('update:visible', false)
}
</script>

<style scoped>
.cve-overlay {
  position: fixed;
  inset: 0;
  z-index: 2000;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.5);
  padding: 24px;
}

.cve-panel {
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 520px;
  max-height: 80vh;
  background: var(--bg-secondary);
  border: 1px solid var(--border);
  border-radius: 12px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.5);
  overflow: hidden;
}

.cve-head {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 18px;
  border-bottom: 1px solid var(--border);
  flex-shrink: 0;
}

.cve-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.cve-type-badge {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  background: var(--accent-muted);
  color: var(--accent);
  font-size: 11px;
  font-weight: 500;
  font-family: var(--font-mono);
}

.cve-close {
  margin-left: auto;
  flex-shrink: 0;
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 6px;
  background: transparent;
  color: var(--text-muted);
  font-size: 14px;
  cursor: pointer;
  transition: background 0.12s, color 0.12s;
}

.cve-close:hover {
  background: var(--bg-hover);
  color: var(--text-primary);
}

.cve-body {
  padding: 18px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.cve-field {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.cve-label {
  font-size: 12px;
  font-weight: 500;
  color: var(--text-muted);
  font-family: var(--font-mono);
}

.cve-input {
  /* inherits from UiInput */
}

.cve-checkbox-field {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 6px;
}

.cve-checkbox {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 8px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.12s;
  font-size: 13px;
  color: var(--text-primary);
}

.cve-checkbox:hover {
  background: var(--bg-hover);
}

.cve-checkbox input[type="checkbox"] {
  accent-color: var(--accent);
}

.cve-section-title {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  padding-top: 4px;
  border-top: 1px solid var(--border);
}

.cve-actions {
  display: flex;
  gap: 8px;
}

.cve-actions--inline {
  justify-content: flex-start;
}

.cve-foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 12px 18px;
  border-top: 1px solid var(--border);
  flex-shrink: 0;
}

.cve-preview {
  font-size: 12px;
  color: var(--text-muted);
  font-family: var(--font-mono);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 240px;
}

.cve-foot-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

/* transition */
.ui-modal-enter-active,
.ui-modal-leave-active {
  transition: opacity 0.15s;
}

.ui-modal-enter-active .cve-panel,
.ui-modal-leave-active .cve-panel {
  transition: transform 0.15s;
}

.ui-modal-enter-from,
.ui-modal-leave-to {
  opacity: 0;
}

.ui-modal-enter-from .cve-panel,
.ui-modal-leave-to .cve-panel {
  transform: translateY(12px) scale(0.98);
}
</style>