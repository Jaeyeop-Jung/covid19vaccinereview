package com.teamproject.covid19vaccinereview.api;

import com.teamproject.covid19vaccinereview.domain.VaccineType;
import com.teamproject.covid19vaccinereview.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardApiController {
    
    private final BoardService boardService;
    
    @PostMapping("/board")
    public ResponseEntity<Map<String, Object>> postBoard(
            @RequestParam VaccineType vaccineType, 
            @RequestParam int ordinalNumber
    )
    {
        Map<String, Object> response = boardService.createBoard(vaccineType, ordinalNumber);
        return ResponseEntity.ok(response);
    }
}
