package model;

public class Vene {
	private int tunnus, hinta;
	private String nimi, merkki, malli;
	private double pituus, leveys;
	
	public Vene() {
		super();
	}

	public Vene(int tunnus, int hinta, String nimi, String merkki, String malli, double pituus, double leveys) {
		super();
		this.tunnus = tunnus;
		this.hinta = hinta;
		this.nimi = nimi;
		this.merkki = merkki;
		this.malli = malli;
		this.pituus = pituus;
		this.leveys = leveys;
	}

	public int getTunnus() {
		return tunnus;
	}

	public void setTunnus(int tunnus) {
		this.tunnus = tunnus;
	}

	public int getHinta() {
		return hinta;
	}

	public void setHinta(int hinta) {
		this.hinta = hinta;
	}

	public String getNimi() {
		return nimi;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}

	public String getMerkki() {
		return merkki;
	}

	public String getMalli() {
		return malli;
	}
	
	public void setMerkki(String merkki) {
		this.merkki = merkki;
	}

	public void setMalli(String malli) {
		this.malli = malli;
	}

	public double getPituus() {
		return pituus;
	}

	public void setPituus(double pituus) {
		this.pituus = pituus;
	}

	public double getLeveys() {
		return leveys;
	}

	public void setLeveys(double leveys) {
		this.leveys = leveys;
	}

	@Override
	public String toString() {
		return "Vene [tunnus=" + tunnus + ", hinta=" + hinta + ", nimi=" + nimi + ", merkki=" + merkki
				+ ", malli=" + malli + ", pituus=" + pituus + ", leveys=" + leveys + "]";
	}
		
}
