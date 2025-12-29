import request from '@/utils/request'

// 智能对话
export function chat(messages) {
  return request.post('/ai/chat', { messages })
}

// 单次问答
export function ask(question) {
  return request.post('/ai/ask', { question })
}

// 检查AI状态
export function getAiStatus() {
  return request.get('/ai/status')
}

