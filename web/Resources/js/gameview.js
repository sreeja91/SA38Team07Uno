/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

        $ (function(){
            
            /**retrieve username**/
            $.getJSON("gameControl",{cmd:"GetUser"}).done(function(data){
               $("#welcomelabel").text(data.userName);
            });
            
            /**View Games**/
            $("#viewGameBtn").on("click",function(){
              $("#gameViewListBody").empty();
               var promise =  $.getJSON("gameControl",{cmd:"SelectWaitingGames"});
                    promise.done(function(jsonObjResult){
                         waitingGameList = jsonObjResult;                        
                     for ( var i=0; i< waitingGameList.length ; i++)
                        {
                            var target = $("#gameViewListBody");
                            var newRow = $("<tr></tr>");
                            newRow.appendTo(target);
                                                      
                            var restOfCellString = "<td>"+waitingGameList[i].gameID+"</td>"+"<td>"+waitingGameList[i].maxPlayers+"</td><td>"+
                                                    waitingGameList[i].currPlayers+"</td><td>"+waitingGameList[i].status+"</td>";
                            var restOfCells = $(restOfCellString);
                            restOfCells.appendTo(newRow);
                            
                            var joinBtn=$("<button>Select</button>");
                            joinBtn.prop("gameID", waitingGameList[i].gameID);
                            joinBtn.prop("class", "dsgnmoo  dsgnmoo:hover dsgnmoo:active");
                            joinBtn.bind("click",onGameJoin);                            
                            joinBtn.appendTo(newRow);
                        }
                });
            });
            /**logout**/
             $("#logoutBtn").on("click",function(){
                  $.get("logout",{}).done(function(){
                  window.location.href = "index.html"; 
                });
             });
                            
        });
        
        
        function onGameJoin()
        {
            var gameID = $(this).prop("gameID"); 
            var userName = $("#welcomelabel").text();
            var url = "playerview.html?gameID=" +gameID+"&userName="+userName;
            window.location.href = url;

        }
                
            