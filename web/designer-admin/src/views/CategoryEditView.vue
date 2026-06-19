<template>
  <Layout>
    <div class="max-w-2xl mx-auto">
      <h2 class="text-xl font-semibold mb-6">
        {{ isNew ? '新建分类' : '编辑分类' }}
      </h2>
      <CategoryForm :category="category" @submit="handleSubmit" />
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Layout from '@/components/Layout.vue'
import CategoryForm from '@/components/CategoryForm.vue'
import { api } from '@/api/client'
import type { Category } from '@/types'

const route = useRoute()
const router = useRouter()

const category = ref<Category | null>(null)
const isNew = computed(() => route.params.id === 'new' || !route.params.id)

onMounted(async () => {
  if (!isNew.value) {
    category.value = await api.getCategory(route.params.id as string)
  }
})

async function handleSubmit(data: Partial<Category>) {
  if (isNew.value) {
    await api.createCategory(data)
  } else {
    await api.updateCategory(route.params.id as string, data)
  }
  router.push({ name: 'CategoryList' })
}
</script>
