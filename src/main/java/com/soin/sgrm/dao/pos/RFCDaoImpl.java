package com.soin.sgrm.dao.pos;

import java.util.List;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PRFC;

@Repository("rfcDao")
public class RFCDaoImpl extends AbstractDao<Long, PRFC> implements RFCDao {

}