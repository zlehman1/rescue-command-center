import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vuetify from 'vite-plugin-vuetify'

export default defineConfig({
  plugins: [
      vue(),
      vuetify({ autoImport: true })
  ],
  build: {
    sourcemap: true,
  },
  server: {
    host: true,
    proxy: {
      '/api': {
        target: 'http://host.docker.internal:9191',
        changeOrigin: true,
        secure: false
      }
    }
  }
})