import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vuetify from 'vite-plugin-vuetify'

export default defineConfig({
  plugins: [
      vue(),
      vuetify({ autoImport: true })
  ],
  build: {
    sourcemap: true, // Enable source maps
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:9191',
        changeOrigin: true,
        secure: false
      }
    }
  }
})