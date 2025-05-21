package org.example.safetyconnection.car.repository;

import java.util.Optional;

import org.example.safetyconnection.car.entity.Car;
import org.example.safetyconnection.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

public interface CarRepository extends JpaRepository<Car, Long> {
	boolean existsByCarId(String carId);
	Optional<Car> findByMember_Username(String username);
}
