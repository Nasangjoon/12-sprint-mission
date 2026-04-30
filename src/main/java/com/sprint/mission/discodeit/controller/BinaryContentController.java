package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContent")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;


    @GetMapping(path = "find/{binaryContentId}")
    public ResponseEntity<BinaryContent> find(@PathVariable UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);

        return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
    }

    @GetMapping(path = "findAll")
    public ResponseEntity<List<BinaryContent>> findAll(@RequestParam List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContentList = binaryContentService.findAllByIdIn(binaryContentIds);

        return ResponseEntity.status(HttpStatus.OK).body(binaryContentList);
    }
}
