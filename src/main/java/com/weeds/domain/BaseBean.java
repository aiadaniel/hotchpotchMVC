package com.weeds.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class BaseBean implements Serializable{
	
	private static final long serialVersionUID = -6603938214228541893L;

	@Version
	@JsonIgnore
	private Integer version;
	
	@JsonIgnore
	private boolean deleted;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	//@JsonIgnore
	private Date dateCreated;


	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

}
