window.onload = function(){
    getSessionUser();
}

function getSessionUser(){
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function(){
        if(xhttp.readyState == 4 && xhttp.status == 200){
            if(!xhttp.responseText){
                $('#validateModal').modal('show');
            }else{
                let userInfo = JSON.parse(xhttp.responseText);
                console.log(userInfo);
                document.getElementById("welcomeHeading").innerHTML=`Welcome ${userInfo.user.firstName} ${userInfo.user.lastName}`;
                document.getElementById("name").innerHTML=`${userInfo.user.firstName} ${userInfo.user.lastName}`;
                document.getElementById("username").innerHTML=`Username: ${userInfo.user.username}`;
                document.getElementById("password").innerHTML=userInfo.user.password;
                document.getElementById("password").style.display="none";
                document.getElementById("email").innerHTML=`Email: ${userInfo.user.email}`;
                for (ticket of userInfo.tickets) {
                    let newTicketDiv = document.createElement("div");
                    newTicketDiv.className = "ticketDiv";
                    let date = new Date(ticket.submitDate);
                    let divText = document.createTextNode(`${date.toDateString()}: ${ticket.description} $${ticket.amount} For: ${ticket.type.type} Status:${ticket.status.status}`);
                    newTicketDiv.appendChild(divText);

                    if(ticket.status.status === "Pending"){
                        let updateButton = document.createElement("button");
                        updateButton.id = "updateButton";
                        updateButton.className="btn btn-primary";
                        updateButton.setAttribute("data-toggle", "modal")
                        updateButton.setAttribute("data-target", "#updateTicketModal");
                        updateButton.setAttribute("value", ticket.id);
                        updateButton.addEventListener("click", ()=>{
                            document.getElementById("ticketId").value = updateButton.value;
                        });
                        let buttonText = document.createTextNode("Edit Ticket");
                        updateButton.appendChild(buttonText);
                        newTicketDiv.appendChild(updateButton);
                    }

                    document.getElementById("pastTickets").append(newTicketDiv);
                }

            }
            
        }
    }
    xhttp.open("GET", "http://localhost:8080/proj1/getsessionuser.json");
    xhttp.send();
}

function getTickets(){
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function(){
        if(xhttp.readyState == 4 && xhttp.status == 200){
            if(!xhttp.responseText){
                $('#validateModal').modal('show');
            }else{
                let userInfo = JSON.parse(xhttp.responseText);
                console.log(userInfo);
                for (ticket of userInfo.tickets) {
                    let newTicketDiv = document.createElement("div");
                    newTicketDiv.className = "ticketDiv";
                    let divText = document.createTextNode(`${ticket.submitDate}: ${ticket.description} $${ticket.amount} For: ${ticket.type.type} Status:${ticket.status.status}`);
                    newTicketDiv.appendChild(divText);
                    document.getElementById("pastTickets").append(newTicketDiv);
                }

            }
            
        }
    }

    xhttp.open("GET", "http://localhost:8080/proj1/ticketsubmitted.json");
    xhttp.send();
}

let submitTicket = document.getElementById("ticketSubmit");
submitTicket.addEventListener("click", getSessionUser);