package com.halter.herd.rest;

import static org.springframework.http.ResponseEntity.ok;

import com.halter.herd.dto.CowRequestDto;
import com.halter.herd.dto.CowResponseDto;
import com.halter.herd.service.HerdService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cows")
public class HerdController {

  private final HerdService herdService;

  public HerdController(HerdService herdService) {
    this.herdService = herdService;
  }

  @GetMapping
  public ResponseEntity<List<CowResponseDto>> getAllCows() {

    return ok(herdService.getAllCows());
  }

  @PostMapping
  public ResponseEntity<CowResponseDto> createCow(@RequestBody CowRequestDto cowRequest) {

    return ok(herdService.createCow(cowRequest));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CowResponseDto> updateCow(@PathVariable("id") String id,
      @RequestBody CowRequestDto cowRequest) {

      return ok(herdService.updateCow(id, cowRequest));
  }
}
