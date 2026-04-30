package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController implements UserApi {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<User> create(
            @RequestPart("userCreateRequest") UserCreateRequest request,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
                .flatMap(this::resolveProfileRequest);
        User createUser = userService.create(request, profileRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @GetMapping("findAll")
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> usersList = userService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @PatchMapping(path = "update/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<User> update(
            @PathVariable UUID userId,
            @RequestPart UserUpdateRequest request,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
                .flatMap(this::resolveProfileRequest);
        User updateUser = userService.update(userId, request, profileRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }

    @DeleteMapping(path = "delete/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        userService.delete(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(path = "userStatus/{userId}")
    public ResponseEntity<UserStatus> userStatus(
            @PathVariable UUID userId,
            @RequestBody UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusService.updateByUserId(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(userStatus);
    }

    private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profile) {
        if (profile.isEmpty()) {
            return Optional.empty();
        } else {
            try {
                BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                        profile.getOriginalFilename(),
                        profile.getContentType(),
                        profile.getBytes()
                );
                return Optional.of(request);
            } catch (IOException e) {
                throw new RuntimeException("파일을 읽을 수 없습니다.", e);
            }
        }
    }
}
