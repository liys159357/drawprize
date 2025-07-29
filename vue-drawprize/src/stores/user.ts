import { defineStore } from 'pinia'
import { login, register } from '@/api/prize'
import router from '@/router'  // 添加router导入

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userId: localStorage.getItem('userId') || '',
    userInfo: null
  }),
  actions: {
    async login(username: string, password: string) {
      const res = await login({ username, password })
      this.token = res.data.token
      this.userId = res.data.userId
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('userId', res.data.userId)
      
      // 使用导入的router对象
      router.push('/records')
    },
    async register(username: string, password: string) {
      const res = await register({ username, password })
      // 注册成功后可以自动登录或跳转到登录页面
      return res
    }
  }
})