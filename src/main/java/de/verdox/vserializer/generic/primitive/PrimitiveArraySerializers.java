package de.verdox.vserializer.generic.primitive;

import de.verdox.vserializer.exception.SerializationException;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationElement;
import de.verdox.vserializer.generic.Serializer;

import java.util.function.Function;

public interface PrimitiveArraySerializers {
    abstract class PrimitiveArraySerializer<PRIMITIVE, WRAPPER> implements Serializer<PRIMITIVE> {

        protected final Serializer<WRAPPER[]> wrapperSerializer;
        protected final Function<PRIMITIVE, WRAPPER[]> wrapFunction;
        protected final Function<WRAPPER[], PRIMITIVE> unwrapFunction;
        protected final String id;
        protected final Class<? extends PRIMITIVE> type;

        protected PrimitiveArraySerializer(
                Serializer<WRAPPER[]> wrapperSerializer,
                Function<PRIMITIVE, WRAPPER[]> wrapFunction,
                Function<WRAPPER[], PRIMITIVE> unwrapFunction,
                String id,
                Class<? extends PRIMITIVE> type) {

            this.wrapperSerializer = wrapperSerializer;
            this.wrapFunction = wrapFunction;
            this.unwrapFunction = unwrapFunction;
            this.id = id;
            this.type = type;
        }

        @Override
        public SerializationElement serialize(SerializationContext ctx, PRIMITIVE primitiveArray) throws SerializationException {
            if (primitiveArray == null) return ctx.createNull();
            WRAPPER[] boxed = wrapFunction.apply(primitiveArray);
            return wrapperSerializer.serialize(ctx, boxed);
        }

        @Override
        public PRIMITIVE deserialize(SerializationElement element) throws SerializationException {
            if (element == null || element.isNull()) return unwrapFunction.apply(null);
            WRAPPER[] boxed = wrapperSerializer.deserialize(element);
            return unwrapFunction.apply(boxed);
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public Class<? extends PRIMITIVE> getType() {
            return type;
        }
    }

    class BoolArray extends PrimitiveArraySerializer<boolean[], Boolean> {
        public BoolArray() {
            super(
                    Serializer.Array.create(Primitive.BOOLEAN, Boolean[]::new),
                    (boolean[] arr) -> {
                        if (arr == null) return null;
                        Boolean[] boxed = new Boolean[arr.length];
                        for (int i = 0; i < arr.length; i++) boxed[i] = arr[i];
                        return boxed;
                    },
                    (Boolean[] boxed) -> {
                        if (boxed == null) return new boolean[0];
                        boolean[] unboxed = new boolean[boxed.length];
                        for (int i = 0; i < boxed.length; i++) unboxed[i] = boxed[i];
                        return unboxed;
                    },
                    "boolean_array",
                    boolean[].class
            );
        }
    }

    public static final class ByteArray extends PrimitiveArraySerializer<byte[], Byte> {
        public ByteArray() {
            super(
                    Serializer.Array.create(Primitive.BYTE, Byte[]::new),
                    arr -> {
                        if (arr == null) return null;
                        Byte[] boxed = new Byte[arr.length];
                        for (int i = 0; i < arr.length; i++) boxed[i] = arr[i];
                        return boxed;
                    },
                    boxed -> {
                        if (boxed == null) return new byte[0];
                        byte[] unboxed = new byte[boxed.length];
                        for (int i = 0; i < boxed.length; i++) unboxed[i] = boxed[i];
                        return unboxed;
                    },
                    "byte_array",
                    byte[].class
            );
        }
    }

    public static final class ShortArray extends PrimitiveArraySerializer<short[], Short> {
        public ShortArray() {
            super(
                    Serializer.Array.create(Primitive.SHORT, Short[]::new),
                    arr -> {
                        if (arr == null) return null;
                        Short[] boxed = new Short[arr.length];
                        for (int i = 0; i < arr.length; i++) boxed[i] = arr[i];
                        return boxed;
                    },
                    boxed -> {
                        if (boxed == null) return new short[0];
                        short[] unboxed = new short[boxed.length];
                        for (int i = 0; i < boxed.length; i++) unboxed[i] = boxed[i];
                        return unboxed;
                    },
                    "short_array",
                    short[].class
            );
        }
    }

    public static final class IntArray extends PrimitiveArraySerializer<int[], Integer> {
        public IntArray() {
            super(
                    Serializer.Array.create(Primitive.INTEGER, Integer[]::new),
                    arr -> {
                        if (arr == null) return null;
                        Integer[] boxed = new Integer[arr.length];
                        for (int i = 0; i < arr.length; i++) boxed[i] = arr[i];
                        return boxed;
                    },
                    boxed -> {
                        if (boxed == null) return new int[0];
                        int[] unboxed = new int[boxed.length];
                        for (int i = 0; i < boxed.length; i++) unboxed[i] = boxed[i];
                        return unboxed;
                    },
                    "int_array",
                    int[].class
            );
        }
    }

    public static final class LongArray extends PrimitiveArraySerializer<long[], Long> {
        public LongArray() {
            super(
                    Serializer.Array.create(Primitive.LONG, Long[]::new),
                    arr -> {
                        if (arr == null) return null;
                        Long[] boxed = new Long[arr.length];
                        for (int i = 0; i < arr.length; i++) boxed[i] = arr[i];
                        return boxed;
                    },
                    boxed -> {
                        if (boxed == null) return new long[0];
                        long[] unboxed = new long[boxed.length];
                        for (int i = 0; i < boxed.length; i++) unboxed[i] = boxed[i];
                        return unboxed;
                    },
                    "long_array",
                    long[].class
            );
        }
    }

    public static final class FloatArray extends PrimitiveArraySerializer<float[], Float> {
        public FloatArray() {
            super(
                    Serializer.Array.create(Primitive.FLOAT, Float[]::new),
                    arr -> {
                        if (arr == null) return null;
                        Float[] boxed = new Float[arr.length];
                        for (int i = 0; i < arr.length; i++) boxed[i] = arr[i];
                        return boxed;
                    },
                    boxed -> {
                        if (boxed == null) return new float[0];
                        float[] unboxed = new float[boxed.length];
                        for (int i = 0; i < boxed.length; i++) unboxed[i] = boxed[i];
                        return unboxed;
                    },
                    "float_array",
                    float[].class
            );
        }
    }

    public static final class DoubleArray extends PrimitiveArraySerializer<double[], Double> {
        public DoubleArray() {
            super(
                    Serializer.Array.create(Primitive.DOUBLE, Double[]::new),
                    arr -> {
                        if (arr == null) return null;
                        Double[] boxed = new Double[arr.length];
                        for (int i = 0; i < arr.length; i++) boxed[i] = arr[i];
                        return boxed;
                    },
                    boxed -> {
                        if (boxed == null) return new double[0];
                        double[] unboxed = new double[boxed.length];
                        for (int i = 0; i < boxed.length; i++) unboxed[i] = boxed[i];
                        return unboxed;
                    },
                    "double_array",
                    double[].class
            );
        }
    }

    public static final class CharArray extends PrimitiveArraySerializer<char[], Character> {
        public CharArray() {
            super(
                    Serializer.Array.create(Primitive.CHARACTER, Character[]::new),
                    arr -> {
                        if (arr == null) return null;
                        Character[] boxed = new Character[arr.length];
                        for (int i = 0; i < arr.length; i++) boxed[i] = arr[i];
                        return boxed;
                    },
                    boxed -> {
                        if (boxed == null) return new char[0];
                        char[] unboxed = new char[boxed.length];
                        for (int i = 0; i < boxed.length; i++) unboxed[i] = boxed[i];
                        return unboxed;
                    },
                    "char_array",
                    char[].class
            );
        }
    }
}
