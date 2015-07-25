package joaopaulo.acompdiario.persistence.model;

import java.io.Serializable;

public class Estudo extends Model implements Comparable<Estudo>, Serializable {
	private int tempoMins;
	private Assunto assunto;
	private Acompanhamento acompanhamento;
	
	public static final String ACTUAL_NAME = "Estudo";
	
	
	public Estudo() {
		this.assunto = new Assunto();
		this.acompanhamento = new Acompanhamento();
	}

	
	public int getTempoMins() {
		return tempoMins;
	}

	public void setTempoMins(int tempoMins) {
		this.tempoMins = tempoMins;
	}

	public Assunto getAssunto() {
		return assunto;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

	public Acompanhamento getAcompanhamento() {
		return acompanhamento;
	}

	public void setAcompanhamento(Acompanhamento acompanhamento) {
		this.acompanhamento = acompanhamento;
	}


	@Override
	public String toString() {
		return "Estudo [" + this.getId() + "]";
	}

	@Override
	public int compareTo(Estudo another) {
		return another.getId().compareTo(this.getId());
	}

	
}
