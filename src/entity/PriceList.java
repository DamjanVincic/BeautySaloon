package entity;

import java.util.HashMap;
import java.util.stream.Collectors;

public class PriceList {
    private static int count = 0;

    private HashMap<TreatmentType, Double> prices;
    private int id;

    public PriceList() {
        this.prices = new HashMap<>();
        this.id = ++count;
    }

    public PriceList(int id) {
        this.prices = new HashMap<>();
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<TreatmentType, Double> getPrices() {
        return this.prices;
    }

    public void setPrices(HashMap<TreatmentType, Double> prices) {
        this.prices = prices;
    }

    public Double getPrice(TreatmentType treatmentType) {
        return this.prices.get(treatmentType);
    }

    public void setPrice(TreatmentType treatmentType, Double price) {
        this.prices.put(treatmentType, price);
    }

    public void remove(TreatmentType treatmentType) {
        this.prices.remove(treatmentType);
    }


    public static void setCount(int count) {
        PriceList.count = count;
    }

    public String toFileString() {
        String pricesString = this.prices.entrySet().stream().map(e -> e.getKey().getId() + ";" + e.getValue()).collect(Collectors.joining("|"));
        return this.id + "," + pricesString;
    }
}
