<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/main.css">
<title>Veneet - listaa</title>
</head>
<body onkeydown="tutkiKey(event)">
	 <ul>
  		<li><a href="listaaveneet.jsp" class="valittu">Listaa</a></li>
  		<li><a href="lisaavene.jsp">Lis‰‰</a></li>
  		<li><a href="muutavene.jsp">Muuta</a></li>
	</ul> 
<div class="haku">
	<input type="text" id="hakusana" class="hakukentta" placeholder="Hae...">
	<input type="button" class="haenappi" id="hae" value="Hae" onclick="haeTiedot()">
</div>
	<span id="ilmo"></span>
	<table id="listaus">
		<thead>
			<tr>
				<th>Nimi</th>
				<th>Merkki</th>
				<th>Malli</th>
				<th>Pituus</th>	
				<th>Leveys</th>
				<th>Hinta</th>
				<th></th>			
			</tr>
		</thead>
		<tbody id="tbody">
		</tbody>
	</table>
<script>

//Kutsutaan haeTiedot funktiota sivun latauksessa
haeTiedot();

//vied‰‰n kursori hakusana-kentt‰‰n sivun latauksen yhteydess‰
document.getElementById("hakusana").focus();

//K‰ynnistet‰‰n haeTiedot jos k‰ytt‰j‰ painaa enter
function tutkiKey(event){
	if(event.keyCode==13){//Enter
		haeTiedot();
	}	
}

//Funktio tietojen hakemista varten
//GET   /veneet/{hakusana}
function haeTiedot(){
	document.getElementById("tbody").innerHTML = "";
	fetch("veneet/" + document.getElementById("hakusana").value,{
	      method: 'GET'	      
	    })
	.then(function (response) {
		return response.json()
	})
	.then(function (responseJson) {
		var veneet = responseJson.veneet;
		//Luodaan html string, johon kirjoitetaan tiedot asiakkaat JSON objektista
		var htmlStr="";
		//Loopataan veneet objekti l‰pi ja lis‰t‰‰n html stringiin
		for(var i=0;i<veneet.length;i++){			
      		htmlStr+="<tr id='rivi_" +veneet[i].tunnus+"'>";
      		htmlStr+="<td>"+veneet[i].nimi+"</td>";
      		htmlStr+="<td>"+veneet[i].merkki+"</td>";
      		htmlStr+="<td>"+veneet[i].malli +"</td>";
      		htmlStr+="<td>"+veneet[i].pituus+"</td>"; 
      		htmlStr+="<td>"+veneet[i].leveys+"</td>"; 
      		htmlStr+="<td>"+veneet[i].hinta+"</td>";
      		//Taulukon viimeiseen soluun luodaan muuta ja poista linkit
      		htmlStr+="<td class=muuta><a href='muutavene.jsp?tunnus="+veneet[i].tunnus+"'>Muuta</a>&nbsp;";
      		htmlStr+="<span class='poista' onclick=poista('"+veneet[i].tunnus+"','"+veneet[i].nimi+"')>Poista</span></td>";
      		htmlStr+="</tr>";        	
		}
		document.getElementById("tbody").innerHTML = htmlStr;		
	})	
}

//Funktio poistamista varten
function poista(tunnus, nimi){
	if(confirm("Poista vene " + nimi+"?")){
		fetch("veneet/" + tunnus,{
		      method: 'DELETE'	      
		    })
		.then( function (response) {
			return response.json()
		})
		.then( function (responseJson) {
			var vastaus = responseJson.response;		
			if(vastaus==0){
				document.getElementById("ilmo").innerHTML= "Veneen poisto ep‰onnistui.";
	        }else if(vastaus==1){
	        	document.getElementById("rivi_"+tunnus).style.background="red"; //V‰rj‰t‰‰n postettavan veneen rivi
	        	alert("Veneen " + nimi +" poisto onnistui.");
				haeTiedot();
			}	
		})	
	}	
}

</script>
</body>
</html>