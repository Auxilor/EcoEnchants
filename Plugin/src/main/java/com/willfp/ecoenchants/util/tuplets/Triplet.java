package com.willfp.ecoenchants.util.tuplets;

/**
 * Spigot doesn't include javafx
 */
public class Triplet<A, B, C> {
    private A first;
    private B second;
    private C third;

    /**
     * Create a triplet
     *
     * @param first  The first item in the tuplet
     * @param second The second item in the tuplet
     * @param third  The third item in the tuplet
     */
    public Triplet(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
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
     * Get the third item in the tuplet
     *
     * @return The third item
     */
    public C getThird() {
        return third;
    }

    /**
     * Set the first item in the tuplet
     *
     * @param first The value to set
     */
    public void setFirst(A first) {
        this.first = first;
    }

    /**
     * Set the second item in the tuplet
     *
     * @param second The value to set
     */
    public void setSecond(B second) {
        this.second = second;
    }

    /**
     * Set the third item in the tuplet
     *
     * @param third The value to set
     */
    public void setThird(C third) {
        this.third = third;
    }

    @Override
    public String toString() {
        return "Triplet{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}
