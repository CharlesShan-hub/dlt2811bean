<template>
  <section class="ui-card">
    <header v-if="title || $slots.header" class="ui-card__header">
      <span v-if="icon" class="ui-card__icon">{{ icon }}</span>
      <span class="ui-card__title">{{ title }}</span>
      <slot name="header" />
    </header>
    <div class="ui-card__body">
      <slot />
    </div>
  </section>
</template>

<script setup>
defineProps({
  title: String,
  icon: String,
})
</script>

<style scoped>
.ui-card {
  background: var(--bg-tertiary);
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.ui-card__header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
}

.ui-card__icon {
  font-size: 15px;
}

.ui-card__title {
  font-size: 12px;
  font-weight: 600;
  color: var(--text-muted);
  text-transform: uppercase;
  letter-spacing: 0.8px;
}
</style>
