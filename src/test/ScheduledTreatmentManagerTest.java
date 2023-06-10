package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.CosmeticSaloon;
import entity.EducationLevel;
import entity.Role;
import entity.ScheduledTreatment;
import entity.Service;
import entity.State;
import entity.TreatmentType;
import entity.User;
import manage.CosmeticSaloonManager;
import manage.ManagerFactory;
import manage.ScheduledTreatmentManager;
import manage.ServiceManager;
import manage.TreatmentTypeManager;
import manage.UserManager;

class ScheduledTreatmentManagerTest {

	private ScheduledTreatmentManager scheduledTreatmentManager;

	@BeforeEach
	public void setUp() throws Exception {
		String sep = System.getProperty("file.separator");
		
		CosmeticSaloon.setCount(0);
		User.setCount(0);
		ScheduledTreatment.setCount(0);
		Service.setCount(0);
		TreatmentType.setCount(0);
		
		CosmeticSaloonManager cosmeticSaloonManager = new CosmeticSaloonManager(String.format("testdata%scosmetic_saloons.csv", sep));
		cosmeticSaloonManager.add("Saloon", LocalTime.parse("08:00"), LocalTime.parse("16:00"), String.format("testdata%susers.csv", sep), String.format("testdata%streatment_types.csv", sep), String.format("testdata%sservices.csv", sep), String.format("testdata%sscheduled_treatments.csv", sep), String.format("testdata%sprices.csv", sep));
		
		ManagerFactory managerFactory = cosmeticSaloonManager.findCosmeticSaloonById(1).getManagerFactory();
		UserManager userManager = managerFactory.getUserManager();
		TreatmentTypeManager treatmentTypeManager = managerFactory.getTreatmentTypeManager();
		ServiceManager serviceManager = managerFactory.getServiceManager();
		scheduledTreatmentManager = managerFactory.getScheduledTreatmentManager();
		
		
		
		treatmentTypeManager.add("Treatment type 1");
		treatmentTypeManager.add("Treatment type 2");
		treatmentTypeManager.add("Treatment type 3");
		serviceManager.add(1, "Service 1", 100, 15);
		serviceManager.add(2, "Service 2", 200, 30);
		serviceManager.add(3, "Service 3", 50, 55);
		
		userManager.add("name", "surname", "gender", "123", "Address", "username", "password");
		userManager.add("name", "surname", "gender", "123", "Address", "username2", "password", EducationLevel.BACHELORS, 3, 1200.0, Role.MANAGER, null);
		userManager.add("name", "surname", "gender", "123", "Address", "username3", "password", EducationLevel.BACHELORS, 2, 900.0, Role.RECEPTIONIST, null);
		userManager.add("name", "surname", "gender", "123", "Address", "username4", "password", EducationLevel.BACHELORS, 2, 1000.0, Role.BEAUTICIAN, new HashMap<Integer, TreatmentType>() {{put(1, treatmentTypeManager.findTreatmentTypeByID(1));put(2, treatmentTypeManager.findTreatmentTypeByID(2));}});

		scheduledTreatmentManager.add(1, 1, 4, LocalDate.now().atTime(LocalTime.of(11, 0)));
		scheduledTreatmentManager.add(1, 2, 4, LocalDate.now().atTime(LocalTime.of(13, 0)));
	}
	
	private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }
	
	@AfterEach
	public void tearDown() {
		deleteDirectory(new File("testdata"));
	}

	@Test
	void testFindScheduledTreatmentById() {
		assertNotNull(scheduledTreatmentManager.findScheduledTreatmentById(1));
		assertNull(scheduledTreatmentManager.findScheduledTreatmentById(4));
	}

	@Test
	void testAdd() {
		assertEquals(2, scheduledTreatmentManager.getScheduledTreatments().size());
	}

	@Test
	void testUpdate() throws Exception {
		scheduledTreatmentManager.update(1, 1, 1, 4, LocalDate.now().atTime(LocalTime.of(16, 0)), 150);
		assertTrue(LocalDate.now().atTime(LocalTime.of(16, 0)).isEqual(scheduledTreatmentManager.findScheduledTreatmentById(1).getDateTime()));
		assertEquals(150, scheduledTreatmentManager.findScheduledTreatmentById(1).getPrice());
	}

	@Test
	void testScheduleTreatment() throws Exception {
		scheduledTreatmentManager.scheduleTreatment(1, 1, null, LocalDate.now().atTime(LocalTime.of(8, 0)));
		assertEquals(3, scheduledTreatmentManager.getScheduledTreatments().size());
		assertThrows(Exception.class, () -> scheduledTreatmentManager.scheduleTreatment(1, 1, 4, LocalDate.now().atTime(LocalTime.of(11, 0))));
		assertThrows(Exception.class, () -> scheduledTreatmentManager.scheduleTreatment(1, 3, null, LocalDate.now().atTime(LocalTime.of(12, 0))));
	}

	@Test
	void testCancelTreatment() {
		scheduledTreatmentManager.cancelTreatment(1, 1);
		assertEquals(State.CANCELED_CLIENT, scheduledTreatmentManager.findScheduledTreatmentById(1).getState());
		scheduledTreatmentManager.cancelTreatment(2, 3);
		assertEquals(State.CANCELED_SALOON, scheduledTreatmentManager.findScheduledTreatmentById(2).getState());
	}

	@Test
	void testChangeScheduledTreatmentState() {
		scheduledTreatmentManager.changeScheduledTreatmentState(1, State.COMPLETED);
		assertEquals(State.COMPLETED, scheduledTreatmentManager.findScheduledTreatmentById(1).getState());
	}

	@Test
	void testGetTreatmentEarnings() {
		assertEquals(100, scheduledTreatmentManager.getTreatmentEarnings(scheduledTreatmentManager.findScheduledTreatmentById(1)));
		scheduledTreatmentManager.changeScheduledTreatmentState(1, State.CANCELED_CLIENT);
		assertEquals(10, scheduledTreatmentManager.getTreatmentEarnings(scheduledTreatmentManager.findScheduledTreatmentById(1)));
		scheduledTreatmentManager.changeScheduledTreatmentState(2, State.CANCELED_SALOON);
		assertEquals(0, scheduledTreatmentManager.getTreatmentEarnings(scheduledTreatmentManager.findScheduledTreatmentById(2)));
	}

	@Test
	void testGetEarnings() {
		assertEquals(300, scheduledTreatmentManager.getEarnings(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)));
	}

	@Test
	void testGetExpenses() {
		assertEquals(3780, scheduledTreatmentManager.getExpenses(LocalDate.now().minusDays(30), LocalDate.now()));
	}

	@Test
	void testGetBeauticianSchedule() {
		assertEquals(2, scheduledTreatmentManager.getBeauticianSchedule(4).size());
	}

	@Test
	void testGetClientTreatments() {
		assertEquals(2, scheduledTreatmentManager.getClientTreatments(1).size());
	}

}
