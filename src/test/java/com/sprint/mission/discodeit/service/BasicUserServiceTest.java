package com.sprint.mission.discodeit.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicUserService basicUserService;

  private UUID userId;
  private String username;
  private String password;
  private String email;
  private User user;
  private UserDto userDto;


  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    username = "test1";
    password = "test1";
    email = "test1@gmail.com";
    user = new User(username, email, password, null);
    ReflectionTestUtils.setField(user, "id", userId);
    userDto = new UserDto(userId, username, email, null, true);
  }

  @Test
  @DisplayName("사용자 생성 테스트 성공 사례")
  void createUser_Success() {
    //given
    UserCreateRequest request = new UserCreateRequest(username, password, email);

    given(userRepository.existsByEmail(eq(email))).willReturn(false);
    given(userRepository.existsByUsername(eq(username))).willReturn(false);
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    //when
    UserDto result = basicUserService.create(request, Optional.empty());

    //then
    assertThat(result).isEqualTo(userDto);
    verify(userRepository).save(any(User.class));
  }

  @Test
  @DisplayName("사용자 생성 테스트 실패 사례1 (이미 존재하는 이메일)")
  void createUser_ExistingUser() {
    //given
    UserCreateRequest request = new UserCreateRequest(username, password, email);
    given(userRepository.existsByEmail(eq(email))).willReturn(true);

    //when & then
    assertThatThrownBy(() -> basicUserService.create(request, Optional.empty()))
        .isInstanceOf(UserAlreadyExistsException.class);

  }

  @Test
  @DisplayName("사용자 생성 테스트 실패 사례2 (이미 존재하는 사용자명)")
  void createUser_UserAlreadyExists() {
    //given
    UserCreateRequest request = new UserCreateRequest(username, password, email);
    given(userRepository.existsByEmail(eq(email))).willReturn(false);
    given(userRepository.existsByUsername(eq(username))).willReturn(true);

    // when & then
    assertThatThrownBy(() -> basicUserService.create(request, Optional.empty()))
        .isInstanceOf(UserAlreadyExistsException.class);
  }

  @Test
  @DisplayName("사용자 조회 테스트 성공 사례")
  void findById_Success() {
    //given
    given(userRepository.findById(eq(userId))).willReturn(Optional.of(user));
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    //when
    UserDto result = basicUserService.find(userId);

    //then
    assertThat(result).isEqualTo(userDto);
  }

  @Test
  @DisplayName("사용자 조회 테스트 실패 사례 (존재하지 않는 사용자)")
  void findById_UserNotFound() {
    //given
    given(userRepository.findById(eq(userId))).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> basicUserService.find(userId))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @DisplayName("사용자 수정 테스트 성공 사례")
  void updateUser_Success() {
    // given
    String newUsername = "newUsername";
    String newPassword = "newPassword";
    String newEmail = "newEmail@gmail.com";
    UserUpdateRequest request = new UserUpdateRequest(newUsername, newPassword, newEmail);

    given(userRepository.findById(eq(userId))).willReturn(Optional.of(user));
    given(userRepository.existsByEmail(eq(newEmail))).willReturn(false);
    given(userRepository.existsByUsername(eq(newUsername))).willReturn(false);
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    UserDto result = basicUserService.update(userId, request, Optional.empty());

    assertThat(result).isEqualTo(userDto);
  }

  @Test
  @DisplayName("사용자 수정 테스트 실패 사례 (존재하지 않는 사용자)")
  void updateUser_UserNotFound() {
    UserUpdateRequest request = new UserUpdateRequest("newUsername", "newPassword", "newEmail");
    given(userRepository.findById(eq(userId))).willReturn(Optional.empty());

    assertThatThrownBy(() -> basicUserService.update(userId, request, Optional.empty()))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @DisplayName("사용자 삭제 테스트 성공 사례")
  void deleteUser_Success() {
    given(userRepository.existsById(eq(userId))).willReturn(true);
    basicUserService.delete(userId);
    verify(userRepository).existsById(eq(userId));
  }

  @Test
  @DisplayName("사용자 삭제 테스트 실패 사례 (존재하지 않는 사용자)")
  void deleteUser_UserNotFound() {
    given(userRepository.existsById(eq(userId))).willReturn(false);
    assertThatThrownBy(() -> basicUserService.delete(userId))
        .isInstanceOf(UserNotFoundException.class);
  }
}
