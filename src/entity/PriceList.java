package entity;

import java.util.HashMap;

public class PriceList {
    private HashMap<TreatmentType, Double> prices;

    public PriceList() {
        prices = new HashMap<>();
    }

    public HashMap<TreatmentType, Double> getPrices() {
        return this.prices;
    }

    public void setPrices(HashMap<TreatmentType, Double> prices) {
        this.prices = prices;
    }

    public void setPrice(TreatmentType treatmentType, Double price) {
        prices.put(treatmentType, price);
    }
}
