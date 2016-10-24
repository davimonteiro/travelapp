package br.uece.travelapp.services.hotel;

import java.util.Random;

import service.atomic.AtomicService;
import service.auxiliary.ServiceOperation;

public class HotelReservationService extends AtomicService {
	
	public static final String OPERATION = "searchHotel";
	
	public static final int RENT_CAR = 1;
	public static final int RENT_BIKE = 2;
	
	public HotelReservationService(String serviceName, String serviceEndpoint) {
		super(serviceName, serviceEndpoint);
	}

	@ServiceOperation
	public int searchHotel(int travelerId) {
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
