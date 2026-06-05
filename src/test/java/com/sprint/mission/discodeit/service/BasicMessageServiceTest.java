package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceTest {

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private MessageMapper messageMapper;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private PageResponseMapper pageResponseMapper;

  @InjectMocks
  private BasicMessageService basicMessageService;

  private UUID channelId;
  private UUID authorId;
  private UUID messageId;
  private Channel channel;
  private User author;
  private Message message;
  private MessageDto messageDto;
  private UserDto authorDto;

  @BeforeEach
  void setUp() {
    channelId = UUID.randomUUID();
    authorId = UUID.randomUUID();
    messageId = UUID.randomUUID();

    channel = new Channel(ChannelType.PUBLIC, "General", "General Description");
    ReflectionTestUtils.setField(channel, "id", channelId);

    author = new User("user1", "user1@gmail.com", "pass", null);
    ReflectionTestUtils.setField(author, "id", authorId);

    message = new Message("Hello World", channel, author, new ArrayList<>());
    ReflectionTestUtils.setField(message, "id", messageId);
    ReflectionTestUtils.setField(message, "createdAt", Instant.now());

    authorDto = new UserDto(authorId, "user1", "user1@gmail.com", null, true);
    messageDto = new MessageDto(messageId, message.getCreatedAt(), null, "Hello World", channelId,
        authorDto, Collections.emptyList());
  }

  // --- create ---

  @Test
  @DisplayName("메시지 생성 테스트 성공 사례 (첨부파일 없음)")
  void createMessage_Success_NoAttachments() {
    // given
    MessageCreateRequest request = new MessageCreateRequest("Hello World", channelId, authorId);
    given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(channel));
    given(userRepository.findById(eq(authorId))).willReturn(Optional.of(author));
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

    // when
    MessageDto result = basicMessageService.create(request, Collections.emptyList());

    // then
    assertThat(result).isEqualTo(messageDto);
    verify(messageRepository).save(any(Message.class));
  }

  @Test
  @DisplayName("메시지 생성 테스트 성공 사례 (첨부파일 있음)")
  void createMessage_Success_WithAttachments() {
    // given
    MessageCreateRequest request = new MessageCreateRequest("Hello World", channelId, authorId);
    BinaryContentCreateRequest attachmentRequest = new BinaryContentCreateRequest("test.png",
        "image/png", new byte[]{1, 2, 3});
    List<BinaryContentCreateRequest> attachmentRequests = List.of(attachmentRequest);

    BinaryContent binaryContent = new BinaryContent("test.png", 3L, "image/png");
    UUID attachmentId = UUID.randomUUID();
    ReflectionTestUtils.setField(binaryContent, "id", attachmentId);

    given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(channel));
    given(userRepository.findById(eq(authorId))).willReturn(Optional.of(author));
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

    // when
    MessageDto result = basicMessageService.create(request, attachmentRequests);

    // then
    assertThat(result).isEqualTo(messageDto);
    verify(binaryContentRepository).save(any(BinaryContent.class));
    verify(binaryContentStorage).put(any(UUID.class), eq(attachmentRequest.bytes()));
    verify(messageRepository).save(any(Message.class));
  }

  @Test
  @DisplayName("메시지 생성 테스트 실패 사례 (존재하지 않는 채널)")
  void createMessage_Failure_ChannelNotFound() {
    // given
    MessageCreateRequest request = new MessageCreateRequest("Hello World", channelId, authorId);
    given(channelRepository.findById(eq(channelId))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> basicMessageService.create(request, Collections.emptyList()))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  @DisplayName("메시지 생성 테스트 실패 사례 (존재하지 않는 사용자)")
  void createMessage_Failure_UserNotFound() {
    // given
    MessageCreateRequest request = new MessageCreateRequest("Hello World", channelId, authorId);
    given(channelRepository.findById(eq(channelId))).willReturn(Optional.of(channel));
    given(userRepository.findById(eq(authorId))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> basicMessageService.create(request, Collections.emptyList()))
        .isInstanceOf(UserNotFoundException.class);
  }

  // --- update ---

  @Test
  @DisplayName("메시지 수정 테스트 성공 사례 (일반 텍스트 변경)")
  void updateMessage_Success_Normal() {
    // given
    String newContent = "Updated Hello World";
    MessageUpdateRequest request = new MessageUpdateRequest(newContent);
    given(messageRepository.findById(eq(messageId))).willReturn(Optional.of(message));
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

    // when
    MessageDto result = basicMessageService.update(messageId, request);

    // then
    assertThat(result).isEqualTo(messageDto);
    assertThat(message.getContent()).isEqualTo(newContent);
  }

  @Test
  @DisplayName("메시지 수정 테스트 성공 사례 (이전과 동일한 내용으로 변경 시도)")
  void updateMessage_Success_SameContent() {
    // given
    String sameContent = "Hello World";
    MessageUpdateRequest request = new MessageUpdateRequest(sameContent);
    given(messageRepository.findById(eq(messageId))).willReturn(Optional.of(message));
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

    // when
    MessageDto result = basicMessageService.update(messageId, request);

    // then
    assertThat(result).isEqualTo(messageDto);
    assertThat(message.getContent()).isEqualTo(sameContent);
  }

  @Test
  @DisplayName("메시지 수정 테스트 실패 사례 (메시지 존재하지 않음)")
  void updateMessage_Failure_MessageNotFound() {
    // given
    MessageUpdateRequest request = new MessageUpdateRequest("Updated Content");
    given(messageRepository.findById(eq(messageId))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> basicMessageService.update(messageId, request))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  @DisplayName("메시지 수정 테스트 실패 사례 (수정 시 데이터베이스 연결/저장 오류)")
  void updateMessage_Failure_RepositoryException() {
    // given
    MessageUpdateRequest request = new MessageUpdateRequest("Update error context");
    given(messageRepository.findById(eq(messageId))).willThrow(
        new RuntimeException("Database timeout"));

    // when & then
    assertThatThrownBy(() -> basicMessageService.update(messageId, request))
        .isInstanceOf(RuntimeException.class);
  }

  // --- delete ---

  @Test
  @DisplayName("메시지 삭제 테스트 성공 사례 1 (일반 삭제)")
  void deleteMessage_Success_Base() {
    // given
    given(messageRepository.existsById(eq(messageId))).willReturn(true);

    // when
    basicMessageService.delete(messageId);

    // then
    verify(messageRepository).deleteById(eq(messageId));
  }

  @Test
  @DisplayName("메시지 삭제 테스트 성공 사례 2 (다른 메시지 ID 삭제 검증)")
  void deleteMessage_Success_DifferentId() {
    // given
    UUID anotherMessageId = UUID.randomUUID();
    given(messageRepository.existsById(eq(anotherMessageId))).willReturn(true);

    // when
    basicMessageService.delete(anotherMessageId);

    // then
    verify(messageRepository).deleteById(eq(anotherMessageId));
  }

  @Test
  @DisplayName("메시지 삭제 테스트 실패 사례 (메시지 존재하지 않음)")
  void deleteMessage_Failure_MessageNotFound() {
    // given
    given(messageRepository.existsById(eq(messageId))).willReturn(false);

    // when & then
    assertThatThrownBy(() -> basicMessageService.delete(messageId))
        .isInstanceOf(MessageNotFoundException.class);
  }

  @Test
  @DisplayName("메시지 삭제 테스트 실패 사례 (삭제 중 레포지토리 차원의 데이터베이스 예외 발생)")
  void deleteMessage_Failure_RepositoryException() {
    // given
    given(messageRepository.existsById(eq(messageId))).willReturn(true);
    doThrow(new RuntimeException("Delete query failed")).when(messageRepository)
        .deleteById(eq(messageId));

    // when & then
    assertThatThrownBy(() -> basicMessageService.delete(messageId))
        .isInstanceOf(RuntimeException.class);
  }

  // --- findByChannelId (findAllByChannelId) ---

  @Test
  @DisplayName("채널 ID로 메시지 목록 조회 성공 사례 (메시지 있음)")
  void findAllByChannelId_Success_WithMessages() {
    // given
    Instant now = Instant.now();
    Pageable pageable = PageRequest.of(0, 10);
    List<Message> messageList = List.of(message);
    Slice<Message> slice = new SliceImpl<>(messageList, pageable, false);

    given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(Instant.class),
        eq(pageable)))
        .willReturn(slice);
    given(messageMapper.toDto(eq(message))).willReturn(messageDto);

    PageResponse<MessageDto> pageResponse = new PageResponse<>(List.of(messageDto),
        message.getCreatedAt(), pageable.getPageSize(), false, null);
    given(pageResponseMapper.fromSlice(any(Slice.class), eq(message.getCreatedAt()))).willReturn(
        pageResponse);

    // when
    PageResponse<MessageDto> result = basicMessageService.findAllByChannelId(channelId, now,
        pageable);

    // then
    assertThat(result).isEqualTo(pageResponse);
  }

  @Test
  @DisplayName("채널 ID로 메시지 목록 조회 성공 사례 (메시지 없음)")
  void findAllByChannelId_Success_Empty() {
    // given
    Instant now = Instant.now();
    Pageable pageable = PageRequest.of(0, 10);
    Slice<Message> slice = new SliceImpl<>(Collections.emptyList(), pageable, false);

    given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(Instant.class),
        eq(pageable)))
        .willReturn(slice);

    PageResponse<MessageDto> pageResponse = new PageResponse<>(Collections.emptyList(), null,
        pageable.getPageSize(), false, null);
    given(pageResponseMapper.fromSlice(any(Slice.class), eq(null))).willReturn(pageResponse);

    // when
    PageResponse<MessageDto> result = basicMessageService.findAllByChannelId(channelId, now,
        pageable);

    // then
    assertThat(result).isEqualTo(pageResponse);
  }

  @Test
  @DisplayName("채널 ID로 메시지 목록 조회 실패 사례 (레포지토리 에러)")
  void findAllByChannelId_Failure_RepositoryError() {
    // given
    Instant now = Instant.now();
    Pageable pageable = PageRequest.of(0, 10);
    given(messageRepository.findAllByChannelIdWithAuthor(eq(channelId), any(Instant.class),
        eq(pageable)))
        .willThrow(new RuntimeException("Database connection failure"));

    // when & then
    assertThatThrownBy(() -> basicMessageService.findAllByChannelId(channelId, now, pageable))
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("채널 ID로 메시지 목록 조회 실패 사례 (채널 ID가 null일 때 예외 발생)")
  void findAllByChannelId_Failure_NullChannelId() {
    // given
    Instant now = Instant.now();
    Pageable pageable = PageRequest.of(0, 10);
    given(
        messageRepository.findAllByChannelIdWithAuthor(eq(null), any(Instant.class), eq(pageable)))
        .willThrow(new IllegalArgumentException("Channel ID must not be null"));

    // when & then
    assertThatThrownBy(() -> basicMessageService.findAllByChannelId(null, now, pageable))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
