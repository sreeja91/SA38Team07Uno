$(function() {
$("#addUserBtn").click(function(event) {
        event.preventDefault();        
        addPlayer();
        checkPass();
    });
  
    $("#CancelBtn").click(function(event){
        event.preventDefault();
        resetBtnStates();
      // checkPass();
        
    });

function addPlayer(){
    var addUserName = $("#user_name").val();
    var addPassword = $("#password").val();
    var addConfirmPassword = $("#password_confirmation").val();
    var addEmail = $("#email").val();
    
    if(addPassword===addConfirmPassword)
    {
        {
    var promise = $.post("Register", {user_name:
                addUserName, password: addPassword, email: addEmail});
    promise.done(function(){
        alert("Registration successfull!");

        });
      
    }
    
   
    }
    
    else 
    {
        alert("Passwords Do Not Match");
    }
    }

function resetBtnStates(){
    
    $("#user_name").val("");
    $("#email").val("");
    $("#password").val("");
    $("#password_confirmation").val("");
        }

function checkPass()
{
   
    var pass1 = document.getElementById('password');
    var pass2 = document.getElementById('password_confirmation');
    
    var message = document.getElementById('confirmMessage');

    var goodColor = "#66cc66";
    var badColor = "#ff6666";
    
    if(pass1.value === pass2.value){
        
        pass2.style.backgroundColor = goodColor;
        message.style.color = goodColor;
        message.innerHTML = "Passwords Match!";
    }else{
        
        pass2.style.backgroundColor = badColor;
        message.style.color = badColor;
        message.innerHTML = "Passwords Do Not Match!";
    }
}

});