package com.homestay.service;

import java.util.List;
import java.util.Map;

/**
 * AI服务接口
 */
public interface AiService {
    
    /**
     * 智能对话
     * @param messages 消息历史
     * @return AI回复
     */
    String chat(List<Map<String, String>> messages);
    
    /**
     * 单次问答
     * @param question 问题
     * @return AI回复
     */
    String ask(String question);
    
    /**
     * 检查AI服务是否可用
     */
    boolean isEnabled();
}

