<script setup lang="ts">
</script>

<template>
  <div class="fullscreen-container">
    <el-card class="fullscreen-card">
      <div class="content">
        <img src="@/assets/logo.svg" alt="抽奖logo" class="logo">
        <h2 class="title">幸运抽奖</h2>
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
  background: 
    linear-gradient(145deg, #1e88e5, #0d47a1),
    radial-gradient(circle at 20% 50%, rgba(255,255,255,0.2) 0%, transparent 40%),
    radial-gradient(circle at 80% 30%, rgba(255,255,255,0.1) 0%, transparent 40%);
  background-blend-mode: overlay;
  animation: gradientShift 15s ease infinite alternate;
}

@keyframes gradientShift {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

.fullscreen-card {
  width: 100%;
  height: 100%;
  border-radius: 0;
  box-shadow: none;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.fullscreen-card::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, transparent 70%);
  animation: rotate 15s linear infinite;
}

@keyframes rotate {
  100% { transform: rotate(360deg); }
}

.prize-card {
  width: 1000px;
  border-radius: 28px;
  box-shadow: 
    0 15px 30px rgba(0,0,0,0.3),
    0 0 0 2px rgba(255,255,255,0.1);
  background: linear-gradient(145deg, #ffffff, #f5f7fa);
  border: none;
  overflow: hidden;
  position: relative;
  transform-style: preserve-3d;
  transition: all 0.5s cubic-bezier(0.23, 1, 0.32, 1);
}

.prize-card:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 20px 40px rgba(0,0,0,0.4);
}

.prize-card::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(45deg, 
    rgba(41,128,185,0.08) 0%, 
    rgba(255,255,255,0.15) 50%, 
    rgba(41,128,185,0.08) 100%);
  animation: shine 4s ease-in-out infinite alternate;
}

@keyframes shine {
  from { transform: translateX(-100%) skew(-15deg); }
  to { transform: translateX(100%) skew(-15deg); }
}

.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 500px;
  padding: 2rem;
  position: relative;
  z-index: 1;
}

.logo {
  width: 250px;
  height: 250px;
  margin-bottom: 2rem;
  filter: 
    drop-shadow(0 10px 20px rgba(0,0,0,0.2))
    brightness(1.1);
  transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.logo:hover {
  transform: scale(1.1) rotate(8deg);
  filter: 
    drop-shadow(0 15px 30px rgba(0,0,0,0.3))
    brightness(1.2);
}

.title {
  font-size: 3.5rem;
  background: linear-gradient(to right, #2980b9, #1a237e);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-align: center;
  font-weight: 900;
  text-shadow: 
    0 3px 6px rgba(255,255,255,0.5),
    0 6px 12px rgba(41,128,185,0.3);
  margin-top: 2rem;
  position: relative;
  letter-spacing: 3px;
  font-family: 'Microsoft YaHei', sans-serif;
  padding: 0.5rem 2rem;
  border-radius: 50px;
  background-color: rgba(255,255,255,0.2);
  backdrop-filter: blur(5px);
  border: 1px solid rgba(255,255,255,0.3);
}
</style>
