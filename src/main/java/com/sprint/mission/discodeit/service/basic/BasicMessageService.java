package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public BasicMessageService(MessageRepository messageRepository, ChannelRepository channelRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Message create(String content, UUID channelId, UUID userId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel not found with id " + channelId + "찾을 수 없습니다.");
        }
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("Author not found with id " + userId + "찾을 수 없습니다.");
        }

        Message message = new Message(userId, content, channelId.toString());
        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + "찾을 수 없습니다.") );
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + "찾을 수 없습니다."));
        message.update(newContent);
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel not found with id " + channelId + "찾을 수 없습니다.");
        }
        messageRepository.deleteById(channelId);
    }
}
