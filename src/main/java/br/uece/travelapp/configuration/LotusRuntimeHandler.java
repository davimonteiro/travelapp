package br.uece.travelapp.configuration;

import br.com.davimonteiro.lotus_runtime.annotation.Subscriber;
import br.com.davimonteiro.lotus_runtime.annotation.Topic;
import br.com.davimonteiro.lotus_runtime.checker.Property;
import br.com.davimonteiro.lotus_runtime.notifier.ViolationHandler;
import service.composite.CompositeService;

@Subscriber
public class LotusRuntimeHandler implements ViolationHandler {
	
	private CompositeService service;
	
	public LotusRuntimeHandler(CompositeService service) {
		this.service = service;
	}
	
	@Topic
	public void handler(Property property) {
		
		switch (property.getId()) {
		case 1:
			service.useReliabilityQoSStrategy();
			break;
			
		case 2:
			service.useReliabilityQoSStrategy();
			break;
			
		case 3:
			service.useMinCostQoSStrategy();
			break;

		default:
			break;
		}
		
	}

}
