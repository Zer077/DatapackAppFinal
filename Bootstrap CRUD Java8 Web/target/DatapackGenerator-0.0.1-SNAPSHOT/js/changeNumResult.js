/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Funcion para ajustar el máximo del input 
 * @param {type} input
 * @param {type} maxStack
 * @returns {undefined}
 */
function cambiarMaxInput(input, maxStack) {
    if (parseInt(input.attr("max")) !== maxStack) {
        input.attr("max", maxStack);
    }
}// Fin funcion

/**
 * Funcion que muestra o esconde la alerta de que se ha
 * alcanzado el stack máximo del item
 * @param {type} num
 * @param {type} stack
 * @returns {undefined}
 */
function showAndHideMaxStack(num, stack = $MAX_STACK) {
    if (stack === num) {
        let $max = $(getId("maxStack"));
        $(getId("maxValue")).html("<span style='color:orange'>\"" + $(getId("number-result")).attr("max") + "\"</span>");
        $max.removeClass($strInvisible);
    } else if (!$(getId("maxStack")).hasClass($strInvisible)) {
        $(getId("maxStack")).addClass($strInvisible);
}
}// Fin funcion

// Dialogo para establecer el stack
var $dialogVar = $("#countResult").dialog($dialogStack);

// Funcion para mostrar el dialogo para establecer el stack
$($classResDiv).mousedown(function () {
    if (event.button === $RIGHT_BUTTON &&
            $($classResDiv).children().length > 0) {
        $dialogVar.removeClass($strInvisible);
        $dialogVar.dialog("open");
    }
});

/*
 * Funcion para establecer el stack del objeto resultante
 */
$(getId("number-result")).on("input", function () {
    var num = $(this).val();
    var stack = $($classResItem + " " + "input[name=stackSize]");
    if (num) {
        num = parseInt(num);
        if (stack.length > 0) {
            stack = parseInt(stack.val());
            cambiarMaxInput($(this), stack);
            showAndHideMaxStack(num, stack);
        } else {
            cambiarMaxInput($(this), $MAX_STACK);
            showAndHideMaxStack(num);
        }
    }
});

