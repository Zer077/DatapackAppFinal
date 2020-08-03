<%-- 
    Document   : indexDP
    Created on : 14-dic-2019, 20:46:05
    Author     : jdreyes
--%>

<%@page import="java.util.Enumeration"%>
<%@page import="java.io.File"%>
<%@page import="javax.swing.JFileChooser"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="DPGdao.Category"%>
<%@page import="DPGdao.Item"%>
<%@page import="DPGdao.Effect"%>
<%@page import="DPGdao.DAO"%>
<%@page import="instalacion.InstalacionDatapack"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    // Dimension de la tabla de crafteo
    int craftD = 3;
    // Rutas
    final String file = application.getRealPath("js") + "/items.json";
    final String imgItemsFold = "img/items/";
    final String imgCatFold = "img/categories/";
    // Obtengo las categorias cons sus respectivos objetos
    final Category[] dao = new DAO(file).getObjects();
    InstalacionDatapack ins = new InstalacionDatapack();
%>
<%!
    /**
     * Funcion que crea las categorias de los items
     *
     * @param imgRoot Ruta de la imagen (Carpeta)
     * @param cats Array de categorias
     * @param position Posicion donde situar las cateogias return String
     */
    public String createCats(String imgRoot, Category[] cats, int position) {
        String idDiv = position == Category.UP ? "header" : "footer";
        String classButton = (position == Category.UP ? "up" : "down") + "Cats";
        String html = "<div id='" + idDiv + "'>";
        Category c = new Category();
        int numCats = position == Category.UP ? c.getUpCats(cats) : c.getDownCats(cats);
        if (numCats > 0) {
            // Para obtener adecuadamente las categorias inferiores acorto el array
            if (position == Category.DOWN) {
                cats = Arrays.copyOfRange(cats, c.getUpCats(cats), cats.length);
            }
            for (int z = 0; z < numCats; z++) {
                String clase = cats[z].isSelect() ? "selectUp " : "";
                if (cats[z].getPosition() > Category.SPECIAL) {
                    clase += "specialCat";
                }
                html += "<button class='cat-" + cats[z].getName() + " categoria " + classButton + " " + clase + "'>";
                html += "<img src='" + imgRoot + cats[z].getImgRoot() + "' width='50px'";
                html += "</button>";
            }
        }
        html += "</div>";
        return html;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <!--referencias a los scipts y los css-->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/craftingStyles.css">
        <link rel="stylesheet" href="css/jquery-ui.css">
        <link rel="stylesheet" href="css/hover.css">
        <script src="js/jquery-3.4.1.js"></script>
        <script src="js/jquery-ui-1.12.1/jquery-ui.js"></script>
        <script src="js/jquery-cookie-master/jquery.cookie.js"></script>
        <title>DataPackGenerator</title>
    </head>
    <body oncontextmenu="return false;">
        <%
            boolean existAttr = false;
            String install = "invisible";
            Enumeration enu = session.getAttributeNames();
            while (enu.hasMoreElements()) {
                String name = enu.nextElement().toString();
                if (name.equals("install")) {
                    existAttr = true;
                }
            }
            if (existAttr && (Boolean) session.getAttribute("install")) {
                session.removeAttribute("install");
                install = "true";
                out.println("<scrip></script>");
            }
        %>
        <input type="hidden" id="ins" value="<%=install%>">
        <div class="success-install dialog-confirm invisible">
            <h1>Success install!</h1>
        </div>
        <div class="mesa-craft">
            <div id="trash">
                <img src="img/garbage.svg" height="50px">
            </div>
            <table class="table-bordered table-responsive drop">
                <%
                    /**
                     * Creo la mesa de crafteo (TAMAÑO: 3X3)
                     */
                    // Creo las filas
                    for (int i = craftD, id = 0; i > 0; i--) {
                %>
                <tr class="">
                    <%
                        // Creo las celdas
                        // El id servira para obtener el pattern
                        for (int j = craftD; j > 0; j--) {
                    %>
                    <td class="cell">
                        <div class="div-cell bordes" id="<%=id%>"></div>
                    </td>
                    <%
                            id++;
                        }
                    %>
                </tr>
                <%
                    }// Fin for filas
                %>
            </table>
            <div class="arrow"><img src="img/flecha.png"></div>
            <div class="result">
                <div class="result-div"></div>
                <span class="info-count invisible" title="Unless it's established a stack size the recet only give a result item"><img src="img/Info_icon.svg"></span>
            </div>
            <div id="countResult" class="invisible">
                <label for="number-result">Number of result items:</label>
                <input id="number-result" class="count-result" type="number" value="1" min="1" max="<%=Item.STACK_SIZE_MAX%>"/>

                <div id="maxStack" class="invisible alert alert-warning">
                    <label><span id="maxValue"></span> is the max value for the stack of that item.</label>
                </div>
            </div>
        </div>

        <div class="superior">
            <div>

                <%=createCats(imgCatFold, dao, Category.UP)%>
                <div class="list-item">
                    <%
                        // Creo las categorias
                        int z = 0;
                        for (; z < dao.length; z++) {
                            ArrayList<Item> itemsOfCat = dao[z].getItems();
                            // Aniado las clases necesarias
                            String clase = dao[z].isSelect() ? "visible" : "invisible";
                    %>
                    <div id="<%=dao[z].getName()%>" class="<%=clase%>">
                        <span class="titulo"><%=dao[z].getDescription()%></span>
                        <%if (dao[z].isSearcher()) {%>
                        <input id="search" class="bordes" type="text">
                        <%}%>
                        <div class="div-table <%if (dao[z].isSearcher()) {
                                out.print("relative");
                            }%>">
                            <%if (dao[z].isSearcher()) {%>
                            <div class="no-results">
                                <div class="not_found alert alert-warning"><img src="img/warning.png"><label>No items found!</label></div>
                                <div class="blocked"></div>
                            </div>
                            <%}%>
                            <table class="table-bordered table-responsive drag">
                                <%
                                    /*
                                    Creo la lista de objetos (TAMAÑO: 9X4)
                                     */
                                    int ColumnsItemsList = 9;
                                    int MinRowsItemList = itemsOfCat.size() <= 36 ? 4 : itemsOfCat.size() / 9
                                            + (itemsOfCat.size() % 9 == 0 ? 0 : 1);
                                    // Creo las filas
                                    for (int i = MinRowsItemList, x = 0; i > 0; i--) {
                                %>
                                <tr class="">
                                    <%
                                        // Creo las celdas
                                        for (int j = ColumnsItemsList; j > 0; j--) {
                                            String divClass = "div-cell";
                                    %>
                                    <td class="cell">
                                        <%
                                            // Obtengo la información de los items
                                            String name = "", desc = "", root = "", type = "";
                                            ArrayList<String> effect = new ArrayList<String>(),
                                                    states = new ArrayList<String>(),
                                                    dur = new ArrayList<String>();
                                            int stack = -1, rar = -1;
                                            Item item = null;
                                            if (x < itemsOfCat.size()) {
                                                divClass = "item ";
                                                item = itemsOfCat.get(x);
                                                name = item.getName();
                                                root = imgItemsFold + item.getRuta();
                                                type = item.getType();
                                                stack = item.getStackSize();
                                                rar = item.getRarity();

                                                switch (rar) {
                                                    case Item.RAR_UNCOMMON:
                                                        divClass += "uncommon ";
                                                        break;
                                                    case Item.RAR_RARITY:
                                                        divClass += "rare ";
                                                        break;
                                                    case Item.RAR_EPIC:
                                                        divClass += "epic ";
                                                        break;
                                                }
                                                if (item.getEffects() != null) {
                                                    for (Effect efecto : item.getEffects()) {
                                                        if (!efecto.getName().equals(Effect.NO_EFFECT)) {
                                                            effect.add(efecto.getName());
                                                            if (efecto.getDuration() != null) {
                                                                dur.add(efecto.getDuration());
                                                            }
                                                            switch (efecto.getState()) {
                                                                case Effect.NEGATIVE:
                                                                    states.add(Effect.STATE_NEGATIVE);
                                                                    break;
                                                                case Effect.POSITIVE:
                                                                    states.add(Effect.STATE_POSITIVE);
                                                                    break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        %>
                                        <div class="<%=divClass%> bordes">
                                            <%if (item != null) {%>
                                            <input type="hidden" name="name" value="<%=name%>">
                                            <%if (!type.equalsIgnoreCase(Item.ITEM)) {%>
                                            <input type="hidden" name="type" value="<%=type%>">
                                            <%}%>
                                            <%if (stack != Item.STACK_SIZE_MAX) {
                                                    switch (item.getStackSize()) {
                                                        case 1:
                                                            stack = 1;
                                                            break;
                                                        case 16:
                                                            stack = 16;
                                                            break;
                                                    }
                                            %>
                                            <input type="hidden" name="stackSize" value="<%=stack%>">
                                            <%
                                                }
                                            %>
                                            <%
                                                for (int k = 0; k < effect.size(); k++) {
                                            %>
                                            <input type="hidden" name="effect_<%=k%>" value="<%=effect.get(k)%>">
                                            <%}%>
                                            <%
                                                for (int k = 0; k < states.size(); k++) {
                                            %>
                                            <input type="hidden" name="state_<%=k%>" value="<%=states.get(k)%>">
                                            <%}%>
                                            <%
                                                if (!dur.isEmpty()) {
                                                    for (int k = 0; k < states.size(); k++) {
                                            %>
                                            <input type="hidden" name="dur_<%=k%>" value="<%=dur.get(k)%>">
                                            <%      }
                                                }%>
                                            <div class="div-cell">
                                                <img src="<%=root%>" width="50px">
                                            </div>
                                            <%}%>
                                        </div>
                                    </td>
                                    <%
                                            x++;
                                        }// Fin for celdas
                                    %>
                                </tr>
                                <%
                                    }// Fin for filas
                                %>
                            </table>
                        </div>
                    </div>
                    <%
                        }// Fin for categorias
                    %>
                </div>
                <%=createCats(imgCatFold, dao, Category.DOWN)%>
            </div>

            <form id="inst" action="Install" method="post">
                <div class="shapeType">
                    <label id="off" class="shape">Shapeless</label>
                    <label class="switch">
                        <input id="shape" name="shape" type="checkbox">
                        <span class="slider round"></span>
                    </label>
                    <label id="on" class="shape desactivated"><del>Shaped</del></label>
                </div>
                <fieldset id="dp_info">
                    <fieldset>
                        <label for="dp_name">Datapack name:</label>
                        <input type="text" name="dp_name" placeholder="dpg_{$CURRENT_TIME}" pattern="[a-z0-9_-./]">
                    </fieldset>
                    <fieldset>
                        <label for="dp_desc">Description:</label>
                        <input type="text" autocomplete="off" name="dp_desc" placeholder="New recipe for {$RESULT_ITEM}">
                    </fieldset>
                    <fieldset>
                        <label for="namespace">Namespace:</label>
                        <input type="text" name="namespace" placeholder="dpg_{$CURRENT_TIME}" pattern="[a-z0-9_-.]">
                    </fieldset>
                    <fieldset>
                        <label for="sub">Subfolder(s):</label>
                        <input type="text" name="sub" placeholder="Separate each folder with ','" pattern="[a-z0-9_-./]">
                    </fieldset>
                    <fieldset>
                        <label for="rec_name">Recipe file name:</label>
                        <input type="text" name="rec_name" placeholder="dpg_{$CURRENT_TIME}" pattern="[a-z0-9_-./]">
                    </fieldset>
                </fieldset>
                <%
                    String[] worlds = ins.buscadorDeSaves();
                    String disabled = "", cssClass = "hvr-wobble-vertical";
                    if (worlds == null) {
                        disabled = "title=\"Minecraft installation not found\"";
                        cssClass = "ban-pointer btn-disabled";
                    } else {
                        String lastClass = "world", html = "<div class='worldChooser invisible' title='Select a world'><ul>";
                        for (int i = 0; i < worlds.length; i++) {
                            if (i == worlds.length - 1) {
                                lastClass += "-last";
                            }
                            html += "<li><div class='" + lastClass + "'>" + worlds[i] + "</div></li>";
                        }
                        html += "</ul></div>";
                        out.print(html);
                    }
                %>

                <!--Botones del formulario-->
                <fieldset>
                    <div class="separator">
                        <button id="aux" class="<%=cssClass%>" type="submit" <%=disabled%>>
                            <image src="img/instalar.png">Install
                        </button>

                        <button id="down" type="submit" class="hvr-hang">
                            <img src="img/flecha.png">Download
                        </button>
                    </div>
                </fieldset>
                <div class="error-inst alert alert-danger invisible"></div>
            </form>
        </div>
    </body>
    <script src="js/primaryFunctions.js"></script>
    <script>
        if ($("#ins").val() === "true") {
            $(".success-install").removeClass($strInvisible).dialog(
                    {
                        autoOpen: false,
                        draggable: false,
                        resizable: false,
                        modal: true,
                        open: function (event, ui) {
                            $(this).parent().children(":first-child").hide();
                            $(this).parent().children(":last-child").css("background", "unset");
                            $(this).parent().css(
                                    {"background": "url('./img/CARTEL.png')",
                                        "border": "unset",
                                        "border-radius": "unset"}
                            );
                        },
                        buttons: {
                            "OK": function () {
                                $(this).dialog("close");
                            }
                        }
                    }
            ).dialog("open");
        }
        function foundCk() {
            if ($.cookie("download") !== undefined) {
                $(".separator").find("input").each(function (x, y) {
                    y.remove();
                });
                $("#inst").attr("action", "Install");
                $($classResItem).remove();
                $(".drop " + $classItem).remove();
                $("input[name='dp_desc']").val("");
                refreshPHDesc();
                if (!$.removeCookie("download")) {
                    console.warn("NO SE HA PODIDO BORRAR LA COOKIE");
                }
            }
            setTimeout(foundCk, 10);
        }
        setTimeout(foundCk, 10);
    </script>
    <script src="js/tooltipJS.js"></script>
    <script src="js/fixSize.js"></script>
    <script src="js/dragAndDrop.js"></script>
    <script src="js/changeCat.js"></script>
    <script src="js/changeNumResult.js"></script>
    <script src="js/searchItem.js"></script>
</html>
