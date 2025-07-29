<template>
  <div class="winning-records">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>中奖记录</h2>
        </div>
      </template>
      
      <el-table :data="records" style="width: 100%" v-loading="loading">
        <el-table-column prop="prizeName" label="奖品名称" width="180" />
        <el-table-column prop="createTime" label="中奖时间" width="200">
          <template #default="{row}">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { getWinningRecords } from '../api/lottery'

interface WinningRecord {
  prizeName: string
  createTime: Date
}

const userStore = useUserStore()
const records = ref<WinningRecord[]>([])
const loading = ref(false)

const formatDate = (date: Date) => {
  return new Date(date).toLocaleString()
}

onMounted(async () => {
  if (!userStore.token) {
    ElMessage.error('请先登录')
    return
  }
  
  try {
    loading.value = true
    const res = await getWinningRecords()
    console.log('API response:', res) // 添加详细日志
    if (res.data && Array.isArray(res.data)) {
      records.value = res.data.map(item => ({
        prizeName: item.prizeName || '未知奖品',
        createTime: item.createTime || new Date()
      }))
    } else {
      console.warn('Unexpected response format:', res)
    }
  } catch (error) {
    ElMessage.error('获取中奖记录失败')
    console.error('Error details:', error)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.winning-records {
  padding: 20px;
}
</style>