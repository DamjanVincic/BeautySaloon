package entity;

public enum EducationLevel {
    HIGH_SCHOOL("High School", 0.05),
    BACHELORS("Bachelor's Degree", 0.1),
    MASTERS("Masters", 0.15),
    DOCTORATE("Doctorate", 0.2);

    private String text;
    private double bonus;

    EducationLevel(String text, double bonus) {
        this.text = text;
        this.bonus = bonus;
    }

    public double getBonus() {
        return this.bonus;
    }

    public String getText() {
        return this.text;
    }
}
