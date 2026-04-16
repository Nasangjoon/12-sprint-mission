package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    static User setupUser(UserService userService) {
        return userService.create("woody", "woody1234", "woody@codeit.com", "woody", null, null);
    }

    static User findUser(UserService userService, UUID userId) {
        return userService.find(userId);
    }

    static User findAllUsers(UserService userService) {
        return userService.findAll().get(0);
    }

    static User updateUser(UserService userService, UUID id) {
        return userService.update(id, "newWoody", "update@gmail.com", "update123");
    }

    static void deleteUser(UserService userService, UUID userId) {
        userService.delete(userId);
    }

    static Channel setupChannel(ChannelService channelService, User admin) {
        return channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.", admin.getId());
    }

    static Channel findChannel(ChannelService channelService, UUID channelId) {
        return channelService.find(channelId);
    }

    static Channel findAllChannels(ChannelService channelService) {
        return channelService.findAll().get(0);
    }

    static Channel updateChannel(ChannelService channelService, UUID channelId) {
        return channelService.update(channelId, "업데이트된 채널 이름", "업데이트된 채널 설명");
    }

    static void deleteChannel(ChannelService channelService, UUID channelId) {
        channelService.delete(channelId);
    }

    static Message messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
        return message;
    }

    static Message findMessage(MessageService messageService, UUID messageId) {
        Message message = messageService.find(messageId);
        System.out.println("메시지 조회: " + message.getContent());
        return message;
    }

    static Message findALlMessage(MessageService messageService, UUID channelId) {
        return messageService.findAll().get(0);
    }

    static Message updateMessage(MessageService messageService, UUID channelId, Message message) {
        Message updatedMessage = messageService.update(message.getId(), "업데이트된 메시지 내용");
        System.out.println("메시지 업데이트: " + updatedMessage.getContent());
        return updatedMessage;
    }

    static void deleteMessage(MessageService messageService, UUID messageId) {
        messageService.delete(messageId);
        System.out.println("메시지 삭제: " + messageId);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // Spring Context에서 Bean을 조회하여 각 서비스 구현체 할당
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // 셋업
        User user = setupUser(userService);
        User findUser = findUser(userService, user.getId());
        User findAllUsers = findAllUsers(userService);
        User updateUser = updateUser(userService, findUser.getId());

        Channel channel = setupChannel(channelService, user);
        Channel findChannel = findChannel(channelService, channel.getId());
        Channel findAllChannels = findAllChannels(channelService);
        Channel updateChannel = updateChannel(channelService, channel.getId());

        Message message = messageCreateTest(messageService, channel, user);
        Message findMessage = findMessage(messageService, message.getId());
        Message findAllMessage = findALlMessage(messageService, channel.getId());
        Message updateMessage = updateMessage(messageService, channel.getId(), message);

        System.out.println("-------------------유저 테스트-----------------------");
        System.out.println(user);
        System.out.println(findUser);
        System.out.println(findAllUsers);
        System.out.println(updateUser);
        deleteUser(userService, findUser.getId());
        System.out.println("삭제 후 사용자 목록: " + userService.findAll());
        System.out.println("-------------------유저 테스트 끝-----------------------");

        System.out.println("-------------------채널 테스트-----------------------");
        System.out.println(channel);
        System.out.println(findChannel);
        System.out.println(findAllChannels);
        System.out.println(updateChannel);
        deleteChannel(channelService, findChannel.getId());
        System.out.println("삭제 후 채널 목록: " + channelService.findAll());
        System.out.println("-------------------채널 테스트 끝---------------------");

        System.out.println("-------------------메시지 테스트-----------------------");
        System.out.println(message);
        System.out.println(findMessage);
        System.out.println(findAllMessage);
        System.out.println(updateMessage);
        deleteMessage(messageService, findMessage.getId());
        System.out.println("삭제 후 메시지 목록: " + messageService.findAll());
        System.out.println("-------------------메시지 테스트 끝---------------------");
    }

}
