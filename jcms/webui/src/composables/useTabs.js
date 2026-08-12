/**
 * 标签页管理 composable
 * 管理 App.vue 的主内容标签：打开、切换、关闭、排序、固定
 */
import { ref, computed } from 'vue'
import { CMD_DEFS } from '../cmddefs/index.js'

/** 从 CMD_DEFS title 中提取中文短名（"关联 associate (8.2.1)" → "关联"） */
export function cnTitle(id) {
  const t = CMD_DEFS[id] && CMD_DEFS[id].title ? CMD_DEFS[id].title : id
  return t.split(' ')[0] || t
}

/** 各功能块视图 → 标签标题（侧边栏大块与标签页一一对应） */
const VIEW_TITLES = {
  'connect-root': '连接管理',
  'dir-tree': '目录与数据',
  'dataset-view': '数据集',
  'sg-view': '定值组',
  'report-view': '报告服务',
  'log-view': '日志服务',
  'goose-view': 'GOOSE',
  'msv-view': '多播采样值',
  'file-view': '文件服务',
  'rpc-view': 'RPC 接口',
}

/** 根据 viewId 获取标签标题 */
function tabTitle(viewId) {
  return VIEW_TITLES[viewId] || cnTitle(viewId)
}

export function useTabs() {
  const tabs = ref([
    { id: 'tab-connect', viewId: 'connect-root', title: '连接管理', pinned: false, persistent: true },
    { id: 'tab-dir', viewId: 'dir-tree', title: '目录与数据', pinned: false, persistent: true },
  ])
  const activeTab = ref('tab-connect')
  let nextTabId = 3

  /** 从当前标签推导 activeView，用于侧边栏高亮 */
  const activeView = computed(() => {
    const tab = tabs.value.find((t) => t.id === activeTab.value)
    return tab ? tab.viewId : ''
  })

  /** 打开/切换标签 */
  function openTab(viewId, forceNew) {
    // 双击（forceNew=true）：总是新建一个 persistent 标签
    if (forceNew) {
      const id = 'tab-' + (nextTabId++)
      tabs.value.push({ id, viewId, title: tabTitle(viewId), pinned: false, persistent: true })
      activeTab.value = id
      return
    }

    // 单击：先看是否已有相同 viewId 的标签（不管什么类型）
    const existing = tabs.value.find((t) => t.viewId === viewId)
    if (existing) {
      activeTab.value = existing.id
      return
    }

    // 没找到：找可替换的标签（非 persistent、非 pinned）
    const replaceable = tabs.value.find((t) => !t.persistent && !t.pinned)
    if (replaceable) {
      replaceable.viewId = viewId
      replaceable.title = tabTitle(viewId)
      replaceable.pinned = false
      activeTab.value = replaceable.id
      return
    }

    // 没有可替换的，新建一个 non-persistent 标签
    const id = 'tab-' + (nextTabId++)
    tabs.value.push({ id, viewId, title: tabTitle(viewId), pinned: false, persistent: false })
    activeTab.value = id
  }

  function switchTab(id) {
    activeTab.value = id
  }

  function closeTab(id) {
    const idx = tabs.value.findIndex((t) => t.id === id)
    if (idx === -1) return
    // 允许关闭最后一个标签，显示空状态背景
    tabs.value.splice(idx, 1)
    if (tabs.value.length === 0) {
      activeTab.value = ''
      return
    }
    // 如果关闭的是当前标签，切换到相邻标签
    if (activeTab.value === id) {
      const newIdx = idx > 0 ? idx - 1 : 0
      activeTab.value = tabs.value[newIdx]?.id || ''
    }
  }

  /** 关闭指定索引左边的所有非固定标签 */
  function closeLeft(index) {
    const toRemove = tabs.value.slice(0, index).filter((t) => !t.pinned)
    const activeLost = toRemove.some((t) => t.id === activeTab.value)
    toRemove.forEach((t) => {
      const i = tabs.value.findIndex((x) => x.id === t.id)
      if (i !== -1) tabs.value.splice(i, 1)
    })
    if (activeLost) {
      activeTab.value = tabs.value[0]?.id || ''
    }
  }

  /** 关闭指定索引右边的所有非固定标签 */
  function closeRight(index) {
    const toRemove = tabs.value.slice(index + 1).filter((t) => !t.pinned)
    const activeLost = toRemove.some((t) => t.id === activeTab.value)
    toRemove.forEach((t) => {
      const i = tabs.value.findIndex((x) => x.id === t.id)
      if (i !== -1) tabs.value.splice(i, 1)
    })
    if (activeLost) {
      activeTab.value = tabs.value[index]?.id || ''
    }
  }

  /** 关闭除指定索引以外的所有非固定标签 */
  function closeOthers(index) {
    const toRemove = tabs.value.filter((t, i) => i !== index && !t.pinned)
    const target = tabs.value[index]
    toRemove.forEach((t) => {
      const i = tabs.value.findIndex((x) => x.id === t.id)
      if (i !== -1) tabs.value.splice(i, 1)
    })
    activeTab.value = target?.id || tabs.value[0]?.id || ''
  }

  /** 关闭所有非固定标签（有 pin 的保留） */
  function closeAll() {
    const toRemove = tabs.value.filter((t) => !t.pinned)
    toRemove.forEach((t) => {
      const i = tabs.value.findIndex((x) => x.id === t.id)
      if (i !== -1) tabs.value.splice(i, 1)
    })
    activeTab.value = tabs.value[0]?.id || ''
  }

  /** 切换固定状态 */
  function togglePin(id) {
    const tab = tabs.value.find((t) => t.id === id)
    if (tab) {
      tab.pinned = !tab.pinned
    }
  }

  /** 拖动排序 */
  function reorderTab({ from, to }) {
    const [moved] = tabs.value.splice(from, 1)
    tabs.value.splice(to, 0, moved)
  }

  return {
    tabs,
    activeTab,
    activeView,
    openTab,
    switchTab,
    closeTab,
    closeLeft,
    closeRight,
    closeOthers,
    closeAll,
    togglePin,
    reorderTab,
  }
}