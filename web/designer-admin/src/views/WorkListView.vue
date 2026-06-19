<template>
  <Layout>
    <div class="bg-white rounded-xl border border-stone-200 overflow-hidden">
      <div class="p-6 border-b border-stone-200 flex items-center justify-between">
        <h2 class="text-lg font-semibold">作品列表</h2>
        <router-link
          :to="{ name: 'WorkNew' }"
          class="px-4 py-2 text-sm font-medium text-white bg-charcoal rounded-lg hover:bg-charcoal/90"
        >
          新建作品
        </router-link>
      </div>

      <div class="overflow-x-auto">
        <table class="w-full text-sm text-left">
          <thead class="bg-stone-50 text-warm-gray">
            <tr>
              <th class="px-6 py-3 font-medium">标题</th>
              <th class="px-6 py-3 font-medium">分类</th>
              <th class="px-6 py-3 font-medium">排序</th>
              <th class="px-6 py-3 font-medium">可见</th>
              <th class="px-6 py-3 font-medium text-right">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-stone-100">
            <tr
              v-for="work in works"
              :key="work.id"
              class="hover:bg-stone-50"
            >
              <td class="px-6 py-4 font-medium">{{ work.title }}</td>
              <td class="px-6 py-4">{{ categoryName(work.categoryId) }}</td>
              <td class="px-6 py-4">{{ work.sortOrder }}</td>
              <td class="px-6 py-4">
                <button
                  :class="[
                    'relative inline-flex h-6 w-11 items-center rounded-full transition-colors',
                    work.isVisible ? 'bg-green-500' : 'bg-stone-300',
                  ]"
                  @click="toggleVisibility(work)"
                >
                  <span
                    :class="[
                      'inline-block h-4 w-4 transform rounded-full bg-white transition-transform',
                      work.isVisible ? 'translate-x-6' : 'translate-x-1',
                    ]"
                  />
                </button>
              </td>
              <td class="px-6 py-4 text-right space-x-3">
                <router-link
                  :to="{ name: 'WorkEdit', params: { id: work.id } }"
                  class="text-taupe hover:text-charcoal"
                >
                  编辑
                </router-link>
                <button
                  class="text-red-600 hover:text-red-700"
                  @click="confirmDelete(work)"
                >
                  删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <ConfirmDialog
      :open="deleteTarget !== null"
      title="删除作品"
      message="确定要删除这个作品吗？此操作无法撤销。"
      @confirm="handleDelete"
      @cancel="deleteTarget = null"
    />
  </Layout>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import Layout from '@/components/Layout.vue'
import ConfirmDialog from '@/components/ConfirmDialog.vue'
import { api } from '@/api/client'
import type { Category, Work } from '@/types'

const works = ref<Work[]>([])
const categories = ref<Category[]>([])
const deleteTarget = ref<Work | null>(null)

onMounted(async () => {
  await loadData()
})

async function loadData() {
  const [worksData, categoriesData] = await Promise.all([
    api.listWorks(),
    api.listCategories(),
  ])
  works.value = worksData
  categories.value = categoriesData
}

function categoryName(categoryId: string | null) {
  if (!categoryId) return '-'
  return categories.value.find((c) => c.id === categoryId)?.name || '-'
}

async function toggleVisibility(work: Work) {
  await api.updateWork(work.id, { isVisible: !work.isVisible })
  await loadData()
}

function confirmDelete(work: Work) {
  deleteTarget.value = work
}

async function handleDelete() {
  if (!deleteTarget.value) return
  await api.deleteWork(deleteTarget.value.id)
  deleteTarget.value = null
  await loadData()
}
</script>
