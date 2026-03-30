package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

public class JavaApplication {

    static User setupUser(UserService userService) {
        return userService.create("woody", "woody1234", "woody@codeit.com", "woody", null, null);
    }

    static Channel setupChannel(ChannelService channelService, User admin) {
        return channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.", admin.getId());
    }

    public static void main(String[] args) {

        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new JCFChannelRepository();

        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);

        User user = setupUser(userService);
        Channel channel = setupChannel(channelService, user);

        System.out.println(user);
        System.out.println(channel);


    }
}