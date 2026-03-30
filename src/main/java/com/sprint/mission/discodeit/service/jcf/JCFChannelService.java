package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final List<Channel> data;

    public JCFChannelService() {
        this.data = new ArrayList<>();
    }

    @Override
    public Channel create(ChannelType type, String name, String description, UUID adminId) {
        Channel newChannel = new Channel(name, adminId, type, description);
        data.add(newChannel);
        return newChannel;
    }

    @Override
    public Channel find(UUID channelId) {
        for (Channel channel : data) {
            if (channel.getId().equals(channelId)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel foundChannel = find(channelId);
        if (foundChannel != null) {
            foundChannel.update(newName, newDescription);
        }
        return foundChannel;
    }

    @Override
    public void delete(UUID channelId) {
        data.removeIf(channel -> channel.getId().equals(channelId));
    }
}