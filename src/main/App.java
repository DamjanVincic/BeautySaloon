package main;

import entity.CosmeticSaloon;
import manage.CosmeticSaloonManager;
import manage.ManagerFactory;
import view.MainFrame;

public class App {
    public static void main(String[] args) {
        String sep = System.getProperty("file.separator");
        
        CosmeticSaloonManager cosmeticSaloonManager = new CosmeticSaloonManager(String.format("data%scosmetic_saloons.csv", sep));
        cosmeticSaloonManager.loadData();

//        cosmeticSaloonManager.add("Moj salon", LocalTime.parse("08:00"), LocalTime.parse("16:00"), String.format("data%susers.csv", sep), String.format("data%streatment_types.csv", sep), String.format("data%sservices.csv", sep), String.format("data%sscheduled_treatments.csv", sep), String.format("data%sprices.csv", sep));
        CosmeticSaloon cosmeticSaloon = cosmeticSaloonManager.findCosmeticSaloonById(1);
        ManagerFactory managerFactory = cosmeticSaloon.getManagerFactory();
        managerFactory.loadData();

        MainFrame mainFrame = new MainFrame(managerFactory);
        mainFrame.setVisible(true);
    }
}
