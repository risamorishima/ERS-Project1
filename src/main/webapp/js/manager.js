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
                    let employeeName;
                    for(employee of userInfo.employees){
                        if(employee.id == ticket.authorId){
                            employeeName = `${employee.firstName}, ${employee.lastName}`
                        }
                    }
                    let date = new Date(ticket.submitDate);
                    let divText = document.createTextNode(`${employeeName}: (${date.toDateString()}) ${ticket.description} $${ticket.amount} For: ${ticket.type.type} Status:${ticket.status.status}`);
                    newTicketDiv.appendChild(divText);

                    if(ticket.status.status === "Pending"){
                        let decisionForm = document.createElement("form");
                        decisionForm.setAttribute("method", "post");
                        decisionForm.setAttribute("action", "/proj1/updateTicket.view");
                        let approveButton = document.createElement("button");
                        approveButton.id = "approveButton";
                        approveButton.className="btn btn-primary";
                        approveButton.setAttribute("value", ticket.id);
                        approveButton.setAttribute("name", "Approved");
                        let approveText = document.createTextNode("Approve Ticket");
                        approveButton.appendChild(approveText);
                        decisionForm.appendChild(approveButton);
                        newTicketDiv.appendChild(decisionForm);
                                            
                        let denyButton = document.createElement("button");
                        denyButton.id = "denyButton";
                        denyButton.className="btn btn-primary";
                        denyButton.setAttribute("value", ticket.id);
                        denyButton.setAttribute("name", "Denied");
                        let denyText = document.createTextNode("Deny Ticket");
                        denyButton.appendChild(denyText);
                        decisionForm.appendChild(denyButton);
                        newTicketDiv.appendChild(decisionForm);
                        document.getElementById("pendingTickets").append(newTicketDiv);
                    }else if(ticket.status.status === "Approved"){
                        document.getElementById("approvedTickets").append(newTicketDiv);
                    }else{
                        document.getElementById("deniedTickets").append(newTicketDiv);
                    }
                }

            }
            
        }
    }
    xhttp.open("GET", "http://localhost:8080/proj1/getsessionuser.json");
    xhttp.send();
}