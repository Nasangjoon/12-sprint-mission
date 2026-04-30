package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    @PostMapping(path = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Message> create(
            @RequestPart MessageCreateRequest request,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
        List<BinaryContentCreateRequest> attachmentsList = Optional.ofNullable(attachments)
                .map(files -> files.stream()
                        .map(file -> {
                            try {
                                return new BinaryContentCreateRequest(
                                        file.getOriginalFilename(),
                                        file.getContentType(),
                                        file.getBytes()
                                );
                            } catch (IOException e) {
                                throw new RuntimeException("파일 읽기에 실패했습니다.",e);
                            }
                        })
                        .toList())
                .orElse(new ArrayList<>());
        Message createdMessage = messageService.create(request, attachmentsList);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

    @GetMapping(path = "findAll")
    public ResponseEntity<List<Message>> findAll(@RequestParam UUID channelId) {
        List<Message> messageList = messageService.findAllByChannelId(channelId);

        return ResponseEntity.status(HttpStatus.OK).body(messageList);
    }

    @PatchMapping(path = "update/{messageId}")
    public ResponseEntity<Message> update(
            @PathVariable UUID messageId,
            @RequestBody MessageUpdateRequest request) {
        Message updateMessage = messageService.update(messageId, request);

        return ResponseEntity.status(HttpStatus.OK).body(updateMessage);
    }

    @DeleteMapping(path = "delete/{messageId}")
    public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
        messageService.delete(messageId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
