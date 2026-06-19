import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { api } from '@/api/client'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<{ username: string; role: string } | null>(null)
  const isAuthenticated = computed(() => user.value !== null)

  async function login(username: string, password: string) {
    const result = await api.login(username, password)
    user.value = result
  }

  async function logout() {
    await api.logout()
    user.value = null
  }

  async function fetchMe() {
    try {
      const result = await api.me()
      user.value = result
    } catch {
      user.value = null
    }
  }

  return {
    user,
    isAuthenticated,
    login,
    logout,
    fetchMe,
  }
})
