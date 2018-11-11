package joaopaulo.acompanhamentodiario.persistence.dao.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.ContentValues;

public class UtilDAOImpl implements UtilDAO {
	@Override
	public String buildWhereClauseByCV(ContentValues cv) {
		if (cv == null) {
			return null;
		}
		
    	StringBuilder whereClause = new StringBuilder();
    	for(String key : cv.keySet()) {
    		whereClause.append(key);
            if (cv.get(key) != null) {
                whereClause.append(" = ?");
            } else {
                whereClause.append(" is null");
            }
            whereClause.append(" and ");
    	}
    	whereClause.setLength(whereClause.length()-5);
    	return whereClause.toString();
    }
    
	@Override
    public String[] buildWhereParamsByCV(ContentValues cv) {
    	String[] params = null;
    	if (cv != null) {
    		Set<Entry<String,Object>> set = cv.valueSet();
    		Iterator<Entry<String, Object>> it = set.iterator();
    		List<String> listParams = new ArrayList<>();
    		while(it.hasNext()) {
    			Map.Entry<String,Object> me = it.next();
                if (me.getValue() == null) {
                    continue;
                }
                String value =  me.getValue().toString();
                listParams.add(value);
    		}
    		params = listParams.toArray(new String[listParams.size()]);    		
    	}
    	return params;
    }

}
