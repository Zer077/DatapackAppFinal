/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
const regex = /\s/g; // Expresion regular para encontrar todos los espacios en blanco
const subst = '_';
const $emptyChildCell = "<div class='div-cell bordes'></div>";
const $emptyCell = "<td class='cell'>" + $emptyChildCell + "</td>";

// Esta variable contiene el mensaje que se mostrará cuando la busqueda sea infructuosa
var $mens = $(getClass("no-results"));

/*
 * Funcion que obtiene todas las categorias salvo la del buscador.
 * Esto se hace para no obtener resultados duplicados en la busqueda.
 * @type jQuery
 */
var $context = $(getClass("div-table")).filter(function (index) {
    return $(this).parent().attr("id") !== "search_items";
});

// Esta variable contiene la tabla de resultados
var $buscador = $("#search_items .div-table");

/**
 * Funcion que borra las filas extra de la tabla de busquedas
 * @returns {undefined}
 */
function delExtraRows() {
    var $rows = $(getId("search_items") + " tr").length;
    if ($rows > 4) {
        console.log("remove extra rows");
        // Optimizar con metodo gt
        for (var i = $rows; i > 4; i--) {
            $(getId("search_items") + " tr").remove("tr:last-child");
        }
    }
}// Fin funcion

/**
 * Funcion que borra todos los items resultantes de la busqueda.
 * Además añade las celdas vacías.
 * @returns {undefined}
 */
function delItems() {
    $(getId("search_items") + " " + $classItem).remove();
    // Añado celdas vacías
    $(getId("search_items" + " .cell")).filter(function (index) {
        if (!$(".div-cell", this).length) {
            $(this).append($emptyChildCell);
        }
        return true;
    });
}

/**
 * Funcion que añade los items resultantes de la busqueda a la tabla
 * y los hace draggeables
 * @param {type} $results - Objeto jQuery con los resultados
 * @returns {undefined}
 */
function addItems($results) {
    $(getId("search_items") + " .cell").each(function (index) {
        if (index + 1 <= $results.length) {
            $(this).children().remove(); // Elimina la celda vacía
            $(this).append($results.eq(index).clone());
        }
    });
    $(getId("search_items") + " " + $classItem).draggable($dragItemsFunction);
}

$(getId("search")).on("input", function () {
    var buscado = $(this).val().replace(regex, subst);
    if (!buscado) {
        delExtraRows();
        delItems();
        if ($mens.hasClass($strInvisible)) {
            $mens.removeClass($strInvisible);
            $buscador.addClass("relative");
        }
    } else {
        var $results = $context.find("input[value*=" + buscado + "]").parent();
        // Si no hay resultados muestro el mensaje
        if (!$results.length) {
            $mens.removeClass($strInvisible);
            if (!$buscador.hasClass("relative")) {
                $buscador.addClass("relative");
            }
            delExtraRows();
            delItems();
        } else {
            // Oculto el mensjae
            if (!$mens.hasClass($strInvisible)) {
                $mens.addClass($strInvisible);
                $buscador.removeClass("relative");
            }
            if ($results.length <= 36) {
                delExtraRows();
                delItems();
                addItems($results);
            } else {
                var $difference = $results.length - 36;
                var $newLength = Math.floor($difference / 9) + ($difference % 9 ? 1 : 0);
                var $html = "";
                for (var i = 0; i < $newLength; i++) {
                    $html += "<tr>";
                    for (var j = 0; j < 9; j++) {
                        $html += $emptyCell;
                    }
                    $html += "</tr>";
                }

                $(getId("search_items") + " tbody").append($html);
                addItems($results);
            }
        }
    }
});

