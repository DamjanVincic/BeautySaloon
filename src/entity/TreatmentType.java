package entity;

public class TreatmentType {
    private static int count = 0;
    private String type; // manikir pedikir
    private boolean deleted;
    private int id;

    public TreatmentType(String type, boolean deleted) {
        this(0, type, deleted);
        this.id = ++count;
    }

    public TreatmentType(int id, String type, boolean deleted) {
        this.type = type;
        this.deleted = deleted;
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

    public boolean isDeleted() {
        return this.deleted;
    }
    public void delete() {
        this.deleted = true;
    }

    public static void setCount(int count) {
        TreatmentType.count = count;
    }

    @Override
    public String toString() {
        return this.type;
    }

    public String toFileString() {
        // String beautucianString = this.getBeauticians().stream().map(Beautician::getUsername).collect(Collectors.joining(";"));
        return this.id + "," + this.type + "," + this.deleted; // + beautucianString;
    }
}
