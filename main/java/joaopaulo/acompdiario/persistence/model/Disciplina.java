package joaopaulo.acompdiario.persistence.model;

public class Disciplina extends Model implements Comparable<Disciplina> {
	private String nome;
	private CategoriaCientifica categoriaCientifica;
	
	public static final String ACTUAL_NAME = "Disciplina";
	
	
	public Disciplina() {
		this.categoriaCientifica = new CategoriaCientifica();
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public CategoriaCientifica getCategoriaCientifica() {
		return categoriaCientifica;
	}
	
	public void setCategoriaCientifica(CategoriaCientifica categoriaCientifica) {
		this.categoriaCientifica = categoriaCientifica;
	}

	@Override
	public String toString() {
		return nome;
	}

	@Override
	public int compareTo(Disciplina another) {
		return another.getNome().compareTo(this.nome);
	}

	
}
