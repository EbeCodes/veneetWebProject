<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="scripts/main.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<title>Veneet - Muuta</title>
</head>
<body onkeydown="tutkiKey(event)">
	<ul>
		<li><a href="listaaveneet.jsp">Listaa</a></li>
		<li><a href="lisaavene.jsp">Lis‰‰</a></li>
		<li><a href="muutavene.jsp" class="valittu">Muuta</a></li>
	</ul>
	<div class="haku">
		<input type="text" id="hakusana" class="hakukentta"
			placeholder="Hae..."> <input type="button" class="haenappi"
			id="hae" value="Hae" onclick="haeTiedot()">
	</div>
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
					<td><input type="text" name="nimi" id="nimi"
						class="tekstiboksi"></td>
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
						value="Muuta" onclick="muutaTiedot()"></td>
				</tr>
			</tbody>
		</table>
		<input type="hidden" name="tunnus" id="tunnus">
	</form>
	<span id="ilmo" class="ilmo"></span>
	<script>
		function tutkiKey(event) {
			putsaaIlmo();
			if (event.keyCode == 13) {//Enter
				//Jos hakuun on syˆtetty jotain, haetaan
				if (document.getElementById("hakusana").value.length > 0) {
					haeTiedot();
				} else {
					//Muussa tapauksessa p‰ivitet‰‰n
					muutaTiedot();
				}
			}
		}

		var tunnus = requestURLParam("tunnus"); //Funktio lˆytyy scripts/main.js
		if (tunnus != null) {
			fetch("veneet/haeyksi/" + tunnus, {
				method : 'GET'
			})
					.then(function(response) {
						return response.json()
					})
					.then(
							function(responseJson) {
								document.getElementById("nimi").value = responseJson.nimi;
								document.getElementById("merkki").value = responseJson.merkki;
								if (responseJson.malli != "-") {
									document.getElementById("malli").value = responseJson.malli;
								}
								console.log(responseJson.malli);
								document.getElementById("pituus").value = responseJson.pituus;
								document.getElementById("leveys").value = responseJson.leveys;
								document.getElementById("hinta").value = responseJson.hinta;
								document.getElementById("tunnus").value = responseJson.tunnus;
								document.getElementById("nimi").focus();
							});
		}else
			//Jos tunnusta ei ole v‰litetty vied‰‰n kursori hakukentt‰‰n
			document.getElementById("hakusana").focus();

		//Hakukentt‰
		function haeTiedot() {
			tyhjaaKentat();
			fetch("veneet/" + document.getElementById("hakusana").value, {
				method : 'GET'
			})
					.then(function(response) {
						return response.json()
					})
					.then(
							function(responseJson) {
								var veneet = responseJson.veneet;
								if (veneet.length > 0) {
									//Otetaan tiedot ensimm‰isest‰ objektista
									//T‰t‰ voisi parantaa luomalla painikkeen josta hakisi seuraavat tiedot
									for (var i = 0; i < 1; i++) {
										document.getElementById("nimi").value = veneet[i].nimi;
										document.getElementById("merkki").value = veneet[i].merkki;
										//Jos malli on pelkk‰ viiva, sit‰ ei tuoda t‰h‰n
										if (document.getElementById("malli").value != "-") {
											document.getElementById("malli").value = veneet[i].malli;
										}
										document.getElementById("pituus").value = veneet[i].pituus;
										document.getElementById("leveys").value = veneet[i].leveys;
										document.getElementById("hinta").value = veneet[i].hinta;
										document.getElementById("tunnus").value = veneet[i].tunnus;
										document.getElementById("nimi").focus();
									}
								} else {
									document.getElementById("ilmo").innerHTML = "Venett‰ ei lˆydy!";
								}
							});
			//Tyhjennet‰‰n hakukentt‰
			document.getElementById("hakusana").value = "";
		}

		function putsaaIlmo() {
			document.getElementById("ilmo").innerHTML = "";
		}

		function tyhjaaKentat() {
			document.getElementById("nimi").value = "";
			document.getElementById("merkki").value = "";
			document.getElementById("malli").value = "";
			document.getElementById("pituus").value = "";
			document.getElementById("leveys").value = "";
			document.getElementById("hinta").value = "";
			document.getElementById("tunnus").value = "";
		}

		function muutaTiedot() {
			//Katsotaan ensin ett‰ muutettava vene on haettu
			if (document.getElementById("tunnus").value.length > 0) {
				//Vied‰‰n tiedot muuttujiinsa
				var nimi = document.getElementById("nimi").value;
				var merkki = document.getElementById("merkki").value;
				var malli = document.getElementById("malli").value;
				var pituus = document.getElementById("pituus").value;
				var leveys = document.getElementById("leveys").value;
				var hinta = document.getElementById("hinta").value;
				var tunnus = document.getElementById("tunnus").value;

				//Tehd‰‰n samat validoinnit kuin lis‰yksess‰
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
				//Siivotaan arvoja
				document.getElementById("nimi").value = siivoa(document
						.getElementById("nimi").value);
				document.getElementById("merkki").value = siivoa(document
						.getElementById("merkki").value);
				document.getElementById("malli").value = siivoa(document
						.getElementById("malli").value);

				//muutetaan lomakkeen tiedot json-stringiksi
				var formJsonStr = formDataToJSON(document
						.getElementById("tiedot"));
				//L‰het‰‰n muutetut tiedot backendiin
				fetch("veneet", {
					method : 'PUT',
					body : formJsonStr
				})
						.then(function(response) {
							return response.json();
						})
						.then(
								function(responseJson) {
									var vastaus = responseJson.response;
									if (vastaus == 0) {
										document.getElementById("ilmo").innerHTML = "Muuttaminen ep‰onnistui";
									} else if (vastaus == 1) {
										document.getElementById("ilmo").innerHTML = "Veneen tiedot muutettu";
									}
								});
			} else {
				document.getElementById("ilmo").innerHTML = "Hae ensin muutettava vene!";
			}
		}

		//Funktio jolla siivotaan k‰ytt‰j‰n syˆtt‰mi‰ arvoja
		//estet‰‰n esim. html tagit ja sql injektiot korvaamalla merkkej‰
		function siivoa(teksti) {
			teksti = teksti.replace("<", "");
			teksti = teksti.replace(">", "");
			teksti = teksti.replace(";", "");
			teksti = teksti.replace("'", "''");
			return teksti;
		}
	</script>
</body>
</html>