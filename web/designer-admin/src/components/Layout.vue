<template>
  <div class="min-h-screen flex">
    <Sidebar :is-open="sidebarOpen" @close="sidebarOpen = false" />

    <div
      v-if="sidebarOpen"
      class="fixed inset-0 bg-black/20 z-30 lg:hidden"
      @click="sidebarOpen = false"
    />

    <div class="flex-1 flex flex-col min-w-0 lg:ml-64">
      <header
        class="sticky top-0 z-20 bg-white/80 backdrop-blur border-b border-stone-200 px-6 py-4 flex items-center justify-between"
      >
        <button
          class="lg:hidden p-2 -ml-2 rounded-lg hover:bg-stone-100"
          @click="sidebarOpen = true"
        >
          <Menu class="w-5 h-5" />
        </button>
        <h1 class="text-lg font-semibold">{{ title }}</h1>
        <div class="flex items-center gap-3">
          <span class="text-sm text-warm-gray hidden sm:inline">
            {{ authStore.user?.username }}
          </span>
          <button
            class="text-sm text-warm-gray hover:text-charcoal"
            @click="handleLogout"
          >
            退出
          </button>
        </div>
      </header>

      <main class="flex-1 p-6">
        <slot />
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { Menu } from 'lucide-vue-next'
import { useRoute, useRouter } from 'vue-router'
import Sidebar from './Sidebar.vue'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const sidebarOpen = ref(false)

const title = computed(() => {
  return (route.meta.title as string) || '管理后台'
})

async function handleLogout() {
  await authStore.logout()
  router.push({ name: 'Login' })
}
</script>
