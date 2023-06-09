package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.CosmeticSaloon;
import entity.TreatmentType;
import manage.CosmeticSaloonManager;
import manage.TreatmentTypeManager;

class TreatmentTypeManagerTest {

	private TreatmentTypeManager treatmentTypeManager;

	@BeforeEach
	public void setUp() throws Exception {
		String sep = System.getProperty("file.separator");
		
		CosmeticSaloon.setCount(0);
		TreatmentType.setCount(0);
		
		CosmeticSaloonManager cosmeticSaloonManager = new CosmeticSaloonManager(String.format("testdata%scosmetic_saloons.csv", sep));
		cosmeticSaloonManager.add("Saloon", LocalTime.parse("08:00"), LocalTime.parse("16:00"), String.format("testdata%susers.csv", sep), String.format("testdata%streatment_types.csv", sep), String.format("testdata%sservices.csv", sep), String.format("testdata%sscheduled_treatments.csv", sep), String.format("testdata%sprices.csv", sep));
		
		treatmentTypeManager = cosmeticSaloonManager.findCosmeticSaloonById(1).getManagerFactory().getTreatmentTypeManager();
		
		treatmentTypeManager.add("Treatment type 1");
		treatmentTypeManager.add("Treatment type 2");
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
	void testFindTreatmentTypeByID() {
		assertNotNull(treatmentTypeManager.findTreatmentTypeByID(1));
		assertNull(treatmentTypeManager.findTreatmentTypeByID(3));
	}

	@Test
	void testFindTreatmentTypeByType() {
		assertNotNull(treatmentTypeManager.findTreatmentTypeByType("Treatment type 1"));
		assertNull(treatmentTypeManager.findTreatmentTypeByType("test"));
	}

	@Test
	void testAdd() {
		assertEquals(2, treatmentTypeManager.getTreatmentTypes().size());
	}

	@Test
	void testUpdate() throws Exception {
		treatmentTypeManager.update(1, "Test");
		assertEquals("Test", treatmentTypeManager.findTreatmentTypeByID(1).getType());
		assertThrows(Exception.class, () -> treatmentTypeManager.update(3, "test"));
	}

	@Test
	void testRemove() throws Exception {
		treatmentTypeManager.remove(1);
		assertTrue(treatmentTypeManager.findTreatmentTypeByID(1).isDeleted());
		assertThrows(Exception.class, () -> treatmentTypeManager.remove(3));
	}

}
