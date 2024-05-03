package api.model;

public enum Type {
        CAT,
        DOG;

    public static Type fromString(String value) {
        for (Type name : Type.values()) {
            if (name.name().equalsIgnoreCase(value)) {
                return name;
            }
        }
        throw new IllegalArgumentException("Can't find a type " + value);
    }

    @Override
    public String toString() {
        return name();
    }
}
