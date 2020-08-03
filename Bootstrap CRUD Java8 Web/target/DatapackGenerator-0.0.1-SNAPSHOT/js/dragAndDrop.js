/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var $draggingResult = false,
        $firstTime = true;

/*FUNCIONES*/
$(document).mousedown(function (event) {
    if (event.button === $RIGHT_BUTTON) {
        $isRight = true;
    }
});

$(document).mouseup(function (event) {
    if (event.button === $RIGHT_BUTTON) {
        $isRight = false;
    }
});

function mostrarNota() {
    $(".info-count").removeClass($strInvisible);
}

function esconderNota() {
    $(".info-count").addClass($strInvisible);
}

/**
 * Funcion que permite borrar los elementos arrastadros hasta la papelera.
 * Además, modifica la imagen de la papelera cuando el puntero está sobre ella
 * 
 * @param {type} event
 * @param {Boolean} enter 
 * @returns {undefined}
 */
function deleteItem(event, enter) {
    const $empty = "garbage.svg", $full = "full-trash.svg";
    var $img = $("#trash").find("img"),
            $height = parseInt($img.prop("offsetHeight")),
            $src = "img/";

    if (enter) {
        $src += $full;
    } else {
        $src += $empty;
    }
    $img.attr("src", $src);
    if ($firstTime) {
        $img.attr("height", $height + 10);
        $("#trash").css("margin-left", "-10px");
        $firstTime = false;
    } else {
        $img.attr("height", $height - 10);
        $("#trash").css("margin-left", "0px");
        $firstTime = true;
    }
}// Fin funcion

/**
 * Funcion que actua cuando el puntero esta sobre la papelera
 * @param {type} event
 * @returns {undefined}
 */
function deleteItemOver(event) {
    deleteItem(event, true);
}

/**
 * Funcion que actua cuando el puntero sale de la papalera o 
 * cuando se suelta el objeto encima de ella
 * @param {type} event
 * @returns {undefined}
 */
function deleteItemOut(event) {
    deleteItem(event, false);
}

/**
 * Elimino el valor del stack y la imagen de que el objeto no es stackeable
 * @param {type} helper
 * @returns {undefined}
 */
function deleteStack(helper) {
    var $stack = helper.find(".stack");
    var $noStack = helper.find(".no-stack");
    if ($stack.length) {
        $stack.remove();
    }
    if ($noStack.length) {
        $noStack.remove();
    }
}

/**
 * Funcion establece la funcionalidad de la accion DRAG 
 * sobre los objetos de la mesa de crafteo y del resultado
 */
var $dragFuntion = {
    addClasses: false,
    containment: "document",
    revert:
            function (event) {
                console.log(event);
                if (event) {
                    $draggingResult = true;
                }
                return !event;
            },
    cursorAt: $dragHelperPos,
    start: function (event, ui) {
        console.log(ui);
        if (event.target.classList.contains($strResultItem)) {
            $draggingResult = true;
            esconderNota();
        }
        //Elimino el evento de tooltip
        $(document).tooltip('disable');
        /*
         *Escondo el valor del stack y la imagen de que el objeto
         * no es stackeable
         */
        var $stack = ui.helper.find(".stack");
        if ($stack.length) {
            $stack.addClass($strInvisible);
        }
        var $noStack = ui.helper.find(".no-stack");
        if ($noStack.length) {
            $noStack.addClass($strInvisible);
        }

    },
    stop: function (event, ui) {
        /*Vuelvo a asignar el evento tooltip*/
        $(document).tooltip('enable');
        if ($draggingResult) {
            ui.helper.find(".stack").removeClass($strInvisible);
            ui.helper.find(".no-stack").removeClass($strInvisible);
            mostrarNota();
            $draggingResult = false;
        }
    }
};

var $dropFunction = {
    addClasses: false, // No aniado las clases por defecto de JQ-UI
    accept: ".item, .no-back",
    tolerance: "pointer", // Establezco la tolerancia (la forma como se detecta al elemento al que se le puede hacer Drag sobre un elemento en el que see puede hacer Drog)
    /*
     * Funcion que se ejecuta cuando se hace drop (se suelta el elemento)
     */
    drop: function (event, ui) {
        var $id = event.target.id;
        var $object = ui.helper.clone();
        /*
         * Las celdas de la mesa de crafteo tiene un id numerico
         * que puede ser del 1 al 9, mientras que el objeto resultante
         * solo tiene una clase ("div-result")
         */
        $object.removeClass($strDragging);
        if ($id == "") {
            // Borro el hijo, si es que hay alguno
            $($classResDiv).children().last().remove();
            var $resItem = $classResDiv + ">" + $classItem;
            if (ui.helper.prevObject === undefined) {
                console.log("pase0");
                $($classResDiv).append(ui.helper);
                $($resItem).attr('style', 'position:relative;').addClass($strResultItem);
            } else {
                console.log("pase1");
                $($classResDiv).append($object);
                $($resItem).removeClass($strDragging);
                $($resItem).attr('style', '').addClass($strResultItem);
                var $stack = $($classResItem + ">input[name=stackSize]");
                if ($stack.length) {
                    if ($stack.prop("value") == 1) {
                        $stack.parent().append("<div class='no-stack'><img src='./img/NO_STACKABLE.png' width='40px'></div>");
                    }
                }
            }

            refreshPHDesc($object.find("input").val());
            mostrarNota();
            $($classResItem).addClass("erasable");
            $($classResItem).draggable($dragFuntion);

            /*
             * Esta funcion permite volver a arrastar el result item
             * en caso de que se arrastre (desde el result div!) y se 
             * suelte en el result div
             */
            setTimeout(function () {
                $($classResItem).draggable($dragFuntion);
            }, 1);
        } else {
            console.log("antes");
            $id = getId($id);
            // Borro el hijo, si es que hay alguno
            $($id).children().last().remove();
            if (ui.helper.prevObject === undefined) {
                console.log("pas21");
                $($id).append(ui.helper);
                // Borro el estilo para que se muestre correctamente
                $($id + ">div").attr("style", "position:relative;").removeClass($strResultItem);
                $($id + ">div").addClass("erasable");
            } else {
                console.log("pase3");
                // Añado el clon del helper
                $($id).append($object);
                $($id + ">" + $classItem).attr('style', '');
                $($id + ">" + $classItem).addClass("erasable");
            }

            deleteStack(ui.helper);
            /*
             * Permito que los objetos se puedan arrastrar dentro de la mesa de
             * crafteo y pasarlo al resultado
             */
            setTimeout(function () {
                $(".drop .cell " + $classNoBack).draggable($dragFuntion);
                esconderNota();
            }, 1);
        }
        /*EXTRAER COMO METODO*/
        // Reinicio el input del numero de objetos resultantes
        $(getId("number-result")).val(1);
        var defectWAR = $(getId("defect"));
        if (defectWAR.hasClass($strInvisible)) {
            defectWAR.removeClass($strInvisible);
        }
        var maxWAR = $(getId("maxStack"));
        if (!maxWAR.hasClass($strInvisible)) {
            maxWAR.addClass($strInvisible);
        }
        //-----------------------------------------
    },

    over: function (event, ui) {
        /*
         * Si el boton derecho esta pulsado elimino el objeto que 
         * hubiera en la celda de la mesa de crafteo y añado una 
         * copia del objeto que estoy arrastrando
         * */
        if ($isRight) {
            if (event.target.id !== "") {
                var $id = getId(event.target.id);
                $($id).children().last().remove();
                deleteStack(ui.helper);
                $($id).append(ui.helper.removeClass($strResultItem).clone());
                // Borro el estilo para que se muestre correctamente
                $($id + ">div").attr("style", "position:relative;");
                $($id + ">" + $classItem).removeClass($strDragging).addClass("erasable");
                $(".drop .cell " + $classNoBack).draggable($dragFuntion);
            }
        }
    }// Fin funcion

};// Fin var drog

$(".drop .div-cell").droppable($dropFunction);
$($classResDiv).droppable($dropFunction);
$("#trash").droppable({
    addClasses: false,
    accept: ".erasable",
    tolerance: "pointer",
    drop: function (event, ui) {
        if (ui.helper.hasClass($strResultItem)) {
            refreshPHDesc();
        }
        ui.helper.remove();
        deleteItemOut(event);
    },
    over: deleteItemOver,
    out: deleteItemOut
});

$(".drag " + $classItem).draggable($dragItemsFunction);
