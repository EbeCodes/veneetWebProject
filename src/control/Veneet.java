package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import model.Vene;
import model.dao.Dao;

/**
 * Servlet implementation class Veneet
 */
@WebServlet("/veneet/*")
public class Veneet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Veneet() {
        super();
        System.out.println("Veneet()");
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Veneet.doGet()");
		//Luodaan veneet ArrayList
		ArrayList<Vene> veneet=null;
		String strJSON="";
		Dao dao = new Dao();
		String pathInfo = request.getPathInfo();
		//**
		//Haetaan kaikki veneet jos pathinfo puuttuu
		//**
		if(pathInfo==null) {
			veneet = dao.listaaKaikki();
			//Muutetaan Arraylist JSON:iksi.
			strJSON = new JSONObject().put("veneet", veneet).toString();	
		//**
		//Jos pathinfosta löytyy sana haeyksi, haetaan yksi vene
		//**
		}else if(pathInfo.indexOf("haeyksi")!=-1) {
			//pudotetaan pathinfosta pois /haeyksi/, jolloin jäljelle jää pelkkä veneen tunnus
			String veneenTunnus = pathInfo.replace("/haeyksi/", "");
			Vene vene = dao.haeYksi(veneenTunnus);
			//Jos venettä ei löydy, palautetaan tyhjä JSON stringi
			if(vene==null) {
				strJSON = "{}";
			}else {
				//Eriytetään merkki ja malli
				vene = eriytaMalli(vene);
				//Viedään veneen tiedot JSON stringiin kun sellainen löytyy
				JSONObject JSON = new JSONObject();
				JSON.put("tunnus", Integer.toString(vene.getTunnus()));
				JSON.put("nimi", vene.getNimi());
				JSON.put("merkki", vene.getMerkki());
				JSON.put("malli", vene.getMalli());
				JSON.put("pituus", Double.toString(vene.getPituus()));
				JSON.put("leveys", Double.toString(vene.getLeveys()));
				JSON.put("hinta", Integer.toString(vene.getHinta()));
				strJSON = JSON.toString();
			}
		//**
		//Muusssa tapauksessa suoritetaan haku polusta löytyvällä hakusanalla
		//**
		}else {
			String hakusana = "";
			//Tiputetaan pois ensimmäinen merkki polkusta, joka on kauttaviiva
			hakusana = pathInfo.substring(1, pathInfo.length());
			//Pyydetään listaamaan kaikki veneet ja välitetään hakusana parametrina
			veneet = dao.listaaKaikki(hakusana);
			//Eriytetään merkki ja malli
			for (Vene vene : veneet) {
				vene = eriytaMalli(vene);
			}
			//Muutetaan palautuva Arraylist JSON:iksi.
			strJSON = new JSONObject().put("veneet", veneet).toString();
		}
		//Kirjoitetaan JSON servletin html rajapintaan
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		//Komento tulostaa selaimeen printwriterin avulla
		out.println(strJSON);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Veneet.doPost()");
		//Muutetaan kutsun mukana tuleva json-string json-objektiksi
		JSONObject jsonObj = new JsonStrToObj().convert(request);
		//Luodaan uusi vene olio, johon laitetaan tiedot json objektista
		Vene vene = new Vene();
		Dao dao = new Dao();
		vene.setTunnus(dao.haeSeuraavaTunnus()+1);
		vene.setNimi(jsonObj.getString("nimi"));
		//Jos käyttäjä on syöttänyt mallin, katenoidaan se mukaan merkkiin (merkkimalli)
		if(jsonObj.getString("malli").length()>1) {
			vene.setMerkki(jsonObj.getString("merkki")+" " +jsonObj.getString("malli"));
		}else {
			//muussa tapauksessa merkkiin tulee pelkkä merkki
			vene.setMerkki(jsonObj.getString("merkki"));
		}
		vene.setPituus(Double.parseDouble(jsonObj.getString("pituus")));
		vene.setLeveys(Double.parseDouble(jsonObj.getString("leveys")));
		//Tämä parseroi myös desimaaliluvut integeriksi
		vene.setHinta((int) Double.parseDouble(jsonObj.getString("hinta")));
		//vene.setHinta(Integer.parseInt(jsonObj.getString("hinta"))); (error desimaaliluvuissa)
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		if(dao.lisaaVene(vene)){ //metodi palauttaa true/false
			//Veneen lisääminen onnistui {"response":1}
			out.println("{\"response\":1}");
		}else{
			//Veneen lisääminen epäonnistui {"response":0}
			out.println("{\"response\":0}"); 
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Veneet.doPut()");
		JSONObject jsonObj = new JsonStrToObj().convert(request);
		Vene vene = new Vene();
		vene.setTunnus(Integer.parseInt(jsonObj.getString("tunnus")));
		vene.setNimi(jsonObj.getString("nimi"));
		if(jsonObj.getString("malli").length()>1) {
			vene.setMerkki(jsonObj.getString("merkki")+" " +jsonObj.getString("malli"));
		}else {
			vene.setMerkki(jsonObj.getString("merkki"));
		}
		vene.setPituus(Double.parseDouble(jsonObj.getString("pituus")));
		vene.setLeveys(Double.parseDouble(jsonObj.getString("leveys")));
		vene.setHinta((int) Double.parseDouble(jsonObj.getString("hinta")));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();
		//Metodi palauttaa arvon true tai false
		boolean paluuarvo = dao.muutaVene(vene);
		if(paluuarvo) {
			out.println("{\"response\":1}");
		}else {
			out.println("{\"response\":0}");
		}
	}

	//Poistaminen
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Veneet.doDelete()");
		//Haetaan poistokutsun polkutiedot josta löytyy poistettavan veneen tunnus
		String pathInfo = request.getPathInfo();
		int poistettavaTunnus = Integer.parseInt(pathInfo.substring(1, pathInfo.length()));
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		//Kutsutaan poistamista ja välitetään tunnus
		if(dao.poistaVene(poistettavaTunnus)){
			out.println("{\"response\":1}");
		}else{
			out.println("{\"response\":0}");
		}
		
	}
	
	//Funktio, jolla kannassa oleva merkkimalli tieto eriytetään omiksi tiedoikseen
	private Vene eriytaMalli (Vene vene) {
		String merkkimalli = vene.getMerkki();
		boolean loytyi = false;
		for(int i = 0; i < merkkimalli.length(); i++){
            if(merkkimalli.charAt(i) == ' '){
            	vene.setMerkki(merkkimalli.substring(0, i));
            	vene.setMalli(merkkimalli.substring(i+1, merkkimalli.length()));
            	loytyi = true;
            }
        }
		//Jos välilyöntiä ei löydy, asetetaan malliksi viiva (ei tiedossa)
		if(!loytyi) {
			vene.setMalli("-");
		}
		
		return vene;
	}

}
