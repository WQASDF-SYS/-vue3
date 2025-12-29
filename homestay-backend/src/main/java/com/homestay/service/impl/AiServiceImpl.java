package com.homestay.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.homestay.exception.BusinessException;
import com.homestay.mapper.SysConfigMapper;
import com.homestay.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智谱AI服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {
    
    private final SysConfigMapper sysConfigMapper;
    
    @Value("${zhipu.api-key}")
    private String apiKey;
    
    @Value("${zhipu.model}")
    private String model;
    
    @Value("${zhipu.base-url}")
    private String baseUrl;
    
    /** 系统提示词 */
    private static final String SYSTEM_PROMPT = """
            你是曲靖民宿管理系统的智能客服助手。你的主要职责是：
            1. 回答用户关于民宿预订、入住、退房等问题
            2. 介绍曲靖地区的民宿特色和旅游景点
            3. 帮助用户解决使用系统过程中遇到的问题
            4. 提供友好、专业、简洁的服务
            
            注意事项：
            - 回答要简洁明了，不要过于冗长
            - 如果不确定的信息，请建议用户联系人工客服
            - 保持礼貌和专业的态度
            """;
    
    @Override
    public String chat(List<Map<String, String>> messages) {
        if (!isEnabled()) {
            throw new BusinessException("AI服务未启用");
        }
        
        try {
            // 构建请求消息
            List<Map<String, String>> allMessages = new ArrayList<>();
            
            // 添加系统提示
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", SYSTEM_PROMPT);
            allMessages.add(systemMsg);
            
            // 添加历史消息
            allMessages.addAll(messages);
            
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.set("model", model);
            requestBody.set("messages", allMessages);
            requestBody.set("max_tokens", 1024);
            requestBody.set("temperature", 0.7);
            
            // 发送请求
            HttpResponse response = HttpRequest.post(baseUrl + "/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(requestBody.toString())
                    .timeout(30000)
                    .execute();
            
            if (!response.isOk()) {
                log.error("智谱AI请求失败: {}", response.body());
                throw new BusinessException("AI服务暂时不可用");
            }
            
            // 解析响应
            JSONObject responseBody = JSONUtil.parseObj(response.body());
            JSONArray choices = responseBody.getJSONArray("choices");
            if (choices != null && !choices.isEmpty()) {
                JSONObject choice = choices.getJSONObject(0);
                JSONObject message = choice.getJSONObject("message");
                return message.getStr("content");
            }
            
            return "抱歉，我暂时无法回答您的问题，请稍后再试。";
            
        } catch (Exception e) {
            log.error("智谱AI调用异常", e);
            throw new BusinessException("AI服务异常，请稍后再试");
        }
    }
    
    @Override
    public String ask(String question) {
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", question);
        messages.add(userMsg);
        return chat(messages);
    }
    
    @Override
    public boolean isEnabled() {
        String enabled = sysConfigMapper.selectValueByKey("ai_enabled");
        return "true".equalsIgnoreCase(enabled);
    }
}

