<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="app-container">
    <main class="app-main">
      <RouterView />
    </main>
    
    <div class="floating-nav-container">
      <nav class="floating-nav">
        <RouterLink to="/" class="nav-item">
          <div class="nav-icon">
            <i class="icon-home"></i>
          </div>
          <span class="nav-text">首页</span>
        </RouterLink>
        <RouterLink to="/login" class="nav-item">
          <div class="nav-icon">
            <i class="icon-login"></i>
          </div>
          <span class="nav-text">登录</span>
        </RouterLink>
        <RouterLink to="/register" class="nav-item">
          <div class="nav-icon">
            <i class="icon-register"></i>
          </div>
          <span class="nav-text">注册</span>
        </RouterLink>
        <RouterLink to="/prizes" class="nav-item">
          <div class="nav-icon">
            <i class="icon-prize"></i>
          </div>
          <span class="nav-text">奖品</span>
        </RouterLink>
      </nav>
    </div>
  </div>
</template>

<style scoped>
.app-container {
  min-height: 100vh;
  width: 1472px;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f5f7fa, #e4e8eb);
}

.app-main {
  flex: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 5rem;
  margin-left: 120px; /* 向右移动 */
  width: calc(100% - 200px); /* 增加宽度但不占满屏幕 */
}

.floating-nav-container {
  position: fixed;
  bottom: 1.5rem;
  left: 50%;
  transform: translateX(-50%);
  z-index: 100;
}

.floating-nav {
  display: flex;
  background: white;
  border-radius: 50px;
  padding: 0.5rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0.5rem 1rem;
  text-decoration: none;
  color: #2c3e50;
  transition: all 0.3s ease;
  border-radius: 50px;
  font-size: 1rem; /* 添加与登录按钮相同的字体大小 */
  font-weight: 500; /* 添加与登录按钮相同的字体粗细 */
}

.nav-item:hover {
  background: rgba(52, 152, 219, 0.1);
}

.nav-item.router-link-exact-active {
  background: #3498db;
  color: white;
}
</style>

<style>
/* 移除全局背景色设置 */
body {
  margin: 0;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  line-height: 1.6;
  color: #333;
}
</style>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 2rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }
}
</style>

.app-main {
  flex: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 5rem;
  margin-left: 120px; /* 向右移动 */
  width: calc(100% - 200px); /* 增加宽度但不占满屏幕 */
}
