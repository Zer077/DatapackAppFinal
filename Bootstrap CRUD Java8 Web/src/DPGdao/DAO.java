package DPGdao;

import com.google.gson.Gson;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package DPGdao;
/**
 *
 * @author usuario
 */
public class DAO {

    private final static String rootItemsFile = "items/items.json";
    private LecturaYEscritura lye;

    public DAO(String src) {
        lye = new LecturaYEscritura(src);
    }

    public Category[] getObjects() {
        Gson gson = new Gson();

        System.out.println(Arrays.deepToString(gson.fromJson(String.join("\n", lye.sacarLineas()), Category[].class)));

        return gson.fromJson(String.join("\n", lye.sacarLineas()), Category[].class);
    }

}
