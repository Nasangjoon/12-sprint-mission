package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.UserService;

public class JavaApplication {
    public static void main(String[] args) {
        System.out.println("---------- 사용자 점검 ----------------\n");
        UserService userService = new JCFUserService();

        User user1 = new User("test", "hsa123", "test123@gmail.com", "test", "http://test.com/profile.jpg","+8210-1234-5678");
        User user2 = new User("user2", "pass2", "user2@gmail.com", "닉네임2", "http://test.com/profile.jpg","+8210-1234-5678");
        User user3 = new User("user3", "pass3", "user3@gmail.com", "닉네임3",  "http://test.com/profile.jpg","+8210-1234-5678");
        userService.save(user1);
        userService.save(user2);
        userService.save(user3);

        System.out.println("------------ 단건 조회 ---------------\n");
        System.out.println(userService.findById(user1.getId()));

        System.out.println("------------ 다건 조회 ---------------\n");
        System.out.println(userService.findAll());

        User updateData = new User("updatedUser", "newPass!", "updated@gmail.com", "새닉네임", "http://test.com/profile.jpg", "+8210-1234-5678");
        userService.update(user1.getId(), updateData);

        System.out.println("------------- 유저 업데이트 -------------\n");
        System.out.println(userService.findAll());

        System.out.println("------------- 유저 삭제 -------------\n");
        userService.deleteById(user1.getId());
        System.out.println(userService.findAll());

        System.out.println("--------- 테스트 끝 -------------------");
    }
}