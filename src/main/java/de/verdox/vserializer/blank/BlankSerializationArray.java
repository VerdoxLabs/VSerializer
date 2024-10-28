package de.verdox.vserializer.blank;

import de.verdox.vserializer.generic.SerializationArray;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlankSerializationArray<C extends SerializationContext> extends BlankSerializationElement<C> implements SerializationArray {
    private final List<SerializationElement> elements;

    public BlankSerializationArray(C serializationContext) {
        super(serializationContext);
        this.elements = new ArrayList<>(128);
    }

    public BlankSerializationArray(C blankSerializationContext, int length) {
        super(blankSerializationContext);
        this.elements = new ArrayList<>(length);
    }

    public BlankSerializationArray(C blankSerializationContext, SerializationElement... elements) {
        this(blankSerializationContext, elements.length);
        for (SerializationElement element : elements) {
            add(element);
        }
    }

    @Override
    public int length() {
        return elements.size();
    }

    @Override
    public SerializationElement get(int index) {
        return elements.get(index);
    }

    @Override
    public void add(SerializationElement serializationElement) {
        elements.add(getContext().convert(serializationElement, false));
    }

    @Override
    public void set(int index, SerializationElement serializationElement) {
        elements.set(index, getContext().convert(serializationElement, false));
    }

    @Override
    public SerializationElement remove(int index) {
        return elements.remove(index);
    }

    @NotNull
    @Override
    public Iterator<SerializationElement> iterator() {
        return elements.iterator();
    }
}
