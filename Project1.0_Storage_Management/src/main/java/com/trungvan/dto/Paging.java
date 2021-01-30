package com.trungvan.dto;

import lombok.Setter;

@Setter
public class Paging {

	private long totalRows;
	private int totalPages;
	private int indexPage;		// > Trang hien tai
	private int recordPerPage;	// > So record moi trang mac dinh
	private int offset;			// > STT khi query: 0-9, 10-19, 20-29, ...
	
	public Paging(int recordPerPage) {
		
		this.recordPerPage = recordPerPage;
	}

	public long getTotalRows() {
		
		return totalRows;
	}
	
	public int getTotalPages() {
		
		if(totalRows > 0) {
			
			totalPages = (int) Math.ceil(totalRows / (double) recordPerPage);
		}
		return totalPages;
	}

	public int getIndexPage() {
		
		return indexPage;
	}

	public int getRecordPerPage() {
		
		return recordPerPage;
	}
	
	public int getOffset() {
		
		if(indexPage > 0) {
			
			offset = indexPage * recordPerPage - recordPerPage;	// > 1*10 - 10 = 0, 2*10 - 10 = 10, ...
		}
		return offset;
	}
}
