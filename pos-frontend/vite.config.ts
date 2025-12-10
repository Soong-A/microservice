// https://vitejs.dev/config/
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  // 新增 server 配置
  server: {
    // 明确指定前端开发服务器端口（可选，与您当前端口一致即可）
    port: 3000,
    // 配置代理规则，解决跨域问题
    proxy: {
      // 商品服务 API
      '/api/products': {
        target: 'http://localhost:8082',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/products/, '/api/products')
      },

      // 购物车服务 API
      '/api/cart': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/cart/, '/api/cart')
      },

      // 订单服务 API
      '/api/orders': {
        target: 'http://localhost:8083',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/orders/, '/api/orders')
      },

      // 计数器服务 API
      '/api/counter': {
        target: 'http://localhost:8084',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/counter/, '/api/counter')
      },

      // 配送服务 API
      '/api/delivery': {
        target: 'http://localhost:8085',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/delivery/, '/api/delivery')
      },

      // 如果需要通过网关统一访问，可以配置网关代理
      '/api/gateway': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api\/gateway/, '')
      }
    }
  }
});