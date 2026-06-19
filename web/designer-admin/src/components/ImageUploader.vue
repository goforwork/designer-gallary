<template>
  <div class="space-y-3">
    <div
      :class="[
        'border-2 border-dashed rounded-lg p-6 text-center transition-colors cursor-pointer',
        dragging
          ? 'border-taupe bg-stone-50'
          : 'border-stone-300 hover:border-taupe',
      ]"
      @dragenter.prevent="dragging = true"
      @dragleave.prevent="dragging = false"
      @dragover.prevent
      @drop.prevent="handleDrop"
      @click="triggerFileInput"
    >
      <input
        ref="fileInput"
        type="file"
        accept="image/*"
        class="hidden"
        @change="handleFileChange"
      />
      <Upload class="w-8 h-8 mx-auto text-taupe mb-2" />
      <p class="text-sm text-warm-gray">
        {{ uploading ? '上传中...' : '点击或拖拽上传图片' }}
      </p>
    </div>

    <div
      v-if="modelValue"
      class="relative rounded-lg overflow-hidden border border-stone-200 aspect-video"
    >
      <img
        :src="resolveImageUrl(modelValue)"
        alt="Preview"
        class="w-full h-full object-cover"
      />
      <button
        type="button"
        class="absolute top-2 right-2 p-1.5 bg-white/90 rounded-full shadow-sm hover:bg-white"
        @click.stop="removeImage"
      >
        <X class="w-4 h-4" />
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Upload, X } from 'lucide-vue-next'
import { api } from '@/api/client'

defineProps<{
  modelValue: string | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: string | null]
}>()

const fileInput = ref<HTMLInputElement | null>(null)
const uploading = ref(false)
const dragging = ref(false)

function triggerFileInput() {
  fileInput.value?.click()
}

function resolveImageUrl(url: string) {
  if (url.startsWith('http') || url.startsWith('/uploads')) {
    return url
  }
  return `/uploads/${url}`
}

async function handleFileChange(event: Event) {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    await uploadFile(file)
  }
}

async function handleDrop(event: DragEvent) {
  dragging.value = false
  const file = event.dataTransfer?.files[0]
  if (file) {
    await uploadFile(file)
  }
}

async function uploadFile(file: File) {
  uploading.value = true
  try {
    const url = await api.uploadImage(file)
    emit('update:modelValue', url)
  } finally {
    uploading.value = false
  }
}

function removeImage() {
  emit('update:modelValue', null)
}
</script>
