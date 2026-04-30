package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channel")
public class ChannelController {

    private final ChannelService channelService;

    @PostMapping(path = "createPublic")
    public ResponseEntity<Channel> createPublic(@RequestBody PublicChannelCreateRequest request) {
        Channel createChannel = channelService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createChannel);
    }

    @PostMapping(path = "createPrivate")
    public ResponseEntity<Channel> createPrivate(@RequestBody PrivateChannelCreateRequest request) {
        Channel createChannel = channelService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createChannel);
    }

    @GetMapping(path = "findAll")
    public ResponseEntity<List<ChannelDto>> findAll(@RequestParam UUID userId) {
        List<ChannelDto> channelList = channelService.findAllByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(channelList);
    }

    @PatchMapping(path = "update/{channelId}")
    public ResponseEntity<Channel> update(
            @PathVariable UUID channelId,
            @RequestBody PublicChannelUpdateRequest request) {
        Channel updateChannel = channelService.update(channelId, request);

        return ResponseEntity.status(HttpStatus.OK).body(updateChannel);
    }

    @DeleteMapping(path = "delete/{channelId}")
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
