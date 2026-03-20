package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> data;

    public JCFUserService() {
        data = new ArrayList<>();
    }

    @Override
    public User save(User user) {
        data.add(user);
        return user;
    }

    @Override
    public User findById(UUID id) {
        for (User user : data) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {

        return new ArrayList<>(data);
    }

    @Override
    public User deleteById(UUID id) {
        User deleteUser = findById(id);
        if(deleteUser != null) {
            data.remove(deleteUser);
        }
        return deleteUser;
    }

    @Override
    public User update(UUID id, User user) {
        for (User u : data) {
            if (u.getId().equals(id)) {
                u.update(user.getUsername(), user.getEmail(), user.getPassword(), user.getNickname());
                return u;
            }
        }
        return null;
    }


}
