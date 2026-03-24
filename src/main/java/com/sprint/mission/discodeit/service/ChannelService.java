package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel save(Channel channel);
    Channel findById(UUID id);
    List<Channel> findAll();
    void update(UUID id, Channel updateData);
    public void deleteById(UUID id);

}
