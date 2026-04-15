package com.neogulss.neogulmap.chatbot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyChatbotSendRequest {

    private Long sessionIdx;
    private String userQuery;
    private UserProfile userProfile;

    @Getter
    @Setter
    public static class UserProfile {
        private String industry;
        private Integer age;
        private Boolean hasBusinessRegistration;
        private String region;
        private String startupStatus;
    }
}
