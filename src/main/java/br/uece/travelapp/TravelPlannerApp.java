package br.uece.travelapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.uece.travelapp.configuration.Configuration;
import br.uece.travelapp.configuration.DefaultConfiguration;
import br.uece.travelapp.configuration.ExampleConfiguration;
import br.uece.travelapp.configuration.LotusConfiguration;
import br.uece.travelapp.profiles.ServiceDelayProfile;
import br.uece.travelapp.profiles.ServiceFailureProfile;
import br.uece.travelapp.qos.MinCostQoS;
import br.uece.travelapp.qos.PreferencesQoS;
import br.uece.travelapp.qos.ReliabilityQoS;
import br.uece.travelapp.services.TravelPlannerService;
import br.uece.travelapp.services.TravelPlannerServiceCostProbe;
import br.uece.travelapp.services.attraction.AttractionInformationService;
import br.uece.travelapp.services.bike.BicycleRentalService;
import br.uece.travelapp.services.car.CarRentalService;
import br.uece.travelapp.services.flight.FlightReservationService;
import br.uece.travelapp.services.hotel.HotelReservationService;
import profile.InputProfileValue;
import profile.InputProfileVariable;
import profile.ProfileExecutor;
import service.adaptation.effector.WorkflowEffector;
import service.atomic.AtomicService;
import service.composite.CompositeServiceClient;
import service.registry.ServiceRegistry;

public class TravelPlannerApp {

	private static final double HIGH_COST = 12.0;
	private static final double AVERAGE_COST = 4.0;
	private static final double LOW_COST = 2.0;

	private static final double LOW_FAILURE_RATE = 0.04;
	private static final double AVERAGE_FAILURE_RATE = 0.11;
	private static final double HIGH_FAILURE_RATE = 0.18;

	private static final String COST = "Cost";
	private static final String FAILURE_RATE = "FailureRate";

	private HashMap<String, Configuration> configurations = new LinkedHashMap<>();

	protected ServiceRegistry serviceRegistry;
	protected TravelPlannerService travelPlannerService;
	protected TravelPlannerServiceCostProbe monitor;
	protected WorkflowEffector workflowEffector;

	private AttractionInformationService attractionService1;
	private AttractionInformationService attractionService2;
	private AttractionInformationService attractionService3;

	private BicycleRentalService bikeService1;
	private BicycleRentalService bikeService2;
	private BicycleRentalService bikeService3;

	private CarRentalService carService1;
	private CarRentalService carService2;
	private CarRentalService carService3;

	private FlightReservationService flightService1;
	private FlightReservationService flightService2;
	private FlightReservationService flightService3;

	private HotelReservationService hotelService1;
	private HotelReservationService hotelService2;
	private HotelReservationService hotelService3;

	private boolean isStopped = false;
	private boolean isPaused = false;
	private int currentSteps;

	private Map<String, AtomicService> atomicServices = new HashMap<>();
	private List<Class<?>> serviceProfileClasses = new ArrayList<>();

	private LotusConfiguration lotusTASConfiguration;

	public TravelPlannerApp() throws Exception {
		initializeTAS();
		configurations.put("No Adaptation", new DefaultConfiguration());
		configurations.put("Simple Adaptation", new ExampleConfiguration(travelPlannerService));
		
		lotusTASConfiguration = new LotusConfiguration(travelPlannerService);
		configurations.put("Lotus@Runtime Adaptation", lotusTASConfiguration);
	}

	public void initializeTAS() throws Exception {
		
		serviceRegistry = new ServiceRegistry();
		serviceRegistry.startService();

		// Attraction services
		attractionService1 = new AttractionInformationService("AttractionService1", "service.attractionService1");
		attractionService1.getServiceDescription().getCustomProperties().put(COST, HIGH_COST);
		attractionService1.getServiceDescription().setOperationCost(AttractionInformationService.OPERATION, HIGH_COST);
		attractionService1.getServiceDescription().getCustomProperties().put(FAILURE_RATE, LOW_FAILURE_RATE);
		attractionService1.addServiceProfile(new ServiceFailureProfile(LOW_FAILURE_RATE));
		attractionService1.startService();
		attractionService1.register();

		attractionService2 = new AttractionInformationService("AttractionService2", "service.attractionService2");
		attractionService2.getServiceDescription().getCustomProperties().put(COST, AVERAGE_COST);
		attractionService2.getServiceDescription().setOperationCost(AttractionInformationService.OPERATION, AVERAGE_COST);
		attractionService2.getServiceDescription().getCustomProperties().put(FAILURE_RATE, AVERAGE_FAILURE_RATE);
		attractionService2.addServiceProfile(new ServiceFailureProfile(AVERAGE_FAILURE_RATE));
		attractionService2.startService();
		attractionService2.register();

		attractionService3 = new AttractionInformationService("AttractionService3", "service.attractionService3");
		attractionService3.getServiceDescription().getCustomProperties().put(COST, LOW_COST);
		attractionService3.getServiceDescription().setOperationCost(AttractionInformationService.OPERATION, LOW_COST);
		attractionService3.getServiceDescription().getCustomProperties().put(FAILURE_RATE, HIGH_FAILURE_RATE);
		attractionService3.addServiceProfile(new ServiceFailureProfile(HIGH_FAILURE_RATE));
		attractionService3.startService();
		attractionService3.register();

		// Bike services
		bikeService1 = new BicycleRentalService("BicycleRentalService1", "service.bicycleRentalService1");
		bikeService1.getServiceDescription().getCustomProperties().put(COST, HIGH_COST);
		bikeService1.getServiceDescription().setOperationCost(BicycleRentalService.OPERATION, HIGH_COST);
		bikeService1.getServiceDescription().getCustomProperties().put(FAILURE_RATE, LOW_FAILURE_RATE);
		bikeService1.addServiceProfile(new ServiceFailureProfile(LOW_FAILURE_RATE));
		bikeService1.startService();
		bikeService1.register();

		bikeService2 = new BicycleRentalService("BicycleRentalService2", "service.bicycleRentalService2");
		bikeService2.getServiceDescription().getCustomProperties().put(COST, AVERAGE_COST);
		bikeService2.getServiceDescription().setOperationCost(BicycleRentalService.OPERATION, AVERAGE_COST);
		bikeService2.getServiceDescription().getCustomProperties().put(FAILURE_RATE, AVERAGE_FAILURE_RATE);
		bikeService2.addServiceProfile(new ServiceFailureProfile(AVERAGE_FAILURE_RATE));
		bikeService2.startService();
		bikeService2.register();

		bikeService3 = new BicycleRentalService("BicycleRentalService3", "service.bicycleRentalService3");
		bikeService3.getServiceDescription().getCustomProperties().put(COST, LOW_COST);
		bikeService3.getServiceDescription().setOperationCost(BicycleRentalService.OPERATION, LOW_COST);
		bikeService3.getServiceDescription().getCustomProperties().put(FAILURE_RATE, HIGH_FAILURE_RATE);
		bikeService3.addServiceProfile(new ServiceFailureProfile(HIGH_FAILURE_RATE));
		bikeService3.startService();
		bikeService3.register();

		// Car services
		carService1 = new CarRentalService("CarRentalService1", "service.carRentalService1");
		carService1.getServiceDescription().getCustomProperties().put(COST, HIGH_COST);
		carService1.getServiceDescription().setOperationCost(CarRentalService.OPERATION, HIGH_COST);
		carService1.getServiceDescription().getCustomProperties().put(FAILURE_RATE, LOW_FAILURE_RATE);
		carService1.addServiceProfile(new ServiceFailureProfile(LOW_FAILURE_RATE));
		carService1.startService();
		carService1.register();

		carService2 = new CarRentalService("CarRentalService2", "service.carRentalService2");
		carService2.getServiceDescription().getCustomProperties().put(COST, AVERAGE_COST);
		carService2.getServiceDescription().setOperationCost(CarRentalService.OPERATION, AVERAGE_COST);
		carService2.getServiceDescription().getCustomProperties().put(FAILURE_RATE, AVERAGE_FAILURE_RATE);
		carService2.addServiceProfile(new ServiceFailureProfile(AVERAGE_FAILURE_RATE));
		carService2.startService();
		carService2.register();

		carService3 = new CarRentalService("CarRentalService3", "service.carRentalService3");
		carService3.getServiceDescription().getCustomProperties().put(COST, LOW_COST);
		carService3.getServiceDescription().setOperationCost(CarRentalService.OPERATION, LOW_COST);
		carService3.getServiceDescription().getCustomProperties().put(FAILURE_RATE, HIGH_FAILURE_RATE);
		carService3.addServiceProfile(new ServiceFailureProfile(HIGH_FAILURE_RATE));
		carService3.startService();
		carService3.register();

		// Flight services
		flightService1 = new FlightReservationService("FlightReservationService1", "service.flightReservationService1");
		flightService1.getServiceDescription().getCustomProperties().put(COST, HIGH_COST);
		flightService1.getServiceDescription().setOperationCost(FlightReservationService.OPERATION, HIGH_COST);
		flightService1.getServiceDescription().getCustomProperties().put(FAILURE_RATE, LOW_FAILURE_RATE);
		flightService1.addServiceProfile(new ServiceFailureProfile(LOW_FAILURE_RATE));
		flightService1.startService();
		flightService1.register();

		flightService2 = new FlightReservationService("FlightReservationService2", "service.flightReservationService2");
		flightService2.getServiceDescription().getCustomProperties().put(COST, AVERAGE_COST);
		flightService2.getServiceDescription().setOperationCost(FlightReservationService.OPERATION, AVERAGE_COST);
		flightService2.getServiceDescription().getCustomProperties().put(FAILURE_RATE, AVERAGE_FAILURE_RATE);
		flightService2.addServiceProfile(new ServiceFailureProfile(AVERAGE_FAILURE_RATE));
		flightService2.startService();
		flightService2.register();

		flightService3 = new FlightReservationService("FlightReservationService3", "service.flightReservationService3");
		flightService3.getServiceDescription().getCustomProperties().put(COST, LOW_COST);
		flightService3.getServiceDescription().setOperationCost(FlightReservationService.OPERATION, LOW_COST);
		flightService3.getServiceDescription().getCustomProperties().put(FAILURE_RATE, HIGH_FAILURE_RATE);
		flightService3.addServiceProfile(new ServiceFailureProfile(HIGH_FAILURE_RATE));
		flightService3.startService();
		flightService3.register();

		// Hotel services
		hotelService1 = new HotelReservationService("HotelReservationService1", "service.hotelReservationService1");
		hotelService1.getServiceDescription().getCustomProperties().put(COST, HIGH_COST);
		hotelService1.getServiceDescription().setOperationCost(HotelReservationService.OPERATION, HIGH_COST);
		hotelService1.getServiceDescription().getCustomProperties().put(FAILURE_RATE, LOW_FAILURE_RATE);
		hotelService1.addServiceProfile(new ServiceFailureProfile(LOW_FAILURE_RATE));
		hotelService1.startService();
		hotelService1.register();

		hotelService2 = new HotelReservationService("HotelReservationService2", "service.hotelReservationService2");
		hotelService2.getServiceDescription().getCustomProperties().put(COST, AVERAGE_COST);
		hotelService2.getServiceDescription().setOperationCost(HotelReservationService.OPERATION, AVERAGE_COST);
		hotelService2.getServiceDescription().getCustomProperties().put(FAILURE_RATE, AVERAGE_FAILURE_RATE);
		hotelService2.addServiceProfile(new ServiceFailureProfile(AVERAGE_FAILURE_RATE));
		hotelService2.startService();
		hotelService2.register();

		hotelService3 = new HotelReservationService("HotelReservationService3", "service.hotelReservationService3");
		hotelService3.getServiceDescription().getCustomProperties().put(COST, LOW_COST);
		hotelService3.getServiceDescription().setOperationCost(HotelReservationService.OPERATION, LOW_COST);
		hotelService3.getServiceDescription().getCustomProperties().put(FAILURE_RATE, HIGH_FAILURE_RATE);
		hotelService3.addServiceProfile(new ServiceFailureProfile(HIGH_FAILURE_RATE));
		hotelService3.startService();
		hotelService3.register();
		
		travelPlannerService = new TravelPlannerService("TravelPlannerService", "br.uece.travelapp", "resources/TravelPlannerWorkflow.txt");
		travelPlannerService.startService();
		travelPlannerService.register();
		monitor = new TravelPlannerServiceCostProbe();
		travelPlannerService.getCostProbe().register(monitor);
		travelPlannerService.getWorkflowProbe().register(monitor);
		travelPlannerService.addQosRequirement("ReliabilityQoS", new ReliabilityQoS());
		travelPlannerService.addQosRequirement("PreferencesQoS", new PreferencesQoS());
		travelPlannerService.addQosRequirement("CostQoS", new MinCostQoS());

		workflowEffector = new WorkflowEffector(travelPlannerService);

		this.addAllServices(attractionService1, attractionService2, attractionService3, bikeService1, bikeService2,
				bikeService3, carService1, carService2, carService3, flightService1, flightService2, flightService3,
				hotelService1, hotelService2, hotelService3);

		this.serviceProfileClasses.add(ServiceFailureProfile.class);
		this.serviceProfileClasses.add(ServiceDelayProfile.class);
	}

	public void stopServices() {
		serviceRegistry.stopService();
		attractionService1.stopService();
		travelPlannerService.stopService();
	}

	public void executeWorkflow(String workflowPath, String profilePath) throws Exception {

		CompositeServiceClient client = new CompositeServiceClient("br.uece.travelapp");
		travelPlannerService.setWorkflow(workflowPath);
		workflowEffector.refreshAllServices();

		ProfileExecutor.readFromXml(profilePath);

		lotusTASConfiguration.startLotusRuntime();

		if (ProfileExecutor.profile != null) {
			int maxSteps = (int) ProfileExecutor.profile.getMaxSteps();
			InputProfileVariable variable = ProfileExecutor.profile.getVariable("request");
			List<InputProfileValue> values = variable.getValues();

			int travelerId = (int) ProfileExecutor.profile.getVariable("travelerId").getValues().get(0).getData();
			int pick;
			// System.out.println("start executing workflow !!!");

			start();
			Random rand = new Random();
			for (currentSteps = 0; currentSteps < maxSteps; currentSteps++) {
				double probability = rand.nextDouble();
				double valueProbability = 0;

				for (int j = 0; j < values.size(); j++) {
					if ((values.get(j).getRatio() + valueProbability) > probability) {
						pick = (int) values.get(j).getData();
						client.invokeCompositeService(ProfileExecutor.profile.getQosRequirement(), travelerId, pick);
						break;
					} else {
						valueProbability = valueProbability + values.get(j).getRatio();
					}
				}

				if (isStopped) {
					break;
				}

			}
			stop();
			System.out.println("finish executing workflow !!!");
			lotusTASConfiguration.stopLotusRuntime();
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////

	public HashMap<String, Configuration> getConfigurations() {
		return configurations;
	}

	public synchronized void stop() {
		isStopped = true;
	}

	private synchronized void start() {
		isStopped = false;
	}

	public synchronized void pause() {
		isPaused = true;
	}

	public synchronized void go() {
		isPaused = false;
		this.notifyAll();
	}

	public boolean isStopped() {
		return isStopped;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public int getCurrentSteps() {
		return currentSteps;
	}

	public void addAllServices(AtomicService... services) {
		for (AtomicService service : services) {
			atomicServices.put(service.getServiceDescription().getServiceName(), service);
		}
	}

	public List<Class<?>> getServiceProfileClasses() {
		return this.serviceProfileClasses;
	}

	public AtomicService getService(String name) {
		return atomicServices.get(name);
	}

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public TravelPlannerService getAssistanceService() {
		return travelPlannerService;
	}

	public TravelPlannerServiceCostProbe getMonitor() {
		return monitor;
	}

}
