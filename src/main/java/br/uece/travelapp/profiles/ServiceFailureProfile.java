package br.uece.travelapp.profiles;

import java.util.Random;

import service.atomic.ServiceProfile;
import service.atomic.ServiceProfileAttribute;

public class ServiceFailureProfile extends ServiceProfile {

	@ServiceProfileAttribute
	public double failureRate; // failure number in 100 invocation times

	public ServiceFailureProfile() {
	}

	public ServiceFailureProfile(double failureRate) {
		this.failureRate = failureRate;
	}

	@Override
	public boolean preInvokeOperation(String operationName, Object... args) {
		System.out.println("PreInvokeOperation");
		Random rand = new Random();
		if (rand.nextDouble() <= failureRate) {
			return false;
		}
		return true;
	}

	@Override
	public Object postInvokeOperation(String operationName, Object result, Object... args) {
		return result;
	}
}
