package br.uece.travelapp.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import service.adaptation.probes.interfaces.CostProbeInterface;
import service.adaptation.probes.interfaces.WorkflowProbeInterface;
import service.auxiliary.ServiceDescription;
import service.auxiliary.TimeOutError;

public class TravelPlannerServiceCostProbe implements WorkflowProbeInterface, CostProbeInterface {

	private static String resultFilePath = "results" + File.separator + "result.csv";
	private double totalCost = 0;
	private StringBuilder resultBuilder;
	public int workflowInvocationCount = 0;
	private Map<String, Long> delays = new HashMap<>();

	static {
		File file = new File(resultFilePath);
		if (file.exists() && !file.isDirectory()) {
			file.delete();
		}
	}

	public void reset() {
		File file = new File(resultFilePath);
		if (file.exists() && !file.isDirectory()) {
			file.delete();
		}
		workflowInvocationCount = 0;
		totalCost = 0;
	}

	@Override
	public void workflowStarted(String qosRequirement, Object[] params) {
		System.out.println("Probe: workflowStarted");
		resultBuilder = new StringBuilder();
		totalCost = 0;
		workflowInvocationCount++;
	}

	@Override
	public void workflowEnded(Object result, String qosRequirement, Object[] params) {
		System.out.println("Probe: workflowEnded");
		if (result instanceof TimeOutError) {
			System.out.println("WorkflowError!!!");
			resultBuilder.append(workflowInvocationCount + "," + "AssistanceService" + ",false," + totalCost + "\n");
		} else
			resultBuilder.append(workflowInvocationCount + "," + "AssistanceService" + ",true," + totalCost + "\n");
		try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(resultFilePath, true)))) {
			out.println(resultBuilder.toString());
		} catch (IOException e) {
		}
	}

	@Override
	public void serviceOperationTimeout(ServiceDescription service, String opName, Object[] params) {
		System.out.println("Probe: timeout");
		resultBuilder.append(workflowInvocationCount + "," + service.getServiceName() + ",false\n");
	}

	@Override
	public void serviceCost(String serviceName, String opName, double cost) {
		System.out.println("Serivice Cost: " + cost);
		String fullOperation = serviceName + "." + opName;
		Double begin = delays.get(fullOperation) * 1.0;
		Double end = System.currentTimeMillis() * 1.0;
		totalCost = totalCost + cost;
		resultBuilder.append(workflowInvocationCount + "," + serviceName + ",true," + cost + "," + begin + ","
				+ (end - begin) + "\n");
	}

	@Override
	public void serviceOperationInvoked(ServiceDescription service, String opName, Object[] params) {
		delays.put(service.getServiceName() + "." + opName, System.currentTimeMillis());
	}

	@Override
	public void serviceOperationReturned(ServiceDescription service, Object result, String opName, Object[] params) {
	}

	@Override
	public void serviceNotFound(String serviceType, String opName) {
	}
}
