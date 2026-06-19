<template>
  <aside
    :class="[
      'fixed inset-y-0 left-0 z-40 w-64 bg-white border-r border-stone-200 transform transition-transform duration-200 lg:translate-x-0',
      isOpen ? 'translate-x-0' : '-translate-x-full',
    ]"
  >
    <div class="h-16 flex items-center px-6 border-b border-stone-200">
      <span class="text-lg font-semibold text-charcoal">设计师管理后台</span>
    </div>

    <nav class="p-4 space-y-1">
      <router-link
        v-for="item in menuItems"
        :key="item.name"
        :to="{ name: item.name }"
        :class="[
          'flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium transition-colors',
          isActive(item.name)
            ? 'bg-stone-100 text-charcoal'
            : 'text-warm-gray hover:bg-stone-50 hover:text-charcoal',
        ]"
        @click="$emit('close')"
      >
        <component :is="item.icon" class="w-5 h-5" />
        {{ item.label }}
      </router-link>
    </nav>
  </aside>
</template>

<script setup lang="ts">
import { FolderOpen, Image, LayoutDashboard, Settings } from 'lucide-vue-next'
import { useRoute } from 'vue-router'

defineProps<{
  isOpen: boolean
}>()

defineEmits<{
  close: []
}>()

const route = useRoute()

const menuItems = [
  { name: 'Dashboard', label: '概览', icon: LayoutDashboard },
  { name: 'WorkList', label: '作品管理', icon: Image },
  { name: 'CategoryList', label: '分类管理', icon: FolderOpen },
  { name: 'SiteConfig', label: '站点设置', icon: Settings },
]

function isActive(name: string) {
  return (
    route.name === name ||
    (name === 'WorkList' && route.name?.toString().startsWith('Work')) ||
    (name === 'CategoryList' && route.name?.toString().startsWith('Category'))
  )
}
</script>
