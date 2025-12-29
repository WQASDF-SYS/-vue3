<template>
  <div class="profile-page">
    <h2>个人信息</h2>
    <el-form :model="form" label-width="100px" style="max-width: 500px">
      <el-form-item label="用户名">
        <el-input v-model="form.userName" disabled />
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.nickName" />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="form.phoneNumber" />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="form.gender">
          <el-radio :value="0">保密</el-radio>
          <el-radio :value="1">男</el-radio>
          <el-radio :value="2">女</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleSave">保存修改</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserInfo, updateUserInfo } from '@/api/auth'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)

const form = reactive({
  userName: '',
  nickName: '',
  phoneNumber: '',
  email: '',
  gender: 0
})

const fetchProfile = async () => {
  const res = await getUserInfo()
  Object.assign(form, res.data)
}

const handleSave = async () => {
  loading.value = true
  try {
    await updateUserInfo(form)
    ElMessage.success('保存成功')
    userStore.fetchUserInfo()
  } finally {
    loading.value = false
  }
}

onMounted(() => fetchProfile())
</script>

<style lang="scss" scoped>
h2 { margin-bottom: 24px; font-size: 20px; }
</style>

