var modal = document.getElementById("accountModal");
var btn = document.querySelector(".popup_create_admin");
var span = document.getElementsByClassName("close")[0];
var btnSubmit = document.getElementById("btn-submit");

btn.onclick = function () {
    modal.style.display = "block";
}

span.onclick = function () {
    modal.style.display = "none";
}

window.onclick = function (event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
}

btnSubmit.onclick = function () {
    var inputFields = document.querySelectorAll("#accountModal input");
    var allFilled = true;

    inputFields.forEach(function(input) {
        if (input.value.trim() === "") {
            allFilled = false;
        }
    });

    if (allFilled) {
        modal.style.display = "none";
    } else {
        modal.style.display = "block";
    }
}