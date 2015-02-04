/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var game = {};
var queryString = new Array();
$(function() {
    /**get the query string**/
    if (queryString.length === 0) {
        if (window.location.search.split('?').length > 1) {
            var params = window.location.search.split('?')[1].split('&');
            for (var i = 0; i < params.length; i++) {
                var key = params[i].split('=')[0];
                var value = decodeURIComponent(params[i].split('=')[1]);
                queryString[key] = value;
            }
        }
    }
    if (queryString["gameID"] !== null) {
        $("#gameIDtxt").text(queryString["gameID"]);
    }
    if (queryString["userName"] !== null) {
        $("#playerNametxt").text(queryString["userName"]);
    }
    $("#joinbtn").on("click", function() {
        game.gameID = $("#gameIDtxt").text();
        game.playerName = $("#playerNametxt").text();
        $("#gameStatus").empty().append("Waiting....");
        game.event = new EventSource("api/uno/game/" + game.gameID + "?playerName=" + game.playerName);
        $(game.event).on(game.gameID, unoEventHandler);

    });
    $("#drawbtn").on("click", function() {
        $.getJSON("gameControl",
                {cmd: "DrawCard",
                    userName: $("#playerNametxt").text(),
                    gameID: $("#gameIDtxt").text()
                }).done(function(result) {
            console.log("-->" + JSON.stringify(result));
            if (!(result === null)) {
                game.imageUrl = result.imageUrl;
                $("#uno-el").append("<li><img src=" + game.imageUrl + "></li>");
            }
        });

    });

    /**Play card**/
    $("#uno-el li").live("click", function() {
        game.imageUrl = $("img", this).attr("src");
        $(this).remove();
        $.post("gameControl",
                {cmd: "PlayCard",
                    imageUrl: game.imageUrl,
                    userName: $("#playerNametxt").text(),
                    gameID: $("#gameIDtxt").text()
                });
    });

    /**Undo Move**/

    $("#undobtn").on("click", function() {
        $.getJSON("gameControl",
                {cmd: "Undo",
                    userName: $("#playerNametxt").text(),
                    gameID: $("#gameIDtxt").text()
                }).done(function(data) {
            console.log("-->" + JSON.stringify(data));
            if (!(data === null)) {
                game.imageUrl = data.imageUrl;
                $("#uno-el").append("<li><img src=" + game.imageUrl + "></li>");
            }
        });

    });

    $("#unobtn").on("click", function() {
        $.getJSON("gameControl",
                {cmd: "Uno",
                    userName: $("#playerNametxt").text(),
                    gameID: $("#gameIDtxt").text()

                }).done(function(result) {
            if (!(result === null)) {
                alert(result.msg);
            }
        });
    });

    /**logout**/
    $("#logoutBtn").on("click", function() {
        $.get("logout", {}).done(function() {
            window.location.href = "index.html";
        });
    });

});

function unoEventHandler(event) {
    $("#gameStatus").empty();
    var data = JSON.parse(event.originalEvent.data);
    $("#joinbtn").prop('disabled', true);
    console.log("-->" + JSON.stringify(data));
    if (data.cmd === "GameOver") {
        $("#gameStatus").append(data.msg);
        disableButtons();
    }
    else {
        for (var key in data) {
            if (data.hasOwnProperty(key)) {
                $("#uno-el").append("<li><img src=" + data[key].imageUrl + "></li>");
            }
        }
    }
}

function disableButtons() {
    $("#nav-fan5").prop('disabled', true);
    $("#drawbtn").prop('disabled', true);
    $("#undobtn").prop('disabled', true);
    $("#unobtn").prop('disabled', true);
}