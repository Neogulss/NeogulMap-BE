package com.neogulss.neogulmap.chatbot.controller;

import com.neogulss.neogulmap.auth.dto.UserDTO;
import com.neogulss.neogulmap.chatbot.dto.*;
import com.neogulss.neogulmap.chatbot.service.PolicyChatbotService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatbot")
public class PolicyChatbotController {

    /** 세션 키 상수 */
    private static final String SESSION_KEY_USER = "LOGIN_USER";
    private final PolicyChatbotService policyChatbotService;

    public PolicyChatbotController(PolicyChatbotService policyChatbotService) {
        this.policyChatbotService = policyChatbotService;
    }

    @PostMapping("/send")
    public PolicyChatbotSendResponse send(
            @RequestBody PolicyChatbotSendRequest request,
            HttpSession session
    ){
        Long userIdx = getLoginUserIdx(session);
        return policyChatbotService.send(userIdx, request);
    }

    @GetMapping("/sessions")
    public List<ChatSessionListItemResponse> getSessions(
            HttpSession session
    ){
        Long userIdx = getLoginUserIdx(session);
        return policyChatbotService.getSessions(userIdx);
    }

    @GetMapping("/sessions/{sessionIdx}/logs")
    public List<ChatLogListItemResponse> getSessionLogs(
            @PathVariable Long sessionIdx,
            HttpSession session
    ){
        Long userIdx = getLoginUserIdx(session);
        return policyChatbotService.getSessionLogs(userIdx, sessionIdx);
    }

    @PatchMapping("/sessions/{sessionIdx}/title")
    public ChatSessionTitleResponse updateSessionTitle(
            @PathVariable Long sessionIdx,
            @Valid @RequestBody UpdateChatSessionTitleRequest request,
            HttpSession session
    ){
        Long userIdx = getLoginUserIdx(session);
        return policyChatbotService.updateSessionTitle(userIdx, sessionIdx, request.getTitle());
    }

    @DeleteMapping("/sessions/{sessionIdx}")
    public DeleteChatSessionResponse deleteSession(
            @PathVariable Long sessionIdx,
            HttpSession session
    ){
        Long userIdx = getLoginUserIdx(session);
        return policyChatbotService.deleteSession(userIdx, sessionIdx);
    }

    private Long getLoginUserIdx(HttpSession session){
        UserDTO.UserResponse loginUser = (UserDTO.UserResponse) session.getAttribute(SESSION_KEY_USER);

        if(loginUser == null){
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return Long.valueOf(loginUser.getUserIdx());
    }

}
