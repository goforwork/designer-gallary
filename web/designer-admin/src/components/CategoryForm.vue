<template>
  <form
    class="bg-white rounded-xl border border-stone-200 p-6 space-y-6"
    @submit.prevent="handleSubmit"
  >
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="space-y-2">
        <label class="text-sm font-medium">名称</label>
        <input v-model="form.name" type="text" required :class="inputClass" />
      </div>

      <div class="space-y-2">
        <label class="text-sm font-medium">Slug</label>
        <input v-model="form.slug" type="text" required :class="inputClass" />
      </div>

      <div class="space-y-2">
        <label class="text-sm font-medium">排序</label>
        <input
          v-model.number="form.sortOrder"
          type="number"
          :class="inputClass"
        />
      </div>
    </div>

    <div class="space-y-2">
      <label class="text-sm font-medium">描述</label>
      <textarea v-model="form.description" rows="4" :class="textareaClass" />
    </div>

    <label class="flex items-center gap-2">
      <input
        v-model="form.isVisible"
        type="checkbox"
        class="w-4 h-4 rounded border-stone-300 text-taupe focus:ring-taupe"
      />
      <span class="text-sm">可见</span>
    </label>

    <div class="flex justify-end gap-3 pt-4">
      <button
        type="button"
        class="px-5 py-2.5 text-sm font-medium text-warm-gray border border-stone-200 rounded-lg hover:bg-stone-50"
        @click="$router.back()"
      >
        取消
      </button>
      <button
        type="submit"
        class="px-5 py-2.5 text-sm font-medium text-white bg-charcoal rounded-lg hover:bg-charcoal/90"
      >
        保存
      </button>
    </div>
  </form>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import type { Category } from '@/types'

const props = defineProps<{
  category: Category | null
}>()

const emit = defineEmits<{
  submit: [category: Partial<Category>]
}>()

const form = ref<Partial<Category>>({
  name: '',
  slug: '',
  description: '',
  sortOrder: 0,
  isVisible: true,
})

const inputClass =
  'w-full px-4 py-2 rounded-lg border border-stone-200 focus:outline-none focus:ring-2 focus:ring-taupe/30 focus:border-taupe'
const textareaClass = `${inputClass} resize-none`

watch(
  () => props.category,
  (category) => {
    if (category) {
      form.value = { ...category }
    }
  },
  { immediate: true },
)

function handleSubmit() {
  emit('submit', { ...form.value })
}
</script>
