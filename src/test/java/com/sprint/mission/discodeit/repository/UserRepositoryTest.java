package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  private User createTestUser(String username, String email) {
    BinaryContent profile = new BinaryContent("profile.jpg", 1024L, "image/jpeg");
    User user = new User(username, email, "password", profile);

    UserStatus status = new UserStatus(user, Instant.now());

    return user;
  }

  @Test
  @DisplayName("사용자 이름으로 사용자 조회 성공")
  void testFindByUsername() {

    String username = "username";
    User user = createTestUser(username, "test@gmail.com");
    userRepository.save(user);

    testEntityManager.flush();
    testEntityManager.clear();

    Optional<User> foundUser = userRepository.findByUsername(username);

    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getUsername()).isEqualTo(username);
  }

  @Test
  @DisplayName("존재하지 않는 사용자 이름으로 조회 시 Optional.empty() 반환")
  void testFindByEmail() {
    String notExistingUsername = "nonExistingUsername";

    Optional<User> foundUser = userRepository.findByUsername(notExistingUsername);

    assertThat(foundUser).isEmpty();
  }

  @Test
  @DisplayName("이메일로 사용자 조회 성공")
  void testFindByEmail_Success() {
    String email = "email@gmail.com";
    User user = createTestUser("username", email);
    userRepository.save(user);

    boolean exists = userRepository.existsByEmail(email);

    assertThat(exists).isTrue();
  }
  @Test
  @DisplayName("존재하지 않는 이메일로 확인하면 false를 반환한다")
  void existsByEmail_NonExistingEmail_ReturnsFalse() {
    // given
    String nonExistingEmail = "nonexisting@example.com";

    // when
    boolean exists = userRepository.existsByEmail(nonExistingEmail);

    // then
    assertThat(exists).isFalse();
  }

  @Test
  @DisplayName("모든 사용자를 프로필과 상태 정보와 함께 조회할 수 있다")
  void findAllWithProfileAndStatus_ReturnsUsersWithProfileAndStatus() {
    // given
    User user1 = createTestUser("user1", "user1@example.com");
    User user2 = createTestUser("user2", "user2@example.com");

    userRepository.saveAll(List.of(user1, user2));

    // 영속성 컨텍스트 초기화 - 1차 캐시 비우기
    testEntityManager.flush();
    testEntityManager.clear();

    // when
    List<User> users = userRepository.findAllWithProfileAndStatus();

    // then
    assertThat(users).hasSize(2);
    assertThat(users).extracting("username").containsExactlyInAnyOrder("user1", "user2");

    // 프로필과 상태 정보가 함께 조회되었는지 확인 - 프록시 초기화 없이도 접근 가능한지 테스트
    User foundUser1 = users.stream().filter(u -> u.getUsername().equals("user1")).findFirst()
        .orElseThrow();
    User foundUser2 = users.stream().filter(u -> u.getUsername().equals("user2")).findFirst()
        .orElseThrow();

    // 프록시 초기화 여부 확인
    assertThat(Hibernate.isInitialized(foundUser1.getProfile())).isTrue();
    assertThat(Hibernate.isInitialized(foundUser1.getStatus())).isTrue();
    assertThat(Hibernate.isInitialized(foundUser2.getProfile())).isTrue();
    assertThat(Hibernate.isInitialized(foundUser2.getStatus())).isTrue();
  }

}
