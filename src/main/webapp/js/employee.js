// window.onload = function(){
//     getSessionUser();
// }

// function getSessionUser(){
//     let xhttp = new XMLHttpRequest();
//     xhttp.onreadystatechange = function(){
//         if(xhttp.readyState == 4 && xhttp.status == 200){
//             if(!xhttp.responseText){
//                 $('#exampleModal').modal('show');
//             }else{
//                 let user = JSON.parse(xhttp.responseText);
//                 document.getElementById("welcomeHeading").innerHTML=`Welcome ${user.firstName} ${user.lastName}`;
//             }
            
//         }
//     }

//     xhttp.open("GET", "http://localhost:8080/proj1/getsessionuser.json");
//     xhttp.send();
// }

// let modalButton = document.getElementById("modalButton");
// modalButton.addEventListener("click", toIndex);

// function toIndex(){
//     location.href = 'http://localhost:8080/proj1/html/index.html';
// }