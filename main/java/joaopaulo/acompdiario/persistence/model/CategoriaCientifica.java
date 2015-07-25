package joaopaulo.acompdiario.persistence.model;

public class CategoriaCientifica extends Model implements Comparable<CategoriaCientifica> {
	public static final String ACTUAL_NAME = "Categoria Científica";
	
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return nome;
	}

	@Override
	public int compareTo(CategoriaCientifica another) {
		return another.getNome().compareTo(this.nome);
	}
}
