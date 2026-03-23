package attributes;

public class BasicAttribute<T> extends Attribute<T> {
    private T value;
    public BasicAttribute(String name, T initialValue) {
        super(name);
        this.value = initialValue;
    }
    @Override public T getValue() { return value; }
    @Override public void setValue(T value) { this.value = value; }
}