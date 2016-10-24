package br.uece.travelapp.services.bike;

import service.atomic.AtomicService;
import service.auxiliary.ServiceOperation;

public class BicycleRentalService extends AtomicService {
	
	public static final String OPERATION = "rentBike";
	
	public BicycleRentalService(String serviceName, String serviceEndpoint) {
		super(serviceName, serviceEndpoint);
	}

	@ServiceOperation
	public void rentBike(int travelerId) {
		System.out.println(this.getServiceDescription().getServiceName() + " is triggered!");
	}
	
}
