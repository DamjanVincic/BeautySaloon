package entity;

public enum State {
    SCHEDULED("Scheduled"),
    COMPLETED("Completed"),
    CANCELED_CLIENT("Canceled by client"),
    CANCELED_SALOON("Canceled by saloon"),
    NOT_SHOWED_UP("Didn't show up");
    
    private final String text;

    State(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
