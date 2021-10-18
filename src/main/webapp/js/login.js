window.onload = function(){
    checkSession();
}
function checkSession(){
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function(){
        if(xhttp.readyState == 4 && xhttp.status == 200){
            let userInfo = JSON.parse(xhttp.responseText);
            if(userInfo.wrongcreds){
                let alertText = document.createTextNode("Incorrect Credentials. Please check you username and password and try again.");
                let alertButton = document.createElement("button");
                alertButton.setAttribute("class", "close");
                alertButton.setAttribute("type", "button");
                alertButton.setAttribute("data-dismiss", "alert");
                alertButton.setAttribute("aria-label", "Close");
                let alertSpan = document.createElement("span");
                alertSpan.setAttribute("aria-hidden", "true");
                alertSpan.innerHTML="&times;";
                alertButton.appendChild(alertSpan);

                let alertDiv = document.getElementById("alertDiv");
                alertDiv.className = "alert alert-danger alert-dismissible fade show";
                alertDiv.appendChild(alertText);
                alertDiv.appendChild(alertButton);
            } 
        }
    }
    xhttp.open("GET", "http://localhost:8080/proj1/wrongcreds.json");
    xhttp.send();
}

$('.alert').alert()