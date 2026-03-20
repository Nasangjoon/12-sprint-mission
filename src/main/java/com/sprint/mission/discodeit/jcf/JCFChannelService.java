package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.Arrays;
import java.util.List;

public class JCFChannelService implements ChannelService {

    private final List<Channel> data;

    public JCFChannelService() {
        this.data = Arrays.asList();
    }




}
