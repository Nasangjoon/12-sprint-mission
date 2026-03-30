package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
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

    static Message messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
        return message;
    }

    public static void main(String[] args) {

        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);

        User user = setupUser(userService);
        Channel channel = setupChannel(channelService, user);
        Message message = messageCreateTest(messageService, channel, user);

        System.out.println(user);
        System.out.println(channel);
        System.out.println(message);


    }
}