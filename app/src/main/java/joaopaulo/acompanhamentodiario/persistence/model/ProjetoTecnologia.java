package joaopaulo.acompanhamentodiario.persistence.model;

/**
 * Created by JoaoPaulo on 07/07/2015.
 */
public class ProjetoTecnologia extends Model {

    public ProjetoTecnologia() {
        this.projeto = new Projeto();
        this.tecnologia = new Tecnologia();
    }

    private boolean flgTecnologiaPrincipal;

    private Projeto projeto;

    private Tecnologia tecnologia;

    public boolean isFlgTecnologiaPrincipal() {
        return flgTecnologiaPrincipal;
    }

    public void setFlgTecnologiaPrincipal(boolean flgTecnologiaPrincipal) {
        this.flgTecnologiaPrincipal = flgTecnologiaPrincipal;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Tecnologia getTecnologia() {
        return tecnologia;
    }

    public void setTecnologia(Tecnologia tecnologia) {
        this.tecnologia = tecnologia;
    }
}
