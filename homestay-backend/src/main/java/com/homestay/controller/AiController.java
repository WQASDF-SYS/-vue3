package com.homestay.controller;

import com.homestay.common.Result;
import com.homestay.service.AiService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI控制器
 */
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {
    
    private final AiService aiService;
    
    /**
     * 智能对话
     */
    @PostMapping("/chat")
    public Result<Map<String, Object>> chat(@RequestBody ChatRequest request) {
        String reply = aiService.chat(request.getMessages());
        Map<String, Object> data = new HashMap<>();
        data.put("reply", reply);
        return Result.ok(data);
    }
    
    /**
     * 单次问答
     */
    @PostMapping("/ask")
    public Result<Map<String, Object>> ask(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        String reply = aiService.ask(question);
        Map<String, Object> data = new HashMap<>();
        data.put("reply", reply);
        return Result.ok(data);
    }
    
    /**
     * 检查AI服务状态
     */
    @GetMapping("/status")
    public Result<Map<String, Object>> status() {
        Map<String, Object> data = new HashMap<>();
        data.put("enabled", aiService.isEnabled());
        return Result.ok(data);
    }
    
    /**
     * 聊天请求
     */
    @Data
    public static class ChatRequest {
        private List<Map<String, String>> messages;
    }
}

