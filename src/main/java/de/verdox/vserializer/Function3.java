package de.verdox.vserializer;


/**
 * A function that has 3 input and 1 output parameter
 *
 * @param <T1> arg1
 * @param <T2> arg2
 * @param <T3> arg3
 * @param <R>  output 1
 */
@FunctionalInterface
public interface Function3<T1, T2, T3, R> {
    /**
     * the function
     *
     * @param t1 arg1
     * @param t2 arg2
     * @param t3 arg3
     * @return output 1
     */
    R apply(T1 t1, T2 t2, T3 t3);
}
