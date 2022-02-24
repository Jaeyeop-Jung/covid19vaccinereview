package com.teamproject.covid19vaccinereview.service;

import com.teamproject.covid19vaccinereview.domain.Board;
import com.teamproject.covid19vaccinereview.domain.VaccineType;
import com.teamproject.covid19vaccinereview.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    public Map<String, Object> createBoard(VaccineType vaccineType, int ordinalNumber){

        if(!boardRepository.existsByVaccineTypeAndOrdinalNumber(vaccineType, ordinalNumber)){
            boardRepository.save(Board.of(vaccineType, ordinalNumber));
        }

        HashMap<String, Object> response = new HashMap<>();
        response.put("vaccineType", vaccineType);
        response.put("ordinalNumber", ordinalNumber);

        return response;
    }

}
