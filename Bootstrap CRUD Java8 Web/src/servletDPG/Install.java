/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servletDPG;

import DPGdao.DataPackJSON;
import DPGdao.Item;
import DPGdao.KeyObjectDP;
import DPGdao.LecturaYEscritura;
import DPGdao.ResultObjectDP;
import instalacion.InstalacionDatapack;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.lingala.zip4j.ZipFile;

/**
 *
 * @author utrer
 */
@WebServlet(name = "Install", urlPatterns = {"/Install", "/download"})
public class Install extends HttpServlet {

    private static boolean SHAPE = false;
    private final static char BLANK_SPACE = ' ';
    private final static char[] VALUES_PATTERN = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Install</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Install at " + request.getContextPath() + "</h1>");
            out.println("<h1>" + request.getPathInfo() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathFile = "mydp.json";
        response.setContentType("text/html;charset=UTF-8");

        InstalacionDatapack ins = new InstalacionDatapack();
        HashMap<String, String> parameters = createFileDP(request, response, pathFile);

        if (request.getServletPath().equals("/Install")) {
            ins.instalarDatapack(new File(pathFile), parameters);

            request.getSession().setAttribute("install", true);
            response.sendRedirect("http://localhost:8080");

        } else if (request.getRequestURI().equals("/download")) {

//Crea la estructura de ficheros
            String pathFolder = ins.creaEstructura(parameters, true);
            //Obtiene la ruta del archivo que acabamso de crear
            String absolutePath = new File(pathFolder).getAbsolutePath();
            //Corta la ruta hasta el directorio del datapack, ya que esta se crea el el directorio de tomcat
            absolutePath = absolutePath.substring(0, absolutePath.indexOf(parameters.get("name")));

//Comprime todos los ficheros en zip del datapack (En su orden correcto)
            if (new LecturaYEscritura().moveFile(new File(pathFile), pathFolder + "\\" + parameters.get("file") + ".json")) {
                absolutePath += parameters.get("name");
                ZipFile zip = new ZipFile(parameters.get("name") + ".zip");
                for (File f : new File(absolutePath).listFiles()) {
                    if (f.isDirectory()) {
                        zip.addFolder(new File(absolutePath + "//" + f.getName()));
                    } else {
                        zip.addFile(new File(absolutePath + "//" + f.getName()));
                    }
                }
//Crea un archivo a partir de lo creado y lo acaba en .zip
                // reads input file from an absolute path
                String filePath = absolutePath + ".zip";
                File downloadFile = new File(filePath);
                //Crea un FileInputStream con el archivo
                FileInputStream inStream = new FileInputStream(downloadFile);

                // obtains ServletContext
                ServletContext context = getServletContext();

                //Obtiene el tipo MIME del archivo y si es null lo hace del tipo zip
                String mimeType = context.getMimeType(filePath);
                if (mimeType == null) {
                    // set to binary type if MIME mapping not found
                    mimeType = "application/zip";
                }

                //  modifica la respuesta al servidor
                response.setContentType(mimeType);
                response.setContentLength((int) downloadFile.length());

                // fuerza la descarga , poniendo el header adecuado a la respuesta del servidor
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
                response.setHeader(headerKey, headerValue);

                //Se añade una Cookie a la respuesta para que se sepa que se ha descargado
                Cookie cookie = new Cookie("download", "1");
                response.addCookie(cookie);

                // obtiene el output Stream de la respuesta
                OutputStream outStream = response.getOutputStream();

                //Escribe en el bufer la respuesta
                byte[] buffer = new byte[4096];
                int bytesRead = -1;

                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }

                outStream.close();

            } else {
                System.out.println("Error en la descarga del archivo");
            }
        }

    }// Fin metodo

    /**
     * Este metodo proporiciona la funcionalidad del metodo distinct() propio de
     * Stream en java, es decir devuelve los elementos distintos
     *
     * @param items Lista de items
     * @return List<KeyObjectDP> con valores únicos
     */
    public List<KeyObjectDP> distinct(List<KeyObjectDP> items) {
        Iterator<KeyObjectDP> it = items.iterator();
        List newList;
        List<Integer> indexes = new ArrayList<Integer>();
        while (it.hasNext()) {
            KeyObjectDP key = it.next();
            if (indexes.contains(items.indexOf(key))) {
                continue;
            }
            if (!(newList = items.subList(items.indexOf(key) + 1, items.size())).isEmpty()) {
                Iterator<KeyObjectDP> i = newList.iterator();
                while (i.hasNext()) {
                    KeyObjectDP k = i.next();
                    if (key.equals(k)) {
                        indexes.add(items.indexOf(k));
                    }
                }
            }
        }
        indexes = indexes.stream().sorted().collect(Collectors.toList());
        Collections.reverse(indexes);
        indexes.stream().forEachOrdered((index) -> {
            items.remove(items.get(index));
        });
        return items;

    }

    public HashMap<String, String> createFileDP(HttpServletRequest request, HttpServletResponse response, String path) {
        HashMap<String, String> dataToInstall = null;
        try {
            // Obtengo todos los parametros
            Map<String, String[]> paramsMap = request.getParameterMap();
            // ArrayList que contendra los items de la receta
            ArrayList<KeyObjectDP> items = new ArrayList<>();
            // Lista para el pattern
            List<Object> patternList = new ArrayList<>(9);
            // Relleno la lista con valores que consideraré nulos 
            for (int i = 0; i < 9; i++) {
                patternList.add(-1);
            }
            ResultObjectDP result;
            dataToInstall = new HashMap<>();
            String dpName, namespace, subs, recipeName;

            /**
             * Los campos "datapack name" (dp_name), "recipe name" (rec_name) y
             * "namespace" (homonimo) no pueden estar vacíos, así que si el
             * usuario no ha introducido valores para ellos se les asignará un
             * valor
             */
            if (!request.getParameter("dp_name").equals("")) {
                dpName = request.getParameter("dp_name").replaceAll("[-+.^'¡ç´_:;,*]", "");
                if (dpName.isEmpty()) {
                    dpName = "Default";

                }
            } else {
                dpName = "dpgen_" + System.currentTimeMillis();
            }

            if (!request.getParameter("rec_name").equals("")) {
                recipeName = request.getParameter("rec_name").replaceAll("[-+.^'¡ç´_:;,*]", "");
                if (recipeName.isEmpty()) {
                    recipeName = "Default";

                }
            } else {
                recipeName = dpName;
            }

            if (!request.getParameter("namespace").equals("")) {
                namespace = request.getParameter("rec_name").replaceAll("[-+.^'¡ç´_:;,*]", "");
                if (namespace.isEmpty()) {
                    namespace = "Default";

                }
            } else {
                namespace = dpName;
            }

            /**
             * Introduzco los datos en el mapa
             */
            dataToInstall.put("world", request.getParameter("world"));
            dataToInstall.put("name", dpName);
            dataToInstall.put("desc", request.getParameter("dp_desc").replaceAll("[-+.^'¡ç´_:;,*]", ""));
            dataToInstall.put("ns", namespace);

            if (!request.getParameter("sub").equals("")) {
                subs = request.getParameter("sub").replaceAll(",", "//");
                subs = subs.replaceAll("[-+.^'¡ç´_:;,*]", "");
                dataToInstall.put("sub", subs);
            }
            dataToInstall.put("file", recipeName);
            //-----------------------------------------------

            if (request.getParameter("shape") != null) {
                SHAPE = true;
            }

            // Creo el result item
            if (request.getParameter("count") == null) {
                result = new ResultObjectDP(request.getParameter("result-item_name"));
            } else {
                result = new ResultObjectDP(request.getParameter("result-item_name"),
                        Integer.parseInt(request.getParameter("count")));
            }

            // Obtengo la posicion de los objetos
            int[] nums = paramsMap.keySet().stream().mapToInt(x -> {
                return Character.getNumericValue(x.charAt(0));
            }).distinct().filter(x -> {
                // Obtengo SOLO los items de la receta
                return x < 10;
            }).toArray();

            // Creo los items
            for (int num : nums) {
                List<Object> datos = paramsMap.keySet().stream().filter(x -> {
                    return Character.getNumericValue(x.charAt(0)) == num;
                }).map(x -> {
                    return request.getParameter(x);
                }).collect(Collectors.toList());

                if (SHAPE) {
                    patternList.set(num, datos.get(0));
                }

                switch (datos.size()) {
                    case 1:
                        items.add(new KeyObjectDP(new Item(datos.get(0).toString())));
                        break;
                    case 2:
                        items.add(
                                new KeyObjectDP(
                                        new Item(datos.get(0).toString(),
                                                datos.get(1).toString())));
                        break;
                    default:
                        break;
                }
            }

            if (!SHAPE) {
                items = (ArrayList<KeyObjectDP>) distinct(items);
            }

            LecturaYEscritura lye = new LecturaYEscritura(path);
            if (SHAPE) {
                char[] patternInLine = new char[9];
                Arrays.fill(patternInLine, BLANK_SPACE);

                LinkedHashMap<String, KeyObjectDP> mapKeys = new LinkedHashMap<>();

                int index;
                for (int i = 0; i < items.size(); i++) {
                    char value = VALUES_PATTERN[i];
                    int cont = -1;
                    do {
                        cont++;
                        List<Object> subList = patternList.subList(cont, patternList.size());
                        index = subList.indexOf(items.get(i).getPureItem());
                        if (index != -1) {
                            if (patternInLine[index + cont] != BLANK_SPACE) {
                                continue;
                            }
                            patternInLine[index + cont] = value;
                            mapKeys.put(String.valueOf(value), items.get(i));
                        }
                    } while (index != -1);
                }

                
                //Optimizacion del pattern
                char[][] pattern = new char[3][];
                for (int i = 0, j = 0, k = 3; i < 3; i++, j += 3, k += 3) {
                    pattern[i] = Arrays.copyOfRange(patternInLine, j, k);
                }

                pattern = optimizarPattern(pattern);
                lye.sobreEscribir(new DataPackJSON(pattern, mapKeys, result).imprimir());
            } else {
                lye.sobreEscribir(new DataPackJSON(items, result).imprimir());
            }

        } catch (IOException ex) {
            Logger.getLogger(Install.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dataToInstall;
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    public char[][] trasponMatriz(char[][] array) {
        char[][] trasp = new char[array[0].length][array.length];

        for (int j = 0; j < trasp.length; j++) {
            for (int i = 0; i < trasp[j].length; i++) {
                trasp[j][i] = array[i][j];
            }
        }

        return trasp;
    }

    public char[][] optimizarPattern(char[][] pattern) {
        
        //creo un nuevo pattern optimizando filas
        char[][] nuevo = optimizarFYC(pattern);
        nuevo = trasponMatriz(nuevo);

        char[][] nu = optimizarFYC(nuevo);
        nu = trasponMatriz(nu);

        return nu;
    }

    //Este método optimiza las Filas y las Columnas de la matriz de la manera adecuada
    public char[][] optimizarFYC(char[][] pattern) {
        int f1c1 = 1;
        int f2c2 = 3;
        int f3c3 = 5;
        int fycfinal = 0;
        HashMap<Integer, Integer> mapaValores = new HashMap<>();
        mapaValores.put(f1c1, 0);
        mapaValores.put(f2c2, 1);
        mapaValores.put(f3c3, 2);
//recorre las filas y si son iguales a "" es decir, no contienen nada les sumamos un número a un numero final
        for (int idx = 0; idx < pattern.length; idx++) {
            if (new String(pattern[idx]).trim().equals("")) {
                switch (idx) {
                    case 0:
                        fycfinal += f1c1;
                        break;
                    case 1:
                        fycfinal += f2c2;
                        break;
                    case 2:
                        fycfinal += f3c3;
                        break;
                }
            }
        }

        if (fycfinal == 0) {
            return pattern;
        }

        char[][] aux = new char[3][3];
        if (mapaValores.containsKey(fycfinal) && fycfinal != f2c2) {
            //Elimino la primera o la ultima fila/columna

            aux = new char[2][];

            int cont = 0, contAux = 0;
            for (Map.Entry<Integer, Integer> entry : mapaValores.entrySet()) {

                Integer key = (Integer) entry.getKey();
                if (key != fycfinal) {

                    aux[cont] = pattern[contAux];
                    cont++;
                }
                contAux++;
            }
        } else if (!mapaValores.containsKey(fycfinal)) {
            /*
                    Para eliminar multiples filas o columnas recorro el mapa de las claves
                    para obtener todas las sumas de dos elementos posibles.
                    
                    Si una de las posibles sumas es igual a filafinal, sé que la fila/columna 
                    a rescatar es aquella que no forma parte de la suma.
             */

            Integer[] keys = mapaValores.keySet().toArray(new Integer[0]);
            for (int i = 0; i < keys.length; i++) {
                Integer key = keys[i];
                for (int j = i + 1; j < keys.length; j++) {
                    Integer key1 = keys[j];
                    int suma = key + key1;

                    if (suma == fycfinal) {

                        // Obtengo la fila a rescatar
                        List<Integer> unique = Arrays.asList(keys).stream().filter(x -> {

                            return x != key && x != key1;
                        }).collect(Collectors.toList());

                        aux = new char[][]{pattern[mapaValores.get(unique.get(0))]};
                    }
                }
            }
        }

        if (fycfinal != f2c2) {
            return aux;
        } else {
            return pattern;
        }
    }

}
