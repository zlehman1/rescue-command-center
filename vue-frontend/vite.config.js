import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import vuetify from 'vite-plugin-vuetify';
import dotenv from 'dotenv';

dotenv.config();

export default defineConfig({
  plugins: [
    vue(),
    vuetify({ autoImport: true })
  ],
  build: {
    sourcemap: true,
  },
  server: {
    proxy: {
      '/api': {
        target: `http://${process.env.VITE_BACKEND_IP}:${process.env.VITE_BACKEND_PORT}`,
        changeOrigin: true,
        secure: false
      }
    }
  }
});