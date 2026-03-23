package attributes;



import java.util.List;


import java.util.Set;

public interface AttributeGroup {
    String getGroupName();


    void setAttribute(Object value);

    public List<String> getAttributeNames();
    void addAttribute(Attribute<?> attr);

    List<Attribute<?>> getAttributes();
    void setAttribute(String name, Object value);

    Object getAttributeValue(String name);
    Object getAttribute(Object obj);
}
