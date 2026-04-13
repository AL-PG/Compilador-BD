import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

const rawBackendUrl = process.env.VITE_API_BASE_URL?.trim() || 'http://localhost:8080'
const backendTarget = rawBackendUrl.replace(/\/api\/?$/i, '')

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    proxy: {
      '/api': {
        target: backendTarget,
        changeOrigin: true,
      },
    },
  },
})
