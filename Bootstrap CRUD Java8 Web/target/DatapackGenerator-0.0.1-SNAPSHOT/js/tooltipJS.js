/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// Variable que sirve para almacenar las clases de los items especiales
var $specialClass = undefined;

// Variable que sirve para almacenar el elemento sobre el que estoy
$object = undefined;

/**
 * Funcion que obtiene el nombre del elemento sobre el que estoy
 * @returns {unresolved}
 */
function getName() {
    return $object.find("input[name='name']").attr("value");
}

/**
 * Funcion que muestra la informacion sobre la diferencia
 * entre establecer o no el satck al item resultante
 */
$(".info-count").tooltip({
    position: {
        my: "left+20 bottom+50",
        of: $(this)
    },
    track: true
});

if ($("#aux").hasClass($strBtnDisabled)) {
    $(getClass($strBtnDisabled)).tooltip({
        position: {
            my: "left+20 bottom+50",
            of: $(this)
        },
        track: true
    });
}

/**
 * Funcion que muestra los tooltips de los items
 * @type type
 */
$(document).tooltip({
    /*
     * Esta funcion se ejecuta despues de content(), 
     * por lo que aniado la clase correspondiente
     * a la frecuencia del item si éste no es comun
     */
    open: function (event, ui) {
        if ($specialClass) {
            ui.tooltip.addClass($specialClass);
        }
    },
    classes: {
        "ui-widget": "tooltip"
    },
    items: "button, .item",
    content: function () {
        $object = $(this);
        // Si estoy sobre los botones de categorias
        if ($object.is("button")) {
            $specialClass = undefined; // Reinicio la variable
            $id = $object.attr("class").split(" ")[0];
            $id = getId($id.substr($id.indexOf("-") + 1));
            return $($id + ">" + $classTitulo).text();
        } else if ($object.is("div")) {
            // Tooltip para las pociones
            if ($object.find("[name*='effect_']").length) {
                var $txt = showItemName(getName());

                $object.find("[name*='effect_']").each(function () {
                    let $index = $(this).attr('name'), $class = "";
                    $index = $index.substr($index.indexOf('-'));
                    $class = $object.find("[name*='state_" + $index + "']").val();
                    $txt += "<span class='filler'></span><span class='" + $class + "'>" + $(this).val() + "</span>";
                });
                return $txt;
            }

            //Obtengo la frecuencia del item
            if ($object.hasClass("uncommon")) {
                $specialClass = "uncommon";
            } else if ($object.hasClass("rare")) {
                $specialClass = "rare";
            } else if ($object.hasClass("epic")) {
                $specialClass = "epic";
            } else {// Reinicio la variable
                $specialClass = undefined;
            }

            // Establezco el nombre del item como contenido del tooltip
            $id = getName();
            return showItemName($id);
        }
    },
    position: {
        my: "left+10 bottom+50",
        of: $object
    },
    track: true
});

