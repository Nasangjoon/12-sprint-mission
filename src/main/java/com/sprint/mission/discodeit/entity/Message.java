package com.sprint.mission.discodeit.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Message {
    private UUID id;
    private String content;
    private String link;
    private Long createdAt;
    private Long updatedAt;


    public Message(UUID id, String content, String link) {
        this.id = id;
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


    public void update(UUID id, String content, String link) {
        this.id = id;
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
                UUID: %s,
                문자 내용: %s,
                메세지 링크: %s,
                생성일자: %s,
                수정일자: %s
                }
                """.formatted(id, content, link, createdStr, updatedStr);
    }
}
