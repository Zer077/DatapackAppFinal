/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instalacion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import DPGdao.LecturaYEscritura;
import DPGdao.Pack_MCMETA;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 *
 * @author jose_
 */
public class InstalacionDatapack {

    String saveRoot;
    String rutausuario;

    private final static String FILENAME = "pack.mcmeta", SEPARATOR = "\\", PATH_SAVES = "saves",
            PATH_DATAPACKS = "datapacks", PATH_DATA = "data", PATH_RECIPES = "recipes";

    public InstalacionDatapack() {
        saveRoot = "C:\\Users\\" + buscarUsuario() + "\\AppData\\Roaming\\.minecraft";
    }

    //Crea las carpetas donde debe instalarse el datapack
    public String creaEstructura(HashMap<String, String> data, boolean download) throws IOException {
        LecturaYEscritura lye = new LecturaYEscritura();
        String rutaInicial, rutaFinal;

        if (download) {
            rutaFinal = data.get("name");
        } else {
            rutaInicial = saveRoot + SEPARATOR + PATH_SAVES + SEPARATOR + data.get("world");
            rutaFinal = rutaInicial + SEPARATOR + PATH_DATAPACKS + SEPARATOR + data.get("name");
        }
        //creas pack.mcmeta y data 

        lye.crearDirectorio(rutaFinal);
        lye.crearFichero(FILENAME);

        lye.setRuta(rutaFinal + SEPARATOR + FILENAME);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        lye.sobreEscribir(gson.toJson(new Pack_MCMETA(data.get("desc"))));

        rutaFinal += SEPARATOR + PATH_DATA;
        lye.crearDirectorio(rutaFinal);
        //en data morerecipes
        rutaFinal += SEPARATOR + data.get("ns");
        lye.crearDirectorio(rutaFinal);

        //recipes
        rutaFinal += SEPARATOR + PATH_RECIPES;
        lye.crearDirectorio(rutaFinal);

        // subfolders
        if (data.containsKey("sub")) {
            rutaFinal += SEPARATOR + data.get("sub");
            lye.crearDirectorio(rutaFinal, true);
        }

        return rutaFinal;
    }

    //Realiza la instalacion del datapack 
    public void instalarDatapack(File file, HashMap<String, String> data) throws IOException {
        System.out.println("FILE: " + file.toString() + " - " + file.getPath() + " - " + file.getAbsolutePath() + " - " + file.getCanonicalPath());
        //Crea la estructura con el HashMap
        String rutaFinal = creaEstructura(data, false);
        //Itroduce el .json en la estructura de archivos
        Path temp = Files.move(Paths.get(file.getPath()),
                Paths.get(rutaFinal + SEPARATOR + data.get("file") + ".json"));
        if (temp != null) {
            System.out.println("File renamed and moved successfully");
        } else {
            System.out.println("Failed to move the file");
        }
        //Json con el nombre del objeto resultante
        System.out.println("HECHO ");

    }
//Busca el usuario de nuestro equipo

    public String buscarUsuario() {
        rutausuario = System.getProperty("user.name");
        return rutausuario;
    }
//Busca los mundos que tenemos en nuestro juego

    public String[] buscadorDeSaves() {
        if (!new File(saveRoot).exists()) {
            return null;
        }

        saveRoot += SEPARATOR + PATH_SAVES;

        return new File(saveRoot).list();
    }
}
