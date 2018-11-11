package joaopaulo.acompanhamentodiario.persistence.model;

import java.io.Serializable;

public class Assunto extends Model implements Comparable<Assunto>, Serializable {
	private String nome;
    private String nomeAbreviado;
	private Assunto assuntoPai;
	private Assunto assuntoFilho;
	private Disciplina disciplina;

	//Virtual attributes:
	private String nomeCompleto;

    //consts:
    public static final int DEFAULT_LENGTH = 20;
	
	public static final String ACTUAL_NAME = "Assunto";

	
	public Assunto() {
		this.disciplina = new Disciplina();
	}
	
	

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

    public String getNomeAbreviado() {
        return nomeAbreviado;
    }

    public void setNomeAbreviado(String nomeAbreviado) {
        this.nomeAbreviado = nomeAbreviado;
    }

    public Assunto getAssuntoPai() {
		if (assuntoPai == null) {
			assuntoPai = new Assunto();
		}
		return assuntoPai;
	}

	public void setAssuntoPai(Assunto assuntoPai) {
		this.assuntoPai = assuntoPai;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

    //Getters Calculados:
    public String getNomeCustomLength(int length) {
        String nomeCustomLength;
        if (this.getNome().length() > length) {
            if (this.getNomeAbreviado() != null && this.getNomeAbreviado() != "") {
                nomeCustomLength = this.getNomeAbreviado().length() > length ? this.getNomeAbreviado().substring(0, length-3)+"..." : this.getNomeAbreviado();
            }
            else {
                nomeCustomLength = this.getNome().substring(0, length-3)+"...";
            }
        } else {
            nomeCustomLength = this.getNome();
        }
        return nomeCustomLength;
    }

    public String getNomeCustomLength() {
        return getNomeCustomLength(DEFAULT_LENGTH);
    }

	@Override
	public String toString() {
		return (nomeCompleto == null || nomeCompleto == "") ? getNomeCustomLength() : nomeCompleto;
	}

	@Override
	public int compareTo(Assunto another) {
		return another.getNome().compareTo(this.nome);
	}
}
