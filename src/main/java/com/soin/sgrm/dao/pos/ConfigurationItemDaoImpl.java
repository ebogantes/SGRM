package com.soin.sgrm.dao.pos;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.soin.sgrm.model.pos.PConfigurationItem;
import com.soin.sgrm.response.JsonSheet;

public class ConfigurationItemDaoImpl extends AbstractDao<Long, PConfigurationItem> implements ConfigurationItemDao{

	@Override
	public PConfigurationItem getByKey(Long key, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PConfigurationItem model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void merge(PConfigurationItem model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(PConfigurationItem model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PConfigurationItem model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByKey(Long key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(PConfigurationItem model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonSheet<PConfigurationItem> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PConfigurationItem> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonSheet<PConfigurationItem> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PConfigurationItem> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
