package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final List<Message> data;

    public JCFMessageService() {
        this.data = new ArrayList<>();
    }

    @Override
    public Message create(String content, UUID channelId, UUID userId) {
        Message message = new Message(userId, content, channelId.toString());
        data.add(message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        for (Message message : data) {
            if (message.getId().equals(messageId)) {
                return message;
            }
        }
        return null;
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message target = find(messageId);
        if (target != null) {
            target.update(newContent);
        }
        return target;
    }

    @Override
    public void delete(UUID messageId) {
        data.removeIf(m -> m.getId().equals(messageId));
    }
}