package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String name;
    private String description;
    private UUID admin;
    private List<UUID> user;
    private Long createdAt;
    private ChannelType type; // 추가
    private Long updatedAt;

    public Channel( String name, UUID admin, ChannelType type, String description) {
        id =  UUID.randomUUID();
        this.name = name;
        this.admin = admin;
        this.user = new ArrayList<>();
        this.description = description;
        this.user.add(admin);
        this.type = type;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public UUID getAdmin() {
        return admin;
    }

    public List<UUID> getUser() {
        return user;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public ChannelType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void update(String newName, String newDescription) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
            anyValueUpdated = true;
        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now().getEpochSecond();
        }
    }

    public void addUser(UUID userId) {
        if (!user.contains(userId)) {
            user.add(userId);
            this.updatedAt = System.currentTimeMillis();
        }
    }


    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ssSSS초");
        ZoneId zone = ZoneId.of("Asia/Seoul");
         String createdStr = java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(createdAt), zone).format(dtf);
         String updatedStr = java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(updatedAt), zone).format(dtf);
         return """
                 Channel {
                     채널 UUID: %s
                     채널명 : %s
                     관리자 : %s
                     참여자 : %s
                     채널 유형 : %s
                     생성일자 : %s
                     수정일자 : %s
                 }
                 """.formatted(id, name, admin, user,type, createdStr, updatedStr);


    }
}
