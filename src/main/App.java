package main;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import entity.CosmeticSaloon;
import entity.EducationLevel;
import entity.Role;
import manage.CosmeticSaloonManager;
import manage.ManagerFactory;
import manage.ScheduledTreatmentManager;
import manage.TreatmentTypeManager;
import manage.ServiceManager;
import manage.UserManager;

public class App {
    public static void main(String[] args) {
        String sep = System.getProperty("file.separator");
        
        CosmeticSaloonManager cosmeticSaloonManager = new CosmeticSaloonManager(String.format("data%scosmetic_saloons.csv", sep));
        cosmeticSaloonManager.loadData();

        cosmeticSaloonManager.add("Moj salon", LocalTime.parse("08:00"), LocalTime.parse("16:00"), String.format("data%susers.csv", sep), String.format("data%streatment_types.csv", sep), String.format("data%sservices.csv", sep), String.format("data%sscheduled_treatments.csv", sep), String.format("data%sprices.csv", sep));
        CosmeticSaloon cosmeticSaloon = cosmeticSaloonManager.findCosmeticSaloonById(1);
        ManagerFactory managerFactory = cosmeticSaloon.getManagerFactory();
        managerFactory.loadData();

        try {
            UserManager userManager = managerFactory.getUserManager();

            userManager.add("Nikola", "Nikolic", "Musko", "064123123", "Adresa 1", "niknik", "mnogojakasifra", EducationLevel.BACHELORS, 2, 200, 3000, Role.MANAGER);

            userManager.add("Pera", "Peric", "Musko", "064123124", "Adresa 2", "peraperic", "sifra", EducationLevel.HIGH_SCHOOL, 4, 100, 800, Role.RECEPTIONIST);

            userManager.add("Sima", "Simic", "Musko", "064123125", "Adresa 3", "simke", "sifra2", EducationLevel.DOCTORATE, 1, 0, 1200, Role.BEAUTICIAN);
            userManager.add("Zika", "Zikic", "Musko", "064123126", "Adresa 4", "zika", "sifra4", EducationLevel.HIGH_SCHOOL, 3, 200, 1100, Role.BEAUTICIAN);
            userManager.add("Jadranka", "Jovanovic", "Zensko", "064123127", "Adresa 6", "jad", "sifra6", EducationLevel.BACHELORS, 2, 300, 1100, Role.BEAUTICIAN);
        
            userManager.add("Milica", "Milic", "Zensko", "065656562", "Druga Adresa 2", "mica", "mica");
            userManager.add("Mika", "Mikic", "Musko", "065656563", "Druga Adresa 4", "mika", "pass");


            userManager.update(5, "Jovana", "Jovanovic", "Zensko", "064123127", "Adresa 6", "jad", "sifra6", EducationLevel.BACHELORS, 2, 300, 1100, null);
        
            userManager.getUsers().values().forEach(e -> System.out.println(e));
            System.out.println("---");

            userManager.remove(4);
            userManager.getUsers().values().forEach(e -> System.out.println(e));
            System.out.println("---");

            
            TreatmentTypeManager treatmentTypeManager = managerFactory.getTreatmentTypeManager();

            treatmentTypeManager.add("Masaza");
            treatmentTypeManager.add("Manikir");
            treatmentTypeManager.add("Pedikir");


            ServiceManager serviceManager = managerFactory.getServiceManager();

            serviceManager.add(1, "Relaks masaza", 2000, LocalTime.parse("00:45"));
            serviceManager.add(1, "Sportska masaza", 2500, LocalTime.parse("01:15"));
            serviceManager.add(2, "Francuski manikir", 1500, LocalTime.parse("00:50"));
            serviceManager.add(2, "Gel lak", 1600, LocalTime.parse("01:20"));
            serviceManager.add(2, "Spa manikir", 2000, LocalTime.parse("01:30"));
            serviceManager.add(1, "Spa pedikir", 1600, LocalTime.parse("00:45"));

            serviceManager.getServices().values().forEach(e -> System.out.println(e));
            System.out.println("---");

            serviceManager.update(3, 2, "Francuski manikir", 1500, LocalTime.parse("00:55"));
            serviceManager.update(6, 3, "Spa pedikir", 1600, LocalTime.parse("00:45"));

            serviceManager.getServices().values().forEach(e -> System.out.println(e));
            System.out.println("---");

            treatmentTypeManager.remove(3);

            serviceManager.getServices().values().forEach(e -> System.out.println(e));
            System.out.println("---");

            userManager.update(3, "Sima", "Simic", "Musko", "064123125", "Adresa 3", "simke", "sifra2", EducationLevel.DOCTORATE, 1, 0.0, 1200.0, new ArrayList<Integer>(Arrays.asList(1, 2)));
            userManager.update(5, "Jovana", "Jovanovic", "Zensko", "064123127", "Adresa 6", "jad", "sifra6", EducationLevel.BACHELORS, 2, 300, 1100, new ArrayList<Integer>(Arrays.asList(2)));
            
            ScheduledTreatmentManager scheduledTreatmentManager = managerFactory.getScheduledTreatmentManager();
            scheduledTreatmentManager.add(6, 1, 3, LocalDateTime.now());
            scheduledTreatmentManager.add(7, 4, 3, LocalDateTime.now());
            scheduledTreatmentManager.add(7, 5, 5, LocalDateTime.now());


            scheduledTreatmentManager.getScheduledTreatments().values().forEach(e -> System.out.println(e));
            System.out.println("---");

            scheduledTreatmentManager.update(2, 7, 3, 3, LocalDateTime.now(), 1600);
        
            scheduledTreatmentManager.getScheduledTreatments().values().forEach(e -> System.out.println(e));
            System.out.println("---");

            serviceManager.update(1, 1, "Relaks masaza", 1700, LocalTime.parse("00:45"));

            scheduledTreatmentManager.getScheduledTreatments().values().forEach(e -> System.out.println(e));
            System.out.println("---");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
