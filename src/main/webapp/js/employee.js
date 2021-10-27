window.onload = function(){
    imageLoad();
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
                document.getElementById("welcomeHeading").innerHTML=`Welcome ${userInfo.user.firstName} ${userInfo.user.lastName}`;
                document.getElementById("name").innerHTML=`Name: ${userInfo.user.firstName} ${userInfo.user.lastName}`;
                document.getElementById("username").innerHTML=`Username: ${userInfo.user.username}`;
                document.getElementById("password").innerHTML=userInfo.user.password;
                document.getElementById("password").style.display="none";
                document.getElementById("email").innerHTML=`Email: ${userInfo.user.email}`;
                for (ticket of userInfo.tickets) {
                    let newTicketDiv = document.createElement("div");
                    newTicketDiv.className = "row ticketDiv";

                    let date = new Date(ticket.submitDate);
                    let col = document.createElement("div");
                    col.className ="col";
                    let colText = document.createTextNode(date.toDateString());
                    col.appendChild(colText);
                    newTicketDiv.appendChild(col);

                    col = document.createElement("div");
                    col.className ="col";
                    colText = document.createTextNode(ticket.description);
                    col.appendChild(colText);
                    newTicketDiv.appendChild(col);

                    col = document.createElement("div");
                    col.className ="col";
                    colText = document.createTextNode(`$${ticket.amount}`);
                    col.appendChild(colText);
                    newTicketDiv.appendChild(col);

                    col = document.createElement("div");
                    col.className ="col";
                    colText = document.createTextNode(ticket.type.type);
                    col.appendChild(colText);
                    newTicketDiv.appendChild(col);

                    col = document.createElement("div");
                    col.className ="col";
                    colText = document.createTextNode(ticket.status.status);
                    col.appendChild(colText);
                    newTicketDiv.appendChild(col);

                    if(ticket.status.status === "Pending"){
                        let updateButton = document.createElement("button");
                        updateButton.id = "updateButton";
                        updateButton.className="btn btn-primary btn-sm";
                        updateButton.setAttribute("data-toggle", "modal")
                        updateButton.setAttribute("data-target", "#updateTicketModal");
                        updateButton.setAttribute("value", ticket.id);
                        updateButton.addEventListener("click", ()=>{
                            document.getElementById("ticketId").value = updateButton.value;
                        });
                        let buttonText = document.createTextNode("Edit Ticket");
                        updateButton.appendChild(buttonText);

                        col = document.createElement("div");
                        col.className ="col";
                        col.appendChild(updateButton);
                        newTicketDiv.appendChild(col);
                        document.getElementById("pending-container").append(newTicketDiv);
                    }else if(ticket.status.status === "Approved"){
                        document.getElementById("approved-container").append(newTicketDiv);
                    }else{
                        document.getElementById("denied-container").append(newTicketDiv);
                    }
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

function imageLoad(){
    let images = document.querySelectorAll('img');
    images.forEach(function(image){
        let isLoaded = image.complete && image.naturalWidth !== 0;
        if(!isLoaded){
            image.setAttribute('src', `img/${image.getAttribute('alt')}`);
        }
    });
}