package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final List<Channel> data;

    public JCFChannelService() {data = new ArrayList<Channel>();}

    @Override
    public Channel save(Channel channel) {
        data.add(channel);
        return channel;
    }


    @Override
    public Channel findById(UUID id) {
        for (Channel channel : data) {
            if (channel.getId().equals(id) ) {
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
    public void update(UUID id, Channel updateData) {
        Channel foundChannel = findById(id);
        if (foundChannel != null) {
            foundChannel.update(id, updateData.getName(), updateData.getAdmin(), updateData.getUser());
        }
    }


    @Override
    public void deleteById(UUID id) {
        data.removeIf(channel -> channel.getId().equals(id));
    }





}
