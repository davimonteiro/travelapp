package br.uece.travelapp.adaptation;

import service.adaptation.effector.WorkflowEffector;
import service.auxiliary.ServiceDescription;

public class AdaptationEngine {

	private WorkflowEffector myEffector;

	public AdaptationEngine(Probe myProbe, WorkflowEffector workflowEffector) {
		myProbe.connect(this);
		this.myEffector = workflowEffector;
	}

	public void handleServiceFailure(ServiceDescription service, String opName) {
		this.myEffector.removeService(service);
	}

	public void handleServiceNotFound(String serviceType, String opName) {
		myEffector.refreshAllServices(serviceType, opName);
	}
}
