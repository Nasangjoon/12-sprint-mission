package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Controller
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/api/binaryContent")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;


    @RequestMapping(path = "find")
    public ResponseEntity<BinaryContent> find(@RequestParam UUID BinaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(BinaryContentId);

        return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
    }

    @RequestMapping(path = "findAll")
    public ResponseEntity<List<BinaryContent>> findAll(@RequestParam List<UUID> BinaryContentIds) {
        List<BinaryContent> binaryContentList = binaryContentService.findAllByIdIn(BinaryContentIds);

        return ResponseEntity.status(HttpStatus.OK).body(binaryContentList);
    }
}
