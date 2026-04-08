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
        return "Channel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", admin=" + admin +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", type=" + type +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
