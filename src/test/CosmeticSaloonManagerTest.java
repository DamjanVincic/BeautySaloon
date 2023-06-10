package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.CosmeticSaloon;
import manage.CosmeticSaloonManager;

class CosmeticSaloonManagerTest {

	private CosmeticSaloonManager cosmeticSaloonManager;

	@BeforeEach
	public void setUp() throws Exception {
		String sep = System.getProperty("file.separator");
		
		CosmeticSaloon.setCount(0);
		
		cosmeticSaloonManager = new CosmeticSaloonManager(String.format("testdata%scosmetic_saloons.csv", sep));
		cosmeticSaloonManager.add("Saloon", LocalTime.parse("08:00"), LocalTime.parse("16:00"), String.format("testdata%susers.csv", sep), String.format("testdata%streatment_types.csv", sep), String.format("testdata%sservices.csv", sep), String.format("testdata%sscheduled_treatments.csv", sep), String.format("testdata%sprices.csv", sep));
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
	void testFindCosmeticSaloonById() {
		assertNotNull(cosmeticSaloonManager.findCosmeticSaloonById(1));
		assertNull(cosmeticSaloonManager.findCosmeticSaloonById(2));
	}

	@Test
	void testAdd() {
		String sep = System.getProperty("file.separator");
		cosmeticSaloonManager.add("Saloon", LocalTime.parse("08:00"), LocalTime.parse("16:00"), String.format("testdata%susers2.csv", sep), String.format("testdata%streatment_types2csv", sep), String.format("testdata%sservices2.csv", sep), String.format("testdata%sscheduled_treatments2.csv", sep), String.format("testdata%sprices2.csv", sep));
		assertNotNull(cosmeticSaloonManager.findCosmeticSaloonById(2));
	}

	@Test
	void testUpdate() throws Exception {
		cosmeticSaloonManager.update(1, "New Saloon", LocalTime.of(9, 0), LocalTime.of(17, 0));
		assertEquals("New Saloon", cosmeticSaloonManager.findCosmeticSaloonById(1).getName());
		assertEquals(LocalTime.of(9, 0), cosmeticSaloonManager.findCosmeticSaloonById(1).getStartTime());
		assertEquals(LocalTime.of(17, 0), cosmeticSaloonManager.findCosmeticSaloonById(1).getEndTime());
		assertThrows(Exception.class, () -> cosmeticSaloonManager.update(2, "Saloon", LocalTime.of(9, 0), LocalTime.of(17, 0)));
	}

	@Test
	void testRemove() throws Exception {
		cosmeticSaloonManager.remove(1);
		assertNull(cosmeticSaloonManager.findCosmeticSaloonById(1));
		assertThrows(Exception.class, () -> cosmeticSaloonManager.remove(1));
	}

}
