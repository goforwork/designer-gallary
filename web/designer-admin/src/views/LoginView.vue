<template>
  <div class="min-h-screen flex items-center justify-center bg-pale-bg p-4">
    <div
      class="w-full max-w-md bg-white rounded-2xl shadow-sm border border-stone-200 p-8"
    >
      <h1 class="text-2xl font-semibold text-center mb-2">设计师管理后台</h1>
      <p class="text-warm-gray text-center text-sm mb-8">请登录以继续</p>

      <form class="space-y-5" @submit.prevent="handleLogin">
        <div class="space-y-2">
          <label class="text-sm font-medium">用户名</label>
          <input
            v-model="username"
            type="text"
            required
            :class="inputClass"
          />
        </div>

        <div class="space-y-2">
          <label class="text-sm font-medium">密码</label>
          <input
            v-model="password"
            type="password"
            required
            :class="inputClass"
          />
        </div>

        <p v-if="error" class="text-sm text-red-600">{{ error }}</p>

        <button
          type="submit"
          class="w-full py-2.5 text-sm font-medium text-white bg-charcoal rounded-lg hover:bg-charcoal/90"
        >
          登录
        </button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const error = ref('')

const inputClass =
  'w-full px-4 py-2.5 rounded-lg border border-stone-200 focus:outline-none focus:ring-2 focus:ring-taupe/30 focus:border-taupe'

async function handleLogin() {
  error.value = ''
  try {
    await authStore.login(username.value, password.value)
    router.push({ name: 'Dashboard' })
  } catch (err) {
    error.value = err instanceof Error ? err.message : '登录失败'
  }
}
</script>
