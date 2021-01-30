package com.trungvan.entity;
// Generated May 26, 2020 9:24:19 AM by Hibernate Tools 5.2.12.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_info", catalog = "Storage_Management")
public class ProductInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	// foreign key to category table
	@ManyToOne(cascade = { CascadeType.PERSIST, /* CascadeType.MERGE, */ // > Bo Merge thi moi update cho ProductInfo duoc 
			CascadeType.DETACH, CascadeType.REFRESH})
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "code", nullable = false, length = 50)
	private String code;

	@Column(name = "description", length = 65535)
	private String description;

	@Column(name = "image_url", length = 200)
	private String imageUrl;

	@Column(name = "active_flag", nullable = false)
	private int activeFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false, length = 19)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", nullable = false, length = 19)
	private Date updatedDate;
	
	// OneToMany
	@OneToMany(mappedBy = "productInfo", /* fetch = FetchType.LAZY, */
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, 
					CascadeType.DETACH, CascadeType.REFRESH})
	private Set<ProductInStock> productInStocks = new HashSet<>();
	
	// OneToMany
	@OneToMany(mappedBy = "productInfo", /* fetch = FetchType.LAZY, */
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, 
					CascadeType.DETACH, CascadeType.REFRESH})
	private Set<History> histories = new HashSet<>();
	
	// OneToMany
	@OneToMany(mappedBy = "productInfo", /* fetch = FetchType.LAZY, */
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, 
					CascadeType.DETACH, CascadeType.REFRESH})
	private Set<Invoice> invoices = new HashSet<>();
	
	// > @Transient co tac dung danh dau cac prop la cac prop khong co trong table cua DB
	//		ma no la cac prop binh thuong do ta tu them vao trong class Entity de lam 
	//		cac viec khac
	// > Cach tot nhat van la nen tach class Entity tong hop thanh 2 class Entity va class DTO
	//	- Trong do class Entity chi nen chua cac prop de mapped voi cac cot trong table cua DB
	//	- Va class DTO se chua cac prop binh thuong va cac prop ben ngoai do ta tu them cao
	//		(chinh la cac prop su dung @Transient trong class nay ne)
	@Transient
	private MultipartFile multipartFile;

	@Override
	public String toString() {
		return "ProductInfo [id=" + id + ", category=" + category + ", name=" + name + ", code=" + code
				+ ", description=" + description + ", imageUrl=" + imageUrl + ", activeFlag=" + activeFlag
				+ ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + ", productInStocks="
				+ productInStocks + ", histories=" + histories + ", invoices=" + invoices + ", multipartFile="
				+ multipartFile + "]";
	}
}
