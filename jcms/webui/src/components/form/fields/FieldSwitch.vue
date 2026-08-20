<template>
  <UiSwitch :model-value="value" @update:model-value="value = $event" />
</template>

<script setup>
import { computed } from 'vue'
import UiSwitch from '../../ui/UiSwitch.vue'
import { useFormCtx } from '../formContext.js'

const props = defineProps({ param: { type: Object, required: true } })
const ctx = useFormCtx()

const value = computed({
  get: () => ctx.form[props.param.key],
  set: (v) => (ctx.form[props.param.key] = v),
})
</script>