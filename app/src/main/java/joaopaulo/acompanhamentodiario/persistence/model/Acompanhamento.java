package joaopaulo.acompanhamentodiario.persistence.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Acompanhamento extends Model implements Comparable<Acompanhamento> {
	private Date dataAcompanhamento;
	private Timestamp dataRegistro;
	private int periodoAerobica;
	private int periodoMusculacao;
	private Boolean flgExUr;
    private List<Estudo> estudos;
	private List<Trabalho> trabalhos;
	
	public static final String ACTUAL_NAME = "Acompanhamento";
	

	public Acompanhamento() {
        this.flgExUr = false;
        estudos = new ArrayList<>();
        trabalhos = new ArrayList<>();
	}
	
	// --- Getters e Setters --- //
	public Date getDataAcompanhamento() {
		return dataAcompanhamento;
	}

	public void setDataAcompanhamento(Date dataAcompanhamento) {
		this.dataAcompanhamento = dataAcompanhamento;
	}

	public Timestamp getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Timestamp dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public int getPeriodoAerobica() {
		return periodoAerobica;
	}

	public void setPeriodoAerobica(int periodoAerobica) {
		this.periodoAerobica = periodoAerobica;
	}

	public int getPeriodoMusculacao() {
		return periodoMusculacao;
	}

	public void setPeriodoMusculacao(int periodoMusculacao) {
		this.periodoMusculacao = periodoMusculacao;
	}

	public Boolean getFlgExUr() {
		return flgExUr;
	}

	public void setFlgExUr(Boolean flgExUr) {
		this.flgExUr = flgExUr;
	}

    public List<Estudo> getEstudos() {
        return estudos;
    }

    public void setEstudos(List<Estudo> estudos) {
        this.estudos = estudos;
    }

    public List<Trabalho> getTrabalhos() {
        return trabalhos;
    }

    public void setTrabalhos(List<Trabalho> trabalhos) {
        this.trabalhos = trabalhos;
    }

    @Override
	public String toString() {
		return "Acompanhamento ["+this.getId()+"]";
	}
	
	@Override
	public int compareTo(Acompanhamento another) {
		return another.getDataAcompanhamento().compareTo(this.dataAcompanhamento);
	}
}
