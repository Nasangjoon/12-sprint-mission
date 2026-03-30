package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID userId;
    private String content;
    private String link;
    private Long createdAt;
    private Long updatedAt;


    public Message(UUID userId, String content, String link) {
        id = UUID.randomUUID();
        this.userId = userId;
        this.content = content;
        this.link = link;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void update(UUID id,UUID userId, String content, String link) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.link = link;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초 SSS");
        ZoneId zone = ZoneId.of("Asia/Seoul");
        String createdStr = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(createdAt), zone).format(dtf);
        String updatedStr = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(updatedAt), zone).format(dtf);


        return """
                Message{
                메세지 UUID: %s,
                유저의 UUID: %s,
                문자 내용: %s,
                메세지 링크: %s,
                생성일자: %s,
                수정일자: %s
                }
                """.formatted(id,userId,content, link, createdStr, updatedStr);
    }
}
