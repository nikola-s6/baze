package com.sample;

public class Automobil {
	private int snaga;
	private double cena;
	private int premijskiStepen;
	private int prosliPremijskiStepen;
	private int brojNezgoda;
	private boolean nov;
	private double konacnaCena;
	public String toString() {
		return "Automobil [snaga=" + snaga + ", cena=" + cena
				+ ", premijskiStepen=" + premijskiStepen
				+ ", prosliPremijskiStepen=" + prosliPremijskiStepen
				+ ", brojNezgoda=" + brojNezgoda + ", nov=" + nov
				+ ", konacnaCena=" + konacnaCena + "]";
	}
	public int getSnaga() {
		return snaga;
	}
	public void setSnaga(int snaga) {
		this.snaga = snaga;
	}
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	public int getPremijskiStepen() {
		return premijskiStepen;
	}
	public void setPremijskiStepen(int premijskiStepen) {
		this.premijskiStepen = premijskiStepen;
	}
	public int getProsliPremijskiStepen() {
		return prosliPremijskiStepen;
	}
	public void setProsliPremijskiStepen(int prosliPremijskiStepen) {
		this.prosliPremijskiStepen = prosliPremijskiStepen;
	}
	public int getBrojNezgoda() {
		return brojNezgoda;
	}
	public void setBrojNezgoda(int brojNezgoda) {
		this.brojNezgoda = brojNezgoda;
	}
	public boolean isNov() {
		return nov;
	}
	public void setNov(boolean nov) {
		this.nov = nov;
	}
	public double getKonacnaCena() {
		return konacnaCena;
	}
	public void setKonacnaCena(double konacnaCena) {
		this.konacnaCena = konacnaCena;
	}
	
	
	
}
