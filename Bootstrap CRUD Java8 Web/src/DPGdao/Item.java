/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DPGdao;

import java.util.Arrays;

/**
 * Esta clase representa un objeto de Minecraft
 *
 * @author jose_
 */
public class Item {

    /**
     * Constantes
     */
    public final static String NAMESPACE = "minecraft:";
    public final static String ITEM = "item";
    public final static String TAG = "tag";

    public final static int STACK_SIZE_MIN = 1;
    public final static int STACK_SIZE_16 = 16;
    public final static int STACK_SIZE_MAX = 64;

    public final static int RAR_COMMON = 0x00;
    public final static int RAR_UNCOMMON = 0x01;
    public final static int RAR_RARITY = 0x02;
    public final static int RAR_EPIC = 0x03;

    /**
     * Variable de instancia
     */
    private String name;
    private String type = ITEM;
    private String version;
    private String ruta;
    private int stackSize = STACK_SIZE_MAX;
    private int rarity = RAR_COMMON;
    private Effect[] effects;

    public Item() {

    }

    public Item(String name) {
        this.name = NAMESPACE + name;
    }

    public Item(String name, String type) {
        this(name);
        this.type = type;
    }

    public Item(String name, int stackSize) {
        this(name);
        this.stackSize = stackSize;
    }

    public Item(String name, String type, int stackSize) {
        this(name);
        this.type = type;
        this.stackSize = stackSize;
    }

    public Item(String name, String type, String version, String ruta) {
        this.name = NAMESPACE + name;
        this.type = type;
        this.ruta = ruta;
        this.version = version;
    }

    /**
     * Metodo que comprueba si el tipo del item es TAG
     *
     * @return True en caso afirmativo
     */
    public boolean isATagItem() {
        return type.equals(TAG);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the ruta
     */
    public String getRuta() {
        return ruta;
    }

    /**
     * @param ruta the ruta to set
     */
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String imprimir() {
        return "Item{" + "name=" + name + ", type=" + type + ", version=" + version + ", ruta=" + ruta + ", stackSize=" + stackSize + ", rarity=" + rarity + ", efectos=" + Arrays.toString(effects) + '}';
    }

    public int getStackSize() {
        return stackSize;
    }

    public int getRarity() {
        return rarity;
    }

    public Effect[] getEffects() {
        return effects;
    }

}// Fin clase
