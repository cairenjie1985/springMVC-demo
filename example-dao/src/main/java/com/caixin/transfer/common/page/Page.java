package com.caixin.transfer.common.page;

/**
 * * mybatis分页公共模块 分页信息类
 * 
 * @author linxz
 * @date 2014年12月17日 上午9:24:48
 * @Description:
 */
public class Page implements java.io.Serializable {
	private static final long serialVersionUID = -8056144044596649805L;

	protected int pageSize = 10;// 每页默认10条数据
	protected int currentPage = 1;// 当前页
	protected int totalPages = 0;// 总页数
	protected int totalRows = 0;// 总数据数

	
	protected int offset1 = 0;// 数据的起始位置
	protected int limit1 = 10;// 查询的数据数

	public Page() {
	}

	public Page(int rows, int pageSize) {
		this.init(rows, pageSize);
	}

	/** 初始化分页参数:需要先设置totalRows */
	public void init(int rows, int pageSize) {
		this.pageSize = pageSize;
		this.totalRows = rows;
		if ((totalRows % pageSize) == 0) {
			totalPages = totalRows / pageSize;
		} else {
			totalPages = totalRows / pageSize + 1;
		}

	}

	public void init(int rows, int pageSize, int currentPage) {
		this.init(rows, pageSize);
		this.currentPage = currentPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalPages(int i) {
		totalPages = i;
	}

	public void setCurrentPage(int i) {
		currentPage = i;
	}

	public void setPageSize(int i) {
		pageSize = i;
	}

	public void setTotalRows(int i) {
		totalRows = i;
	}
	public int getOffset1() {
		return this.getPageSize() * (this.getCurrentPage() - 1);
	}
	
	public void setOffset1(int offset1) {
		this.offset1 = offset1;
	}

	public int getLimit1() {
		return this.getPageSize();
	}

	public void setLimit(int limit) {
		this.limit1 = limit;
	}

}
