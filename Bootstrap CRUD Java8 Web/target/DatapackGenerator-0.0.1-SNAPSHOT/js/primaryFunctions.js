/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CONSTANTES
 */

/*MOUSE EVENTS*/
//--------------------------------------------------//
const $MAX_STACK = 64, // INDICA EL VALOR MÁXIMO QUE PUEDE ALCANZAR EL STACK DE LOS ITEMS
        $RIGHT_BUTTON = 2;  // INDICA EL VALOR DEL BOTON DERECHO EN EVENTOS DE RATON
//--------------------------------------------------//

/*DRAG AND DROP EVENTS*/
//--------------------------------------------------
const $dragHelperPos = {left: -10, top: -10}; // ESTABLECE LA POSICION DEL HELPER CON RESPECTO AL MOUSE

// OBJETO QUE ESTABLECE EL COMPORTAMIENTO DE LA FUNCION DRAG
const $dragItemsFunction = {
    addClasses: false,
    containment: "document",
    helper: "clone", //Crea un clon del objeto y sobre este se hace el drag
    cursorAt: $dragHelperPos,
    start: function (event, ui) {
        //Elimino el evento de tooltip
        $(document).tooltip('disable');
        //Elimino el fondo del helper
        ui.helper.removeClass("bordes").addClass($strNoBack);
    },
    stop: function (event, ui) {
        //Vuelvo a asignar el evento tooltip
        $(document).tooltip('enable');
    }
};
//--------------------------------------------------//

/*DIALOGS*/
//--------------------------------------------------//
const $dialogStack = {
    autoOpen: false,
    draggable: false,
    resizable: false,
    modal: true,
    title: "Set the stack",
    open: function (event, ui) {
        // Si se define "buttons" siempre es el último hijo
        $(this).parent().children(":last-child").css("background", "unset");

        // Hago que el boton esta desactivado por defecto
        let $btnDelete = $(this).parent().find("button:contains('Delete')"),
                $classes = $strNoPointer + " " + $strBtnDisabled;
        $btnDelete.prop("disabled", true).addClass($classes);

        // Si hay algun stack activo el boton
        if ($(".stack").length + $(".stack_ge_10").length) {
            //console.log("CON stack");
            $btnDelete.prop("disabled", false).removeClass($classes);
        }
    },
    buttons: {
        "Set number": function () {
            // Establece el stack al item y cierra el dialogo
            var $number = $(getId("number-result")).val(),
                    $class = $number.length > 1 ? 'stack_ge_10' : 'stack',
                    $target = $($classResItem + " " + getClass("div-cell"));
            if ($target.find("span").length) {
                $target.find("span").remove();
            }
            $target.append("<span class='" + $class + "'>" + $number + "</span>");
            $(this).dialog("close");
        },
        "Delete": function () {
            // Elimina el stack del item y cierra el dialogo
            if ($(".stack").length) {
                $(".stack").remove();
            }
            if ($(".stack_ge_10").length) {
                $(".stack_ge_10").remove();
            }
            $(this).dialog("close");
        }
    }
}, $dialogError = {
    autoOpen: false,
    draggable: false,
    resizable: false,
    modal: true,
    title: "Error!",
    open: function (event, ui) {
        let $padre = $(this).parent();
        $padre.css("background-color", "red");
        // El primer hijo siempre es la cabecera
        $padre.children(":first-child").css({"background-color": "orange",
            "border": "unset"});
    }
};
//--------------------------------------------------//

/*CLASSES*/
const $strDragging = "ui-draggable-dragging",
        $strResultItem = "result-item",
        $strResultDiv = "result-div",
        $strNoBack = "no-back",
        $strUpCats = "upCats",
        $strDownCats = "downCats",
        $strSelectUp = "selectUp",
        $strSelectDown = "selectDown",
        $strVisible = "visible",
        $strInvisible = "invisible",
        $strItem = "item",
        $strTitulo = "titulo",
        $strNoPointer = "ban-pointer",
        $strBtnDisabled = "btn-disabled";
const $classResDiv = getClass($strResultDiv),
        $classResItem = getClass($strResultItem),
        $classNoBack = getClass($strNoBack),
        $classItem = getClass($strItem),
        $classTitulo = getClass($strTitulo),
        $classErrorInst = getClass("error-inst");
/*
 * VARIABLES
 */
var $isRight = false, // INDICA SI EL BOTON DERECHO ESTA PULSADO O NO
        $DIV = $($classResDiv);
/**
 * Funcion que obtiene la expresion que se corresponde con una clase CSS
 * 
 * @param {type} nameClass - Nombre de la clase
 * @returns {String}
 */
function getClass(nameClass) {
    return "." + nameClass;
}// Fin funcion

/**
 * Funcion que obtiene la expresion que se corresponde con un id CSS
 * 
 * @param {type} nameId - Nombre del id
 * @returns {String}
 */
function getId(nameId) {
    return "#" + nameId;
}// Fin funcions


$("#shape").change(function () {
    if (!$(this).prop("checked")) {
        StrikeThrough($("#on"), $("#off"), 0);
        $(this).prop("checked", false);
    } else {
        StrikeThrough($("#off"), $("#on"), 0);
        $(this).prop("checked", true);
    }
});

/**
 * Funcion que tacha una opcion y borra la linea que tacha
 * la otra opcion del switch 
 * 
 * @param {type} optOn
 * @param {type} optOff
 * @param {type} index
 * @returns {undefined}
 */
function StrikeThrough(optOn, optOff, index) {
    var optOffTxt = optOn.text(),
            optOnTxt = optOff.text(),
            txtToAct = optOnTxt.substr(-(index + 1)),
            txtStillDel = optOnTxt.substr(0, optOnTxt.length - (index + 1)),
            sToStrike = optOffTxt.substr(0, index + 1),
            sAfter =
            (index < (optOffTxt.length - 1)) ? optOffTxt.substr(index + 1, optOffTxt.length - index) : "";
    optOff.html("<del>" + txtStillDel + "</del>" + txtToAct);
    optOn.html("<del>" + sToStrike + "</del>" + sAfter);
    if (sAfter || txtStillDel) {
        window.setTimeout(function () {
            StrikeThrough(optOn, optOff, index + 1);
        }, 30);
    }

    optOn.addClass("desactivated");
    optOff.removeClass("desactivated");
}// Fin funcion

/**
 * Funcion que copia los valores revelantes de los items que se encuentran
 * en la mesa de crafteo y los inserta dentro del formulario.
 * Tambien crea un campo oculto para la descripcion del datapack.
 * 
 * @returns {undefined}
 */
function prepareToSend() {
    var $opTag = "<",
            $clTag = ">",
            $htmlEle = "input type='hidden' ",
            $info;
    $(".mesa-craft .item").each(function () {
        var $id = $(this).parent().attr("id"),
                $name = "name='" + ($id ? $id : $strResultItem);
        //console.log($(this).find("[name='type']"));
        //console.log($name);
        $info = $opTag + $htmlEle + $name + "_name'" + " value='" +
                $(this).find("[name='name']").val() + "'" + $clTag;
        if ($(this).find("[name='type']").length) {
            $info += $opTag + $htmlEle + $name + "_type'" + " value='" +
                    $(this).find("[name='type']").val() + "'" + $clTag;
        }

        $($info).insertBefore("#aux");
    });

    var $stack = $($classResItem + " span");
    if ($stack.length && $stack.text() != 1) {
        $info = $opTag + $htmlEle + "name='count' value='" + $stack.text() + "'" + $clTag;
        $($info).insertBefore("#aux");
    }

    $("input[name=dp_desc]").val($("input[name=dp_desc]").attr("placeholder"));
    console.log("DESC: " + $("input[name=dp_desc]").val());
}// Fin funcion

function sendDP() {
    prepareToSend();
    $("#inst").submit();
}

/**
 * Funcion que muestra un dialogo cuando hay un error con la receta
 * @returns {undefined}
 */
function showError() {
    $dialError = $($classErrorInst).dialog($dialogError);
    $dialError.dialog("open");
    $($classErrorInst).css("min-height");
}

function sendForm(event) {
    //console.log(this);
    var $dialError;
    let $isDownload = false;
    if ($(this).attr("id") == "down") {
        $isDownload = true;
    }
    /*
     * Si el boton tiene la clase que indica que esta 
     * deshabilitado no se hace nada
     */
    if ($(this).hasClass($strBtnDisabled)) {
        return false;
    }
    if (!$(".worldChooser").hasClass($strInvisible) && !$isDownload) {
        $(".worldChooser").addClass($strInvisible);
    }
    if (!$(".result-item").length) {
        $($classErrorInst).empty().append("The result item has not been indicated").removeClass($strInvisible);
        showError($dialError);
        event.preventDefault();
    } else if (!$(".mesa-craft table .item").length) {
        $($classErrorInst).empty().append("The recipe has not been indicated.").removeClass($strInvisible);
        showError($dialError);
        event.preventDefault();
    } else {
        $($classErrorInst).empty().addClass($strInvisible);
        if ($isDownload) {
            changeAction();
            sendDP();
        } else if ($(".worldChooser").hasClass($strInvisible)) {
            $(".worldChooser").removeClass($strInvisible);
            $(".worldChooser").dialog({
                modal: true
            });
            //console.log($(".worldChooser").parent().find(":first-child .ui-dialog-title"));
            $(".worldChooser").parent().find(":first-child .ui-dialog-title").addClass("wc-title");
            $(".worldChooser li").click(function (event) {
                console.log("Mundo: " + $(this).text());
                if (!$("#inst #selectWorld").length) {
                    $("<input type='hidden' name='world' value='" + $(this).text() + "'>").insertBefore("#aux");
                    sendDP();
                }
            });
        }
        event.preventDefault();
    }
    //event.preventDefault();
}// Fin funcion

/**
 * Funcion que cambia la accion por defecto del formulario
 * para que pueda ddecargar el datapack
 * @returns {undefined}
 */
function changeAction() {
    $("#inst").attr("action", "/download");
    sendDP();
}

$("#aux").click(sendForm);
$("#down").click(sendForm);
var $namespace = $("input[name=namespace]"),
        $recipeFile = $("input[name=rec_name]"),
        $defaultMsg = $("input[name=dp_name]").attr("placeholder");
$("input[name=dp_name]").on("input",
        function () {
            console.log($(this).val());
            var $txt = $(this).val();
            if ($txt == "") {
                $txt = $defaultMsg;
            }
            $namespace.attr("placeholder", $txt);
            $recipeFile.attr("placeholder", $txt);
        });

/**
 * Funcion que formate y muestra el nombre del objeto en el tooltip
 * 
 * @param {type} $name - Nombre del item
 * @returns {unresolved}
 */
function showItemName($name) {
    $name = $name.charAt(0).toUpperCase() + $name.slice(1);
    $name = $name.replace(/_/g, ' ');
    return $name;
}

/**
 * Funcion que actualiza el placeholder del campo referido a la
 * descripcion del datapack.
 * Si no se pasa el parámetro reinicia el placeholder.
 * 
 * @param {type} $item - El item resultante
 * @returns {undefined}
 */
function refreshPHDesc($item) {
    var $desc = $("input[name=dp_desc]"),
            $text = $desc.attr("placeholder"),
            $regexpFirstTime = /{[^)]*}/,
            $regexpOther = /(?:for )(.+)$/;
    if ($item === undefined) {
        $desc.attr("placeholder", $text.replace($text.match($regexpOther)[1], "{$RESULT_ITEM}"));
    } else {
        if ($regexpFirstTime.test($text)) {
            $desc.attr("placeholder", $text.replace($regexpFirstTime, showItemName($item)));
        } else {
            $desc.attr("placeholder", $text.replace($text.match($regexpOther)[1], showItemName($item)));
        }
    }
}// Fin funcion






