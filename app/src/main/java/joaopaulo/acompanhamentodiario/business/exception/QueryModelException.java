package joaopaulo.acompanhamentodiario.business.exception;

public class QueryModelException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public QueryModelException() {
		super();
	}

	public QueryModelException(String msg) {
		super(msg);
	}
	
	public QueryModelException(Throwable ex) {
		super(ex);
	}
	
	public QueryModelException(String msg, Throwable ex) {
		super(msg, ex);
	}

}
