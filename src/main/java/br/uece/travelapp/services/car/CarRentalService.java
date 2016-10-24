package br.uece.travelapp.services.car;

import service.atomic.AtomicService;
import service.auxiliary.ServiceOperation;

public class CarRentalService extends AtomicService {
	
	public static final String OPERATION = "rentCar";
	
	public CarRentalService(String serviceName, String serviceEndpoint) {
		super(serviceName, serviceEndpoint);
	}

	@ServiceOperation
	public void rentCar(int travelerId) {
		System.out.println(this.getServiceDescription().getServiceName() + " is triggered!");
	}
	
}
