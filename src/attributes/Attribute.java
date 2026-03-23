package attributes;

public abstract class Attribute<T> {
    private final String name;

    public Attribute(String name) { this.name = name; }
    public String getName() { return name; }

    public abstract T getValue();
    public abstract void setValue(T value);
}
