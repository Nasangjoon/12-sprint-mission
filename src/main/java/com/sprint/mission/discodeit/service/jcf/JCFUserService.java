package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> data;

    public JCFUserService() {
        this.data = new ArrayList<>();
    }

    @Override
    public User create(String username, String password, String email, String nickname, String profileImageUrl, String phoneNumber) {
        User newUser = new User(username, password, email, nickname, profileImageUrl, phoneNumber);
        data.add(newUser);
        return newUser;
    }

    @Override
    public User find(UUID userId) {
        return data.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = find(userId);
        if (user != null) {
            user.update(newUsername, newEmail, newPassword);
            return user;
        }
        return null;
    }

    @Override
    public void delete(UUID userId) {
        data.removeIf(user -> user.getId().equals(userId));
    }
}