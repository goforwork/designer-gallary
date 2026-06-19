<template>
  <Layout>
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="bg-white rounded-xl border border-stone-200 p-6">
        <div class="flex items-center gap-4">
          <div class="p-3 bg-stone-100 rounded-lg">
            <Image class="w-6 h-6 text-taupe" />
          </div>
          <div>
            <p class="text-sm text-warm-gray">作品总数</p>
            <p class="text-3xl font-semibold">{{ stats.workCount }}</p>
          </div>
        </div>
      </div>

      <div class="bg-white rounded-xl border border-stone-200 p-6">
        <div class="flex items-center gap-4">
          <div class="p-3 bg-stone-100 rounded-lg">
            <FolderOpen class="w-6 h-6 text-taupe" />
          </div>
          <div>
            <p class="text-sm text-warm-gray">分类总数</p>
            <p class="text-3xl font-semibold">{{ stats.categoryCount }}</p>
          </div>
        </div>
      </div>
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { FolderOpen, Image } from 'lucide-vue-next'
import Layout from '@/components/Layout.vue'
import { api } from '@/api/client'

const stats = ref({ workCount: 0, categoryCount: 0 })

onMounted(async () => {
  const [works, categories] = await Promise.all([
    api.listWorks(),
    api.listCategories(),
  ])
  stats.value = {
    workCount: works.length,
    categoryCount: categories.length,
  }
})
</script>
