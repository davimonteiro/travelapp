package br.uece.travelapp.adaptation;

import service.adaptation.probes.interfaces.WorkflowProbeInterface;
import service.auxiliary.ServiceDescription;

public class Probe implements WorkflowProbeInterface {

	private AdaptationEngine myAdaptationEngine;
		
	public void connect(AdaptationEngine myAdaptationEngine) {
	    this.myAdaptationEngine = myAdaptationEngine;
	}

	@Override
	public void workflowStarted(String qosRequirement, Object[] params) {
	    System.out.println("Workflow Started monitoring");
	}

	@Override
	public void workflowEnded(Object result, String qosRequirement, Object[] params) {
	    System.out.println("Workflow Ended");
	}

	@Override
	public void serviceOperationInvoked(ServiceDescription service, String opName, Object[] params) {

	}

	@Override
	public void serviceOperationReturned(ServiceDescription service, Object result, String opName, Object[] params) {

	}

	@Override
	public void serviceOperationTimeout(ServiceDescription service, String opName, Object[] params) {
	    System.err.println("Service Failed: " + service.getServiceName());
	    // Remove service from cache
	    myAdaptationEngine.handleServiceFailure(service, opName);
	}
	
	@Override
	public void serviceNotFound(String serviceType, String opName){
	    System.err.println(serviceType + opName + "Not found");
	    myAdaptationEngine.handleServiceNotFound(serviceType, opName);
	}
	
}
