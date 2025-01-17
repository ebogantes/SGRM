package com.soin.sgrm.service;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.TypeRequest;

public interface RequestService {

	List<Request> list(String search, Object[] projects) throws SQLException;

	Request findByName(String code_soin, String code_ice);

	Request findByName(String code_soin);

	List<Request> list();

	List<Request> listByType(TypeRequest type);

	Request findById(Integer id);

	void save(Request request);

	void update(Request request);

	void delete(Integer id);
	
	void softDelete(Request request);

}
