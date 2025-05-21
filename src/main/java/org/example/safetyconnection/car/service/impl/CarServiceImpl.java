package org.example.safetyconnection.car.service.impl;

import org.example.safetyconnection.car.dto.request.CarRegisterRequestDTO;
import org.example.safetyconnection.car.dto.request.CarRequestDTO;
import org.example.safetyconnection.car.dto.response.CarResponseDTO;
import org.example.safetyconnection.car.entity.Car;
import org.example.safetyconnection.car.exception.CarIdAlreadyExistsException;
import org.example.safetyconnection.car.repository.CarRepository;
import org.example.safetyconnection.car.service.CarService;
import org.example.safetyconnection.entity.Member;
import org.example.safetyconnection.exception.UserNameNotFoundException;
import org.example.safetyconnection.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

	private final CarRepository carRepository;
	private final MemberRepository memberRepository;

	@Override
	public void registerCar(CarRegisterRequestDTO requestDTO) { //username, carID
		System.out.println(requestDTO.carId());

		if (carRepository.existsByCarId(requestDTO.carId())) {
			throw new CarIdAlreadyExistsException(requestDTO.carId());
		}

		Member member = memberRepository.findByUsername(requestDTO.username())
			.orElseThrow(() -> new UsernameNotFoundException(requestDTO.username()));
		Car car = new Car(requestDTO.carId(), member);

		carRepository.save(car);
	}

	@Override
	public CarResponseDTO getCarByUsername(CarRequestDTO requestDTO) {
		if (!memberRepository.existsByUsername(requestDTO.username())) {
			throw new UsernameNotFoundException(requestDTO.username());
		}
		Member member = memberRepository.findByUsername(requestDTO.username())
			.orElseThrow(() -> new UserNameNotFoundException(requestDTO.username()));

		Car car = carRepository.findByMember_Username(member.getName()).orElseThrow();
		CarResponseDTO responseDTO = new CarResponseDTO(car.getCarId(), requestDTO.username());

		return responseDTO;
	}
}
