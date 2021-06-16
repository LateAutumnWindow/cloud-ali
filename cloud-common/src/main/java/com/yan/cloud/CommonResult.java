package com.yan.cloud;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yan.cloud.constant.MsgCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult {
    private Integer code;
    private String  message;
    private Object  data;
    private Long total;
    private Integer page;

    public static CommonResult success(String msg) {
        return new CommonResult(MsgCode.SUCCESS, msg,null, 0L, 0);
    }
    public static CommonResult success(String msg, Object data) {
        return new CommonResult(MsgCode.SUCCESS, msg, data, 0L, 0);
    }
    public static CommonResult success(Object data) {
        return new CommonResult(MsgCode.SUCCESS, "执行成功", data, 0L, 0);
    }
    public static CommonResult error(String msg) {
        return new CommonResult(MsgCode.NO_LOGIN, msg,null, 0L, 0);
    }

    /**
     * 普通分页查询
     * @param list 数据
     */
    public static CommonResult list(List<?> list) {
        PageInfo<?> resp = new PageInfo<>(list);
        return new CommonResult(MsgCode.SUCCESS, "查询成功",
                list, resp.getTotal(), resp.getPages());
    }

    /**
     * PageHelper 后跟 if 的分页查询
     *
     * @param allData  第一次查询返回的数据
     * @param respData 实际要返回的数据
     */
    public static CommonResult list(List<?> allData, List<?> respData) {
        PageInfo<?> resp = new PageInfo<>(allData);

        CommonResult result = new CommonResult(MsgCode.SUCCESS, "查询成功",
                respData, resp.getTotal(), resp.getPages());

        PageHelper.clearPage();
        return result;
    }
}
