/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//-----------------------------------------------------------
//                     SECCIÓN ITEMS
//-----------------------------------------------------------

var $heightCell = $(".div-table tr").first().prop("offsetHeight");
var $widthCell = $(".div-table td").first().prop("offsetWidth");

/*
 * Se establece la anchura suficiente como para que se vean
 * tan solo cuatro filas de items
 */
$heightCell += 1; // Margen de error
$heightCell *= 4;
$(".div-table").css("height", $heightCell);

/**
 * Establezco el ancho del cmapo de texto del buscador
 * al de 5.5 celdas (- 3 es el margen de error)
 */
$widthCell = $widthCell * 5.5 - 3;
$(getId("search")).css("width", $widthCell);

//-----------------------------------------------------------
//              ELEMENTOS FORMULARIO
//-----------------------------------------------------------

// Ajusto de forma dinamica el tamaño de los labels
let $maxWidth = 200;


$("#dp_info fieldset label").css("width", $maxWidth);
//console.log("MAx width: "+$maxWidth);

// Ajusto el ancho del input para la descripcion y las subcarpetas
let $firstInput = $("#dp_info input[type=text]").first(),
        $borde = parseInt($firstInput.css("border-left-width")) + parseInt($firstInput.css("border-right-width")),
        $widthInput = (parseInt($firstInput.prop("scrollWidth")) - $borde * 1.5) + "px";
console.log($widthInput + " - " + $borde);
$("input[name=dp_desc]").css("width", $widthInput);
$("input[name=sub]").css("width", $widthInput);
