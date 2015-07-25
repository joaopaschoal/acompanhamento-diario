package joaopaulo.acompdiario.business.bo;

import java.util.List;

import joaopaulo.acompdiario.business.exception.InsertModelException;
import joaopaulo.acompdiario.business.exception.QueryModelException;
import joaopaulo.acompdiario.business.exception.SaveModelException;
import joaopaulo.acompdiario.business.exception.UpdateModelException;
import joaopaulo.acompdiario.business.exception.ValidationException;
import joaopaulo.acompdiario.persistence.dao.DAO;
import joaopaulo.acompdiario.persistence.dao.exception.ModelNotPersistedException;
import joaopaulo.acompdiario.persistence.model.Model;

import android.content.ContentValues;


public abstract class BO<M extends Model, D extends DAO<M>> {
	
	protected D dao;
	
	
	// ----- Business Methods ----- //
	
	public abstract void validate(M model) throws ValidationException;
	
	
	
	// ----- Data Access Methods ----- //
	
	public void open() {
		dao.open();
	}
	
	public void close() {
		dao.close();
	}
	
	public M save(M model) throws SaveModelException {
		try {
			validate(model);
            beforeSave(model);
			if (model.getId() != null && model.getId() > 0) {
	    		dao.update(model);
	    	} else {
	    		dao.insert(model);
	    	}
            afterSave(model);
		} catch (ValidationException ex) {
			throw new SaveModelException("Houve uma falha de validação: " + ex.getMessage(), ex);
		} catch (InsertModelException ex) {
			throw new SaveModelException("Falha de Banco de Dados: ao inserir o registro", ex);
		} catch (UpdateModelException ex) {
			throw new SaveModelException("Houve uma falha de banco de dados ao editar o registro", ex);
		}
		return model;
	}

    public void beforeSave(M model) { };

    public void afterSave(M model) { };
	
	public void delete(M model) {
		try {
			dao.delete(model);
		} catch (ModelNotPersistedException ex) {
			
		}
	}
	
	public List<M> select(ContentValues cv) throws QueryModelException {
		return dao.select(cv);
	}
	
	public List<M> select(ContentValues cv, String sortField) throws QueryModelException {
		return dao.select(cv,sortField);
	}
	
	public List<M> selectAll() throws QueryModelException {
		return dao.selectAll();
	}
	
	public M selectOneById(Integer id) throws QueryModelException {
		return dao.selectOneById(id);
	}
	
	public int selectCountRows(ContentValues cv) {
		return dao.selectCountRows(cv);
	}
	
}
