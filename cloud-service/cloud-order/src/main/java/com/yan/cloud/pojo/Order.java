package com.yan.cloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Integer id;

    private String userId;

    private String commodityCode;

    private Integer count;

    private Integer money;

}
