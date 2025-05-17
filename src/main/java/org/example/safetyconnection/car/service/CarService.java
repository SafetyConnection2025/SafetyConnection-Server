package org.example.safetyconnection.car.service;

import org.example.safetyconnection.car.dto.request.CarRegisterRequestDTO;
import org.example.safetyconnection.car.dto.request.CarRequestDTO;
import org.example.safetyconnection.car.dto.response.CarResponseDTO;
import org.example.safetyconnection.car.repository.CarRepository;
import org.springframework.http.ResponseEntity;

public interface CarService {

	void registerCar(CarRegisterRequestDTO requestDTO);
	CarResponseDTO getCarByUsername(CarRequestDTO requestDTO);
}
