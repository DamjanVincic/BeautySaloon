package entity;

public class Treatment {
    private static int count = 0;
    private String treatment; // manikir pedikir
    private int id;

    public Treatment(String treatment) {
        this.treatment = treatment;
        this.id = ++count;
    }

    public Treatment(int id, String treatment) {
        this.treatment = treatment;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTreatment() {
        return this.treatment;
    }
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public static void setCount(int count) {
        Treatment.count = count;
    }


    public String toFileString() {
        // String beautucianString = this.getBeauticians().stream().map(Beautician::getUsername).collect(Collectors.joining(";"));
        return this.id + "," + this.treatment; // + beautucianString;
    }
}
