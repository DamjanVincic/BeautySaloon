package entity;

public enum EducationLevel {
    HIGH_SCHOOL("Srednja skola", 0.05),
    BACHELORS("Fakultet", 0.1),
    MASTERS("Master", 0.15),
    DOCTORATE("Doktorat", 0.2);

    private String text;
    private double bonus;

    EducationLevel(String text, double bonus) {
        this.text = text;
        this.bonus = bonus;
    }

    public double getBonus() {
        return this.bonus;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
