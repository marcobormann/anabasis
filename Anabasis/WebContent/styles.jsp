<style type="text/css">
@CHARSET "UTF-8";
@font-face {
    font-family: "Greek";
    src: url('<%= request.getContextPath()+"/fonts/HERAKLES.TTF"%>') format("truetype");
}
p, h1, h2, input, input[type=submit], table, tr, td, a, select, button { 
    font-family: "Greek";
}
p {
  margin-top: 0em;
  margin-bottom: 0em;
}
select {
	font-size: 12pt;
}
select.games {
	font-size: 10pt;
}
option[disabled]{
	color: #000000;
}
input[type=submit] {
	background:#921519;
	color: #927E24;
	font-size: 11pt;
}
input[type=submit].menue {
	width: 10em;
}
h2.error {
	color:#921519;
}
a {
	color:#000000;
}
tr {
	vertical-align: top;
}
table.list, td.list {
	margin-top: 0em;
	margin-bottom: 0em;
	border: 1px solid black;
	font-size: 10pt;
}

td.map, tr.map  { 
	padding: 0px; 
	margin: 0px; 
	border: none;
}
table.map { 
	border-collapse: collapse; 
}

.img-container { 
	position: relative; 
}

.img-container .back {
	padding: 0px; 
	margin: 0px; 
	border: none;
	z-index: 0;
}

.img-container .top {
	padding: 0px; 
	margin: 0px; 
	border: none;
  	position: absolute;
  	top: 0;
  	z-index: 1;
}

p.player {
	color: #FFFFFF;
	font-size: 11pt;
}

p.message {
	color: #921519;
	font-size: 11pt;
}

.lock-screen {
    height: 100%;
    overflow: hidden;
    width: 100%;
    position: fixed;
}

</style>