package org.personal.comerspleject.domain.point.repository;

import org.personal.comerspleject.domain.point.entitty.Point;
import org.personal.comerspleject.domain.users.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByUser(User user);
}
