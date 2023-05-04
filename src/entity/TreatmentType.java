package entity;

public class TreatmentType {
    private static int count = 0;
    private String type; // manikir pedikir
    private int id;

    public TreatmentType(String type) {
        this.type = type;
        this.id = ++count;
    }

    public TreatmentType(int id, String type) {
        this.type = type;
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public static void setCount(int count) {
        TreatmentType.count = count;
    }


    public String toFileString() {
        // String beautucianString = this.getBeauticians().stream().map(Beautician::getUsername).collect(Collectors.joining(";"));
        return this.id + "," + this.type; // + beautucianString;
    }
}
