package de.verdox.vserializer;

/**
 * A function that has 4 input and 1 output parameter
 *
 * @param <T1> arg1
 * @param <T2> arg2
 * @param <T3> arg3
 * @param <T4> arg4
 * @param <R>  output 1
 */
@FunctionalInterface
public interface Function4<T1, T2, T3, T4, R> {
    /**
     * the function
     *
     * @param t1 arg1
     * @param t2 arg2
     * @param t3 arg3
     * @param t4 arg4
     * @return output 1
     */
    R apply(T1 t1, T2 t2, T3 t3, T4 t4);
}
