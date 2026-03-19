package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.UserService;

public class JavaApplication {
    public static void main(String[] args) {
        System.out.println("---------- 사용자 점검 ----------------");
        UserService userService = new JCFUserService();

        User user1 = new User("test", "hsa123", "test123@gmail.com", "test");
        User user2 = new User("user2", "pass2", "user2@gmail.com", "닉네임2");
        User user3 = new User("user3", "pass3", "user3@gmail.com", "닉네임3");
        userService.save(user1);
        userService.save(user2);
        userService.save(user3);

        System.out.println(userService.findById(user1.getId()));
        System.out.println(userService.findAll());

        User updateData = new User("updatedUser", "newPass!", "updated@gmail.com", "새닉네임");
        userService.update(user1.getId(), updateData);
        System.out.println(userService.findAll());

        userService.deleteById(user1.getId());
        System.out.println(userService.findAll());
        System.out.println("--------- 테스트 끝 -------------------");
    }
}
