package org.example.safetyconnection.car.repository;

import java.util.Optional;

import org.example.safetyconnection.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
	boolean existsByCarId(String carId);
	Optional<Car> findByMember_Username(String username); //username으로 car 찾기
}
