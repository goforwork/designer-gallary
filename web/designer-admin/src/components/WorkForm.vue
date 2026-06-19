<template>
  <form class="space-y-8" @submit.prevent="handleSubmit">
    <section class="bg-white rounded-xl border border-stone-200 p-6 space-y-6">
      <h2 class="text-base font-semibold">基本信息</h2>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div class="space-y-2">
          <label class="text-sm font-medium">标题</label>
          <input
            v-model="form.title"
            type="text"
            required
            :class="inputClass"
          />
        </div>

        <div class="space-y-2">
          <label class="text-sm font-medium">Slug</label>
          <input
            v-model="form.slug"
            type="text"
            required
            :class="inputClass"
          />
        </div>

        <div class="space-y-2">
          <label class="text-sm font-medium">分类</label>
          <select v-model="form.categoryId" :class="inputClass">
            <option :value="null">无分类</option>
            <option
              v-for="category in categories"
              :key="category.id"
              :value="category.id"
            >
              {{ category.name }}
            </option>
          </select>
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
        <textarea
          v-model="form.description"
          rows="4"
          :class="textareaClass"
        />
      </div>

      <div class="space-y-2">
        <label class="text-sm font-medium">标签（用逗号分隔）</label>
        <input v-model="tagsInput" type="text" :class="inputClass" />
      </div>

      <label class="flex items-center gap-2">
        <input
          v-model="form.isVisible"
          type="checkbox"
          class="w-4 h-4 rounded border-stone-300 text-taupe focus:ring-taupe"
        />
        <span class="text-sm">可见</span>
      </label>
    </section>

    <section class="bg-white rounded-xl border border-stone-200 p-6 space-y-6">
      <h2 class="text-base font-semibold">封面图</h2>
      <ImageUploader v-model="coverImage" />
    </section>

    <section class="bg-white rounded-xl border border-stone-200 p-6 space-y-6">
      <h2 class="text-base font-semibold">详情图片</h2>
      <div class="space-y-4">
        <div
          v-for="(image, index) in form.images ?? []"
          :key="index"
          class="relative rounded-lg overflow-hidden border border-stone-200 aspect-video"
        >
          <img
            :src="resolveImageUrl(image)"
            alt=""
            class="w-full h-full object-cover"
          />
          <button
            type="button"
            class="absolute top-2 right-2 p-1.5 bg-white/90 rounded-full shadow-sm hover:bg-white"
            @click="removeImage(index)"
          >
            <X class="w-4 h-4" />
          </button>
        </div>
        <ImageUploader v-model="pendingImage" @update:model-value="onDetailImageAdded" />
      </div>
    </section>

    <div class="flex justify-end gap-3">
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
import { computed, ref, watch } from 'vue'
import { X } from 'lucide-vue-next'
import type { Category, Work } from '@/types'
import ImageUploader from './ImageUploader.vue'

const props = defineProps<{
  work: Work | null
  categories: Category[]
}>()

const emit = defineEmits<{
  submit: [work: Partial<Work>]
}>()

const form = ref<Partial<Work>>({
  title: '',
  slug: '',
  categoryId: null,
  coverImage: null,
  images: [],
  description: '',
  tags: [],
  sortOrder: 0,
  isVisible: true,
})

const tagsInput = ref('')
const pendingImage = ref<string | null>(null)

const coverImage = computed({
  get: () => form.value.coverImage ?? null,
  set: (value) => {
    form.value.coverImage = value
  },
})

const inputClass =
  'w-full px-4 py-2 rounded-lg border border-stone-200 focus:outline-none focus:ring-2 focus:ring-taupe/30 focus:border-taupe'
const textareaClass = `${inputClass} resize-none`

watch(
  () => props.work,
  (work) => {
    if (work) {
      form.value = { ...work }
      tagsInput.value = work.tags.join(', ')
    }
  },
  { immediate: true },
)

watch(tagsInput, (value) => {
  form.value.tags = value
    .split(',')
    .map((tag) => tag.trim())
    .filter(Boolean)
})

function resolveImageUrl(url: string) {
  if (url.startsWith('http') || url.startsWith('/uploads')) {
    return url
  }
  return `/uploads/${url}`
}

function removeImage(index: number) {
  form.value.images = form.value.images?.filter((_, i) => i !== index) ?? []
}

function onDetailImageAdded(url: string | null) {
  if (url) {
    form.value.images = [...(form.value.images ?? []), url]
    pendingImage.value = null
  }
}

function handleSubmit() {
  emit('submit', { ...form.value })
}
</script>
