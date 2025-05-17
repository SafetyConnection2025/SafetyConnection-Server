package org.example.safetyconnection.car.repository;

import org.example.safetyconnection.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
	boolean existsByCarId(String carId);
}
