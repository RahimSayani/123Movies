/**
 * 
 */

function submitSelection(name) {
	var submit = document.createElement("input");
	submit.type = "submit";
	submit.name = "action";
	submit.value = "film-" + name;
	submit.style = "display: none;";
	
	var form = document.getElementById("filmForm");
	form.appendChild(submit);
	submit.click();
}

function addClickListener() {
	var film = document.getElementsByClassName("film");
	
	for (var i = 0; i < film.length; i++) {
		film[i].addEventListener("click", function() {
			submitSelection(this.name); // Pass the name when clicked
		});
	}
}

document.addEventListener("DOMContentLoaded", function() {
	addClickListener();
});

