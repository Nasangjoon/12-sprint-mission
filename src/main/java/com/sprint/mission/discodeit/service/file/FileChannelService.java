package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final String FILE_PATH = "channels.dat";
    private final File file = new File(FILE_PATH);

    @SuppressWarnings("unchecked")
    private List<Channel> readFile() {
        if (!file.exists() || file.length() == 0) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void writeFile(List<Channel> channels) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(channels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Channel create(ChannelType type, String name, String description, UUID adminId) {
        Channel newChannel = new Channel(name, adminId, type, description);
        List<Channel> channels = readFile();
        channels.add(newChannel);
        writeFile(channels);
        return newChannel;
    }

    @Override
    public Channel find(UUID channelId) {
        return readFile().stream()
                .filter(c -> c.getId().equals(channelId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Channel> findAll() {
        return readFile();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        List<Channel> channels = readFile();
        Channel updatedChannel = null;

        for (Channel c : channels) {
            if (c.getId().equals(channelId)) {
                c.update(newName, newDescription);
                updatedChannel = c;
                break;
            }
        }

        if (updatedChannel != null) {
            writeFile(channels);
        }
        return updatedChannel;
    }

    @Override
    public void delete(UUID channelId) {
        List<Channel> channels = readFile();
        if (channels.removeIf(c -> c.getId().equals(channelId))) {
            writeFile(channels);
        }
    }
}