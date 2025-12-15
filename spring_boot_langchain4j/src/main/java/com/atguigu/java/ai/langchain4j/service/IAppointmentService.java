package com.atguigu.java.ai.langchain4j.service;

import com.atguigu.java.ai.langchain4j.entity.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 黄天赐
 * @since 2025-07-05
 */
public interface IAppointmentService extends IService<Appointment> {
    Appointment getOne(Appointment appointment);
}
