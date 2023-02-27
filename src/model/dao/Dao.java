package model.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Vene;

public class Dao {
	
	//Esitellään muuttujat/objektit tietokantayhteyksiä varten
	private Connection con = null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep = null;
	private String sql;
	private String db = "Venekanta.sqlite";
	
	private Connection yhdista(){
    	Connection con = null;    	
    	String path = System.getProperty("catalina.base");
    	//Tuo pathinfoon projektin polun
    	path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/");
    	//Lisätään pathinfoon kansio jossa kanta sijaitsee
    	path += "veneetWebProject/WebContent/";
    	String url = "jdbc:sqlite:"+path+db;    	
    	try {	       
    		Class.forName("org.sqlite.JDBC");
	        con = DriverManager.getConnection(url);	
	        System.out.println("Yhteys avattu.");
	     }catch (Exception e){	
	    	 System.out.println("Yhteyden avaus epäonnistui.");
	        e.printStackTrace();	         
	     }
	     return con;
	}
	
	//Kaikkien veneiden listaus
	public ArrayList<Vene> listaaKaikki() {
		//Luodaan veneet ArrayList, johon vene-oliot sijoitetaan
		ArrayList<Vene> veneet = new ArrayList<Vene>();
		sql = "SELECT * FROM veneet";
		try {
			con = yhdista();
			if (con != null) {
				stmtPrep = con.prepareStatement(sql);
				rs = stmtPrep.executeQuery();
				if (rs != null) {
					//loopataan resultsetti läpi jos siellä on tietoja
					while (rs.next()) {
						//Luodaan uusi vene-olio result setissä olevilla tiedoilla
						Vene vene = new Vene();
						vene.setTunnus(rs.getInt(1));
						vene.setNimi(rs.getString(2));
						vene.setMerkki(rs.getString(3));
						vene.setPituus(rs.getDouble(4));
						vene.setLeveys(rs.getDouble(5));
						vene.setHinta(rs.getInt(6));
						//lisätään luotu vene-olio ArrayListiin
						veneet.add(vene);
					}
				}
			}
			//Suljetaan yhteys
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Palautetaan veneet ArrayList
		return veneet;
	}
	
	//Veneiden listaus hakusanalla
	public ArrayList<Vene> listaaKaikki(String hakusana) {
		ArrayList<Vene> veneet = new ArrayList<Vene>();
		//SQL lauseessa etsitään hakusanalla. Jätetään kuitenkin tunnus haun ulkopuolelle.
		sql = "SELECT * FROM veneet WHERE "
				+ "nimi LIKE ? "
				+ "or merkkimalli LIKE ? "
				+ "or pituus LIKE ? "
				+ "or leveys LIKE ? "
				+ "or hinta LIKE ?";
		try {
			con = yhdista();
			if (con != null) {
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setString(1, "%" + hakusana + "%");
				stmtPrep.setString(2, "%" + hakusana + "%");
				stmtPrep.setString(3, "%" + hakusana + "%");
				stmtPrep.setString(4, "%" + hakusana + "%");
				stmtPrep.setString(5, "%" + hakusana + "%");
				rs = stmtPrep.executeQuery();
				if (rs != null) {
					//loopataan resultsetti läpi jos siellä on tietoja
					while (rs.next()) {
						//Luodaan uusi vene-olio result setissä olevilla tiedoilla
						Vene vene = new Vene();
						vene.setTunnus(rs.getInt(1));
						vene.setNimi(rs.getString(2));
						vene.setMerkki(rs.getString(3));
						vene.setPituus(rs.getDouble(4));
						vene.setLeveys(rs.getDouble(5));
						vene.setHinta(rs.getInt(6));
						//lisätään luotu vene-olio ArrayListiin
						veneet.add(vene);
					}
				}
			}
			//Suljetaan yhteys
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Palautetaan veneet ArrayList
		return veneet;
	}
	
	//Yhden veneen hakeminen tunnuksella palauttaa vene-olion
	public Vene haeYksi(String veneenTunnus) {
		//Luodaan tyhjä vene-olio
		Vene vene = null;
		//SQL lauseessa etsitään hakusanalla, joka on tunnus.
		sql = "SELECT * FROM veneet WHERE tunnus=?";
		try {
			con = yhdista();
			if (con != null) {
				stmtPrep = con.prepareStatement(sql);
				stmtPrep.setString(1, veneenTunnus);
				rs = stmtPrep.executeQuery();
				if (rs != null) {
					//loopataan resultsetti läpi jos siellä on tietoja
					while (rs.next()) {
						//Viedään tiedot luotuun vene-olioon result setissä olevilla tiedoilla
						vene = new Vene();
						vene.setTunnus(rs.getInt(1));
						vene.setNimi(rs.getString(2));
						vene.setMerkki(rs.getString(3));
						vene.setPituus(rs.getDouble(4));
						vene.setLeveys(rs.getDouble(5));
						vene.setHinta(rs.getInt(6));
					}
				}
			}
			//Suljetaan yhteys
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Palautetaan vene-olio
		return vene;
	}
	
	//Lisäys
	public boolean lisaaVene(Vene vene) {
		//Asetetaan paluuarvo trueksi
		boolean paluuArvo = true;
		//Viedään arvot tietokantaan, mutta asiakas_id ei anneta, koska autonumber
		sql="INSERT INTO veneet (tunnus,nimi,merkkimalli,pituus,leveys,hinta) VALUES(?,?,?,?,?,?)";
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql);
			stmtPrep.setInt(1, vene.getTunnus());
			stmtPrep.setString(2, vene.getNimi());
			stmtPrep.setString(3, vene.getMerkki());
			stmtPrep.setDouble(4, vene.getPituus());
			stmtPrep.setDouble(5, vene.getLeveys());
			stmtPrep.setInt(6, vene.getHinta());
			stmtPrep.executeUpdate();
	        con.close();
		} catch (Exception e) {		
			//Palautetaan false jos lisääminen epäonnistuu
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}
	
	//Seuraavan tunnuksen hakeminen
	public int haeSeuraavaTunnus() {
		int tunnus=0;
		sql="SELECT MAX(tunnus) AS tunnus FROM veneet";
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql);
			rs = stmtPrep.executeQuery();
			if(rs!=null) {
				rs.next();
				tunnus=rs.getInt(1);
			}
	        con.close();
		} catch (Exception e) {		
			e.printStackTrace();
		}
		return tunnus;
	}
	
	//Muuttaminen
	public boolean muutaVene(Vene vene) {
		boolean paluuarvo=true;
		sql="UPDATE veneet SET nimi=?, merkkimalli=?, pituus=?, leveys=?, hinta=? WHERE tunnus=?";	
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql);
			stmtPrep.setString(1, vene.getNimi());
			stmtPrep.setString(2, vene.getMerkki());
			stmtPrep.setDouble(3, vene.getPituus());
			stmtPrep.setDouble(4, vene.getLeveys());
			stmtPrep.setInt(5, vene.getHinta());
			stmtPrep.setInt(6, vene.getTunnus());
			stmtPrep.executeUpdate();
			con.close();
		}catch (Exception e) {
			e.printStackTrace();
			paluuarvo=false;
		}
		return paluuarvo;
	}
	
	//Poistaminen
	public boolean poistaVene(int tunnus) {
		boolean paluuArvo=true;
		sql="DELETE FROM veneet WHERE tunnus=?";						  
		try {
			con = yhdista();
			stmtPrep=con.prepareStatement(sql); 
			stmtPrep.setInt(1, tunnus);			
			stmtPrep.executeUpdate();
	        con.close();
		} catch (Exception e) {				
			e.printStackTrace();
			paluuArvo=false;
		}				
		return paluuArvo;
	}
}
