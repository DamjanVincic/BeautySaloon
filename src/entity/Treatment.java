package entity;

public class Treatment {
    private String type; // manikir pedikir

    public Treatment(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
