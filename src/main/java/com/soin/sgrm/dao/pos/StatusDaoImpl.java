package com.soin.sgrm.dao.pos;

import java.util.List;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PStatus;

@Repository("statusDao")
public class StatusDaoImpl extends AbstractDao<Long, PStatus> implements StatusDao {


}
