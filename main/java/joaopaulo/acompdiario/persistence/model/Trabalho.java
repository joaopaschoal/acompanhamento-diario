package joaopaulo.acompdiario.persistence.model;

/**
 * Created by JoaoPaulo on 07/07/2015.
 */
public class Trabalho extends Model implements Comparable<Trabalho> {

    private int tempoMins;

    private Projeto projeto;

    private Acompanhamento acompanhamento;

    public int getTempoMins() {
        return tempoMins;
    }

    public void setTempoMins(int tempoMins) {
        this.tempoMins = tempoMins;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Acompanhamento getAcompanhamento() {
        return acompanhamento;
    }

    public void setAcompanhamento(Acompanhamento acompanhamento) {
        this.acompanhamento = acompanhamento;
    }

    @Override
    public int compareTo(Trabalho another) {
        return another.getId().compareTo(this.getId());
    }
}
