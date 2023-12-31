package com.sample;

public class Klima {
	
	private String tipKlime = "obicna";
	private int snaga;
	private boolean inverter;
	private int kvadratura;
	private boolean viseProstorija;
	private boolean mogucaUgradnja;
	private String koriscenje;
	private boolean manjaTemperatura;

	public String toString() {
		return "Klima [tipKlime=" + tipKlime + ", snaga=" + snaga + ", inverter=" + inverter + ", kvadratura="
				+ kvadratura + ", viseProstorija=" + viseProstorija + ", mogucaUgradnja=" + mogucaUgradnja
				+ ", koriscenje=" + koriscenje + "]";
	}
	
	public String getTipKlime() {
		return tipKlime;
	}
	
	public void setTipKlime(String tipKlime) {
		this.tipKlime = tipKlime;
	}
	
	public int getSnaga() {
		return snaga;
	}
	
	public void setSnaga(int snaga) {
		this.snaga = snaga;
	}
	
	public boolean isInverter() {
		return inverter;
	}
	
	public void setInverter(boolean inverter) {
		this.inverter = inverter;
	}
	
	public int getKvadratura() {
		return kvadratura;
	}
	
	public void setKvadratura(int kvadratura) {
		this.kvadratura = kvadratura;
	}
	
	public boolean isViseProstorija() {
		return viseProstorija;
	}
	
	public void setViseProstorija(boolean viseProstorija) {
		this.viseProstorija = viseProstorija;
	}
	
	public boolean isMogucaUgradnja() {
		return mogucaUgradnja;
	}
	
	public void setMogucaUgradnja(boolean mogucaUgradnja) {
		this.mogucaUgradnja = mogucaUgradnja;
	}
	
	public String getKoriscenje() {
		return koriscenje;
	}
	
	public void setKoriscenje(String koriscenje) {
		this.koriscenje = koriscenje;
	}
	
	public boolean isManjaTemperatura() {
		return manjaTemperatura;
	}

	public void setManjaTemperatura(boolean manjaTemperatura) {
		this.manjaTemperatura = manjaTemperatura;
	}
	
}
