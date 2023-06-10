package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.CosmeticSaloon;
import entity.Service;
import entity.TreatmentType;
import manage.CosmeticSaloonManager;
import manage.ManagerFactory;
import manage.ServiceManager;
import manage.TreatmentTypeManager;

class ServiceManagerTest {

	private TreatmentTypeManager treatmentTypeManager;
	private ServiceManager serviceManager;

	@BeforeEach
	public void setUp() throws Exception {
		String sep = System.getProperty("file.separator");
		
		CosmeticSaloon.setCount(0);
		Service.setCount(0);
		TreatmentType.setCount(0);
		
		CosmeticSaloonManager cosmeticSaloonManager = new CosmeticSaloonManager(String.format("testdata%scosmetic_saloons.csv", sep));
		cosmeticSaloonManager.add("Saloon", LocalTime.parse("08:00"), LocalTime.parse("16:00"), String.format("testdata%susers.csv", sep), String.format("testdata%streatment_types.csv", sep), String.format("testdata%sservices.csv", sep), String.format("testdata%sscheduled_treatments.csv", sep), String.format("testdata%sprices.csv", sep));
		
		ManagerFactory managerFactory = cosmeticSaloonManager.findCosmeticSaloonById(1).getManagerFactory();
		treatmentTypeManager = managerFactory.getTreatmentTypeManager();
		serviceManager = managerFactory.getServiceManager();
		
		
		
		treatmentTypeManager.add("Treatment type 1");
		treatmentTypeManager.add("Treatment type 2");
		serviceManager.add(1, "Service 1", 100, 15);
		serviceManager.add(2, "Service 2", 50, 75);
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
	void testFindServiceByID() {
		assertNotNull(serviceManager.findServiceByID(1));
		assertNull(serviceManager.findServiceByID(3));
	}

	@Test
	void testAdd() {
		assertEquals(2, serviceManager.getServices().size());
	}

	@Test
	void testUpdate() throws Exception {
		serviceManager.update(1, 2, "Service 1", 90, 15);
		assertEquals(treatmentTypeManager.findTreatmentTypeByID(2).getId(), serviceManager.findServiceByID(1).getTreatmentType().getId());
		assertEquals(90, serviceManager.findServiceByID(1).getPrice());
	}

	@Test
	void testRemove() throws Exception {
		serviceManager.remove(1);
		assertTrue(serviceManager.findServiceByID(1).isDeleted());
		assertThrows(Exception.class, () -> serviceManager.remove(3));
	}

}
