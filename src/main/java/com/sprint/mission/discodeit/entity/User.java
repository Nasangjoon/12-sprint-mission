package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String username;
    private transient String password;
    private String email;
    private String nickname;
    private Long createdAt;
    private Long updatedAt;
    private String phoneNumber;
    private String profileImageUrl;

    public User(String username, String password, String email, String nickname, String profileImageUrl, String phoneNumber) {
        id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.phoneNumber = phoneNumber;
        createdAt = System.currentTimeMillis();
        updatedAt = System.currentTimeMillis();

    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void update(String username, String email, String password, String nickname, String profileImageUrl,  String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.phoneNumber = phoneNumber;
        updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss.SSS초");
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        String createdStr = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(createdAt), zoneId).format(formatter);
        String updatedStr = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(updatedAt), zoneId).format(formatter);


        return """
                User {
                    UUID: %s,
                    이름: %s,
                    이메일: %s,
                    닉네임: %s,
                    생성일자: %s,
                    업데이트일자: %s
                    프로필 이미지: %s
                    전화번호: %s
                }
                """.formatted(id, username, email, nickname, createdStr, updatedStr,  profileImageUrl, phoneNumber);
    }



}
