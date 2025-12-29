<template>
  <div class="page-container">
    <div class="search-bar">
      <el-form inline>
        <el-form-item label="类型">
          <el-select v-model="query.type" placeholder="全部" clearable @change="fetchData">
            <el-option label="公告" :value="0" />
            <el-option label="资讯" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable @change="fetchData">
            <el-option label="显示" :value="1" />
            <el-option label="隐藏" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <div class="table-card">
      <div class="card-header">
        <span class="title">公告/资讯列表</span>
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>发布
        </el-button>
      </div>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.type === 0 ? 'warning' : 'primary'">{{ row.type === 0 ? '公告' : '资讯' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column label="置顶" width="80">
          <template #default="{ row }">
            <el-switch v-model="row.isTop" :active-value="1" :inactive-value="0" @change="handleTopChange(row)" />
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(row)" />
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
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="query.pageNum"
          v-model:page-size="query.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @size-change="fetchData"
          @current-change="fetchData"
        />
      </div>
    </div>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio :value="0">公告</el-radio>
            <el-radio :value="1">资讯</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="8" />
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
import { getAnnouncements, createAnnouncement, updateAnnouncement, updateAnnouncementStatus, setAnnouncementTop, deleteAnnouncement } from '@/api/announcement'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const tableData = ref([])
const total = ref(0)
const formRef = ref()

const query = reactive({ pageNum: 1, pageSize: 10, type: null, status: null })
const form = reactive({ id: null, title: '', content: '', type: 0 })
const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
}
const dialogTitle = computed(() => form.id ? '编辑' : '发布')

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getAnnouncements(query)
    tableData.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const resetForm = () => { Object.assign(form, { id: null, title: '', content: '', type: 0 }) }

const handleAdd = () => { resetForm(); dialogVisible.value = true }

const handleEdit = (row) => { Object.assign(form, row); dialogVisible.value = true }

const handleSubmit = async () => {
  await formRef.value?.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (form.id) {
        await updateAnnouncement(form.id, form)
        ElMessage.success('更新成功')
      } else {
        await createAnnouncement(form)
        ElMessage.success('发布成功')
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
    await updateAnnouncementStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch { row.status = row.status === 1 ? 0 : 1 }
}

const handleTopChange = async (row) => {
  try {
    await setAnnouncementTop(row.id, row.isTop)
    ElMessage.success('操作成功')
  } catch { row.isTop = row.isTop === 1 ? 0 : 1 }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteAnnouncement(row.id)
      ElMessage.success('删除成功')
      fetchData()
    }).catch(() => {})
}

fetchData()
</script>

