package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private Graph<Business, DefaultWeightedEdge> grafo;
	private YelpDao dao;
	private List<Business> best;
	private Map<String, Business> idMap;
	
	public Model() {
		dao = new YelpDao();
		idMap = new HashMap<>();
		
		for(Business business : dao.getAllBusiness()) {
			idMap.put(business.getBusinessId(), business);
		}
	}
	
	public List<String> getCity() {
		return dao.getCity();
	}
	
	public void creaGrafo(String citta) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(citta));
		
		for(Adiacenza adiacenza : dao.getArchi(citta, idMap)) {
			Graphs.addEdgeWithVertices(grafo, adiacenza.getVertice1(), adiacenza.getVertice2(), adiacenza.getPeso());
		}
	}
	
	public Set<Business> getVertici() {
		return grafo.vertexSet();
	}
	
	public Integer getNVertici() {
		return grafo.vertexSet().size();
	}
	 
	public Integer getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public String getLocaleDistante(Business b1) {
		Double pesoMax = 0.0;
		Business bestBusiness = null;
		
		for(Business b2 : Graphs.neighborListOf(grafo, b1)) {
			Double peso = grafo.getEdgeWeight(grafo.getEdge(b1, b2));
			if(peso > pesoMax) {
				pesoMax = peso;
				bestBusiness = b2;
			}
		}
		
		return bestBusiness.getBusinessName() + " - Distanza: " + pesoMax;
	}
	
	public String trovaSequenza(Business partenza, Business arrivo, int x) {
		String string = "";
		best = new ArrayList<>();
		Double kmTot = 0.0;
		List<Business> parziale = new ArrayList<>();
		parziale.add(partenza);
		cerca(parziale, partenza, arrivo, x);
		
		string += partenza + "\n";
		for(int i = 1; i < best.size(); i++) {
			Double peso = grafo.getEdgeWeight(grafo.getEdge(best.get(i-1), best.get(i)));
			kmTot += peso;
			string += best.get(i) + "\n";
		}
		
		string += "\nKM totali: " + kmTot;
		return string;
	}

	private void cerca(List<Business> parziale, Business partenza, Business arrivo, int x) {
		if(parziale.get(parziale.size()-1).equals(arrivo)) {
			if(parziale.size() > best.size()) {
				best = new ArrayList<>(parziale);
			}
			return;
		}
		
		for(Business business : grafo.vertexSet()) {
			if(!business.equals(partenza)) {
				if(!parziale.contains(business)) {
					if(business.equals(arrivo)) {
						parziale.add(business);
						cerca(parziale, partenza, arrivo, x);
						parziale.remove(parziale.size()-1);
					}
					else {
						if(business.getStars() > x) {
							parziale.add(business);
							cerca(parziale, partenza, arrivo, x);
							parziale.remove(parziale.size()-1);
						}	
					}
					
				}
			}
			
			
		}
	}
	
}
