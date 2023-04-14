package entity;

public enum State {
    SCHEDULED("ZAKAZAN"),
    COMPLETED("IZVRSEN"),
    CANCELED_CLIENT("OTKAZAO KLIJENT"),
    CANCELED_SALOON("OTKAZAO SALON"),
    NOT_SHOWED_UP("NIJE SE POJAVIO");
    
    private final String text;

    State(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
