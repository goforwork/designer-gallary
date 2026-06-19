<template>
  <Layout>
    <div class="max-w-3xl mx-auto space-y-8">
      <section class="bg-white rounded-xl border border-stone-200 p-6 space-y-6">
        <h2 class="text-lg font-semibold">站点信息</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-2">
            <label class="text-sm font-medium">站点标题</label>
            <input v-model="form.siteTitle" type="text" :class="inputClass" />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium">品牌名</label>
            <input v-model="form.brandName" type="text" :class="inputClass" />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium">联系邮箱</label>
            <input
              v-model="form.contactEmail"
              type="email"
              :class="inputClass"
            />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium">联系电话</label>
            <input
              v-model="form.contactPhone"
              type="text"
              :class="inputClass"
            />
          </div>
        </div>
        <div class="space-y-2">
          <label class="text-sm font-medium">站点描述</label>
          <textarea
            v-model="form.siteDescription"
            rows="3"
            :class="textareaClass"
          />
        </div>
        <div class="space-y-2">
          <label class="text-sm font-medium">页脚文本</label>
          <textarea
            v-model="form.footerText"
            rows="2"
            :class="textareaClass"
          />
        </div>
      </section>

      <section class="bg-white rounded-xl border border-stone-200 p-6 space-y-6">
        <h2 class="text-lg font-semibold">社交媒体</h2>
        <div
          v-for="(link, index) in form.socialLinks"
          :key="index"
          class="grid grid-cols-1 md:grid-cols-2 gap-4 items-end"
        >
          <div class="space-y-2">
            <label class="text-sm font-medium">名称</label>
            <input v-model="link.name" type="text" :class="inputClass" />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium">链接</label>
            <div class="flex gap-2">
              <input v-model="link.url" type="url" :class="inputClass" />
              <button
                type="button"
                class="px-3 py-2 text-sm font-medium text-red-600 border border-stone-200 rounded-lg hover:bg-stone-50"
                @click="removeSocialLink(index)"
              >
                删除
              </button>
            </div>
          </div>
        </div>
        <button
          type="button"
          class="px-4 py-2 text-sm font-medium text-charcoal border border-stone-200 rounded-lg hover:bg-stone-50"
          @click="addSocialLink"
        >
          添加链接
        </button>
      </section>

      <section class="bg-white rounded-xl border border-stone-200 p-6 space-y-6">
        <h2 class="text-lg font-semibold">账号设置</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-2">
            <label class="text-sm font-medium">管理员用户名</label>
            <input
              v-model="form.adminUsername"
              type="text"
              :class="inputClass"
            />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium">管理员密码</label>
            <input
              v-model="form.adminPassword"
              type="password"
              placeholder="留空表示不修改"
              :class="inputClass"
            />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium">确认密码</label>
            <input
              v-model="confirmPassword"
              type="password"
              placeholder="留空表示不修改"
              :class="inputClass"
            />
          </div>
        </div>
      </section>

      <section class="bg-white rounded-xl border border-stone-200 p-6 space-y-6">
        <h2 class="text-lg font-semibold">Hero 配置</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div class="space-y-2">
            <label class="text-sm font-medium">眉题</label>
            <input
              v-model="form.heroConfig.eyebrow"
              type="text"
              :class="inputClass"
            />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium">主标题</label>
            <input
              v-model="form.heroConfig.titleLine"
              type="text"
              :class="inputClass"
            />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium">强调词</label>
            <input
              v-model="form.heroConfig.titleEmphasis"
              type="text"
              :class="inputClass"
            />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium">CTA 文字</label>
            <input
              v-model="form.heroConfig.ctaText"
              type="text"
              :class="inputClass"
            />
          </div>
          <div class="space-y-2">
            <label class="text-sm font-medium">CTA 目标</label>
            <input
              v-model="form.heroConfig.ctaTarget"
              type="text"
              :class="inputClass"
            />
          </div>
        </div>
        <div class="space-y-2">
          <label class="text-sm font-medium">副标题第一行</label>
          <input
            v-model="form.heroConfig.subtitleLine1"
            type="text"
            :class="inputClass"
          />
        </div>
        <div class="space-y-2">
          <label class="text-sm font-medium">副标题第二行</label>
          <input
            v-model="form.heroConfig.subtitleLine2"
            type="text"
            :class="inputClass"
          />
        </div>
        <div class="space-y-2">
          <label class="text-sm font-medium">背景媒体</label>
          <ImageUploader v-model="form.heroConfig.backgroundMedia" />
        </div>
      </section>

      <div class="flex justify-end">
        <button
          type="button"
          class="px-5 py-2.5 text-sm font-medium text-white bg-charcoal rounded-lg hover:bg-charcoal/90"
          @click="handleSave"
        >
          保存设置
        </button>
      </div>
    </div>
  </Layout>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import Layout from '@/components/Layout.vue'
import ImageUploader from '@/components/ImageUploader.vue'
import { api } from '@/api/client'
import type { SiteConfig } from '@/types'

const form = ref<SiteConfig>({
  siteTitle: '',
  siteDescription: '',
  brandName: '',
  contactEmail: '',
  contactPhone: '',
  socialLinks: [],
  footerText: '',
  heroConfig: {
    eyebrow: '',
    titleLine: '',
    titleEmphasis: '',
    subtitleLine1: '',
    subtitleLine2: '',
    backgroundMedia: '',
    ctaText: '',
    ctaTarget: '',
  },
  adminUsername: 'admin',
  adminPassword: '',
})

const confirmPassword = ref('')

const inputClass =
  'w-full px-4 py-2 rounded-lg border border-stone-200 focus:outline-none focus:ring-2 focus:ring-taupe/30 focus:border-taupe'
const textareaClass = `${inputClass} resize-none`

onMounted(async () => {
  const config = await api.getSiteConfig()
  form.value = config
})

function addSocialLink() {
  form.value.socialLinks.push({ name: '', url: '' })
}

function removeSocialLink(index: number) {
  form.value.socialLinks.splice(index, 1)
}

async function handleSave() {
  if (form.value.adminUsername.trim() === '') {
    alert('管理员用户名不能为空')
    return
  }
  if (form.value.adminPassword !== '') {
    if (form.value.adminPassword.length < 6) {
      alert('管理员密码长度不能少于 6 位')
      return
    }
    if (form.value.adminPassword !== confirmPassword.value) {
      alert('两次输入的密码不一致')
      return
    }
  }

  const payload: Partial<SiteConfig> = {
    ...form.value,
    adminPassword: form.value.adminPassword === '' ? undefined : form.value.adminPassword,
  }
  await api.updateSiteConfig(payload)
  alert('保存成功')
  confirmPassword.value = ''
}
</script>
