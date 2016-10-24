package br.uece.travelapp.configuration;

import br.uece.travelapp.adaptation.AdaptationEngine;
import br.uece.travelapp.adaptation.Probe;
import br.uece.travelapp.services.TravelPlannerService;
import service.adaptation.effector.WorkflowEffector;

public class ExampleConfiguration implements Configuration {

	private Probe myProbe;
	private WorkflowEffector myEffector;
	private TravelPlannerService assistanceService;

	public ExampleConfiguration(TravelPlannerService assistanceService) {
		this.assistanceService = assistanceService;
		myProbe = new Probe();
		myEffector = new WorkflowEffector(assistanceService);
		AdaptationEngine adaptationEngine = new AdaptationEngine(myProbe, myEffector);
		System.out.println(adaptationEngine);
	}

	@Override
	public void setConfiguration() {
		assistanceService.getWorkflowProbe().register(myProbe);
		myEffector.refreshAllServices();
	}

	@Override
	public void removeConfiguration() {
		assistanceService.getWorkflowProbe().unRegister(myProbe);
	}
}
