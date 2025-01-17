package com.soin.sgrm.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

@SuppressWarnings("serial")
@Entity
@Table(name = "SISTEMAS_OBJETO")
public class ReleaseObjectEdit implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SISTEMAS_OBJETO_SQ")
	@SequenceGenerator(name="SISTEMAS_OBJETO_SQ", sequenceName="SISTEMAS_OBJETO_SQ", allocationSize=1)
	@Column(name = "ID")
	private int id;

	@Column(name = "NOMBRE")
	private String name;

	@Column(name = "DESCRIPCION")
	private String description;

	@Column(name = "REVISION_RESPOSITORIO")
	private String revision_SVN;

	@Column(name = "FECHA_REVISION")
	private Date revision_Date;

	@Column(name = "TIPO_OBJETO_ID")
	private int typeObject;

	@Column(name = "ITEM_DE_CONFIGURACION_ID")
	private int itemConfiguration;
	
	@Column(name = "MODULO_ID")
	private int moduleId;

	@Value("${execute:0}")
	@Column(name = "OCUPA_EJECUTAR")
	private int execute;

	@Column(name = "ESQUEMA")
	private String dbScheme;

	@Value("${executePlan:0}")
	@Column(name = "PLAN_EJECUCION")
	private int executePlan;

	@Value("${isSql:0}")
	@Column(name = "SQL")
	private int isSql;

	public ReleaseObjectEdit() {
	}

	public ReleaseObjectEdit(String name, String description, String revision_SVN, int moduleId) {
		super();
		this.name = name;
		this.description = description;
		this.revision_SVN = revision_SVN;
		this.moduleId = moduleId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRevision_SVN() {
		return revision_SVN;
	}

	public void setRevision_SVN(String revision_SVN) {
		this.revision_SVN = revision_SVN;
	}

	public Date getRevision_Date() {
		return revision_Date;
	}

	public void setRevision_Date(Date revision_Date) {
		this.revision_Date = revision_Date;
	}

	public int getTypeObject() {
		return typeObject;
	}

	public void setTypeObject(int typeObject) {
		this.typeObject = typeObject;
	}

	public int getItemConfiguration() {
		return itemConfiguration;
	}

	public void setItemConfiguration(int itemConfiguration) {
		this.itemConfiguration = itemConfiguration;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public int getExecute() {
		return execute;
	}

	public void setExecute(int execute) {
		this.execute = execute;
	}

	public String getDbScheme() {
		return dbScheme;
	}

	public void setDbScheme(String dbScheme) {
		this.dbScheme = dbScheme;
	}

	public int getExecutePlan() {
		return executePlan;
	}

	public void setExecutePlan(int executePlan) {
		this.executePlan = executePlan;
	}

	public int getIsSql() {
		return isSql;
	}

	public void setIsSql(int isSql) {
		this.isSql = isSql;
	}

	
}
