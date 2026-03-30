package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final String FILE_PATH = "message.dat";
    private final File file = new File(FILE_PATH);

    @SuppressWarnings("unchecked")
    private List<Message> readFile() {
        if (!file.exists() || file.length() == 0) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void writeFile(List<Message> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message create(String content, UUID channelId, UUID userId) {
        Message message = new Message(userId, content, channelId.toString());
        List<Message> messages = readFile();
        messages.add(message);
        writeFile(messages);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        return readFile().stream()
                .filter(m -> m.getId().equals(messageId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Message> findAll() {
        return readFile();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        List<Message> messages = readFile();
        Message target = null;
        for (Message m : messages) {
            if (m.getId().equals(messageId)) {
                m.update(newContent);
                target = m;
                break;
            }
        }
        if (target != null) {
            writeFile(messages);
        }
        return target;
    }

    @Override
    public void delete(UUID messageId) {
        List<Message> messages = readFile();
        if (messages.removeIf(m -> m.getId().equals(messageId))) {
            writeFile(messages);
        }
    }
}