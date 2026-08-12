<template>
  <div class="svc-view">
    <!-- 标题信息 -->
    <header class="page-head">
      <div class="title-row">
        <div class="title-left">
          <span class="sec-badge">✦ 定值组服务</span>
          <h1 class="page-title">定值组</h1>
        </div>
        <div class="title-right">
          <code class="glass cmd-chip">Setting Group</code>
          <span class="sep">·</span>
          <span class="desc-text">选择激活/编辑定值组、读写定值、确认提交（DL/T 2811 §8.6）</span>
        </div>
      </div>
    </header>

    <!-- 三块空白面板（后续填充内容） -->
    <div class="svc-panels">
      <section class="svc-panel"></section>
      <section class="svc-panel"></section>
      <section class="svc-panel"></section>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.svc-view {
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

.sec-badge {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: rgba(91, 141, 239, 0.15);
  color: var(--accent);
  border-radius: 999px;
  padding: 2px 10px;
  font-size: 12px;
  font-weight: 600;
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(91, 141, 239, 0.2);
}

.cmd-chip {
  font-family: var(--font-mono);
  font-size: 12px;
  color: var(--text-primary);
  border-radius: 5px;
  padding: 1px 8px;
}

.sep {
  color: var(--text-muted);
}

.desc-text {
  color: var(--text-secondary);
}

/* 三块空白面板 */
.svc-panels {
  flex: 1;
  min-height: 0;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 12px;
}

.svc-panel {
  background: var(--bg-tertiary);
  border: 1px solid var(--border);
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}
</style>
