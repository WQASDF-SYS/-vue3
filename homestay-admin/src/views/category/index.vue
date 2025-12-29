<template>
  <div class="page-container">
    <div class="table-card">
      <div class="card-header">
        <span class="title">分类列表</span>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>添加分类
        </el-button>
      </div>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="sortOrder" label="排序" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" fixed="right" width="150">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="400px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" />
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
import { getCategories, createCategory, updateCategory, updateCategoryStatus, deleteCategory } from '@/api/category'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const tableData = ref([])
const formRef = ref()

const form = reactive({ id: null, name: '', sortOrder: 0 })
const rules = { name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }] }
const dialogTitle = computed(() => form.id ? '编辑分类' : '添加分类')

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getCategories()
    tableData.value = res.data
  } finally {
    loading.value = false
  }
}

const resetForm = () => { Object.assign(form, { id: null, name: '', sortOrder: 0 }) }

const handleAdd = () => { resetForm(); dialogVisible.value = true }

const handleEdit = (row) => { Object.assign(form, row); dialogVisible.value = true }

const handleSubmit = async () => {
  await formRef.value?.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (form.id) {
        await updateCategory(form.id, form)
        ElMessage.success('更新成功')
      } else {
        await createCategory(form)
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
    await updateCategoryStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch { row.status = row.status === 1 ? 0 : 1 }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该分类吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteCategory(row.id)
      ElMessage.success('删除成功')
      fetchData()
    }).catch(() => {})
}

fetchData()
</script>

