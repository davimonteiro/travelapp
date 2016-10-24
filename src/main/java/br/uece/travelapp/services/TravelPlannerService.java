package br.uece.travelapp.services;

import java.util.Scanner;

import service.auxiliary.CompositeServiceConfiguration;
import service.auxiliary.LocalOperation;
import service.composite.CompositeService;

@CompositeServiceConfiguration(SDCacheMode = true)
public class TravelPlannerService extends CompositeService {

	public TravelPlannerService(String serviceName, String serviceEndpoint, String workflow) {
		super(serviceName, serviceEndpoint, workflow);
	}
	
	@LocalOperation
	public int planTravel(int travelerId) {
		System.out.println("Pick (1) to search a flight, (2) to search an attraction.");
		
		Scanner in = new Scanner(System.in);
		do {
			String line = in.nextLine();
			Integer pick = Integer.parseInt(line);
			if (pick < 1 || pick > 2) {
				System.err.println("Wrong value:" + pick);
			} else {
				return pick;
			}
		} while (true);
		
	}

}
