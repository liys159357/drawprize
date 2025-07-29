import axios from 'axios'
import { useUserStore } from '@/stores/user'

export const getWinningRecords = async () => {
  const userStore = useUserStore()
  return axios.get('/api/lottery/records', {
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
    headers: {
      'token': userStore.token,
      'authorization': userStore.token
    }
  })
}