package it.polito.tdp.yelp.model;

public class Adiacenza implements Comparable<Adiacenza> {
	private Business vertice1;
	private Business vertice2;
	private Double peso;
	
	public Adiacenza(Business vertice1, Business vertice2, Double peso) {
		super();
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
		this.peso = peso;
	}
	public Business getVertice1() {
		return vertice1;
	}
	public void setVertice1(Business vertice1) {
		this.vertice1 = vertice1;
	}
	public Business getVertice2() {
		return vertice2;
	}
	public void setVertice2(Business vertice2) {
		this.vertice2 = vertice2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(Adiacenza o) {
		return this.peso.compareTo(o.peso);
	}
	
}
