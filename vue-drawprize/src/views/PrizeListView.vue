<template>
  <div class="fullscreen-container">
    <el-card class="fullscreen-card">
      <h2 class="title">奖品列表</h2>
      <el-table :data="prizes">
        <el-table-column prop="name" label="奖品名称" />
        <el-table-column prop="remaining" label="剩余数量" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { getPrizes } from '@/api/prize'

const prizes = ref([])

const fetchPrizes = async () => {
  const res = await getPrizes()
  prizes.value = res.data
}

fetchPrizes()
</script>

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
  padding: 2rem;
}

.title {
  font-size: 2.5rem;
  margin-bottom: 2rem;
  color: #2c3e50;
  text-align: center;
}
</style>