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
        
//        CosmeticSaloonManager cosmeticSaloonManager = new CosmeticSaloonManager(String.format("data%scosmetic_saloons.csv", sep));
//        cosmeticSaloonManager.loadData();
//
//        cosmeticSaloonManager.add("Moj salon", LocalTime.parse("08:00"), LocalTime.parse("16:00"), String.format("data%susers.csv", sep), String.format("data%streatment_types.csv", sep), String.format("data%sservices.csv", sep), String.format("data%sscheduled_treatments.csv", sep), String.format("data%sprices.csv", sep));
//        CosmeticSaloon cosmeticSaloon = cosmeticSaloonManager.findCosmeticSaloonById(1);
//        ManagerFactory managerFactory = cosmeticSaloon.getManagerFactory();
//        managerFactory.loadData();

        try {
        	
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
