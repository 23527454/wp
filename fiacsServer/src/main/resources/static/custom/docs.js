
function updateBorders(color) {
    var hexColor = "transparent";
    if(color) {
        hexColor = color.toHexString();
    }
    $("#docs-content").css("border-color", hexColor);
}

$(function() {


$("#hideButtons").spectrum({
    showButtons: false,
    change: updateBorders
});


var isDisabled = true;
$("#toggle-disabled").click(function() {
    if (isDisabled) {
        $("#disabled").spectrum("enable");
    }
    else {
        $("#disabled").spectrum("disable");
    }
    isDisabled = !isDisabled;
    return false;
});

$("input:disabled").spectrum({

});
$("#disabled").spectrum({
    disabled: true
});

$("#pick1").spectrum({
    flat: true,
    change: function(color) {
        var hsv = color.toHsv();
        var rgb = color.toRgbString();
        var hex = color.toHexString();
        //console.log("callback",color.toHslString(), color.toHsl().h, color.toHsl().s, color.toHsl().l)
        $("#docs-content").css({
            'background-color': color.toRgbString()
        }).toggleClass("dark", hsv.v < .5);
        $("#switch-current-rgb").text(rgb);
        $("#switch-current-hex").text(hex);
    },
    show: function() {

    },
    hide: function() {

    },
    showInput: true,
    showPalette: true,
    palette: ['white', '#306', '#c5c88d', '#ac5c5c', '#344660']
});

$("#collapsed").spectrum({
    color: "#dd3333",
    change: updateBorders,
    show: function() {

    },
    hide: function() {

    }
});

$("#flat").spectrum({
    flat: true,
    showInput: true,
    move: updateBorders
});

$("#flatClearable").spectrum({
    flat: true,
    move: updateBorders,
    change: updateBorders,
    allowEmpty:true,
    showInput: true
});

$("#showInput").spectrum({
    color: "#dd33dd",
    showInput: true,
    change: updateBorders,
    show: function() {

    },
    hide: function() {

    }
});

$("#showAlpha").spectrum({
    color: "rgba(255, 128, 0, .5)",
    showAlpha: true,
    change: updateBorders
});

$("#showAlphaWithInput").spectrum({
    color: "rgba(255, 128, 0, .5)",
    showAlpha: true,
    showInput: true,
    showPalette: true,
    palette: [
        ["rgba(255, 128, 0, .9)", "rgba(255, 128, 0, .5)"],
        ["red", "green", "blue"],
        ["hsla(25, 50, 75, .5)", "rgba(100, .5, .5, .8)"]
    ],
    change: updateBorders
});

$("#showAlphaWithInputAndEmpty").spectrum({
    color: "rgba(255, 128, 0, .5)",
    allowEmpty:true,
    showAlpha: true,
    showInput: true,
    showPalette: true,
    palette: [
        ["rgba(255, 128, 0, .9)", "rgba(255, 128, 0, .5)"],
        ["red", "green", "blue"],
        ["hsla(25, 50, 75, .5)", "rgba(100, .5, .5, .8)"]
    ],
    change: updateBorders
});

$("#showInputWithClear").spectrum({
    allowEmpty:true,
    color: "",
    showInput: true,
    change: updateBorders,
    show: function() {

    },
    hide: function() {

    }
});

$("#openWithLink").spectrum({
    color: "#dd3333",
    change: updateBorders,
    show: function() {

    },
    hide: function() {

    }
});

$("#className").spectrum({
    className: 'awesome'
});

$("#replacerClassName").spectrum({
    replacerClassName: 'awesome'
});

$("#containerClassName").spectrum({
    containerClassName: 'awesome'
});

$("#showPalette").spectrum({
    showPalette: true,
    palette: [
        ['black', 'white', 'blanchedalmond'],
        ['rgb(255, 128, 0);', 'hsv 100 70 50', 'lightyellow']
    ],
    change: updateBorders
});

var textPalette = ["rgb(255, 255, 255)", "rgb(204, 204, 204)", "rgb(192, 192, 192)", "rgb(153, 153, 153)", "rgb(102, 102, 102)", "rgb(51, 51, 51)", "rgb(0, 0, 0)", "rgb(255, 204, 204)", "rgb(255, 102, 102)", "rgb(255, 0, 0)", "rgb(204, 0, 0)", "rgb(153, 0, 0)", "rgb(102, 0, 0)", "rgb(51, 0, 0)", "rgb(255, 204, 153)", "rgb(255, 153, 102)", "rgb(255, 153, 0)", "rgb(255, 102, 0)", "rgb(204, 102, 0)", "rgb(153, 51, 0)", "rgb(102, 51, 0)", "rgb(255, 255, 153)", "rgb(255, 255, 102)", "rgb(255, 204, 102)", "rgb(255, 204, 51)", "rgb(204, 153, 51)", "rgb(153, 102, 51)", "rgb(102, 51, 51)", "rgb(255, 255, 204)", "rgb(255, 255, 51)", "rgb(255, 255, 0)", "rgb(255, 204, 0)", "rgb(153, 153, 0)", "rgb(102, 102, 0)", "rgb(51, 51, 0)", "rgb(153, 255, 153)", "rgb(102, 255, 153)", "rgb(51, 255, 51)", "rgb(51, 204, 0)", "rgb(0, 153, 0)", "rgb(0, 102, 0)", "rgb(0, 51, 0)", "rgb(153, 255, 255)", "rgb(51, 255, 255)", "rgb(102, 204, 204)", "rgb(0, 204, 204)", "rgb(51, 153, 153)", "rgb(51, 102, 102)", "rgb(0, 51, 51)", "rgb(204, 255, 255)", "rgb(102, 255, 255)", "rgb(51, 204, 255)", "rgb(51, 102, 255)", "rgb(51, 51, 255)", "rgb(0, 0, 153)", "rgb(0, 0, 102)", "rgb(204, 204, 255)", "rgb(153, 153, 255)", "rgb(102, 102, 204)", "rgb(102, 51, 255)", "rgb(102, 0, 204)", "rgb(51, 51, 153)", "rgb(51, 0, 153)", "rgb(255, 204, 255)", "rgb(255, 153, 255)", "rgb(204, 102, 204)", "rgb(204, 51, 204)", "rgb(153, 51, 153)", "rgb(102, 51, 102)", "rgb(51, 0, 51)"];

$("#showPaletteOnly").spectrum({
    color: 'blanchedalmond',
    showPaletteOnly: true,
    showPalette:true,
    palette: [
        ['black', 'white', 'blanchedalmond',
        'rgb(255, 128, 0);', 'hsv 100 70 50'],
        ['red', 'yellow', 'green', 'blue', 'violet']
    ]
});

$("#clickoutFiresChange").spectrum({
    clickoutFiresChange: true,
    change: updateBorders
});

$("#clickoutDoesntFireChange").spectrum({
    change: updateBorders
});

$("#showInitial").spectrum({
    showInitial: true
});

$("#showInputAndInitial").spectrum({
    showInitial: true,
    showInput: true
});

$("#showInputInitialClear").spectrum({
    allowEmpty:true,
    showInitial: true,
    showInput: true
});

$("#changeOnMove").spectrum({
    move: function(c) {
        var label = $("#changeOnMoveLabel");
        label.text("Move called: " + c.toHexString());
    }
});
$("#changeOnMoveNo").spectrum({
    showInput: true,
    change: function(c) {
        var label = $("#changeOnMoveNoLabel");
        label.text("Change called: " + c.toHexString());
    }
});

function prettyTime() {
    var date = new Date();

    return date.toLocaleTimeString();
}

$("#eventshow").spectrum({
    show: function(c) {
        var label = $("#eventshowLabel");
        label.text("show called at " + prettyTime() + " (color is " + c.toHexString() + ")");
    }
});

$("#eventhide").spectrum({
    hide: function(c) {
        var label = $("#eventhideLabel");
        label.text("hide called at " + prettyTime() + " (color is " + c.toHexString() + ")");
    }
});

$("#eventdragstart").spectrum({
    showAlpha: true
}).on("dragstart.spectrum", function(e, c) {
    var label = $("#eventdragstartLabel");
    label.text("dragstart called at " + prettyTime() + " (color is " + c.toHexString() + ")");
});

$("#eventdragstop").spectrum({
    showAlpha: true
}).on("dragstop.spectrum", function(e, c) {
    var label = $("#eventdragstopLabel");
    label.text("dragstop called at " + prettyTime() + " (color is " + c.toHexString() + ")");
});


$(".basic").spectrum({ change: updateBorders });
$(".override").spectrum({
    color: "yellow",
    change: updateBorders
});

$(".startEmpty").spectrum({
    allowEmpty:true,
    change: updateBorders});

$("#beforeShow").spectrum({
    beforeShow: function() {
        return false;
    }
});


$("#custom").spectrum({
    color: "#f00"
});

$("#buttonText").spectrum({
    allowEmpty:true,
    chooseText: "Alright",
    cancelText: "No way"
});


$("#showSelectionPalette").spectrum({
    showPalette: true,
    showSelectionPalette: true, // true by default
    palette: [ ]
});
$("#showSelectionPaletteStorage").spectrum({
    showPalette: true,
    localStorageKey: "spectrum.homepage", // Any picker with the same string will share selection
    showSelectionPalette: true,
    palette: [ ]
});
$("#showSelectionPaletteStorage2").spectrum({
    showPalette: true,
    localStorageKey: "spectrum.homepage", // Any picker with the same string will share selection
    showSelectionPalette: true,
    palette: [ ]
});


$("#preferredHex").spectrum({
    preferredFormat: "hex",
    showInput: true,
    showPalette: true,
    palette: [["red", "rgba(0, 255, 0, .5)", "rgb(0, 0, 255)"]]
});
$("#preferredHex3").spectrum({
    preferredFormat: "hex3",
    showInput: true,
    showPalette: true,
    palette: [["red", "rgba(0, 255, 0, .5)", "rgb(0, 0, 255)"]]
});
$("#preferredHsl").spectrum({
    preferredFormat: "hsl",
    showInput: true,
    showPalette: true,
    palette: [["red", "rgba(0, 255, 0, .5)", "rgb(0, 0, 255)"]]
});
$("#preferredRgb").spectrum({
    preferredFormat: "rgb",
    showInput: true,
    showPalette: true,
    palette: [["red", "rgba(0, 255, 0, .5)", "rgb(0, 0, 255)"]]
});
$("#preferredName").spectrum({
    preferredFormat: "name",
    showInput: true,
    showPalette: true,
    palette: [["red", "rgba(0, 255, 0, .5)", "rgb(0, 0, 255)"]]
});
$("#preferredNone").spectrum({
    showInput: true,
    showPalette: true,
    palette: [["red", "rgba(0, 255, 0, .5)", "rgb(0, 0, 255)"]]
});

$("#triggerSet").spectrum({
    change: updateBorders
});

// Show the original input to demonstrate the value changing when calling `set`
$("#triggerSet").show();

$("#btnEnterAColor").click(function() {
    $("#triggerSet").spectrum("set", $("#enterAColor").val());
});

$("#toggle").spectrum();
$("#btn-toggle").click(function() {
    $("#toggle").spectrum("toggle");
    return false;
});


$('#toc').toc({
    'selectors': 'h2,h3', //elements to use as headings
    'container': '#docs', //element to find all selectors in
    'smoothScrolling': true, //enable or disable smooth scrolling on click
    'prefix': 'toc', //prefix for anchor tags and class names
    'highlightOnScroll': true, //add class to heading that is currently in focus
    'highlightOffset': 100, //offset to trigger the next headline
    'anchorName': function(i, heading, prefix) { //custom function for anchor name
        return heading.id || prefix+i;
    }
});



});
