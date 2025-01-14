package com.soin.sgrm.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.ReleaseByTime;

@Repository
public class StatisticDaoImpl implements StatisticDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	@Override
	public List<ReleaseByTime> getLastFourYears() {

		Calendar calStart = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		Date start;
		Date end;

		Calendar calendar = new GregorianCalendar();
		int currentYear = calendar.get(Calendar.YEAR);
		int year = currentYear - 3;

		Criteria crit;
		Long count;
		List<ReleaseByTime> list = new ArrayList();
		ReleaseByTime temp = null;
		for (int i = year; i <= currentYear; i++) {
			for (int j = 0; j <= 3; j++) {

				calStart.set(Calendar.YEAR, i);
				calStart.set(Calendar.DAY_OF_YEAR, 1);
				start = calStart.getTime();

				calEnd.set(Calendar.YEAR, i);
				calEnd.set(Calendar.MONTH, 11); // 11 = december
				calEnd.set(Calendar.DAY_OF_MONTH, 31); // new years eve
				end = calEnd.getTime();

				crit = sessionFactory.getCurrentSession().createCriteria(ReleaseEdit.class);
				crit.add(Restrictions.between("createDate", start, end));
				crit.createAlias("status", "status");
				crit.add(Restrictions.eq("status.name", Constant.STATISTICBAR[j]));

				crit.setProjection(Projections.rowCount());
				count = (Long) crit.uniqueResult();
				temp = new ReleaseByTime();
				temp.setYear(i + "");
				temp.setStatus(Constant.STATISTICBAR[j]);
				temp.setAmount(count.intValue() + "");
				list.add(temp);
			}

		}

		for (ReleaseByTime releaseByTime : list) {
			if (releaseByTime.getStatus().equals("Certificacion")) {
				releaseByTime.setStatus("Certificación");
			}
			if (releaseByTime.getStatus().equals("En Revision")) {
				releaseByTime.setStatus("En Revisión");
			}
		}
		return list;
	}

	@Override
	public List<ReleaseByTime> getLastMonth() {

		Calendar calStart = Calendar.getInstance(), calEnd = Calendar.getInstance();
		int year = calStart.get(Calendar.YEAR), month = calStart.get(Calendar.MONTH);
		Date start, end;
		Criteria crit;
		Long count;
		List<ReleaseByTime> list = new ArrayList();

		// Se define el primer dia del mes
		calStart.set(year, month, 1, 0, 0, 0);
		start = calStart.getTime();

		// Se define el ultimo dia del mes
		calEnd.set(year, month, 1, 23, 59, 59);
		calEnd.add(Calendar.MONTH, 1);
		calEnd.add(Calendar.DAY_OF_YEAR, -2);
		end = calEnd.getTime();
		ReleaseByTime temp = null;
		for (int i = 0; i < 4; i++) {
			crit = sessionFactory.getCurrentSession().createCriteria(ReleaseEdit.class);
			crit.add(Restrictions.between("createDate", start, end));
			crit.createAlias("status", "status");
			crit.add(Restrictions.eq("status.name", Constant.STATISTICBAR[i]));

			crit.setProjection(Projections.rowCount());
			count = (Long) crit.uniqueResult();
			temp = new ReleaseByTime();
			temp.setYear(year + "");
			temp.setStatus(Constant.STATISTICBAR[i]);
			temp.setAmount(count.intValue() + "");
			list.add(temp);
		}

		return list;
	}

}
