package in.dk.SpringBazaar.Entity;

public enum Role {
    ADMIN,
    USER;

    public boolean contains(String roleName) {
        return this.name().equalsIgnoreCase(roleName);
    }
}
