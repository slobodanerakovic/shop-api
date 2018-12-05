package com.shop.db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * @author Slobodan Erakovic - Used as place holder for certain properties
 */
@MappedSuperclass
public abstract class AbstractEntity {

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date", nullable = false, updatable = false)
	protected Date creationDate;

	@Version
	private Integer version;

	public Integer getVersion() {
		return version;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Skip checking some util properties from business code. Instead, kae it here
	 * as default
	 */
	@PrePersist
	public void prePersist() {
		if (creationDate == null) {
			creationDate = new Date();
		}
	}

}
