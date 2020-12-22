package com.willfp.eco.util.tuplets;

/**
 * Spigot doesn't include javafx
 */
public class Pair<A, B> {
    private A first;
    private B second;

    /**
     * Create a pair
     *
     * @param first  The first item in the tuplet
     * @param second The second item in the tuplet
     */
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Get the first item in the tuplet
     *
     * @return The first item
     */
    public A getFirst() {
        return first;
    }

    /**
     * Get the second item in the tuplet
     *
     * @return The second item
     */
    public B getSecond() {
        return second;
    }

    /**
     * Set the first item in the tuplet
     *
     * @param first The value to set the first item to
     */
    public void setFirst(A first) {
        this.first = first;
    }

    /**
     * Set the second item in the tuplet
     *
     * @param second The value to set the second item to
     */
    public void setSecond(B second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
