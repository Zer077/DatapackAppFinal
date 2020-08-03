/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Funcion que oculta la antigua categoria seleccionada y hace visible a la nueva
 * @param {type} catSelected - Nueva categoria seleccionada
 * @param {type} classToAdd - Clase a añadir si ningun elemnento ya la tenia anteriormentes
 * @param {type} classToDel - Clase a remover si algun elemento ya la tenia anteriormente
 */
selectCat = function (catSelected, classToAdd, classToDel) {
    // Solo actuo si la categoría seleccionada no lo estaba anteriormente
    if (!catSelected.hasClass($strSelectUp) || !catSelected.hasClass($strSelectDown)) {
        var $id = catSelected.get(0).classList.item(0);// La primera clase hace referencia a la categoria (cat-XXX)

        // Elimino la antigua categoria seleccionada
        $(getClass(classToDel)).toggleClass(classToDel);
        $(getClass(classToAdd)).toggleClass(classToAdd);
        $(getClass($strVisible)).toggleClass($strVisible).toggleClass($strInvisible);

        // Selecciono la nueva categoria
        $(getClass($id)).toggleClass(classToAdd);
        $id = $id.substr($id.indexOf("-") + 1);
        $(getId($id)).toggleClass($strInvisible).toggleClass($strVisible);
    }
};// Fin funcion

$(".categoria").click(function () {
    // Obtengo las clases de la categoría seleccionada
    var $cat = $(this);

    // Actuo en funcion de la posicion de la categoria
    if ($cat.hasClass($strUpCats)) {// Superiores
        selectCat($cat, $strSelectUp, $strSelectDown);
    } else if ($cat.hasClass($strDownCats)) {// Inferiores
        selectCat($cat, $strSelectDown, $strSelectUp);
    }
});

