package br.uece.travelapp.configuration;

import java.nio.file.Paths;

import br.com.davimonteiro.lotus_runtime.LotusRuntime;
import br.com.davimonteiro.lotus_runtime.config.ConfigurationUtil;
import br.uece.travelapp.adaptation.LotusAdaptationEngine;
import br.uece.travelapp.adaptation.LotusProbe;
import br.uece.travelapp.services.TravelPlannerService;
import service.adaptation.effector.WorkflowEffector;

public class LotusConfiguration implements Configuration {

	private static final String CONFIG_PATH = "/Users/davimonteiro/Desktop/TravelPlanApp/config.json";
	
	private LotusProbe lotusProbe;
	private WorkflowEffector effector;
	private TravelPlannerService plannerService;
	private LotusAdaptationEngine adaptationEngine;
	private LotusRuntime lotusRuntime;

	public LotusConfiguration(TravelPlannerService plannerService) throws Exception {
		this.plannerService = plannerService;
		lotusProbe = new LotusProbe();
		effector = new WorkflowEffector(plannerService);
		lotusRuntime = new LotusRuntime(ConfigurationUtil.load(Paths.get(CONFIG_PATH)), new LotusRuntimeHandler(plannerService));
		adaptationEngine = new LotusAdaptationEngine(lotusProbe, effector);
	}

	@Override
	public void setConfiguration() {
		plannerService.getWorkflowProbe().register(lotusProbe);
		effector.refreshAllServices();
	}

	@Override
	public void removeConfiguration() {
		plannerService.getWorkflowProbe().unRegister(lotusProbe);
		adaptationEngine.notify();
	}
	
	public void startLotusRuntime() throws Exception {
		System.err.println("Starting the Lotus@Runtime");
		this.lotusRuntime.start();
	}
	
	public void stopLotusRuntime() throws Exception {
		System.err.println("Stopping the Lotus@Runtime");
		this.lotusRuntime.stop();
	}
	
}
