package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final List<Message> data;

    public JCFMessageService() {data = new ArrayList<>();}

    @Override
    public Message save(Message message)
    {
        data.add(message);
        return message;
    }

    @Override
    public List<Message> findById(UUID id) {
        for (Message message : data) {
            if (message.getId().equals(id)) {
                return List.of(message);
            }
        }
        return null;
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Message update(UUID id, Message message) {
        for (Message m : data) {
            if (m.getId().equals(id)) {
                m.update(id, message.getUserId(), message.getContent(), message.getLink());
                return m;
            }
        }
        return null;
    }

    @Override
    public Message deleteMessage(UUID id) {
        Message deleteMessage = findById(id).get(0);
            if (deleteMessage != null) {
                data.remove(deleteMessage);
            }
        return null;
    }


}
