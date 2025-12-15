<template>
  <div class="app-layout">
    <div class="sidebar">
      <div class="logo-section">
        <img src="@/assets/medical-logo.png" alt="协和康康" width="160" height="160" />
        <span class="logo-text">协和康康（医疗版）</span>
      </div>
      <el-button class="new-chat-button" @click="newChat">
        <i class="fa-solid fa-plus"></i>
        &nbsp;新会话
      </el-button>
    </div>
    <div class="main-content">
      <div class="chat-container">
        <div class="message-list" ref="messaggListRef">
          <div
            v-for="(message, index) in messages"
            :key="index"
            :class="
              message.isUser ? 'message user-message' : 'message bot-message'
            "
          >
            <!-- 会话图标 -->
            <i
              :class="
                message.isUser
                  ? 'fa-solid fa-user message-icon'
                  : 'fa-solid fa-user-doctor message-icon'
              "
            ></i>
            <!-- 会话内容 -->
            <span>
              <span v-html="message.content"></span>
              <!-- loading -->
              <span
                class="loading-dots"
                v-if="message.isThinking || message.isTyping"
              >
                <span class="dot"></span>
                <span class="dot"></span>
              </span>
            </span>
          </div>
        </div>
        <div class="input-container">
          <el-input
            v-model="inputMessage"
            placeholder="请输入消息"
            @keyup.enter="sendMessage"
          ></el-input>
          <el-button @click="sendMessage" :disabled="isSending" type="primary"
            >发送</el-button
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import axios from 'axios'
import { v4 as uuidv4 } from 'uuid'

const messaggListRef = ref()
const isSending = ref(false)
const uuid = ref()
const inputMessage = ref('')
const messages = ref([])

onMounted(() => {
  initUUID()
  // 移除 setInterval，改用手动滚动
  watch(messages, () => scrollToBottom(), { deep: true })
  hello()
})

const scrollToBottom = () => {
  if (messaggListRef.value) {
    messaggListRef.value.scrollTop = messaggListRef.value.scrollHeight
  }
}

const hello = () => {
  sendRequest('你好')
}

const sendMessage = () => {
  if (inputMessage.value.trim()) {
    sendRequest(inputMessage.value.trim())
    inputMessage.value = ''
  }
}

const sendRequest = (message) => {
  isSending.value = true
  const userMsg = {
    isUser: true,
    content: message,
    isTyping: false,
    isThinking: false,
  }
  //第一条默认发送的用户消息”你好“不放入会话列表
  if(messages.value.length > 0){
    messages.value.push(userMsg)
  }


  // 添加机器人加载消息
  const botMsg = {
    isUser: false,
    content: '', // 增量填充
    isTyping: true, // 显示加载动画
    isThinking: false,
  }
  messages.value.push(botMsg)
  const lastMsg = messages.value[messages.value.length - 1]
  scrollToBottom()

  axios
    .post(
      '/api/kangkang/chat',
      { memoryId: uuid.value, message },
      {
        responseType: 'stream', // 必须为合法值 "text"
        onDownloadProgress: (e) => {
          const fullText = e.event.target.responseText // 累积的完整文本
          let newText = fullText.substring(lastMsg.content.length)
          lastMsg.content += newText //增量更新
          console.log(lastMsg)
          scrollToBottom() // 实时滚动
        },
      }
    )
    .then(() => {
      // 流结束后隐藏加载动画
      messages.value.at(-1).isTyping = false
      isSending.value = false
    })
    .catch((error) => {
      console.error('流式错误:', error)
      messages.value.at(-1).content = '请求失败，请重试'
      messages.value.at(-1).isTyping = false
      isSending.value = false
    })
}

// 初始化 UUID
const initUUID = () => {
  let storedUUID = localStorage.getItem('user_uuid')
  if (!storedUUID) {
    storedUUID = uuidToNumber(uuidv4())
    localStorage.setItem('user_uuid', storedUUID)
  }
  uuid.value = storedUUID
}

const uuidToNumber = (uuid) => {
  let number = 0
  for (let i = 0; i < uuid.length && i < 6; i++) {
    const hexValue = uuid[i]
    number = number * 16 + (parseInt(hexValue, 16) || 0)
  }
  return number % 1000000
}

// 转换特殊字符
const convertStreamOutput = (output) => {
  return output
    .replace(/\n/g, '<br>')
    .replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;')
    .replace(/&/g, '&amp;') // 新增转义，避免 HTML 注入
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
}

const newChat = () => {
  // 这里添加新会话的逻辑
  console.log('开始新会话')
  localStorage.removeItem('user_uuid')
  window.location.reload()
}

</script>
<style scoped>
.app-layout {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 200px;
  background: linear-gradient(135deg, #e3f2fd 0%, #e8f5e9 100%);
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 2px 0 10px rgba(0, 150, 136, 0.1);
}

.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.logo-text {
  font-size: 18px;
  font-weight: bold;
  margin-top: 10px;
  background: linear-gradient(135deg, #42a5f5 0%, #26a69a 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.new-chat-button {
  width: 100%;
  margin-top: 20px;
  background: linear-gradient(135deg, #42a5f5 0%, #26a69a 100%);
  border: none;
  border-radius: 24px;
  color: white;
  font-weight: 500;
  transition: all 0.3s ease;
  height: 44px;
}

.new-chat-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(66, 165, 245, 0.4);
  background: linear-gradient(135deg, #1e88e5 0%, #00897b 100%);
}

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: linear-gradient(135deg, #fafafa 0%, #f1f8e9 100%);
}
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  border: none;
  border-radius: 16px;
  background-color: rgba(255, 255, 255, 0.95);
  margin-bottom: 10px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 4px 16px rgba(0, 150, 136, 0.08);
}

.message {
  margin-bottom: 16px;
  padding: 14px 18px;
  border-radius: 16px;
  display: flex;
  animation: fadeIn 0.4s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.user-message {
  max-width: 70%;
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  align-self: flex-end;
  flex-direction: row-reverse;
  border: 1px solid rgba(66, 165, 245, 0.2);
  box-shadow: 0 2px 8px rgba(66, 165, 245, 0.15);
}

.bot-message {
  max-width: 100%;
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  align-self: flex-start;
  border: 1px solid rgba(76, 175, 80, 0.2);
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.12);
}

.message-icon {
  margin: 0 12px;
  font-size: 1.5em;
  background: linear-gradient(135deg, #42a5f5 0%, #26a69a 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.loading-dots {
  padding-left: 5px;
}

.dot {
  display: inline-block;
  margin-left: 5px;
  width: 8px;
  height: 8px;
  background: linear-gradient(135deg, #42a5f5 0%, #26a69a 100%);
  border-radius: 50%;
  animation: pulse 1.2s infinite ease-in-out both;
}

.dot:nth-child(2) {
  animation-delay: -0.6s;
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(0.6);
    opacity: 0.4;
  }

  50% {
    transform: scale(1);
    opacity: 1;
  }
}
.input-container {
  display: flex;
  gap: 12px;
}

.input-container .el-input {
  flex: 1;
}

.input-container :deep(.el-input__wrapper) {
  padding: 8px 20px;
  min-height: 48px;
  border-radius: 24px;
  box-shadow: 0 2px 12px rgba(0, 150, 136, 0.1);
  border: 2px solid #b2dfdb;
  transition: all 0.3s ease;
  background-color: #ffffff;
}

.input-container :deep(.el-input__wrapper:hover) {
  border-color: #4db6ac;
  box-shadow: 0 4px 16px rgba(77, 182, 172, 0.2);
}

.input-container :deep(.el-input__wrapper.is-focus) {
  border-color: #26a69a;
  box-shadow: 0 4px 20px rgba(38, 166, 154, 0.3);
}

.input-container :deep(.el-input__inner) {
  font-size: 16px;
  line-height: 1.5;
}

.input-container :deep(.el-button--primary) {
  height: 48px;
  padding: 0 32px;
  font-size: 16px;
  background: linear-gradient(135deg, #42a5f5 0%, #26a69a 100%);
  border: none;
  border-radius: 24px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(66, 165, 245, 0.3);
}

.input-container :deep(.el-button--primary:hover) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(66, 165, 245, 0.4);
  background: linear-gradient(135deg, #1e88e5 0%, #00897b 100%);
}

.input-container :deep(.el-button--primary:active) {
  transform: translateY(0);
}

/* 媒体查询，当设备宽度小于等于 768px 时应用以下样式 */
@media (max-width: 768px) {
  .main-content {
    padding: 10px 0 10px 0;
  }
  .app-layout {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    padding: 12px 16px;
    background: linear-gradient(135deg, #e3f2fd 0%, #e8f5e9 100%);
    box-shadow: 0 2px 8px rgba(0, 150, 136, 0.1);
  }

  .logo-section {
    flex-direction: row;
    align-items: center;
  }

  .logo-text {
    font-size: 20px;
  }

  .logo-section img {
    width: 40px;
    height: 40px;
  }

  .new-chat-button {
    margin-right: 30px;
    width: auto;
    margin-top: 5px;
  }
}

/* 媒体查询，当设备宽度大于 768px 时应用原来的样式 */
@media (min-width: 769px) {
  .main-content {
    padding: 0 0 10px 10px;
  }

  .app-layout {
    display: flex;
    height: 100vh;
  }

  .sidebar {
    width: 200px;
    background: linear-gradient(135deg, #e3f2fd 0%, #e8f5e9 100%);
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
    box-shadow: 2px 0 10px rgba(0, 150, 136, 0.1);
  }

  .logo-section {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .logo-text {
    font-size: 18px;
    font-weight: bold;
    margin-top: 10px;
  }

  .new-chat-button {
    width: 100%;
    margin-top: 20px;
  }
}
</style>
