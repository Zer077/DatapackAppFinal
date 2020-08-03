/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DPGdao;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Esta clase representa a una categoria de la lista de items disponibles
 *
 *
 * @author jdreyes
 */
public class Category {

    /**
     * Constantes
     */
    /*INDICAN LAS DISTINTAS POSICIONES EN LAS QUE PUEDEN APARECER LAS CATEGORIAS*/
    public final static int UP = 1;
    public final static int DOWN = 2;
    public final static int SPECIAL = 3;
    public final static int SPECIAL_UP = UP + SPECIAL;
    public final static int SPECIAL_DOWN = DOWN + SPECIAL;

    /**
     * Variables
     */
    private int position;
    private String name;
    private String description;
    private String imgRoot;
    private ArrayList<Item> items;
    private boolean select = false; // Indica si la categoria aparece seleccionada
    private boolean searcher = false;// Indica si la categoria es el buscador de items

    public Category() {

    }

    public Category(int position, String name, String description, String imgRoot, ArrayList<Item> items) {
        this.position = position;
        this.name = name;
        this.description = description;
        this.imgRoot = imgRoot;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgRoot() {
        return imgRoot;
    }

    public void setImgRoot(String imgRoot) {
        this.imgRoot = imgRoot;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Category{" + "name=" + name + ", imgRoot=" + imgRoot + ", items=" + items + '}';
    }

    private int countCats(Category[] cats, int position) {
        return (int) Arrays.asList(cats).stream().filter(x -> {
            return (x.getPosition() == position || x.getPosition() == (position + SPECIAL));
        }).count();
    }

    public int getUpCats(Category[] cats) {
        return countCats(cats, UP);
    }

    public int getDownCats(Category[] cats) {
        return countCats(cats, DOWN);
    }

    public boolean isSelect() {
        return select;
    }

    public boolean isSearcher() {
        return searcher;
    }

}// Fin clase
