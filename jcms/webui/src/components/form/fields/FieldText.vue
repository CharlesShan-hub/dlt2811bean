<template>
  <UiInput
    :model-value="value"
    :type="isNumber ? 'number' : 'text'"
    :placeholder="info.placeholder"
    :readonly="info.readonly"
    @update:model-value="value = $event"
  />
</template>

<script setup>
import { computed } from 'vue'
import UiInput from '../../ui/UiInput.vue'
import { useFormCtx } from '../formContext.js'

const props = defineProps({ param: { type: Object, required: true } })
const ctx = useFormCtx()
const info = computed(() => props.param)

const isNumber = computed(() => props.param.type === 'number')
/** 直接读写 ctx.form[param.key]，保持与 buildCmd 相同的键。 */
const value = computed({
  get: () => ctx.form[props.param.key],
  set: (v) => (ctx.form[props.param.key] = v),
})
</script>