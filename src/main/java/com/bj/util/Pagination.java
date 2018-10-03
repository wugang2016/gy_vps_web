package com.bj.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by liqingkun on 2017-3-6.
 */
public class Pagination {
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_PAGE_PARAMETER = "p";

    private int totalItemCount = 0;
    private int pageItemCount = 0;

    private HttpServletRequest request;
    private String pageParameter = DEFAULT_PAGE_PARAMETER;

    private int currentPageNo;
    private int totalPageNo;
    private List<Page> pageList;
    
    public void setPageParameter(String pageParameter) {
    	this.pageParameter = pageParameter;
    }

    public Pagination(HttpServletRequest request, int currentPageNo, int totalItemCount, int pageItemCount) {
        this(request, currentPageNo, (totalItemCount + pageItemCount - 1) / pageItemCount);

        this.totalItemCount = totalItemCount;
        this.pageItemCount = pageItemCount;
    }

    public Pagination(HttpServletRequest request, int currentPageNo, int totalPageNo) {
        this.request = request;
        this.currentPageNo = currentPageNo;
        this.totalPageNo = totalPageNo;

        int startPageNo = (currentPageNo - 5 > 1) ? (currentPageNo - 5) : 1;
        int endPageNo = (currentPageNo + 5 < totalPageNo) ? (currentPageNo + 5) : totalPageNo;
        this.pageList = initPageList(startPageNo, endPageNo);
    }

    private static String getFullURL(HttpServletRequest httpServletRequest) {
        StringBuffer requestURL = httpServletRequest.getRequestURL();
        String queryString = httpServletRequest.getQueryString();

        if (StringUtils.hasText(queryString)) {
            requestURL.append("?").append(queryString);
        }

        return requestURL.toString();
    }

    private List<Page> initPageList(int startPageNo, int endPageNo) {
        List<Page> list = new ArrayList<>(10);
        for (int p = startPageNo; p <= endPageNo; p++) {
            Page page = new Page();
            page.setPageNo(p);
            page.setCurrent(p == currentPageNo);
            list.add(page);
        }
        return list;
    }

    public boolean hasPreviousPage() {
        return currentPageNo > 1;
    }

    public int getPreviousPageNo() {
        return currentPageNo - 1;
    }

    public String getPreviousPageUri() {
        return buildPageUri(getPreviousPageNo());
    }

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public String getCurrentPageUri() {
        return buildPageUri(getCurrentPageNo());
    }

    public boolean hasNextPage() {
        return currentPageNo < totalPageNo;
    }

    public int getNextPageNo() {
        return currentPageNo + 1;
    }

    public String getNextPageUri() {
        return buildPageUri(getNextPageNo());
    }

    public int getTotalPageNo() {
        return totalPageNo;
    }

    public List<Page> getPageList() {
        return pageList;
    }

    private String buildPageUri(int pageNo) {
        String fullURL = getFullURL(request);
        UriComponents uri = UriComponentsBuilder.fromUriString(fullURL)
                .replaceQueryParam(pageParameter, pageNo)
                .build();
        return uri.toUriString();
    }

    public String getDescription() {
        if (totalItemCount > 0 && pageItemCount > 0) {
            int itemStart = (currentPageNo - 1) * pageItemCount + 1;
            int itemEnd = currentPageNo * pageItemCount;
            if (itemEnd > totalItemCount) {
                itemEnd = totalItemCount;
            }
            return "当前显示 " + itemStart + " 至 " + itemEnd + " 条记录，总记录数 " + totalItemCount + " 条";
        }
        return "";
    }

    public class Page {
        private int pageNo;
        private boolean current;

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public String getPageUri() {
            return buildPageUri(getPageNo());
        }

        public boolean isCurrent() {
            return current;
        }

        public void setCurrent(boolean current) {
            this.current = current;
        }
    }
}
