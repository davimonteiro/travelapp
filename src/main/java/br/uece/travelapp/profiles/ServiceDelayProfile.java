package br.uece.travelapp.profiles;

import java.util.Random;

import service.atomic.ServiceProfile;
import service.atomic.ServiceProfileAttribute;
import service.utility.Time;

public class ServiceDelayProfile extends ServiceProfile {

	@ServiceProfileAttribute
	public int minDelay;

	@ServiceProfileAttribute
	public int maxDelay;

	Random random = new Random();

	@Override
	public boolean preInvokeOperation(String operationName, Object... args) {
		try {
			Thread.sleep((random.nextInt(maxDelay - minDelay + 1) + minDelay) * Time.scale);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public Object postInvokeOperation(String operationName, Object result, Object... args) {
		return result;
	}
}
