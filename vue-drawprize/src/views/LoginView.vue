<template>
  <div class="fullscreen-container">
    <el-card class="fullscreen-card">
      <div class="content">
        <h2 class="title">用户登录</h2>
        <el-form class="form" label-position="top">
          <el-form-item label="用户名">
            <el-input 
              v-model="form.username" 
              placeholder="请输入用户名"
              clearable
            />
          </el-form-item>
          <el-form-item label="密码">
            <el-input 
              v-model="form.password" 
              type="password" 
              placeholder="请输入密码"
              show-password
            />
          </el-form-item>
          <el-button 
            type="primary" 
            class="submit-btn" 
            @click="handleLogin"
            :loading="loading"
          >
            登录
          </el-button>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.fullscreen-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(145deg, #1e88e5, #0d47a1);
}

.fullscreen-card {
  width: 100%;
  height: 100%;
  border-radius: 0;
  box-shadow: none;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(12px);
  display: flex;
  justify-content: center;
  align-items: center;
}

.content {
  padding: 2rem;
}

.title {
  font-size: 2.5rem;
  margin-bottom: 2rem;
  color: #2c3e50;
  text-align: center;
}

.form {
  margin-top: 1rem;
}

.submit-btn {
  width: 100%;
  margin-top: 1.5rem;
  height: 40px;
  font-size: 1rem;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #5a5e66;
}
</style>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const form = reactive({
  username: '',
  password: ''
})
const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  try {
    await userStore.login(form.username, form.password)
    ElMessage.success('登录成功')
    router.push('/draw')  // 直接跳转，不再延迟
  } finally {
    loading.value = false
  }
}
</script>