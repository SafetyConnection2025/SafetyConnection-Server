package org.example.safetyconnection.car.controller;

import org.example.safetyconnection.car.dto.request.CarRegisterRequestDTO;
import org.example.safetyconnection.car.dto.request.CarRequestDTO;
import org.example.safetyconnection.car.dto.response.CarResponseDTO;
import org.example.safetyconnection.car.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/car")
public class CarController {
	private final CarService carService;

	public CarController(CarService carService) {
		this.carService = carService;
	}

	@PostMapping("/register")
	public ResponseEntity<Void> registerCar(@RequestBody CarRegisterRequestDTO requestDTO) {
		carService.registerCar(requestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}


	@GetMapping("/{userId}")
	public ResponseEntity<CarResponseDTO> getCarId(@PathVariable("userId") String userId) {
		CarRequestDTO requestDTO = new CarRequestDTO(userId);
		CarResponseDTO carResponseDTO = carService.getCarByUsername(requestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(carResponseDTO);
	}

}
