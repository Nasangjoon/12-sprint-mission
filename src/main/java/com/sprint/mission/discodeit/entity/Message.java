package com.sprint.mission.discodeit.entity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Message {
    private UUID messageId;
    private List<Message> message;
    private List<User> admin;
    private List<User> user;
    private String profileImageUrl;
    private Long createdAt;
    private Long updatedAt;

    public Message(UUID messageId, List<Message>message, List<User> admin, List<User> user, String profileImageUrl ) {
        this.messageId = messageId;
        this.message = message;
        this.admin = admin;
        this.user = user;
        this.profileImageUrl = profileImageUrl;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getMessageId() {
        return messageId;
    }

    public List<Message> getMessage() {
        return message;
    }

    public List<User> getAdmin() {
        return admin;
    }

    public List<User> getUser() {
        return user;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateMessage(UUID messageId,Message message, User admin, User user, String profileImageUrl) {
        this.messageId = messageId;
        this.message = Arrays.asList(message);
        this.admin = Arrays.asList(admin);
        this.user = Arrays.asList(user);
        this.profileImageUrl = profileImageUrl;
        this.updatedAt = System.currentTimeMillis();
    }


}
