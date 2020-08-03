/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DPGdao;

/**
 *
 * @author utrer
 */
public class Effect {

    @Override
    public String toString() {
        return "Effect{" + "name=" + name + ", positive=" + state + ", duration=" + duration + '}';
    }

    public final static String NO_EFFECT = "No effects";

    public final static int POSITIVE = 0x10;
    public final static int NEGATIVE = 0x11;

    public final static String STATE_NULL = "no-effects";
    public final static String STATE_POSITIVE = "positive";
    public final static String STATE_NEGATIVE = "negative";

    private String name = NO_EFFECT;
    private int state;
    private String duration;

    public Effect() {

    }

    public Effect(String name, int sta) {
        this.name = name;
        this.state = sta;
    }

    public Effect(String name, int sta, String duration) {
        this(name, sta);
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public int getState() {
        return state;
    }

    public String getDuration() {
        return duration;
    }

}
