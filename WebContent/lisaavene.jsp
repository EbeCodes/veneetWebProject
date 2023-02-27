<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="scripts/main.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<title>Veneet - lis‰‰</title>
</head>
<body onkeydown="tutkiKey(event)">
	<ul>
		<li><a href="listaaveneet.jsp">Listaa</a></li>
		<li><a href="lisaavene.jsp" class="valittu">Lis‰‰</a></li>
		<li><a href="muutavene.jsp">Muuta</a></li>
	</ul>
	<div class="tyhjaatilaa"></div>
	<form id="tiedot">
		<table>
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
				<tr>
					<td><input type="text" name="nimi" id="nimi" class="tekstiboksi"></td>
					<td><input type="text" name="merkki" id="merkki"
						class="tekstiboksi"></td>
					<td><input type="text" name="malli" id="malli"
						class="tekstiboksi"></td>
					<td><input type="text" name="pituus" id="pituus"
						class="tekstiboksi"></td>
					<td><input type="text" name="leveys" id="leveys"
						class="tekstiboksi"></td>
					<td><input type="text" name="hinta" id="hinta"
						class="tekstiboksi"></td>
					<td><input type="button" class="lisaa" id="lisaa"
						value="Lis‰‰" onclick="lisaaTiedot()"></td>
				</tr>
			</tbody>
		</table>
	</form>
	<span id="ilmo" class="ilmo"></span>
	<script>
		//vied‰‰n kursori nimi-kentt‰‰n sivun latauksen yhteydess‰
		document.getElementById("nimi").focus();

		//Jos k‰ytt‰j‰ painaa enter kutsutaan lis‰‰Tiedot funktiota
		function tutkiKey(event) {
			if (event.keyCode == 13) {//Enter
				lisaaTiedot();
			}
		}

		function lisaaTiedot() {
			var nimi = document.getElementById("nimi").value;
			var merkki = document.getElementById("merkki").value;
			var malli = document.getElementById("malli").value;
			var pituus = document.getElementById("pituus").value;
			var leveys = document.getElementById("leveys").value;
			var hinta = document.getElementById("hinta").value;
			
			//Siivotaan arvoja
			document.getElementById("nimi").value=siivoa(document.getElementById("nimi").value);
			document.getElementById("merkki").value=siivoa(document.getElementById("merkki").value);
			document.getElementById("malli").value=siivoa(document.getElementById("malli").value);
			
			if (nimi.length < 2) {
				document.getElementById("ilmo").innerHTML = "Nimi ei kelpaa!"
					document.getElementById("nimi").focus();
				return;
			}
			if (merkki.length < 2) {
				document.getElementById("ilmo").innerHTML = "Merkki ei kelpaa!"
					document.getElementById("merkki").focus();
				return;
			}
			if (pituus * 1 != pituus || pituus.length < 1) {
				document.getElementById("ilmo").innerHTML = "Pituus ei kelpaa!"
					document.getElementById("pituus").focus();
				return;
			}
			if (leveys * 1 != leveys || leveys.length < 1) {
				document.getElementById("ilmo").innerHTML = "Leveys ei kelpaa!"
					document.getElementById("leveys").focus();
				return;
			}
			if (hinta * 1 != hinta || hinta.length < 1) {
				document.getElementById("ilmo").innerHTML = "Hinta pit‰‰ olla kokonaisluku!"
					document.getElementById("hinta").focus();
				return;
			}
			//Jos validoinnit menev‰t l‰pi, muutetaan lomakkeen tiedot json-stringiksi
			var formJsonStr = formDataToJSON(document.getElementById("tiedot"));
			//L‰het‰‰n uudet tiedot backendiin
			fetch("veneet", {
				method : 'POST',
				body : formJsonStr
			})
					.then(function(response) {
						return response.json()
					})
					.then(
							function(responseJson) {
								var vastaus = responseJson.response;
								if (vastaus == 0) {
									document.getElementById("ilmo").innerHTML = "Veneen lis‰ys ep‰onnistui";
								} else if (vastaus == 1) {
									document.getElementById("ilmo").innerHTML = "Vene lis‰tty";
								}
							});
		}

		function siivoa(teksti){
			teksti=teksti.replace("<","");
			teksti=teksti.replace(">","");
			teksti=teksti.replace(";","");
			teksti=teksti.replace("'","''");
			return teksti;
		}
	</script>
</body>
</html>