package com.trungvan.entity;
// Generated May 26, 2020 9:24:19 AM by Hibernate Tools 5.2.12.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth", catalog = "Storage_Management")
public class Auth implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	// foreign key to role table
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, 
			CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

	// foreign key to menu table
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, 
			CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "menu_id", nullable = false)
	private Menu menu;

	@Column(name = "permission", nullable = false)
	private int permission;

	@Column(name = "active_flag", nullable = false)
	private int activeFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false, length = 19)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", nullable = false, length = 19)
	private Date updatedDate;
}
