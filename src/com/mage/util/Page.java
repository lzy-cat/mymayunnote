package com.mage.util;

import java.util.Arrays;
import java.util.List;

public class Page<T>{
	//这四个Integer变量和dataList需要从后台获取其他不用管
	private Integer totalCount = 0;//总记录数
	private Integer pageSize = 4;//每页的记录数
	private Integer totalpages = 1;//总页面数
	private Integer currentPage = 1;//当前页码	
	//
	private boolean isFirst;//是否为首页
	private boolean isLast;//是否为尾页
	private boolean hasPrev;//是否有上一页
	private boolean hasLast;//是否有下一页
	private Integer navPages = 8;//总的导航页数
	private Integer[] navPageNumbers;//导航页码数组
	private List<T> dataList;//每页存放的数据
	
	
	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalpages() {
		return totalpages;
	}

	public void setTotalpages(Integer totalpages) {
		this.totalpages = totalpages;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public boolean isHasPrev() {
		return hasPrev;
	}

	public void setHasPrev(boolean hasPrev) {
		this.hasPrev = hasPrev;
	}

	public boolean isHasLast() {
		return hasLast;
	}

	public void setHasLast(boolean hasLast) {
		this.hasLast = hasLast;
	}

	public Integer getNavPages() {
		return navPages;
	}

	public void setNavPages(Integer navPages) {
		this.navPages = navPages;
	}

	public Integer[] getNavPageNumbers() {
		return navPageNumbers;
	}

	public void setNavPageNumbers(Integer[] navPageNumbers) {
		this.navPageNumbers = navPageNumbers;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public Page() {	
	}

	public Page(Integer totalCount,Integer currentPage) {
		init(totalCount,pageSize,currentPage);
	}
	
	public Page(Integer totalCount, Integer pageSize, Integer currentPage) {
		init(totalCount,pageSize,currentPage);
	}

	//实现分页调用这个主方法
	public void init(Integer totalCount, Integer pageSize, Integer currentPage) {	
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.totalpages = (totalCount-1)/pageSize+1;
		
		if(currentPage < 1){
	    	this.currentPage = 1;
	    }else if(currentPage > this.totalpages){
	        this.currentPage = this.totalpages;
	    }else{
	        this.currentPage = currentPage;
	    }	
		//基本参数设定之后进行导航页面的计算
        calNavPageNumbers();
        //以及页面边界的判定
        judgePageBoudary();
		
	}

	private void judgePageBoudary() {
		 //当总页数小于或等于导航页码数时
        if(totalpages <= navPages){
            navPageNumbers = new Integer[totalpages];
            for(int i=0;i<totalpages;i++){
            	navPageNumbers[i] = i+1;
            }
        }else{ //当总页数大于导航页码数时
        	navPageNumbers = new Integer[navPages];
        	
        	//导航条中的起始页码和尾页码
            int startNum = currentPage - navPages/2;
            int endNum = currentPage + navPages/2;
             
            if(startNum<1){
                startNum=1;
                //(最前navPageCount页
                for(int i = 0;i<navPages;i++){
                    navPageNumbers[i] = startNum++;
                }
            }else if(endNum > totalpages){
                endNum = totalpages;
                //最后navPageCount页
                for(int i = navPages-1;i>=0;i--){
                    navPageNumbers[i] = endNum--;
                }
            }else{
                //所有中间页
                for(int i=0;i<navPages;i++){
                    navPageNumbers[i] = startNum++;
                }
            }
        }
	}

	private void calNavPageNumbers() {
		isFirst = currentPage == 1;
		isLast = currentPage == totalpages;
		hasPrev = currentPage != 1;
		hasLast = currentPage != totalpages;
		
	}

	@Override
	public String toString() {
		return "Page [totalCount=" + totalCount + ", pageSize=" + pageSize + ", totalpages=" + totalpages
				+ ", currentPage=" + currentPage + ", isFirst=" + isFirst + ", isLast=" + isLast + ", hasPrev="
				+ hasPrev + ", hasLast=" + hasLast + ", navPages=" + navPages + ", navPageNumbers="
				+ Arrays.toString(navPageNumbers) + ", dataList=" + dataList + "]";
	}

	
}
