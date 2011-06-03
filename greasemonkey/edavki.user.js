// ==UserScript==
// @name           eDavki
// @namespace      com.tnode.mitar
// @description    Enable custom signing component for eDavki.
// @include        https://edavki.durs.si/*
// @require        https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js
// ==/UserScript==
var applet = $('#signingApplet').clone();
$('#signingApplet').remove();
applet.attr({
    'codebase': 'https://common.tnode.com/edavki/'
}).insertAfter('#objectContainer');
