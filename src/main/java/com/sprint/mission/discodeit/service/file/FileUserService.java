package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private final String FILE_PATH = "users.dat";

    File file = new File(FILE_PATH);

    @SuppressWarnings("unchecked")
    private List<User> readFile() {
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private void writeFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(users);
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User save(User user) {
        List<User> users = readFile();
        users.add(user);
        writeFile(users);
        return user;
    }


    @Override
    public User findById(UUID id) {
        return readFile().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {
        return readFile();
    }

    @Override
    public User deleteById(UUID id) {
        List<User> users = readFile();
        User target = users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
        if (target != null) {
            users.remove(target);
            writeFile(users);
        }
        return target;
    }

    @Override
    public User update(UUID id, User user) {
        List<User> users = readFile();
        for (User u : users) {
            if (u.getId().equals(id)) {
                u.update(user.getUsername(), user.getEmail(), user.getPassword(),
                        user.getNickname(), user.getProfileImageUrl(), user.getPhoneNumber());
                writeFile(users);
                return u;
            }
        }
        return null;
    }


}
