package joaopaulo.acompdiario.business.exception;

//TODO: verificar se InsertModelException e UpdateModelException não deveriam estar na camada DAO
public class InsertModelException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public InsertModelException() {
		super();
	}

	public InsertModelException(String msg) {
		super(msg);
	}
	
	public InsertModelException(Throwable ex) {
		super(ex);
	}
	
	public InsertModelException(String msg, Throwable ex) {
		super(msg, ex);
	}

}
