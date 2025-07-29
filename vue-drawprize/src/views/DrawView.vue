<template>
  <div class="fullscreen-container">
    <el-card class="fullscreen-card">
      <div class="draw-container">
        <!-- 扭蛋球区域 -->
        <div class="gacha-container">
          <h2 class="title">幸运扭蛋球</h2>
          <div class="ball-container">
            <div 
              v-for="(ball, index) in visibleBalls" 
              :key="index"
              class="ball"
              :ref="el => { if(el) ballRefs[index] = el as HTMLElement }"
              :style="{ backgroundColor: prizeColors[index % prizeColors.length] }">
            </div>
          </div>
          <div class="result-ball" :class="{ show: result }">
            <div class="prize-name">{{ result?.prizeName }}</div>
          </div>
          <el-button
            type="primary"
            class="draw-btn"
            @click="startSpin"
            :disabled="isSpinning || !canDraw">
            {{ isSpinning ? '抽奖中...' : '开始抽奖' }}
          </el-button>
        </div>
        
        <!-- 中奖记录区域 -->
        <div class="records-container">
          <WinningRecordsView />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { drawPrize, getPrizes } from '@/api/prize'
import WinningRecordsView from './WinningRecordsView.vue'

const router = useRouter()
// 添加prizeColors常量声明
const prizeColors = [
  '#FF5252', '#FF4081', '#E040FB', '#7C4DFF',
  '#536DFE', '#448AFF', '#40C4FF', '#18FFFF'
]

interface Prize {
  id: number
  name: string
  remaining: number
  total?: number  // 可选字段，如果需要的话
  enabled?: boolean  // 可选字段，如果需要的话
}

interface LotteryResult {
  userId: string
  prizeId: number | null
  prizeName: string
  win: boolean
  timestamp: number
}

const userStore = useUserStore()
const prizes = ref<Prize[]>([])
const isSpinning = ref(false)
const canDraw = ref(true)
const spinDegrees = ref(0)
const result = ref<LotteryResult | null>(null)
const mqConnection = ref<WebSocket | null>(null)

// 初始化奖品列表
const initPrizes = async () => {
  if (!userStore.token) {
    router.push('/login')
    return
  }
  try {
    const res = await getPrizes()
    if (res.data && res.data.length > 0) {
      prizes.value = res.data.map((prize: any) => ({
        id: prize.id,
        name: prize.name,
        remaining: prize.remaining
      }))
    } else {
      ElMessage.warning('暂无可用奖品')
    }
  } catch (error) {
    ElMessage.error('获取奖品列表失败，请先登录')
    console.error('Error loading prizes:', error)
    // 添加重定向到登录页
    router.push('/login')
  }
}

// // 连接RabbitMQ WebSocket
// const connectMQ = () => {
//   try {
//     // 修改为更稳定的websocket地址
//     const wsUrl = `ws://${window.location.hostname}:15674/ws?token=${userStore.token}`
//     mqConnection.value = new WebSocket(wsUrl)

//     mqConnection.value.onopen = () => {
//       console.log('WebSocket连接成功')
//     }

//     mqConnection.value.onerror = (error) => {
//       console.error('WebSocket连接错误:', error)
//       ElMessage.error('实时连接失败，请刷新页面重试')
//     }

//     mqConnection.value.onmessage = (event) => {
//       const data = JSON.parse(event.data)
//       if (data.userId === userStore.token) {
//         result.value = data
//         isSpinning.value = false
//       }
//     }
//   } catch (error) {
//     console.error('WebSocket初始化错误:', error)
//     // 添加重试机制
//     setTimeout(connectMQ, 5000)
//   }


// 开始抽奖
const visibleBalls = ref(Array(8).fill(0)) // 减少到10个小球

const ballRefs = ref<HTMLElement[]>([])

const startSpin = async () => {
  if (isSpinning.value || !canDraw.value) return

  isSpinning.value = true
  result.value = null

  try {
    const res = await drawPrize(userStore.userId)
    canDraw.value = false
    
    // 炫酷动画效果
    const animateBalls = () => {
      const time = Date.now() * 0.002
      ballRefs.value.forEach((ball, index) => {
        const wave = Math.sin(time * 3 + index * 0.5)
        const xMove = wave * 40
        const yMove = Math.cos(time * 4 + index) * 30
        const rotate = wave * 360
        const scale = 0.8 + Math.abs(wave) * 0.4
        ball.style.transform = `translate(${xMove}px, ${yMove}px) rotate(${rotate}deg) scale(${scale})`
        ball.style.filter = `hue-rotate(${time * 30 + index * 20}deg) blur(${Math.abs(wave) * 0.8}px)`
        ball.style.opacity = `${0.6 + Math.abs(wave) * 0.4}`
      })
      
      if (isSpinning.value) {
        requestAnimationFrame(animateBalls)
      }
    }
    
    animateBalls()

    setTimeout(() => {
      isSpinning.value = false
      result.value = res.data
      ballRefs.value.forEach(ball => {
        ball.style.transform = ''
        ball.style.filter = ''
        ball.style.opacity = ''
      })
      // 添加结果展示动画
      setTimeout(() => {
        ElMessageBox.alert(`恭喜您获得: ${res.data.prizeName}`, '抽奖结果', {
          confirmButtonText: '确定',
          customClass: 'result-popup'
        })
      }, 500)
    }, 3000)
  } catch (error: any) {
    isSpinning.value = false
    ElMessageBox.alert('抽奖失败: ' + (error.response?.data?.message || '请重试'), '错误')
  }
}

// 格式化时间
const formatTime = (timestamp: number) => {
  return new Date(timestamp).toLocaleString()
}

onMounted(() => {
  initPrizes()
//   connectMQ()
})

onUnmounted(() => {
  if (mqConnection.value) {
    mqConnection.value.close()
  }
})
</script>

<style scoped>
.ball-container {
  display: flex;
  flex-wrap: wrap;
  width: 300px;
  height: 200px;
  margin: 20px auto;
  justify-content: center;
  align-items: center;
  background: rgba(255,255,255,0.8);
  border-radius: 15px;
  padding: 15px;
  box-shadow: inset 0 0 20px rgba(0,0,0,0.1);
}

.ball {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin: 8px;
  box-shadow: 
    0 6px 12px rgba(0,0,0,0.3),
    inset 0 -8px 25px rgba(255,255,255,0.4),
    inset 0 8px 25px rgba(255,255,255,0.3);
  transition: all 0.2s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  transform-origin: center;
  position: relative;
  overflow: hidden;
  will-change: transform, filter, opacity;
  background: linear-gradient(145deg, var(--ball-color), #ffffff);
  border: 2px solid rgba(255,255,255,0.5);
}

.ball::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 30% 30%, rgba(255,255,255,0.9), transparent 70%);
  border-radius: 50%;
  animation: shine 3s infinite linear;
}

@keyframes shine {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
.ball::after {
  content: '';
  position: absolute;
  top: 10%;
  left: 10%;
  width: 30%;
  height: 30%;
  background: rgba(255,255,255,0.6);
  border-radius: 50%;
  transform: rotate(45deg);
  filter: blur(1px);
}

.result-ball {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin: 20px auto;
  background: linear-gradient(145deg, #FF5722, #FF9800);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 16px;
  box-shadow: 0 8px 20px rgba(233,30,99,0.3);
  transform: scale(0);
  transition: transform 0.5s ease;
}

.result-ball.show {
  transform: scale(1);
}

.prize-name {
  text-align: center;
  padding: 10px;
  text-shadow: 1px 1px 3px rgba(0,0,0,0.3);
}
.draw-container {
  display: flex;
  width: 100%;
  height: 100vh;
  gap: 30px;
  flex-direction: row;
  padding: 20px;
  box-sizing: border-box;
}

.gacha-container {
  flex: 1;
  min-width: 450px;
  max-width: 50%;
  height: 600px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: linear-gradient(145deg, #ffffff, #f8faff);
  border-radius: 30px;
  padding: 35px;
  box-shadow: 
    0 20px 50px rgba(0,0,0,0.2),
    inset 0 0 40px rgba(255,255,255,0.9);
}

.records-container {
  flex: 1;
  min-width: 500px;
  max-width: 50%;
  height: 600px;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  padding: 20px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.ball-container {
  position: relative;
  width: 280px;
  height: 350px;
  margin: 25px auto;
  background: linear-gradient(145deg, rgba(255,255,255,0.95), rgba(255,255,255,0.85));
  border-radius: 20px;
  box-shadow: 
    inset 0 0 30px rgba(0,0,0,0.15),
    0 15px 40px rgba(0,0,0,0.25);
  backdrop-filter: blur(5px);
  border: 1px solid rgba(255,255,255,0.5);
}

.ball {
  width: 55px;
  height: 55px;
  border-radius: 50%;
  margin: 10px;
  box-shadow: 
    0 10px 20px rgba(0,0,0,0.4),
    inset 0 -12px 35px rgba(255,255,255,0.5),
    inset 0 12px 35px rgba(255,255,255,0.4);
  transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  transform-origin: center;
  position: relative;
  overflow: hidden;
  will-change: transform, filter, opacity;
  background: linear-gradient(145deg, var(--ball-color), #ffffff);
  border: 3px solid rgba(255,255,255,0.8);
  cursor: pointer;
}

.capsule {
  position: absolute;
  width: 90px;
  height: 130px;
  background: linear-gradient(145deg, #FF5722, #FF9800);
  border-radius: 45px;
  top: 40px;
  left: 75px;
  z-index: 10;
  transition: all 0.5s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 16px;
  box-shadow: 0 8px 20px rgba(233,30,99,0.3);
  border: 3px solid rgba(255,255,255,0.5);
}

.capsule.dropping {
  animation: drop 1.5s cubic-bezier(0.4, 0, 0.2, 1) forwards;
}

@keyframes drop {
  0% { transform: translateY(0) rotate(0deg); opacity: 1; }
  30% { transform: translateY(100px) rotate(180deg); opacity: 0.9; }
  100% { transform: translateY(260px) rotate(360deg); opacity: 0; }
}

.machine-body {
  position: absolute;
  width: 220px;
  height: 280px;
  background: linear-gradient(145deg, #2196F3, #1976D2);
  border-radius: 15px;
  top: 0;
  left: 10px;
  z-index: 5;
  box-shadow: inset 0 -10px 30px rgba(0,0,0,0.2);
  border: 3px solid rgba(255,255,255,0.3);
}

.machine-base {
  position: absolute;
  width: 240px;
  height: 40px;
  background: linear-gradient(145deg, #1976D2, #0D47A1);
  border-radius: 10px;
  top: 280px;
  left: 0;
  z-index: 5;
  box-shadow: 0 5px 15px rgba(0,0,0,0.2);
}

.prize-name {
  transform: rotate(-5deg);
  text-shadow: 1px 1px 3px rgba(0,0,0,0.3);
  padding: 5px 10px;
  background: rgba(255,255,255,0.2);
  border-radius: 15px;
}
.title {
  font-size: 2rem;
  margin-bottom: 1.5rem;
  color: #2c3e50;
  text-align: center;
}

.wheel-container {
  position: relative;
  width: 100%;
  height: 350px;
}

.draw-btn {
  width: 200px;
  height: 50px;
  font-size: 1.2rem;
  margin-top: 2rem;
}

.wheel {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  position: relative;
  overflow: hidden;
  transition: transform 3s cubic-bezier(0.17, 0.67, 0.21, 0.99);
  transform: rotate(0deg);
  background: rgba(0,0,0,0.1); /* 添加背景色增强视觉效果 */
}

.wheel-container.spinning .wheel {
  transform: rotate(v-bind('spinDegrees + "deg"'));
}

.prize-item {
  position: absolute;
  width: 50%;
  height: 50%;
  transform-origin: 100% 100%;
  left: 0;
  top: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  clip-path: polygon(0 0, 100% 100%, 0 100%);
  box-sizing: border-box;
  z-index: 2; /* 确保奖品项在正确层级 */
}

.prize-text {
  transform: rotate(45deg);
  color: white;
  font-weight: bold;
  text-shadow: 0 1px 3px rgba(0,0,0,0.5);
  font-size: 14px; /* 确保文字大小合适 */
  white-space: nowrap; /* 防止文字换行 */
}

.pointer {
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 30px;
  background-color: #FF5722;
  clip-path: polygon(50% 0%, 0% 100%, 100% 100%);
  z-index: 10;
}

.result-panel {
  margin-top: 2rem;
  text-align: center;
  animation: fadeIn 0.5s ease-out;
}

.prize-result {
  font-size: 2rem;
  color: #FF5722;
  font-weight: bold;
  margin-top: 1rem;
}

.draw-btn {
  width: 200px;
  height: 50px;
  font-size: 1.2rem;
  margin-top: 2rem;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

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
</style>