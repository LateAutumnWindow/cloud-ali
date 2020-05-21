package com.yan.cloud.util;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExcelUtil {
    public static void main(String[] args) {
        String path = "F:\\ee.xls";
        List<ExcelDemo> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int i1 = new Random().nextInt(20);
            data.add(new ExcelDemo("A" + i, i1, i1 % 2));
        }
        writerExcel(path, data);
    }

    public static void writerExcel(String path, List<ExcelDemo> data) {
        EasyExcel.write(path, ExcelDemo.class).sheet("人员").doWrite(data);
    }
}
