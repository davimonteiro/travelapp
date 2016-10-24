package br.uece.travelapp.adaptation;

import br.com.davimonteiro.tracelog.TraceLog;
import service.adaptation.probes.interfaces.WorkflowProbeInterface;
import service.auxiliary.ServiceDescription;

public class LotusProbe implements WorkflowProbeInterface {

	private static final String FAILURE = "failure";

	private static final String FAILED = "failed";

	private static final String END = "end";

	private static final String STOP = "stop";

	private static final String START = "start";

	private static final String PATH = "/Users/davimonteiro/Desktop/TravelPlanApp/traces.csv";

	private LotusAdaptationEngine lotusAdaptationEngine;

	public void connect(LotusAdaptationEngine lotusAdaptationEngine) {
		this.lotusAdaptationEngine = lotusAdaptationEngine;
	}

	@Override
	public void workflowStarted(String qosRequirement, Object[] params) {
		if (TraceLog.openTrace(PATH).isEmpty()) {
			TraceLog.openTrace(PATH).addAction(START);
		}
	}

	@Override
	public void workflowEnded(Object result, String qosRequirement, Object[] params) {
		if (!TraceLog.openTrace(PATH).isEmpty()) {
			TraceLog.openTrace(PATH).addAction(STOP).addAction(END).endTrace();
		}
	}

	@Override
	public void serviceOperationInvoked(ServiceDescription service, String opName, Object[] params) {
		TraceLog.openTrace(PATH).addAction(opName);
	}

	@Override
	public void serviceOperationReturned(ServiceDescription service, Object result, String opName, Object[] params) {
	}

	@Override
	public void serviceOperationTimeout(ServiceDescription service, String opName, Object[] params) {
		TraceLog.openTrace(PATH).addAction(FAILED + opName.substring(0, 1).toUpperCase() + opName.substring(1))
				.addAction(FAILURE).endTrace();
		lotusAdaptationEngine.handleServiceFailure(service, opName);
	}

	@Override
	public void serviceNotFound(String serviceType, String opName) {
		TraceLog.openTrace(PATH).addAction(FAILED + opName.substring(0, 1).toUpperCase() + opName.substring(1))
				.addAction(FAILURE).endTrace();
		lotusAdaptationEngine.handleServiceNotFound(serviceType, opName);
	}

}
