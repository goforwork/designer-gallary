<template>
  <Layout>
    <div class="max-w-4xl mx-auto">
      <h2 class="text-xl font-semibold mb-6">
        {{ isNew ? '新建作品' : '编辑作品' }}
      </h2>
      <WorkForm :work="work" :categories="categories" @submit="handleSubmit" />
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Layout from '@/components/Layout.vue'
import WorkForm from '@/components/WorkForm.vue'
import { api } from '@/api/client'
import type { Category, Work } from '@/types'

const route = useRoute()
const router = useRouter()

const work = ref<Work | null>(null)
const categories = ref<Category[]>([])
const isNew = computed(() => route.params.id === 'new' || !route.params.id)

onMounted(async () => {
  categories.value = await api.listCategories()
  if (!isNew.value) {
    work.value = await api.getWork(route.params.id as string)
  }
})

async function handleSubmit(data: Partial<Work>) {
  if (isNew.value) {
    await api.createWork(data)
  } else {
    await api.updateWork(route.params.id as string, data)
  }
  router.push({ name: 'WorkList' })
}
</script>
