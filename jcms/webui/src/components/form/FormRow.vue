<template>
  <div class="field-row" :class="{ single: !inline }">
    <slot />
  </div>
</template>

<script setup>
defineProps({
  /** 非空表示本行是并排一组的 inline 组合，否则为单字段行。 */
  inline: { type: String, default: null },
})
</script>

<style scoped>
.field-row {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
}
</style>