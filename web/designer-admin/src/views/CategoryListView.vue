<template>
  <Layout>
    <div class="bg-white rounded-xl border border-stone-200 overflow-hidden">
      <div class="p-6 border-b border-stone-200 flex items-center justify-between">
        <h2 class="text-lg font-semibold">分类列表</h2>
        <router-link
          :to="{ name: 'CategoryNew' }"
          class="px-4 py-2 text-sm font-medium text-white bg-charcoal rounded-lg hover:bg-charcoal/90"
        >
          新建分类
        </router-link>
      </div>

      <div class="overflow-x-auto">
        <table class="w-full text-sm text-left">
          <thead class="bg-stone-50 text-warm-gray">
            <tr>
              <th class="px-6 py-3 font-medium">名称</th>
              <th class="px-6 py-3 font-medium">Slug</th>
              <th class="px-6 py-3 font-medium">排序</th>
              <th class="px-6 py-3 font-medium">可见</th>
              <th class="px-6 py-3 font-medium text-right">操作</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-stone-100">
            <tr
              v-for="category in categories"
              :key="category.id"
              class="hover:bg-stone-50"
            >
              <td class="px-6 py-4 font-medium">{{ category.name }}</td>
              <td class="px-6 py-4">{{ category.slug }}</td>
              <td class="px-6 py-4">{{ category.sortOrder }}</td>
              <td class="px-6 py-4">
                <button
                  :class="[
                    'relative inline-flex h-6 w-11 items-center rounded-full transition-colors',
                    category.isVisible ? 'bg-green-500' : 'bg-stone-300',
                  ]"
                  @click="toggleVisibility(category)"
                >
                  <span
                    :class="[
                      'inline-block h-4 w-4 transform rounded-full bg-white transition-transform',
                      category.isVisible ? 'translate-x-6' : 'translate-x-1',
                    ]"
                  />
                </button>
              </td>
              <td class="px-6 py-4 text-right space-x-3">
                <router-link
                  :to="{ name: 'CategoryEdit', params: { id: category.id } }"
                  class="text-taupe hover:text-charcoal"
                >
                  编辑
                </router-link>
                <button
                  class="text-red-600 hover:text-red-700"
                  @click="confirmDelete(category)"
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
      title="删除分类"
      message="确定要删除这个分类吗？此操作无法撤销。"
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
import type { Category } from '@/types'

const categories = ref<Category[]>([])
const deleteTarget = ref<Category | null>(null)

onMounted(async () => {
  await loadData()
})

async function loadData() {
  categories.value = await api.listCategories()
}

async function toggleVisibility(category: Category) {
  await api.updateCategory(category.id, { isVisible: !category.isVisible })
  await loadData()
}

function confirmDelete(category: Category) {
  deleteTarget.value = category
}

async function handleDelete() {
  if (!deleteTarget.value) return
  await api.deleteCategory(deleteTarget.value.id)
  deleteTarget.value = null
  await loadData()
}
</script>
