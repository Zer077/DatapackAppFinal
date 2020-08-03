/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DPGdao;

import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author jose_
 */
public class DataPackJSON {

    /**
     * Variables de clase
     */
    public final static String SHAPED = "crafting_shaped";
    public final static String SHAPELESS = "crafting_shapeless";

    /**
     * Variables de instancia
     */
    private String type;
    private String[] pattern;
    private LinkedHashMap<String, KeyObjectDP> key;
    private ArrayList<KeyObjectDP> ingredients;
    private ResultObjectDP result;

    public DataPackJSON() {

    }

    public DataPackJSON(char[][] pattern, LinkedHashMap<String, KeyObjectDP> key, ResultObjectDP result) {
        this.type = SHAPED;

        this.pattern = new String[pattern.length];
        for (int i = 0; i < this.pattern.length; i++) {
            this.pattern[i] = new String(pattern[i]);
        }

        System.out.println("KEY: " + key.toString());

        this.key = key;
        this.result = result;

        System.out.println("PASE");
        System.out.println("GSON: " + new GsonBuilder().setPrettyPrinting().create().toJson(this));
    }

    public DataPackJSON(ArrayList<KeyObjectDP> ingredients, ResultObjectDP result) {
        this.type = SHAPELESS;

        System.out.println("INGREDIENTS: " + ingredients.toString());

        this.ingredients = ingredients;
        this.result = result;

        System.out.println("PASE");
        System.out.println("GSON: " + new GsonBuilder().setPrettyPrinting().create().toJson(this));
    }

    public String imprimir() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}// Fin
