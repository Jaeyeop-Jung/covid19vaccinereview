package com.teamproject.covid19vaccinereview.repository;

import com.teamproject.covid19vaccinereview.domain.Board;
import com.teamproject.covid19vaccinereview.domain.User;
import com.teamproject.covid19vaccinereview.domain.VaccineType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByVaccineTypeAndOrdinalNumber(VaccineType vaccineType, int ordinalNumber);

}
