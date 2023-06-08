package entity;

public enum Role {
    CLIENT("Client"),
    MANAGER("Manager"),
    BEAUTICIAN("Beautician"),
    RECEPTIONIST("Receptionist");
	
	private final String text;
	
	Role(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
}
