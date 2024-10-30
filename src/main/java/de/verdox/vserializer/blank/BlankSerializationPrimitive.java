package de.verdox.vserializer.blank;

import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.NumberLimits;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationPrimitive;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class BlankSerializationPrimitive extends BlankSerializationElement implements SerializationPrimitive {
    private final Object value;

    public BlankSerializationPrimitive(SerializationContext serializationContext, boolean value) {
        super(serializationContext);
        this.value = value;
    }

    public BlankSerializationPrimitive(SerializationContext serializationContext, String value) {
        super(serializationContext);
        this.value = Objects.requireNonNull(value);
    }

    public BlankSerializationPrimitive(SerializationContext serializationContext, Number value) {
        super(serializationContext);
        this.value = Objects.requireNonNull(value);
    }

    public BlankSerializationPrimitive(SerializationContext serializationContext, char value) {
        super(serializationContext);
        this.value = String.valueOf(value);
    }

    public BlankSerializationPrimitive(SerializationContext serializationContext, byte value) {
        this(serializationContext, Byte.valueOf(value));
    }

    public BlankSerializationPrimitive(SerializationContext serializationContext, short value) {
        this(serializationContext, Short.valueOf(value));
    }

    public BlankSerializationPrimitive(SerializationContext serializationContext, int value) {
        this(serializationContext, Integer.valueOf(value));
    }

    public BlankSerializationPrimitive(SerializationContext serializationContext, long value) {
        this(serializationContext, Long.valueOf(value));
    }

    public BlankSerializationPrimitive(SerializationContext serializationContext, float value) {
        this(serializationContext, Float.valueOf(value));
    }

    public BlankSerializationPrimitive(SerializationContext serializationContext, double value) {
        this(serializationContext, Double.valueOf(value));
    }


    @Override
    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    @Override
    public boolean getAsBoolean() {
        if (isBoolean()) {
            return (Boolean) value;
        }
        // Check to see if the value as a String is "true" in any case.
        return Boolean.parseBoolean(getAsString());
    }

    @Override
    public boolean isNumber() {
        return value instanceof Number;
    }

    @Override
    public Number getAsNumber() {
        if (value instanceof Number) {
            return (Number) value;
        } else if (value instanceof String) {
            return new LazilyParsedNumber((String) value);
        }
        throw new UnsupportedOperationException("Primitive is neither a number nor a string");
    }

    @Override
    public boolean isString() {
        return value instanceof String;
    }

    @Override
    public String getAsString() {
        if (value instanceof String) {
            return (String) value;
        } else if (isNumber()) {
            return getAsNumber().toString();
        } else if (isBoolean()) {
            return ((Boolean) value).toString();
        }
        throw new AssertionError("Unexpected value type: " + value.getClass());
    }

    @Override
    public double getAsDouble() {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }


    public BigDecimal getAsBigDecimal() {
        return value instanceof BigDecimal
                ? (BigDecimal) value
                : NumberLimits.parseBigDecimal(getAsString());
    }


    public BigInteger getAsBigInteger() {
        return value instanceof BigInteger
                ? (BigInteger) value
                : isIntegral(this)
                ? BigInteger.valueOf(this.getAsNumber().longValue())
                : NumberLimits.parseBigInteger(this.getAsString());
    }

    @Override
    public float getAsFloat() {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    @Override
    public long getAsLong() {
        return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
    }

    @Override
    public short getAsShort() {
        return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
    }

    @Override
    public int getAsInt() {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    @Override
    public byte getAsByte() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    @Override
    public char getAsCharacter() {
        String s = getAsString();
        if (s.isEmpty()) {
            throw new UnsupportedOperationException("String value is empty");
        } else {
            return s.charAt(0);
        }
    }

    @Override
    public int hashCode() {
        if (value == null) {
            return 31;
        }
        // Using recommended hashing algorithm from Effective Java for longs and doubles
        if (isIntegral(this)) {
            long value = getAsNumber().longValue();
            return (int) (value ^ (value >>> 32));
        }
        if (value instanceof Number) {
            long value = Double.doubleToLongBits(getAsNumber().doubleValue());
            return (int) (value ^ (value >>> 32));
        }
        return value.hashCode();
    }

    /**
     * Returns whether the other object is equal to this. This method only considers the other object
     * to be equal if it is an instance of {@code JsonPrimitive} and has an equal value.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BlankSerializationPrimitive other = (BlankSerializationPrimitive) obj;
        if (value == null) {
            return other.value == null;
        }
        if (isIntegral(this) && isIntegral(other)) {
            return (this.value instanceof BigInteger || other.value instanceof BigInteger)
                    ? this.getAsBigInteger().equals(other.getAsBigInteger())
                    : this.getAsNumber().longValue() == other.getAsNumber().longValue();
        }
        if (value instanceof Number && other.value instanceof Number) {
            if (value instanceof BigDecimal && other.value instanceof BigDecimal) {
                // Uses compareTo to ignore scale of values, e.g. `0` and `0.00` should be considered equal
                return this.getAsBigDecimal().compareTo(other.getAsBigDecimal()) == 0;
            }

            double thisAsDouble = this.getAsDouble();
            double otherAsDouble = other.getAsDouble();
            // Don't use Double.compare(double, double) because that considers -0.0 and +0.0 not equal
            return (thisAsDouble == otherAsDouble)
                    || (Double.isNaN(thisAsDouble) && Double.isNaN(otherAsDouble));
        }
        return value.equals(other.value);
    }

    private static boolean isIntegral(BlankSerializationPrimitive primitive) {
        if (primitive.value instanceof Number) {
            Number number = (Number) primitive.value;
            return number instanceof BigInteger
                    || number instanceof Long
                    || number instanceof Integer
                    || number instanceof Short
                    || number instanceof Byte;
        }
        return false;
    }
}
