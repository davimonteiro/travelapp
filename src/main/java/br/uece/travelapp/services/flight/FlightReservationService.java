package br.uece.travelapp.services.flight;

import service.atomic.AtomicService;
import service.auxiliary.ServiceOperation;

public class FlightReservationService extends AtomicService {

	public static final String OPERATION = "searchFlight";
	
	public FlightReservationService(String serviceName, String serviceEndpoint) {
		super(serviceName, serviceEndpoint);
	}
	
	@ServiceOperation
	public void searchFlight(int travelerId) {
		System.out.println(this.getServiceDescription().getServiceName() + " is triggered!");
	}

}
