package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.Beautician;
import entity.Client;
import entity.CosmeticSaloon;
import entity.EducationLevel;
import entity.Manager;
import entity.Role;
import entity.ScheduledTreatment;
import entity.Service;
import entity.TreatmentType;
import entity.User;
import manage.CosmeticSaloonManager;
import manage.ManagerFactory;
import manage.ServiceManager;
import manage.TreatmentTypeManager;
import manage.UserManager;

class UserManagerTest {
	
	private UserManager userManager;
	private ManagerFactory managerFactory;

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
		
		managerFactory = cosmeticSaloonManager.findCosmeticSaloonById(1).getManagerFactory();
		userManager = managerFactory.getUserManager();
		TreatmentTypeManager treatmentTypeManager = managerFactory.getTreatmentTypeManager();
		ServiceManager serviceManager = managerFactory.getServiceManager();
		
		
		
		treatmentTypeManager.add("Treatment type 1");
		treatmentTypeManager.add("Treatment type 2");
		serviceManager.add(1, "Service 1", 100, 15);
		
		userManager.add("name", "surname", "gender", "123", "Address", "username", "password");
		userManager.add("name", "surname", "gender", "123", "Address", "username2", "password", EducationLevel.BACHELORS, 3, 1200.0, Role.MANAGER, null);
		userManager.add("name", "surname", "gender", "123", "Address", "username3", "password", EducationLevel.BACHELORS, 2, 900.0, Role.RECEPTIONIST, null);
		userManager.add("name", "surname", "gender", "123", "Address", "username4", "password", EducationLevel.BACHELORS, 2, 1000.0, Role.BEAUTICIAN, new HashMap<Integer, TreatmentType>() {{put(1, treatmentTypeManager.findTreatmentTypeByID(1));}});

		managerFactory.getScheduledTreatmentManager().add(1, 1, 4, LocalDate.now().atTime(LocalTime.of(11, 0)));
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
	void testAdd() {
		assertEquals(4, userManager.getUsers().size());
	}

	@Test
	void testFindUserById() {
		assertNotNull(userManager.findUserById(1));
		assertNull(userManager.findUserById(5));
	}

	@Test
	void testFindUserByUsername() {
		assertNotNull(userManager.findUserByUsername("username"));
		assertNull(userManager.findUserByUsername("username5"));
	}

	@Test
	void testUpdate() throws Exception {
		userManager.update(1, "newname", "surname", "gender", "123", "Address", "new_password");
		assertEquals("newname", userManager.findUserById(1).getName());
		assertEquals("new_password", userManager.findUserById(1).getPassword());
		
		userManager.update(2, "name", "surname", "gender", "123", "Address", "password", EducationLevel.BACHELORS, 3, 1201.0, null);
		assertEquals(1201.0, ((Manager)userManager.findUserById(2)).getBaseSalary());
		
		assertThrows(Exception.class, () -> userManager.update(5, "new_name", "surname", "gender", "123", "Address", "new_password"));
	}

	@Test
	void testRemove() throws Exception {
		userManager.remove(1);
		assertEquals(true, userManager.findUserById(1).isDeleted());
		assertThrows(Exception.class, () -> userManager.remove(5));
	}

	@Test
	void testSetLoyaltyCardThreshold() {
		userManager.setLoyaltyCardThreshold(90);
		assertTrue(((Client)userManager.findUserById(1)).hasLoyaltyCard());
	}

	@Test
	void testGetLoyaltyCardEligibleClients() {
		userManager.setLoyaltyCardThreshold(90);
		assertEquals(1, userManager.getLoyaltyCardEligibleClients().size());
	}

	@Test
	void testGetClientAmountSpent() {
		assertEquals(100, userManager.getClientAmountSpent(1));
	}

	@Test
	void testGetBeauticianAmountEarned() {
		assertEquals(100, userManager.getBeauticianAmountEarned(4));
	}

	@Test
	void testSetBonusRequirement() {
		userManager.setBonusRequirement(0, 10);
		assertEquals(10, ((Beautician)userManager.findUserById(4)).getBonus());
	}

	@Test
	void testLogin() throws Exception {
		userManager.login("username", "password");
		assertEquals(1, userManager.getCurrentUser().getId());
		assertThrows(Exception.class, () -> userManager.login("username", "test"));
	}

	@Test
	void testGetBeauticiansTrainedForTreatmentType() {
		assertEquals(1, userManager.getBeauticiansTrainedForTreatmentType(managerFactory.getTreatmentTypeManager().findTreatmentTypeByID(1)).size());
	}

	@Test
	void testGetAvailableTimesForBeautician() {
		assertEquals(7, userManager.getAvailableTimesForBeautician((Beautician)userManager.findUserById(4), LocalDate.now(), 10).size());
	}

}
