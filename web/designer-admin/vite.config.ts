import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 5174,
    proxy: {
      '/api': 'http://127.0.0.1:8081',
      '/uploads': 'http://127.0.0.1:8081',
    },
  },
  base: '/admin/',
  build: {
    outDir: '../../backend/designerAdminDist',
    emptyOutDir: true,
  },
})
