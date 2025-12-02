package org.cityu.supermarket.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 * Query params wrapper for pagination stuff, replaces pageHelper
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
public class MemberQuery implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * Name filter
     */
    private String name;
    /**
     * Birthday filter
     */
    private String birthdayQuery;
    /**
     * Exact match for member ID
     */
    private String memberId;
    /**
     * Starting index
     */
    private Integer startIndex;
    /**
     * Items per page
     */
    private Integer pageSize;


    /**
     * format date
     **/

    public static String formatDate(Date date, String format) {
        try {
            if (StringUtils.isBlank(format)) {
                format = "yyyy-MM-dd";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        } catch (Exception var3) {
            return "";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdayQuery() {
        return birthdayQuery;
    }

    public void setBirthdayQuery(String order) {
        if (StringUtils.isBlank(order)) {
            this.birthdayQuery = null;
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - Integer.parseInt(order));

        this.birthdayQuery = formatDate(calendar.getTime(), "");
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }


    public void setPageIndex(Integer pageIndex, Integer pageSize) {
        this.startIndex = (pageIndex - 1) * pageSize;
        this.pageSize = pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getStartIndex() {
        return startIndex;
    }
}
