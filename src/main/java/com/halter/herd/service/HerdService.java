package com.halter.herd.service;

import com.halter.herd.dto.CowRequestDto;
import com.halter.herd.dto.CowResponseDto;
import java.util.List;

public interface HerdService {

  List<CowResponseDto> getAllCows();

  CowResponseDto createCow(CowRequestDto cowRequest);

  CowResponseDto updateCow(String cowId, CowRequestDto cowRequest);
}
