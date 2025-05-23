package de.verdox.vserializer.blank;

import de.verdox.vserializer.generic.SerializationArray;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationElement;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A blank implementation of a serialization array.
 * Check out {@link BlankSerializationElement} for further information
 */
public class BlankSerializationArray extends BlankSerializationElement implements SerializationArray {
    private final List<SerializationElement> elements;
    private int boolCounter;
    private int charCounter;
    private int stringCounter;
    private int byteCounter;
    private int shortCounter;
    private int intCounter;
    private int longCounter;
    private int floatCounter;
    private int doubleCounter;
    private int arrayCounter;
    private int containerCounter;
    private boolean isConflicting;

    /**
     * A basic constructor with no standard elements
     *
     * @param serializationContext the context used for this element
     */
    public BlankSerializationArray(SerializationContext serializationContext) {
        super(serializationContext);
        this.elements = new ArrayList<>(128);
    }

    /**
     * A basic constructor with standard elements
     *
     * @param elements             the standard elements
     * @param serializationContext the context used for this element
     */
    public BlankSerializationArray(SerializationContext serializationContext, SerializationElement... elements) {
        this(serializationContext);
        for (SerializationElement element : elements) {
            add(element);
        }
    }

    /**
     * Returns a serialization array consisting of number values
     *
     * @param serializationContext the serialization context
     * @param values               the values
     * @return the array
     */
    public static BlankSerializationArray byNumbers(SerializationContext serializationContext, Collection<? extends Number> values) {
        BlankSerializationArray array = new BlankSerializationArray(serializationContext);
        for (Number value : values)
            array.add(serializationContext.create(value));
        return array;
    }

    /**
     * Returns a serialization array consisting of bool values
     *
     * @param serializationContext the serialization context
     * @param values               the values
     * @return the array
     */
    public static BlankSerializationArray byBooleans(SerializationContext serializationContext, Collection<? extends Boolean> values) {
        BlankSerializationArray array = new BlankSerializationArray(serializationContext);
        for (Boolean value : values)
            array.add(serializationContext.create(value));
        return array;
    }

    /**
     * Returns a serialization array consisting of string values
     *
     * @param serializationContext the serialization context
     * @param values               the values
     * @return the array
     */
    public static BlankSerializationArray byStrings(SerializationContext serializationContext, Collection<? extends String> values) {
        BlankSerializationArray array = new BlankSerializationArray(serializationContext);
        for (String value : values)
            array.add(serializationContext.create(value));
        return array;
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

        if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isBoolean())
            boolCounter++;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isCharacter())
            charCounter++;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isString())
            stringCounter++;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isByte())
            byteCounter++;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isShort())
            shortCounter++;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isInteger())
            intCounter++;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isLong())
            longCounter++;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isFloat())
            floatCounter++;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isDouble())
            doubleCounter++;
        else if (serializationElement.isArray())
            arrayCounter++;
        else if (serializationElement.isContainer())
            containerCounter++;

        searchForAnyConflicts();
    }

    @Override
    public SerializationElement remove(int index) {
        SerializationElement serializationElement = elements.remove(index);
        if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isBoolean())
            boolCounter--;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isCharacter())
            charCounter--;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isString())
            stringCounter--;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isByte())
            byteCounter--;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isShort())
            shortCounter--;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isInteger())
            intCounter--;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isLong())
            longCounter--;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isFloat())
            floatCounter--;
        else if (serializationElement.isPrimitive() && serializationElement.getAsPrimitive().isDouble())
            doubleCounter--;
        else if (serializationElement.isArray())
            arrayCounter--;
        else if (serializationElement.isContainer())
            containerCounter--;
        return serializationElement;
    }

    @NotNull
    @Override
    public Iterator<SerializationElement> iterator() {
        return elements.iterator();
    }

    @Override
    public boolean isBoolArray() {
        return boolCounter == 1 && !isConflicting;
    }

    @Override
    public boolean isCharArray() {
        return charCounter == 1 && !isConflicting;
    }

    @Override
    public boolean isStringArray() {
        return stringCounter == 1 && !isConflicting;
    }

    @Override
    public boolean isByteArray() {
        return byteCounter == 1 && !isConflicting;
    }

    @Override
    public boolean isShortArray() {
        return shortCounter == 1 && !isConflicting;
    }

    @Override
    public boolean isIntArray() {
        return intCounter == 1 && !isConflicting;
    }

    @Override
    public boolean isLongArray() {
        return longCounter == 1 && !isConflicting;
    }

    @Override
    public boolean isFloatArray() {
        return floatCounter == 1 && !isConflicting;
    }

    @Override
    public boolean isDoubleArray() {
        return doubleCounter == 1 && !isConflicting;
    }

    @Override
    public boolean isArrayOfArrays() {
        return arrayCounter == 1 && !isConflicting;
    }

    @Override
    public boolean isContainerArray() {
        return containerCounter == 1 && !isConflicting;
    }

    /**
     * @return true if any conflict was found
     */
    private void searchForAnyConflicts() {
        if (isConflicting)
            return;
        int trueCount = 0;

        if (isBoolArray()) trueCount++;
        if (isCharArray()) trueCount++;
        if (isStringArray()) trueCount++;
        if (isByteArray()) trueCount++;
        if (isShortArray()) trueCount++;
        if (isIntArray()) trueCount++;
        if (isFloatArray()) trueCount++;
        if (isDoubleArray()) trueCount++;
        if (isArrayOfArrays()) trueCount++;
        if (isContainerArray()) trueCount++;

        isConflicting = trueCount >= 2;
    }

    @Override
    public String toString() {
        return "{" + elements + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BlankSerializationArray that = (BlankSerializationArray) o;
        return Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(elements);
    }
}

