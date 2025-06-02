package org.example.safetyconnection.car.entity;

import org.example.safetyconnection.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Car {
	@Id
	@Column(name = "car_id")
	private String carId;

	@OneToOne
	@JoinColumn(name="username")
	private Member member;

	public Car(String carId, Member member) {
		this.carId = carId;
		this.member = member;
	}
}
