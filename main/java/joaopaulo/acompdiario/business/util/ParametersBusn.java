package joaopaulo.acompdiario.business.util;

public class ParametersBusn {
	
	public static String getMsgModelSuccessfulSaved(String modelName) {
		return modelName + " Salvo com Sucesso!";
	}
	
	public static String getMsgFailedSavingModel(String modelName) {
		return "Falha ao salvar " + modelName;
	}
	
	public static String getMsgFailedInsertingModel(String modelName) {
		return "Falha ao inserir " + modelName;
	}
	
	public static String getMsgFailedUpdatingModel(String modelName) {
		return "Falha ao editar " + modelName;
	}

    public static int ID_DISCIPLINA_DEFAULT = 1;
}
