<template>
  <div class="ai-chat-widget">
    <!-- 悬浮按钮 -->
    <div class="chat-trigger" @click="isOpen = !isOpen" v-show="!isOpen">
      <el-icon size="28"><ChatDotSquare /></el-icon>
      <span class="tooltip">智能客服</span>
    </div>
    
    <!-- 聊天窗口 -->
    <transition name="slide">
      <div class="chat-window" v-show="isOpen">
        <div class="chat-header">
          <div class="header-info">
            <el-icon size="24"><Service /></el-icon>
            <span>智能客服</span>
          </div>
          <el-icon class="close-btn" @click="isOpen = false"><Close /></el-icon>
        </div>
        
        <div class="chat-messages" ref="messagesRef">
          <div 
            v-for="(msg, index) in messages" 
            :key="index" 
            class="message"
            :class="msg.role"
          >
            <div class="avatar">
              <el-icon v-if="msg.role === 'assistant'"><Service /></el-icon>
              <el-icon v-else><User /></el-icon>
            </div>
            <div class="content">{{ msg.content }}</div>
          </div>
          <div v-if="loading" class="message assistant">
            <div class="avatar"><el-icon><Service /></el-icon></div>
            <div class="content typing">
              <span></span><span></span><span></span>
            </div>
          </div>
        </div>
        
        <div class="chat-input">
          <el-input 
            v-model="inputText"
            placeholder="请输入您的问题..."
            @keyup.enter="sendMessage"
          />
          <el-button type="primary" @click="sendMessage" :disabled="!inputText.trim() || loading">
            <el-icon><Promotion /></el-icon>
          </el-button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { chat, getAiStatus } from '@/api/ai'

const isOpen = ref(false)
const loading = ref(false)
const inputText = ref('')
const messagesRef = ref()
const aiEnabled = ref(false)

const messages = ref([
  { role: 'assistant', content: '您好！我是曲靖民宿智能客服，有什么可以帮您的吗？' }
])

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}

const sendMessage = async () => {
  const text = inputText.value.trim()
  if (!text || loading.value) return
  
  // 添加用户消息
  messages.value.push({ role: 'user', content: text })
  inputText.value = ''
  scrollToBottom()
  
  if (!aiEnabled.value) {
    messages.value.push({ 
      role: 'assistant', 
      content: 'AI服务暂时不可用，请联系人工客服。' 
    })
    scrollToBottom()
    return
  }
  
  loading.value = true
  try {
    const chatMessages = messages.value.slice(1).map(m => ({
      role: m.role,
      content: m.content
    }))
    
    const res = await chat(chatMessages)
    messages.value.push({ 
      role: 'assistant', 
      content: res.data.reply 
    })
  } catch (error) {
    messages.value.push({ 
      role: 'assistant', 
      content: '抱歉，我暂时无法回答您的问题，请稍后再试。' 
    })
  } finally {
    loading.value = false
    scrollToBottom()
  }
}

const checkAiStatus = async () => {
  try {
    const res = await getAiStatus()
    aiEnabled.value = res.data.enabled
  } catch {
    aiEnabled.value = false
  }
}

onMounted(() => {
  checkAiStatus()
})
</script>

<style lang="scss" scoped>
.ai-chat-widget {
  position: fixed;
  bottom: 24px;
  right: 24px;
  z-index: 1000;
}

.chat-trigger {
  width: 56px;
  height: 56px;
  background: var(--primary-color);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);
  transition: all 0.3s;
  position: relative;
  
  &:hover {
    transform: scale(1.1);
    
    .tooltip {
      opacity: 1;
      transform: translateX(0);
    }
  }
  
  .tooltip {
    position: absolute;
    right: 70px;
    background: #1f2937;
    color: white;
    padding: 8px 12px;
    border-radius: 6px;
    font-size: 13px;
    white-space: nowrap;
    opacity: 0;
    transform: translateX(10px);
    transition: all 0.3s;
    
    &::after {
      content: '';
      position: absolute;
      right: -6px;
      top: 50%;
      transform: translateY(-50%);
      border: 6px solid transparent;
      border-left-color: #1f2937;
    }
  }
}

.chat-window {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 380px;
  height: 520px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: var(--primary-color);
  color: white;
  
  .header-info {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 500;
  }
  
  .close-btn {
    cursor: pointer;
    font-size: 20px;
    opacity: 0.8;
    
    &:hover {
      opacity: 1;
    }
  }
}

.chat-messages {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  background: #f9fafb;
  
  .message {
    display: flex;
    gap: 12px;
    margin-bottom: 16px;
    
    &.user {
      flex-direction: row-reverse;
      
      .avatar {
        background: var(--primary-color);
      }
      
      .content {
        background: var(--primary-color);
        color: white;
        border-radius: 16px 4px 16px 16px;
      }
    }
    
    &.assistant {
      .avatar {
        background: #e5e7eb;
        color: #6b7280;
      }
      
      .content {
        background: white;
        border-radius: 4px 16px 16px 16px;
      }
    }
    
    .avatar {
      width: 36px;
      height: 36px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      color: white;
    }
    
    .content {
      max-width: 70%;
      padding: 12px 16px;
      font-size: 14px;
      line-height: 1.6;
      box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
      
      &.typing {
        display: flex;
        gap: 4px;
        padding: 12px 20px;
        
        span {
          width: 8px;
          height: 8px;
          background: #9ca3af;
          border-radius: 50%;
          animation: typing 1.4s infinite;
          
          &:nth-child(2) { animation-delay: 0.2s; }
          &:nth-child(3) { animation-delay: 0.4s; }
        }
      }
    }
  }
}

@keyframes typing {
  0%, 60%, 100% { transform: translateY(0); }
  30% { transform: translateY(-6px); }
}

.chat-input {
  display: flex;
  gap: 8px;
  padding: 16px;
  border-top: 1px solid #e5e7eb;
  
  .el-input {
    flex: 1;
  }
}

.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateY(20px) scale(0.95);
}

@media (max-width: 480px) {
  .chat-window {
    width: calc(100vw - 32px);
    height: calc(100vh - 100px);
    bottom: 70px;
    right: -8px;
  }
}
</style>

