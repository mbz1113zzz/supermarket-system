package org.cityu.supermarket.entity;

import java.io.Serializable;
import java.util.List;


/**
 * Wrapper for paginated data
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
public class PageResult<T> implements Serializable {
    private Long pageTotal;
    private List<T> list;
    private static final long serialVersionUID = 1L;
    public Long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Long pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
