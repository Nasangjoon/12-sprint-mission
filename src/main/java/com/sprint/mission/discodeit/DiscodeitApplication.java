package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Optional;


@SpringBootApplication
public class DiscodeitApplication {

    static User setupUser(UserService userService) {
        UserCreateRequest request = new UserCreateRequest("나상준", "sangjoon@codeit.com", "sangjoon1212");
        User user = userService.create(request, Optional.empty());
        return user;
    }

    static User findUser(UserService userService) {
        UserCreateRequest request = new UserCreateRequest("나상1", "test123@gmail.com", "123");
                User user1 = userService.create(request, Optional.empty());
                User user2 = userService.find(user1.getId());
                return user2;
    }

    static Channel setupChannel(ChannelService channelService) {
        PublicChannelCreateRequest request = new PublicChannelCreateRequest("공지", "공지 채널입니다.");
        Channel channel = channelService.create(request);
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        MessageCreateRequest request = new MessageCreateRequest("안녕하세요.", channel.getId(), author.getId());
        Message message = messageService.create(request, new ArrayList<>());
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);



        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        User user1 = findUser(userService);
        // 테스트
        messageCreateTest(messageService, channel, user);
        System.out.println(user1.toString());
        }



    }

