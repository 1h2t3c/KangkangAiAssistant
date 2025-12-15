package com.atguigu.java.ai.langchain4j.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 黄天赐
 * @since 2025-07-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("appointment")
@Accessors(chain = true)
//? mysql中挂号表
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String idCard;

    private String department;

    private String date;

    private String time;

    private String doctorName;


}
