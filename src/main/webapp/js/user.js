let validateButton = document.getElementById("validateButton");
validateButton.addEventListener("click", toIndex);

function toIndex(){
    location.href = 'http://localhost:8080/proj1/html/index.html';
}

let password = document.getElementById("showPassword");
password.addEventListener("click", showPassword);

function showPassword() {
    var pw = document.getElementById("password");
    if (pw.style.display === "none") {
        pw.style.display = "inline";
    } else {
        pw.style.display = "none";
    }
}
