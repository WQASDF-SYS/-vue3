<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-form inline>
        <el-form-item label="关键词">
          <el-input v-model="query.keyword" placeholder="民宿名称" clearable @clear="fetchData" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="query.categoryId" placeholder="全部" clearable @change="fetchData">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable @change="fetchData">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchData">搜索</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 表格 -->
    <div class="table-card">
      <div class="card-header">
        <span class="title">民宿列表</span>
        <div class="header-actions">
          <el-button v-permission="'homestay:export'" type="success" @click="handleExport">
            <el-icon><Download /></el-icon>导出
          </el-button>
          <el-button v-permission="'homestay:import'" type="warning" @click="showImportDialog">
            <el-icon><Upload /></el-icon>导入
          </el-button>
          <el-button v-permission="'homestay:add'" type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>添加民宿
          </el-button>
        </div>
      </div>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="封面" width="100">
          <template #default="{ row }">
            <el-image 
              :src="row.coverImage" 
              style="width: 60px; height: 60px; border-radius: 4px"
              fit="cover"
            />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column label="价格" width="100">
          <template #default="{ row }">¥{{ row.price }}/晚</template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="80" />
        <el-table-column label="推荐" width="80">
          <template #default="{ row }">
            <el-switch
              v-model="row.isRecommend"
              :active-value="1"
              :inactive-value="0"
              @change="handleRecommendChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="180">
          <template #default="{ row }">
            <el-button v-permission="'homestay:edit'" link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button v-permission="'homestay:edit'" link :type="row.status === 1 ? 'warning' : 'success'" @click="handleStatusChange(row)">
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
            <el-button v-permission="'homestay:delete'" link type="danger" @click="handleDelete(row)">删除</el-button>
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
    
    <!-- 导入对话框 -->
    <el-dialog v-model="importDialogVisible" title="批量导入民宿" width="500px">
      <div class="import-dialog">
        <div class="import-tip">
          <el-icon><InfoFilled /></el-icon>
          <span>请先下载模板，按照模板格式填写数据后上传</span>
        </div>
        <div class="template-download">
          <el-button type="primary" link @click="downloadTemplate">
            <el-icon><Download /></el-icon>下载导入模板
          </el-button>
        </div>
        <el-upload
          ref="uploadRef"
          class="upload-area"
          drag
          :auto-upload="false"
          :limit="1"
          accept=".xlsx,.xls"
          :on-change="handleFileChange"
        >
          <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">只能上传 xlsx/xls 文件</div>
          </template>
        </el-upload>
      </div>
      <template #footer>
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImport" :loading="importing" :disabled="!importFile">
          确认导入
        </el-button>
      </template>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" top="5vh">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="民宿名称" prop="name">
              <el-input v-model="form.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="form.categoryId" style="width: 100%">
                <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="form.contactPhone" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="封面图片">
          <el-upload
            class="cover-uploader"
            action="/api/file/upload/image"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
          >
            <img v-if="form.coverImage" :src="form.coverImage" class="cover-image" />
            <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="详细描述">
          <el-input v-model="form.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="房间数量">
              <el-input-number v-model="form.roomCount" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最大入住">
              <el-input-number v-model="form.maxGuests" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="推荐">
              <el-switch v-model="form.isRecommend" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Download, Upload, UploadFilled, InfoFilled } from '@element-plus/icons-vue'
import { getHomestays, createHomestay, updateHomestay, updateHomestayStatus, updateHomestayRecommend, deleteHomestay } from '@/api/homestay'
import { getCategories } from '@/api/category'
import { exportHomestays, importHomestays, homestayTemplateUrl } from '@/api/excel'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const uploadHeaders = computed(() => ({ Authorization: `Bearer ${userStore.token}` }))

const loading = ref(false)
const submitting = ref(false)
const importing = ref(false)
const dialogVisible = ref(false)
const importDialogVisible = ref(false)
const importFile = ref(null)
const uploadRef = ref()
const tableData = ref([])
const categories = ref([])
const total = ref(0)
const formRef = ref()

const query = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  categoryId: null,
  status: null
})

const form = reactive({
  id: null,
  name: '',
  categoryId: null,
  coverImage: '',
  description: '',
  price: 0,
  address: '',
  roomCount: 1,
  maxGuests: 2,
  contactPhone: '',
  isRecommend: 0
})

const rules = {
  name: [{ required: true, message: '请输入民宿名称', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }]
}

const dialogTitle = computed(() => form.id ? '编辑民宿' : '添加民宿')

const fetchCategories = async () => {
  const res = await getCategories()
  categories.value = res.data
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getHomestays(query)
    tableData.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const resetQuery = () => {
  query.keyword = ''
  query.categoryId = null
  query.status = null
  query.pageNum = 1
  fetchData()
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    name: '',
    categoryId: null,
    coverImage: '',
    description: '',
    price: 0,
    address: '',
    roomCount: 1,
    maxGuests: 2,
    contactPhone: '',
    isRecommend: 0
  })
}

const handleAdd = () => {
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleCoverSuccess = (res) => {
  if (res.code === 200) {
    form.coverImage = res.data.url
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      if (form.id) {
        await updateHomestay(form.id, form)
        ElMessage.success('更新成功')
      } else {
        await createHomestay(form)
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
  const newStatus = row.status === 1 ? 0 : 1
  await updateHomestayStatus(row.id, newStatus)
  row.status = newStatus
  ElMessage.success('操作成功')
}

const handleRecommendChange = async (row) => {
  try {
    await updateHomestayRecommend(row.id, row.isRecommend)
    ElMessage.success('操作成功')
  } catch {
    row.isRecommend = row.isRecommend === 1 ? 0 : 1
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该民宿吗？', '提示', { type: 'warning' })
    .then(async () => {
      await deleteHomestay(row.id)
      ElMessage.success('删除成功')
      fetchData()
    })
    .catch(() => {})
}

// ==================== 导入导出 ====================

const handleExport = () => {
  const params = {}
  if (query.keyword) params.keyword = query.keyword
  if (query.categoryId) params.categoryId = query.categoryId
  if (query.status !== null) params.status = query.status
  window.open(exportHomestays(params), '_blank')
}

const showImportDialog = () => {
  importFile.value = null
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
  importDialogVisible.value = true
}

const downloadTemplate = () => {
  window.open(homestayTemplateUrl(), '_blank')
}

const handleFileChange = (file) => {
  importFile.value = file.raw
}

const handleImport = async () => {
  if (!importFile.value) {
    ElMessage.warning('请选择要导入的文件')
    return
  }
  
  importing.value = true
  try {
    const res = await importHomestays(importFile.value)
    ElMessage.success(res.message || '导入成功')
    importDialogVisible.value = false
    fetchData()
  } finally {
    importing.value = false
  }
}

onMounted(() => {
  fetchCategories()
  fetchData()
})
</script>

<style lang="scss" scoped>
.header-actions {
  display: flex;
  gap: 10px;
}

.import-dialog {
  .import-tip {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px 16px;
    background: #ecf5ff;
    border-radius: 4px;
    color: #409eff;
    margin-bottom: 16px;
  }
  
  .template-download {
    margin-bottom: 16px;
    text-align: center;
  }
  
  .upload-area {
    width: 100%;
    
    :deep(.el-upload-dragger) {
      width: 100%;
    }
  }
}

.cover-uploader {
  :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: border-color 0.3s;
    
    &:hover {
      border-color: #409eff;
    }
  }
}

.cover-image {
  width: 150px;
  height: 100px;
  object-fit: cover;
}

.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 100px;
  text-align: center;
  line-height: 100px;
}
</style>

