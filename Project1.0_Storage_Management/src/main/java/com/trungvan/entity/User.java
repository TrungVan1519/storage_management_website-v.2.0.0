package com.trungvan.entity;
// Generated May 26, 2020 9:24:19 AM by Hibernate Tools 5.2.12.Final

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "user", catalog = "Storage_Management")
public class User implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "username", nullable = false, length = 50)
	private String username;

	@Column(name = "password", nullable = false, length = 50)
	private String password;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "email", length = 100)
	private String email;

	@Column(name = "active_flag", nullable = false)
	private int activeFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false, length = 19)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", nullable = false, length = 19)
	private Date updatedDate;
	
	// OneToMany
	// > Phai su dung fetch = FetchType.EAGER thi moi co the lay ra object UserRole duoc, deo the hieu noi @@
	// > Gia thuyet giai thich o trong LoginController
	@OneToMany(mappedBy = "user", /* fetch = FetchType.EAGER, */
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, 
					CascadeType.DETACH, CascadeType.REFRESH})
	private Set<UserRole> userRoles = new HashSet<>();
}
