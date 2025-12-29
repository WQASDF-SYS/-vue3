import request from '@/utils/request'

// 上传图片
export function uploadImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/file/upload/image', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 上传文件
export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/file/upload/file', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// 删除文件
export function deleteFile(url) {
  return request.delete('/file/delete', { params: { url } })
}

