package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private final String FILE_PATH = "message.dat";

    File file = new File(FILE_PATH);

    @SuppressWarnings("unchecked")
    private List<Message> readFile() {
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Message>) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }


    private void  writeFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(readFile());
        }  catch (IOException e) {
            e.printStackTrace();
        }}

    private void writeMessage() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(readFile());
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message save(Message message) {
        List<Message> messages = readFile();
        messages.add(message);
        writeFile();
        return message;
    }

    @Override
    public List<Message> findById(UUID id) {
        return readFile().stream()
                .filter(m -> m.getId().equals(id))
                .toList();
    }

    @Override
    public List<Message> findAll() {
        return readFile();
    }

    @Override
    public Message deleteMessage(UUID id) {
        List<Message> messages = readFile();
        Message target = messages.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
        if (target != null) {
            messages.remove(target);
            writeFile();
        }
        return target;
    }

    @Override
    public Message update(UUID id, Message message) {
        List<Message> messages = readFile();
        Message target = messages.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
        if (target != null) {
            messages.remove(target);
        }
        messages.add(message);
        writeFile();
        return target;
    }


}
