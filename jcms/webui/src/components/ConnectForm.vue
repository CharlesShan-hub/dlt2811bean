<template>
  <div class="connect-form">
    <div class="row-2">
      <div class="field">
        <label class="field-label">IP 地址</label>
        <UiInput v-model="form.ip" placeholder="127.0.0.1" />
      </div>
      <div class="field">
        <label class="field-label">端口</label>
        <UiInput v-model.number="form.port" type="number" readonly />
      </div>
    </div>

    <div class="field switch-field">
      <span class="field-label">TLS 加密</span>
      <UiSwitch v-model="form.secure" />
    </div>

    <div class="field switch-field">
      <span class="field-label">关联 AP</span>
      <UiSwitch v-model="form.withAp" />
    </div>

    <div v-if="form.withAp" class="field switch-field">
      <span class="field-label">应用层安全认证</span>
      <UiSwitch v-model="form.apsecure" />
    </div>

    <template v-if="form.withAp">
      <div class="divider"></div>

      <ApPicker v-model="form.ap" />

      <div class="row-pair">
        <div class="field">
          <label class="field-label">APDU 大小</label>
          <UiInput v-model.number="form.apdu" type="number" />
        </div>

        <div class="field">
          <label class="field-label">ASDU 大小</label>
          <UiInput v-model.number="form.asdu" type="number" />
        </div>
      </div>

      <div class="field">
        <label class="field-label">协议版本</label>
        <UiInput v-model.number="form.version" type="number" readonly />
      </div>
    </template>

    <div class="actions">
      <UiButton variant="primary" :loading="busy" @click="submit">{{ submitLabel }}</UiButton>
      <slot name="extra"></slot>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import ApPicker from './ApPicker.vue'
import UiButton from './ui/UiButton.vue'
import UiSwitch from './ui/UiSwitch.vue'
import UiInput from './ui/UiInput.vue'
import { executeJson } from '../api/cms.js'

defineProps({
  /** 执行期间按钮 loading 状态，由父组件控制 */
  busy: Boolean,
  /** 主按钮文案 */
  submitLabel: { type: String, default: '连接' },
})

const emit = defineEmits(['submit', 'update:cmd'])

const form = ref({ ap: '', ip: '127.0.0.1', port: 8102, secure: false, apsecure: false, withAp: true, apdu: 65535, asdu: 65531, version: 1 })

// 端口由 TLS 开关决定（只读展示）：TLS → 9102（sslPort），明文 → 8102
watch(() => form.value.secure, (secure) => {
  form.value.port = secure ? 9102 : 8102
})

// 表单任何变化都实时向外输出拼好的命令（供命令预览使用）
watch(form, () => emit('update:cmd', buildCmd()), { immediate: true, deep: true })

/** 读取当前 neg-cfg 配置并回填协商参数。 */
async function loadConfig() {
  try {
    const neg = await executeJson('neg-cfg --json')
    if (neg.success && neg.data) {
      form.value.apdu = neg.data.apduSize
      form.value.asdu = neg.data.asduSize
      form.value.version = neg.data.protocolVersion
    }
  } catch {
    // 协商参数读取失败时保留默认值
  }
}

/** 拼接 connect 命令（不含 --json），交由父组件执行。 */
function buildCmd() {
  const ip = form.value.ip.trim() || '127.0.0.1'
  const port = form.value.port || 8102
  const secure = form.value.secure ? ' --secure' : ''
  let cmd = `connect --ip ${ip} --port ${port}${secure}`
  if (form.value.withAp && form.value.ap.trim()) {
    cmd += ` --ap ${form.value.ap.trim()} --apdu ${form.value.apdu} --asdu ${form.value.asdu} --version ${form.value.version}`
    if (form.value.apsecure) {
      cmd += ' --apsecure'
    }
  }
  return cmd
}

function submit() {
  emit('submit', buildCmd())
}

onMounted(loadConfig)
</script>

<style scoped>
.field {
  margin-bottom: 20px;
}

.row-2 {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.row-2 .field {
  margin-bottom: 0;
}

.row-2 .field:first-child {
  margin-bottom: 20px;
}

.row-pair {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.row-pair .field {
  margin-bottom: 20px;
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
  display: flex;
  gap: 10px;
  margin-top: 24px;
}
</style>
