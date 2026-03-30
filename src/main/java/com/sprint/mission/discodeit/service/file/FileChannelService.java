package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final String FILE_PATH = "channels.dat";

    File file = new File(FILE_PATH);

    @SuppressWarnings("unchecked")
    private List<Channel> readFile() {
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Channel>) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void writeFile(List<Channel> channels) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(channels);
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Channel save(Channel channel) {
        List<Channel> channels = readFile();
        channels.add(channel);
        writeFile(channels);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return readFile().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(UUID id, Channel updateData) {
        List<Channel> channels = readFile();
        for (int i = 0; i < channels.size(); i++) {
            if (channels.get(i).getId().equals(id)) {
                channels.get(i).update(id, updateData.getName(), updateData.getAdmin(), updateData.getUser(), updateData.getType());
                break;
            }
        }
        writeFile(channels);
    }

    @Override
    public List<Channel> findAll() {return readFile();}

    @Override
    public void deleteById(UUID id) {
        List<Channel> channels = readFile();
        channels.removeIf(c -> c.getId().equals(id));
        writeFile(channels);
    }

}
