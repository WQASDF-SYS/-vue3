<template>
  <div class="page-container">
    <div class="table-card">
      <div class="card-header">
        <span class="title">轮播图列表</span>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>添加轮播图
        </el-button>
      </div>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="图片" width="150">
          <template #default="{ row }">
            <el-image :src="row.imageUrl" style="width: 120px; height: 60px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="sortOrder" label="排序" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(row)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <el-upload
            class="image-uploader"
            action="/api/file/upload/image"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
          >
            <img v-if="form.imageUrl" :src="form.imageUrl" class="uploaded-image" />
            <el-icon v-else class="upload-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="跳转链接">
          <el-input v-model="form.linkUrl" placeholder="可选" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCarousels, createCarousel, updateCarousel, updateCarouselStatus, deleteCarousel } from '@/api/carousel'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const uploadHeaders = computed(() => ({ Authorization: `Bearer ${userStore.token}` }))

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const tableData = ref([])
const formRef = ref()

const form = reactive({ id: null, title: '', imageUrl: '', linkUrl: '', sortOrder: 0 })
const rules = { imageUrl: [{ required: true, message: '请上传图片', trigger: 'change' }] }
const dialogTitle = computed(() => form.id ? '编辑轮播图' : '添加轮播图')

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getCarousels()
    tableData.value = res.data
  } finally {
    loading.value = false
  }
}

const resetForm = () => { Object.assign(form, { id: null, title: '', imageUrl: '', linkUrl: '', sortOrder: 0 }) }

const handleAdd = () => { resetForm(); dialogVisible.value = true }

const handleEdit = (row) => { Object.assign(form, row); dialogVisible.value = true }

const handleUploadSuccess = (res) => { if (res.code === 200) form.imageUrl = res.data.url }

const handleSubmit = async () => {
  await formRef.value?.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (form.id) {
        await updateCarousel(form.id, form)
        ElMessage.success('更新成功')
      } else {
        await createCarousel(form)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      fetchData()
    } finally {
      submitting.value = false
    }
  })
}

const handleStatusChange = async (row) => {
  try {
    await updateCarouselStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch { row.status = row.status === 1 ? 0 : 1 }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该轮播图吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteCarousel(row.id)
      ElMessage.success('删除成功')
      fetchData()
    }).catch(() => {})
}

fetchData()
</script>

<style lang="scss" scoped>
.image-uploader {
  :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    &:hover { border-color: #409eff; }
  }
}
.uploaded-image { width: 200px; height: 100px; object-fit: cover; }
.upload-icon { font-size: 28px; color: #8c939d; width: 200px; height: 100px; line-height: 100px; text-align: center; }
</style>

