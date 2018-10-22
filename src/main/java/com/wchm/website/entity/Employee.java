package com.wchm.website.entity;

import lombok.Data;

@Data
public class Employee {
    private String name;
    private String gender;
    private int age;
    private String department;
    private double salary;
    /**
     * 注意：读取日期操作要将Excel单元格设为文本格式，然后按字符串读取；写入操作时，直接按字符串写入
     */
    private String date;
}
