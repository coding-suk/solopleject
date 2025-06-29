package org.personal.comerspleject.domain.point.repository;

import org.personal.comerspleject.domain.point.entitty.PointHistory;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findByUserOrderCreatedAtDesc(User user);


}
