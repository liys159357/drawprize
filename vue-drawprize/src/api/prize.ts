import axios from 'axios'
import { useUserStore } from '@/stores/user'

const api = axios.create({
  baseURL: 'http://localhost:8080'
})

// 添加请求拦截器
api.interceptors.request.use(config => {
  const userStore = useUserStore()
  console.log('Interceptor token:', userStore.token)
  if (userStore.token) {
    // config.headers['token'] = userStore.token // 只保留token字段
    config.headers['token'] = localStorage.getItem('token') // 只保留token字段
    config.headers['authorization'] = localStorage.getItem('token') // 只保留token字段
    console.log('Request headers:', config.headers)
  }
  return config
})

export const getPrizes = () => api.get('/api/prizes/available')
export const drawPrize = (userId: string) => api.get(`/api/lottery/draw/${userId}`) // 修改为GET请求并添加路径参数
export const login = (data: {username: string, password: string}) => 
  api.post('/users/login', data)
export const register = (data: {username: string, password: string}) => 
  api.post('/users/register', data)