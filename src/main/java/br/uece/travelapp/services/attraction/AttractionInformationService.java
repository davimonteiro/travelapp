package br.uece.travelapp.services.attraction;

import java.util.Random;

import service.atomic.AtomicService;
import service.auxiliary.ServiceOperation;

public class AttractionInformationService extends AtomicService {
	
	public static final String OPERATION = "searchAttraction";
	
	public static final int RENT_CAR = 1;
	public static final int RENT_BIKE = 2;
	
	public AttractionInformationService(String serviceName, String serviceEndpoint) {
		super(serviceName, serviceEndpoint);
	}
	
	@ServiceOperation
	public int searchAttraction(int travelerId) {
		System.out.println(this.getServiceDescription().getServiceName() + " is triggered!");
		int result = 0;
		
		Random random = new Random();
	    if (random.nextInt(10) < 2) {
	    	result = RENT_CAR;
	    } else {
	    	result = RENT_BIKE;
	    }
		
	    return result;
	}
	
}
