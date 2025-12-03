package org.cityu.supermarket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Customer behavior analysis entity
 *
 * @version 0.0.1
 * @date 2025-12-01
 */
@Data
public class CustomerBehavior {
    private Integer id;
    private String memberId;
    private String cardId;
    private Integer behaviorType;   // Behavior type: 1=browse, 2=add to cart, 3=purchase, 4=favorite
    private Integer productId;
    private String productName;     // Redundant field for frontend display
    private String productCategory; // Redundant field for frontend display
    private BigDecimal orderAmount; // Order amount
    private String sessionId;       // Session ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date behaviorTime;     // Behavior time

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}