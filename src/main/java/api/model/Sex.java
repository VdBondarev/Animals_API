package api.model;

public enum Sex {
    MALE,
    FEMALE;

    public static Sex fromString(String value) {
        for (Sex name : Sex.values()) {
            if (name.name().equalsIgnoreCase(value)) {
                return name;
            }
        }
        throw new IllegalArgumentException("Can't find sex " + value);
    }

    @Override
    public String toString() {
        return name();
    }
}