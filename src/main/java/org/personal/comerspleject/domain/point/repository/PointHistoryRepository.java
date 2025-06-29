package org.personal.comerspleject.domain.point.repository;

import org.personal.comerspleject.domain.point.entitty.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
