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
                    let employeeName;
                    for(employee of userInfo.employees){
                        if(employee.id == ticket.authorId){
                            employeeName = `${employee.lastName}, ${employee.firstName}`
                        }
                    }
                    let col = document.createElement("div");
                    col.className ="col";
                    let colText = document.createTextNode(employeeName);
                    col.appendChild(colText);
                    newTicketDiv.appendChild(col);


                    let date = new Date(ticket.submitDate);
                    col = document.createElement("div");
                    col.className ="col";
                    colText = document.createTextNode(date.toDateString());
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
                        let decisionForm = document.createElement("form");
                        decisionForm.setAttribute("method", "post");
                        decisionForm.setAttribute("action", "/proj1/updateTicket.view");
                        let approveButton = document.createElement("button");
                        approveButton.id = "approveButton";
                        approveButton.className="btn btn-outline-success btn-sm";
                        approveButton.setAttribute("value", ticket.id);
                        approveButton.setAttribute("name", "Approved");
                        let approveText = document.createTextNode("Approve Ticket");
                        approveButton.appendChild(approveText);
                        decisionForm.appendChild(approveButton);
                        
                        col = document.createElement("div");
                        col.className ="col";
                        col.appendChild(decisionForm);
                        newTicketDiv.appendChild(col);

                        decisionForm = document.createElement("form");
                        decisionForm.setAttribute("method", "post");
                        decisionForm.setAttribute("action", "/proj1/updateTicket.view");
                        let denyButton = document.createElement("button");
                        denyButton.id = "denyButton";
                        denyButton.className="btn btn-outline-danger btn-sm";
                        denyButton.setAttribute("value", ticket.id);
                        denyButton.setAttribute("name", "Denied");
                        let denyText = document.createTextNode("Deny Ticket");
                        denyButton.appendChild(denyText);
                        decisionForm.appendChild(denyButton);

                        col = document.createElement("div");
                        col.className ="col";
                        col.appendChild(decisionForm);
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
    xhttp.open("POST", "http://localhost:8080/proj1/getsessionuser.json");
    xhttp.send("test=1");
}

function imageLoad(){
    let images = document.querySelectorAll('img');
    images.forEach(function(image){
        let isLoaded = image.complete && image.naturalWidth !== 0;
        if(!isLoaded){
            image.setAttribute('src', `img/${image.getAttribute('alt')}`);
        }
    });
}