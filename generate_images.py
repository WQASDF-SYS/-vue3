# -*- coding: utf-8 -*-
"""
曲靖民宿管理系统 - AI图片生成脚本
使用 ModelScope API 生成民宿相关主题图片
"""
import requests
import time
import json
import os
import sys
from PIL import Image
from io import BytesIO
from datetime import datetime

# API配置
BASE_URL = 'https://api-inference.modelscope.cn/'
API_KEY = "ms-a50cdec5-dd52-4c06-8733-b44a320e291d"

COMMON_HEADERS = {
    "Authorization": f"Bearer {API_KEY}",
    "Content-Type": "application/json",
}

# 图片保存路径
UPLOAD_PATH = "./homestay-backend/uploads/images"

# 民宿相关主题提示词 (英文效果更好)
PROMPTS = {
    # 轮播图
    "carousel_1": "Beautiful traditional Chinese courtyard homestay in Qujing Yunnan, mountains in background, sunrise, warm lighting, professional photography, 4k quality",
    "carousel_2": "Cozy wooden cabin homestay with mountain view, Yunnan landscape, morning mist, peaceful atmosphere, high quality photography",
    "carousel_3": "Modern minimalist homestay interior, large windows overlooking rice terraces, natural light, clean design, architectural photography",
    
    # 民宿封面图
    "homestay_traditional": "Traditional Yunnan Bai ethnic minority style homestay, white walls with grey tiles, courtyard with flowers, blue sky, professional real estate photography",
    "homestay_modern": "Contemporary boutique hotel room, minimalist design, warm wooden tones, comfortable bed, soft lighting, interior photography",
    "homestay_mountain": "Hillside homestay villa with infinity pool, overlooking Qujing mountains, sunset colors, luxury travel photography",
    "homestay_garden": "Garden view homestay with traditional Chinese architecture, bamboo garden, stone path, lanterns, serene atmosphere",
    "homestay_lakeside": "Lakeside wooden homestay cabin, reflection on calm water, autumn colors, peaceful morning, landscape photography",
    
    # 民宿设施
    "room_bedroom": "Comfortable homestay bedroom, traditional Chinese decor meets modern comfort, wooden furniture, soft bedding, warm ambient lighting",
    "room_living": "Spacious living room in Chinese homestay, tea table, calligraphy on wall, natural materials, cozy atmosphere",
    "room_bathroom": "Modern bathroom in boutique homestay, stone bathtub, plants, natural light through frosted glass, spa-like atmosphere",
}

def ensure_dir(path):
    """确保目录存在"""
    if not os.path.exists(path):
        os.makedirs(path)
        print(f"创建目录: {path}")

def generate_image(prompt, save_name):
    """生成单张图片"""
    print(f"\n🎨 开始生成: {save_name}")
    print(f"   提示词: {prompt[:60]}...")
    
    try:
        # 发起生成请求
        response = requests.post(
            f"{BASE_URL}v1/images/generations",
            headers={**COMMON_HEADERS, "X-ModelScope-Async-Mode": "true"},
            data=json.dumps({
                "model": "Tongyi-MAI/Z-Image-Turbo",
                "prompt": prompt
            }, ensure_ascii=False).encode('utf-8'),
            timeout=30
        )
        response.raise_for_status()
        task_id = response.json()["task_id"]
        print(f"   任务ID: {task_id}")
        
        # 轮询等待结果
        max_retries = 60  # 最多等待5分钟
        for i in range(max_retries):
            result = requests.get(
                f"{BASE_URL}v1/tasks/{task_id}",
                headers={**COMMON_HEADERS, "X-ModelScope-Task-Type": "image_generation"},
                timeout=30
            )
            result.raise_for_status()
            data = result.json()
            
            status = data.get("task_status", "UNKNOWN")
            
            if status == "SUCCEED":
                # 下载并保存图片
                image_url = data["output_images"][0]
                image_data = requests.get(image_url, timeout=60).content
                image = Image.open(BytesIO(image_data))
                
                # 保存路径
                date_folder = datetime.now().strftime("%Y%m%d")
                save_dir = os.path.join(UPLOAD_PATH, date_folder)
                ensure_dir(save_dir)
                
                save_path = os.path.join(save_dir, f"{save_name}.jpg")
                image.save(save_path, "JPEG", quality=95)
                print(f"   ✅ 保存成功: {save_path}")
                return save_path
                
            elif status == "FAILED":
                print(f"   ❌ 生成失败: {data.get('message', '未知错误')}")
                return None
            
            else:
                print(f"   ⏳ 等待中... ({i+1}/{max_retries}) 状态: {status}")
                time.sleep(5)
        
        print(f"   ❌ 超时")
        return None
        
    except Exception as e:
        print(f"   ❌ 错误: {str(e)}")
        return None

def print_usage():
    print("""
用法: python generate_images.py [选项]

选项:
  all           - 生成全部图片
  carousel      - 只生成轮播图 (3张)
  homestay      - 只生成民宿封面图 (5张)
  room          - 只生成房间图 (3张)
  <名称>        - 生成指定名称的图片

可用名称:""")
    for name in PROMPTS.keys():
        print(f"  - {name}")

def main():
    print("=" * 60)
    print("🏠 曲靖民宿管理系统 - AI图片生成")
    print("=" * 60)
    
    ensure_dir(UPLOAD_PATH)
    
    # 解析命令行参数
    if len(sys.argv) < 2:
        print_usage()
        return
    
    arg = sys.argv[1].lower()
    
    if arg == 'all':
        # 生成全部
        to_generate = list(PROMPTS.items())
    elif arg == 'carousel':
        to_generate = [(k, v) for k, v in PROMPTS.items() if k.startswith('carousel')]
    elif arg == 'homestay':
        to_generate = [(k, v) for k, v in PROMPTS.items() if k.startswith('homestay')]
    elif arg == 'room':
        to_generate = [(k, v) for k, v in PROMPTS.items() if k.startswith('room')]
    elif arg in PROMPTS:
        to_generate = [(arg, PROMPTS[arg])]
    else:
        print(f"❌ 未知选项: {arg}")
        print_usage()
        return
    
    print(f"\n📋 准备生成 {len(to_generate)} 张图片...")
    
    results = []
    for name, prompt in to_generate:
        result = generate_image(prompt, name)
        results.append((name, result))
        if len(to_generate) > 1:
            time.sleep(2)  # 避免请求过快
    
    print("\n" + "=" * 60)
    print("📊 生成结果汇总:")
    success = sum(1 for _, r in results if r)
    print(f"   成功: {success}/{len(results)}")
    for name, result in results:
        status = "✅" if result else "❌"
        print(f"   {status} {name}")
    print("=" * 60)

if __name__ == "__main__":
    main()
