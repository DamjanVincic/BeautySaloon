package main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import entity.CosmeticSaloon;
import entity.EducationLevel;
import entity.Employee;
import entity.Role;
import entity.ScheduledTreatment;
import entity.State;
import manage.CosmeticSaloonManager;
import manage.ManagerFactory;
import manage.ScheduledTreatmentManager;
import manage.ServiceManager;
import manage.TreatmentTypeManager;
import manage.UserManager;

public class KT3 {

	public static void main(String[] args) {
		String sep = System.getProperty("file.separator");
        
        CosmeticSaloonManager cosmeticSaloonManager = new CosmeticSaloonManager(String.format("data%scosmetic_saloons.csv", sep));
        cosmeticSaloonManager.loadData();

        cosmeticSaloonManager.add("Moj salon", LocalTime.parse("01:00"), LocalTime.parse("20:00"), String.format("data%susers.csv", sep), String.format("data%streatment_types.csv", sep), String.format("data%sservices.csv", sep), String.format("data%sscheduled_treatments.csv", sep), String.format("data%sprices.csv", sep));
        CosmeticSaloon cosmeticSaloon = cosmeticSaloonManager.findCosmeticSaloonById(1);
        ManagerFactory managerFactory = cosmeticSaloon.getManagerFactory();
        managerFactory.loadData();

        try {
            UserManager userManager = managerFactory.getUserManager();

            userManager.add("Nikola", "Nikolic", "Musko", "064123123", "Adresa 1", "nikolanikolic.menadzer", "mnogojakasifra", EducationLevel.BACHELORS, 2, 0, 3000, Role.MANAGER, null);

            userManager.add("Pera", "Peric", "Musko", "064123124", "Adresa 2", "peraperic.recepcioner", "sifra", EducationLevel.HIGH_SCHOOL, 4, 0, 800, Role.RECEPTIONIST, null);

            userManager.add("Sima", "Simic", "Musko", "064123125", "Adresa 3", "simasimic.kozmeticar", "sifra2", EducationLevel.DOCTORATE, 1, 0, 1200, Role.BEAUTICIAN, null);
            userManager.add("Zika", "Zikic", "Musko", "064123126", "Adresa 4", "zikazikic.kozmeticar", "sifra4", EducationLevel.HIGH_SCHOOL, 3, 0, 1100, Role.BEAUTICIAN, null);
            userManager.add("Jadranka", "Jovanovic", "Zensko", "064123127", "Adresa 6", "jad.kozmeticar", "sifra6", EducationLevel.BACHELORS, 2, 0, 1100, Role.BEAUTICIAN, null);
        
            userManager.add("Milica", "Milic", "Zensko", "065656562", "Druga Adresa 2", "milicamilic.klijent", "mica");
            userManager.add("Mika", "Mikic", "Musko", "065656563", "Druga Adresa 4", "mikamikic.klijent", "pass");
            
            TreatmentTypeManager treatmentTypeManager = managerFactory.getTreatmentTypeManager();

            treatmentTypeManager.add("Masaza");
            treatmentTypeManager.add("Manikir");
            treatmentTypeManager.add("Pedikir");
            
            userManager.update(3, "Sima", "Simic", "Musko", "064123125", "Adresa 3", "simke", "sifra2", EducationLevel.DOCTORATE, 1, 0.0, 1200.0, new ArrayList<Integer>(Arrays.asList(1, 2)));
            userManager.update(4, "Zika", "Zikic", "Musko", "064123126", "Adresa 4", "zikazikic.kozmeticar", "sifra4", EducationLevel.HIGH_SCHOOL, 3, 200, 1100, new ArrayList<Integer>(Arrays.asList(1, 2, 3)));
            userManager.update(5, "Jovana", "Jovanovic", "Zensko", "064123127", "Adresa 6", "jad", "sifra6", EducationLevel.BACHELORS, 2, 300, 1100, new ArrayList<Integer>(Arrays.asList(2)));
            
            ServiceManager serviceManager = managerFactory.getServiceManager();

            serviceManager.add(1, "Relaks masaza", 2000, 45);
            serviceManager.add(1, "Sportska masaza", 2500, 75);
            serviceManager.add(2, "Francuski manikir", 1500, 50);
            serviceManager.add(2, "Gel lak", 1600, 80);
            serviceManager.add(2, "Spa manikir", 2000, 90);
            serviceManager.add(3, "Spa pedikir", 1600, 45);

            ScheduledTreatmentManager scheduledTreatmentManager = managerFactory.getScheduledTreatmentManager();
            
            scheduledTreatmentManager.add(6, 3, 5, LocalDateTime.of(2023, 6, 10, 18, 0));
            scheduledTreatmentManager.add(6, 6, 4, LocalDateTime.of(2023, 6, 11, 9, 0));
            scheduledTreatmentManager.add(7, 2, 3, LocalDateTime.of(2023, 6, 12, 8, 0));
            scheduledTreatmentManager.add(7, 1, 4, LocalDateTime.of(2023, 6, 13, 19, 0));
            try {
            	scheduledTreatmentManager.add(7, 3, 5, LocalDateTime.of(2023, 6, 10, 18, 0));
            } catch (Exception ex) {
            	System.out.println(ex.getMessage());
            	System.out.println("------");
            }
            
            scheduledTreatmentManager.getBeauticianSchedule(4).forEach(item -> System.out.println(item));
            System.out.println("------");
            
            userManager.setLoyaltyCardThreshold(3500);
            
            scheduledTreatmentManager.changeScheduledTreatmentState(1, State.COMPLETED);
            printDetails(1, scheduledTreatmentManager, userManager);
            
            scheduledTreatmentManager.changeScheduledTreatmentState(2, State.CANCELED_CLIENT);
            printDetails(2, scheduledTreatmentManager,userManager);
            
            scheduledTreatmentManager.changeScheduledTreatmentState(3, State.CANCELED_SALOON);
            printDetails(3, scheduledTreatmentManager, userManager);
            scheduledTreatmentManager.changeScheduledTreatmentState(4, State.NOT_SHOWED_UP);
            printDetails(4, scheduledTreatmentManager, userManager);
            
            scheduledTreatmentManager.add(6, 4, 3, LocalDateTime.of(2023, 6, 14, 9, 0));
            scheduledTreatmentManager.add(7, 5, null, LocalDateTime.of(2023, 6, 14, 9, 0));
            scheduledTreatmentManager.changeScheduledTreatmentState(6, State.COMPLETED);
            printDetails(6, scheduledTreatmentManager, userManager);
            
            scheduledTreatmentManager.getClientTreatments(7).forEach(item -> System.out.println(item));
            System.out.println("------");
            
            System.out.println(String.format("Jun 2023 - Prihodi: %.2f, Rashodi: %.2f", scheduledTreatmentManager.getEarnings(LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 30)), scheduledTreatmentManager.getExpenses(LocalDate.of(2023, 6, 1), LocalDate.of(2023, 6, 30))));
            System.out.println("------");
            
            userManager.setBonusRequirement(1, 500);
            userManager.getUsers().values().stream().filter(item -> item instanceof Employee).map(item -> (Employee) item).forEach(item -> System.out.println(String.format("Uloga: %s, Ime: %s, Prezime: %s, Bonus: %.2f", item.getRole(), item.getName(), item.getSurname(), item.getBonus())));
            
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
	}
	
	public static void printDetails(int scheduledTreatmentID, ScheduledTreatmentManager scheduledTreatmentManager, UserManager userManager) {
		ScheduledTreatment scheduledTreatment = scheduledTreatmentManager.findScheduledTreatmentById(scheduledTreatmentID);
		
		System.out.println(scheduledTreatment);
        System.out.println(String.format("Stanje klijenta %s na kartici lojalnosti: %.2f", scheduledTreatment.getClient().getUsername(), userManager.getClientAmountSpent(scheduledTreatment.getClient().getId())));
        System.out.println(String.format("Bilans novca salona: %.2f", scheduledTreatmentManager.getTotalEarnings()));
        System.out.println("------");
	}
}
