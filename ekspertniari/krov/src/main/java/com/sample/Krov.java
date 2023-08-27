package com.sample;

import java.util.ArrayList;
import java.util.List;

public class Krov {
	private List<String> prokisnjava = new ArrayList<>();
	private int brojNedostajucihCrepova;
	private double kvadratura;
	private boolean ulegao;
	private boolean drvoSaKrosnjom;
	private boolean oluciZardjali;
	private List<String> potrebniRadovi = new ArrayList<>();
	private double ukupnaCenaRadova;
	@Override
	public String toString() {
		return "Krov [prokisnjava=" + prokisnjava
				+ ", brojNedostajucihCrepova=" + brojNedostajucihCrepova
				+ ", kvadratura=" + kvadratura + ", ulegao=" + ulegao
				+ ", drvoSaKrosnjom=" + drvoSaKrosnjom + ", oluciZardjali="
				+ oluciZardjali + ", potrebniRadovi=" + potrebniRadovi
				+ ", ukupnaCenaRadova=" + ukupnaCenaRadova + "]";
	}
	public List<String> getProkisnjava() {
		return prokisnjava;
	}
	public void setProkisnjava(List<String> prokisnjava) {
		this.prokisnjava = prokisnjava;
	}
	public int getBrojNedostajucihCrepova() {
		return brojNedostajucihCrepova;
	}
	public void setBrojNedostajucihCrepova(int brojNedostajucihCrepova) {
		this.brojNedostajucihCrepova = brojNedostajucihCrepova;
	}
	public double getKvadratura() {
		return kvadratura;
	}
	public void setKvadratura(double kvadratura) {
		this.kvadratura = kvadratura;
	}
	public boolean isUlegao() {
		return ulegao;
	}
	public void setUlegao(boolean ulegao) {
		this.ulegao = ulegao;
	}
	public boolean isDrvoSaKrosnjom() {
		return drvoSaKrosnjom;
	}
	public void setDrvoSaKrosnjom(boolean drvoSaKrosnjom) {
		this.drvoSaKrosnjom = drvoSaKrosnjom;
	}
	public boolean isOluciZardjali() {
		return oluciZardjali;
	}
	public void setOluciZardjali(boolean oluciZardjali) {
		this.oluciZardjali = oluciZardjali;
	}
	public List<String> getPotrebniRadovi() {
		return potrebniRadovi;
	}
	public void setPotrebniRadovi(List<String> potrebniRadovi) {
		this.potrebniRadovi = potrebniRadovi;
	}
	public double getUkupnaCenaRadova() {
		return ukupnaCenaRadova;
	}
	public void setUkupnaCenaRadova(double ukupnaCenaRadova) {
		this.ukupnaCenaRadova = ukupnaCenaRadova;
	}
	
	public void dodajRadove(String rad){
		this.potrebniRadovi.add(rad);
	}
	public void dodajProkisnjavanje(String prokisnjavanje){
		this.prokisnjava.add(prokisnjavanje);
	}
}
