package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType type, String name, String description, UUID adminId) {
        Channel channel = new Channel(name, adminId, type, description);
        return channelRepository.save(channel);
    }

    @Override
    public Channel find(UUID channelId){
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel with id " + channelId + " 찾을 수 없습니다."));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID channelId, String newName, String newDescription) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel with id " + channelId + " 찾을 수 없습니다."));
        channel.update(newName, newDescription);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new RuntimeException("Channel with id " + channelId + " 찾을 수 없습니다.");
        }
        channelRepository.deleteById(channelId);
    }



}
