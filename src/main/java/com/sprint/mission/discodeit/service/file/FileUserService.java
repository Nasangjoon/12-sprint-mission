package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private final String FILE_PATH = "users.dat";
    private final File file = new File(FILE_PATH);

    @SuppressWarnings("unchecked")
    private List<User> readFile() {
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void writeFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public User create(String username, String password, String email, String nickname, String profileImageUrl, String phoneNumber) {
        List<User> users = readFile();
        User newUser = new User(username, password, email, nickname, profileImageUrl, phoneNumber);
        users.add(newUser);
        writeFile(users);
        return newUser;
    }

    @Override
    public User find(UUID userId) {
        return readFile().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return readFile();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        List<User> users = readFile();
        for (User u : users) {
            if (u.getId().equals(userId)) {
                u.update(newUsername, newEmail, newPassword);
                writeFile(users);
                return u;
            }
        }
        return null;
    }

    @Override
    public void delete(UUID userId) {
        List<User> users = readFile();
        if (users.removeIf(u -> u.getId().equals(userId))) {
            writeFile(users);
        }
    }
}