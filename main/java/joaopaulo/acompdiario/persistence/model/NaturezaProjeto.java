package joaopaulo.acompdiario.persistence.model;

/**
 * Created by JoaoPaulo on 07/07/2015.
 */
public class NaturezaProjeto extends Model implements Comparable<NaturezaProjeto> {
    public static final String ACTUAL_NAME = "Natureza Projeto";

    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    @Override
    public int compareTo(NaturezaProjeto another) {
        return another.getDescricao().compareTo(this.descricao);
    }
}
