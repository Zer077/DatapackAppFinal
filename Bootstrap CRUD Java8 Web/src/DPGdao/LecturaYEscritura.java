/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DPGdao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 *
 * @author Usuario
 */
public class LecturaYEscritura {
//Metodos en minuscula

    private String ruta;
    private FileReader fr;
    private BufferedReader br;
    private FileWriter fw;
    BufferedWriter bw;
    FileOutputStream fo;

    public LecturaYEscritura() {
    }

    public LecturaYEscritura(String ruta) {
        this.ruta = ruta;

    }

    /**
     *
     * lee el fichero
     *
     *
     * @param ruta
     */
    public void leerFichero(String ruta) {
        this.setRuta(ruta);
        leer();

    }

//Lee un archivo
    private void leer() {

        abrirFichero();
        leerLineas();
        try {
            fr.close();
        } catch (IOException ex) {
        }
    }

    //abre el fichero
    public void abrirFichero() {
        try {
            fr = new FileReader(getRuta());
            br = new BufferedReader(fr);

        } catch (FileNotFoundException ex) {
            System.out.println("error");
        }
    }

    //lee las lineas
    private void leerLineas() {
        try {
            String linea = br.readLine();
            while (linea != null) {
                System.out.println(linea);

                linea = br.readLine();
            }
        } catch (IOException ex) {
        }
    }

    /**
     *
     * sobreescribe el archivo con el texto que metas
     *
     *
     * @param palabra
     * @throws java.io.IOException
     *
     *
     */
    public void sobreEscribir(String palabra) throws IOException {

        fw = new FileWriter(getRuta());
        try (PrintWriter out = new PrintWriter(fw, true)) {
            out.write(palabra);
            out.flush();
        }

    }

    /**
     *
     * escribe el archivo con el texto que metas
     *
     *
     * @param palabra
     * @throws java.io.IOException
     *
     *
     */
    public void escribir(String palabra) throws IOException {

        fw = new FileWriter(getRuta(), true);
        bw = new BufferedWriter(fw);
        bw.write(palabra);
        bw.close();
        fw.close();

    }

    /**
     *
     * escribe el texto en la siguiente linea del archivo
     *
     *
     * @param palabra
     *
     *
     */
    public void escribirSiguienteLinea(String palabra) {
        try {
            fw = new FileWriter(getRuta(), true);
            bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write(palabra);
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Error al escribir");
            System.out.println(e);
        }

    }

    /**
     *
     * Crea un directorio en la ruta indicada
     *
     *
     * @param ruta
     *
     *
     */
    public void crearDirectorio(String ruta) {
        crearDirectorio(ruta, false);
    }

    public void crearDirectorio(String ruta, boolean multiples) {

        File directorio = new File(ruta);
        if (multiples) {
            directorio.mkdirs();
        } else {
            directorio.mkdir();
        }
    }

    //Crea un fichero del nombre que quieras en la ruta que se le haya predefinido
    public void crearFichero(String archv) throws IOException {

        File archivo = new File(archv);
        archivo.createNewFile();
    }

    public File crearFicheroYDevolver(String archv) throws IOException {

        File archivo = new File(archv);
        archivo.createNewFile();
        return archivo;
    }

    /**
     * Elimina el directorio deseado aunque contenga mas directorios dentro
     *
     * @param ruta
     */
    public void eliminarDirectorio(String ruta) {
        File directorio = new File(ruta);
        File[] ficheros = directorio.listFiles();
        eliminarRecursivamente(ficheros);
        directorio.delete();
    }

    /**
     * Elimina el fichero que se le indique
     *
     * @param archv -- ruta del fichero
     */
    public void eliminarFichero(String archv) {
        File archivo = new File(archv);
        archivo.delete();
    }

//Elimina los directorios recursivamente
    private void eliminarRecursivamente(File[] ficheros) {

        for (File fichero1 : ficheros) {
            if (fichero1.isDirectory()) {
                File[] fichero = fichero1.listFiles();
                eliminarRecursivamente(fichero);
                fichero1.delete();
            } else {
                fichero1.delete();
            }

        }

    }

    public ArrayList<File> cogerImagenes(String ruta) {
        File directorio = new File(ruta);
        File[] ficheros = directorio.listFiles();
        ArrayList<File> imagenes = new ArrayList<>();
        for (File fichero : ficheros) {

            imagenes.add(fichero);

        }
        return imagenes;
    }

    public boolean CarpetaVacia(String ruta) {
        File file = new File(ruta);
        return file.listFiles().length == 1;
    }

    public void mostrarDirectorio(String ruta) {
        try {
            crearFichero("listadirectorio.txt");
        } catch (IOException ex) {
        }
        File directorio = new File(ruta);
        File[] ficheros = directorio.listFiles();
        mostrarRecursivamente(ficheros);
        LecturaYEscritura lye = new LecturaYEscritura("listadirectorio.txt");
        lye.abrirFichero();
        lye.escribirSiguienteLinea(directorio.getName());
    }

    private void mostrarRecursivamente(File[] ficheros) {
        LecturaYEscritura lye = new LecturaYEscritura("listadirectorio.txt");
        lye.abrirFichero();
        for (File fichero1 : ficheros) {
            if (fichero1.isDirectory()) {
                File[] fichero = fichero1.listFiles();
                mostrarRecursivamente(fichero);
                lye.escribirSiguienteLinea(fichero1.getName());

            } else {
                lye.escribirSiguienteLinea(fichero1.getName());

            }

        }

    }

    /**
     * Escribe usando el metodo NextLine
     *
     * @param palabra
     */
    public void escribirNextLine(String palabra) {
        try {
            fw = new FileWriter(ruta, true);
            bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write(palabra);
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Error al escribir");
            System.out.println(e);
        }

    }

    /**
     * Saca la siguiente linea del lector
     *
     * @return
     */
    public String sacarLinea() {

        try {
            abrirFichero();
            String linea = br.readLine();
            return linea;
        } catch (IOException ex) {
            System.out.println("Error al leer Lineas");
            System.out.println(ex);
        }

        return null;

    }

    /**
     *
     * Introduce un salto de Linea
     *
     * @throws java.io.IOException
     */
    public void introducirSaltoLinea() throws IOException {
        fw = new FileWriter(getRuta(), true);
        bw = new BufferedWriter(fw);
        bw.newLine();
        bw.close();
        fw.close();

    }

    /**
     *
     * Saca lineas del archivo en un ArrayList
     *
     * @return
     */
    public ArrayList sacarLineas() {
        ArrayList lineas = new ArrayList();
        try {
            abrirFichero();
            lineas.add(br.readLine());
            while (lineas.get(lineas.size() - 1) != null) {

                lineas.add(br.readLine());
            }
            lineas.remove(null);
            return lineas;

        } catch (IOException ex) {
            System.out.println("Error al leer Lineas");
            System.out.println(ex);
        }

        return null;

    }

    /**
     *
     * Separa las lineas por el caracter que le digas y lo mete en un ArrayList
     *
     * @param caracterDeSeparacion
     * @return
     *
     */
    public ArrayList lineaSplit(String caracterDeSeparacion) {

        ArrayList lineas = sacarLineas();
        ArrayList todaslaspalabras = new ArrayList();
        for (int i = 0; i < lineas.size() - 1; i++) {
            String linea = lineas.get(i).toString();
            String[] palabras = linea.split(caracterDeSeparacion);
            todaslaspalabras.addAll(Arrays.asList(palabras));

        }
        return todaslaspalabras;

    }

    /**
     *
     * Cuenta todas las palabras del documento
     *
     * @param caracterDeSeparacion --introduce como se separan las palabras, el
     * caracter
     * @return
     *
     */
    public int cuentaPalabras(String caracterDeSeparacion) {
        int palabra = 0;
        ArrayList lineas = sacarLineas();
        for (int i = 0; i < lineas.size() - 1; i++) {
            String linea = lineas.get(i).toString();
            String[] palabras = linea.split(caracterDeSeparacion);
            for (String palabra1 : palabras) {
                //System.out.println(b[j]);
                palabra++;
            }

        }
        return palabra;

    }

    /**
     *
     * Escribe con el metodo DataOutput
     *
     *
     * @param palabra
     * @param nombreArchivo
     * @throws java.io.FileNotFoundException
     */
    public void EscribirDataOutput(String palabra, File nombreArchivo) throws FileNotFoundException, IOException {

        try (DataOutputStream data = new DataOutputStream(new FileOutputStream(nombreArchivo))) {
            data.writeUTF(palabra);
            data.close();

        }
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

    /**
     * Metodo que lee un archivo y se lo muestra al usuario paginado
     *
     * @param tamaniolinea
     * @param paginas
     */
    public void paginador(int tamaniolinea, int paginas) {

        char[] linea = new char[tamaniolinea];
        try {
            String mensaje = "\n------------------------------------\n";
            //int centi;
            boolean salir = false;
            // Se puedes usar Files.lines() o Files.readAllLines()

            while (!salir) {

                int size = 0;

                while (size < paginas && !salir) {
                    if (br.read(linea) == -1) {
                        salir = true;
                    } else {
                        mensaje += String.valueOf(linea);
                        size++;
                        linea = new char[tamaniolinea];
                    }// Fin if-else

                }// Fin while interno

                mensaje += "\n------------------------------------\n"
                        + "Pulse ENTER para seguir leyendo.\n";

                System.out.print(mensaje);

                // Solo cuando pulse ENTER muestro la siguiente pagina
                try {
                    System.in.read();
                } catch (IOException e) {
                    System.out.println("error");
                }

            }// Fin while

            System.out.println("\n----------- Fin del archivo ------------");

        } catch (FileNotFoundException ex) {
            System.out.println("error, archivo no encontrado");
        } catch (IOException ex) {
            System.out.println("error, IOException");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
            }

        }

    }

    /**
     * Metodo para contar las letras de un fichero introduce un array con las
     * letras que contar
     *
     *
     * @param letras
     */
    public void contarLetras(String[] letras) {

        try {
            br = new BufferedReader(
                    new FileReader(new File(ruta)));
            //     String[] vocales = {"aAáÁ", "eEéÉ", "iIíÍ", "oOóÓ", "uUúÚüÜ"};
            int[] contadores = new int[letras.length];
            String l;

            while ((l = br.readLine()) != null) {
                for (int i = 0; i < letras.length; i++) {
                    // Reemplazo las vocales por cadena vacía
                    String mod = l.replaceAll("[" + letras[i] + "]", "");
                    /*
                  La diferencia entre la longitud de la cadena original con
                  respecto a la modificada es el número de vocales encontradas
                     */
                    contadores[i] += l.length() - mod.length();

                }// Fin for

            }// Fin while

            System.out.println("El recuento de letras es el siguiente: ");

            for (int i = 0; i < letras.length; i++) {
                System.out.println(letras[i].charAt(0) + " en distintas formas: " + contadores[i]);
            }// Fin for

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
            }// Fin try-catch interno

        }// Fin try-catch-finally

    }// Fin metodo

    /**
     * Metodo que permite contar las palabras de un fichero
     *
     * @throws java.io.IOException
     */
    public void contarPalabras() throws IOException {

        try {
            br = new BufferedReader(new FileReader(new File(ruta)));

            int contador = 0;
            String l;

            while ((l = br.readLine()) != null) {

                // 1º Elimino todos los espcios que no esten seguidos de caracteres y nums
                // 2º Sustituyo todo lo que no sean letras o los espacios seguidos de un caracter por un espacio
                String mod = filtrarTexto(l);//l.replaceAll("\\s(?!\\b)|\\d", " ").replaceAll("[^a-zA-z]|\\s(?=\\b)", " ");

                StringTokenizer st = new StringTokenizer(mod);
                int cont = st.countTokens();
                //System.out.println(cont+" - "+mod);
                contador += cont;

            }// Fin while

            System.out.println("El fichero contiene " + contador + " palabras(s)");

        } catch (FileNotFoundException ex) {
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
            }// Fin try-catch interno

        }// Fin try-catch-finally

    }// Fin metodo

    /**
     * Funcion que elimina del texto los numeros y todo los simbolos que no
     * esten entre la A y la Z
     *
     * @param txt --> String a filtrar
     * @return --> String filtrada
     */
    private String filtrarTexto(String txt) {
        // 1º Elimino todos los espcios que no esten seguidos de caracteres y nums
        // 2º Sustituyo todo lo que no sean letras o los espacios seguidos de un caracter por un espacio
        return txt.replaceAll("\\s(?!\\b)|\\d", " ").replaceAll("[^a-zA-zñÑáÁéÉíÍóÓúÚüÜ]|\\s(?=\\b)", " ");
    }

    /**
     * Metodo que lee un fichero y escribe su contenido en otro caracter a
     * caracter
     *
     * @param readFile --> Ruta del fichero a leer
     * @param writeFile --> Ruta del fichero donde escribir
     */
    public static void escribirYLeerChar(String readFile, String writeFile) {
        BufferedReader br = null;
        BufferedWriter bw = null;
        System.out.println(readFile + " - " + writeFile);
        try {

            // Se puedes usar Files.lines() o Files.readAllLines()
            br = new BufferedReader(new FileReader(readFile));
            bw = new BufferedWriter(new FileWriter(writeFile));

            int ch;
            while ((ch = br.read()) != -1) {
                bw.write(ch);
            }
            System.out.println("**-- La escritura del contenido se ha realizado con éxito --**");

        } catch (IOException ex) {
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ex) {
            }// Final try-catch interno

        }// Final try-catch-finally

    }// Fin metodo

    public boolean moveFileSimple(File fromFile, String toFile) {
        File f = null;
        try {
            f = crearFicheroYDevolver(toFile);
        } catch (IOException ex) {
        }
        return fromFile.renameTo(f);
    }

    public boolean moveFile(File fromFile, String toFile) throws IOException {
        Path temp = Files.move(Paths.get(fromFile.getPath()),
                Paths.get(new File(toFile).getPath()));
        if (temp != null) {
            System.out.println("File renamed and moved successfully");
            return true;
        } else {
            System.out.println("Failed to move the file");
            return false;
        }
    }
}
