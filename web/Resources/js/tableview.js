/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var unoApp = {};
$(function() {

    /**Creating a new game **/
    $("#startbtn").on("click", function() {
        $("#gameIDtxt").empty();
        $("#table").empty();
        var maxPlayers = $("#maxPlayerstxt").val();
        if(maxPlayers < 2){
            alert("Max players cannot be less than two !");
        }
        else{
        $.getJSON("gameControl", {
            cmd: "NewGame",
            maxPlayers: $("#maxPlayerstxt").val()
        }).done(function(result) {
            $("#startbtn").prop('disabled', true);
            unoApp.gameID = result.gameID;
            $("#gameIDtxt").append(unoApp.gameID);
            $("#status").empty().append("Waiting....");
            unoApp.event = new EventSource("api/uno/game/" + unoApp.gameID + "?playerName=Table");
            $(unoApp.event).on(result.gameID, unoEventHandler);
        });
    }
    });
    /**get scores**/
    $("#btnscores").on("click", function() {
        $.getJSON("gameControl", {
            cmd: "GetScore",
            gameID: $("#gameIDtxt").text()
        }).done(function(result) {
            $("#status").empty().append("Congratulations!!");
            $("#table").empty();
            $("#score").append("<table class='table table-bordered'><thead><tr><th>PLAYER</th><th>SCORE</th></tr></thead><tbody id='score_table'></tbody></table>");  
        for (var key in result) {
                if (result.hasOwnProperty(key)) {
                    $("#score_table").append("<tr><td style='width:30%;'>" + result[key].userName + "</td><td style='width:30%;'>" + result[key].score + "</td></tr>");  
                }
            }
        });

    });
    $("#btnHome").on("click",function(){
        window.location.href ="index.html";
    });
});
function unoEventHandler(event) {

    var data = JSON.parse(event.originalEvent.data);
    console.log("-->" + JSON.stringify(data));
    switch (data.cmd) {
        case "setupTable":
            {
                $("#status").empty();
                $("#table").empty().append("<img src=" + data.imageUrl + ">");
                break;
            }
        case "PlayerJoin":
            $("#playerjoin").append("<li><img src='Resources/images/user-blue-icon.png'><label class='div-color'>" + data.userName + "</label></li>");
            break;
        case "PlayCard":
            $("#table").empty().append("<img src=" + data.imageUrl + ">");
            break;
        case "Undo":
            $("#table").empty().append("<img src=" + data.imageUrl + ">");
            break;
        case "Uno":
            $("#status").empty().append(data.msg);
            break;
        case "GameOver":
            {
                $("#status").empty().append(data.msg);
                $("#btnscores").removeAttr("disabled");
                break;
            }
    }

}
