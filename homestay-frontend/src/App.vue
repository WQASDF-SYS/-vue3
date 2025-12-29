<template>
  <div id="app">
    <router-view />
    <!-- AI客服悬浮按钮 -->
    <AiChat />
  </div>
</template>

<script setup>
import AiChat from '@/components/AiChat.vue'
</script>

