package com.jyou.thinker.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * TODO: 实体类通用基础字段
 * @author wgbing
 * @date 2019-08-15 19:54
 */
@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    /** 自增主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /** 创建时间 */
    @Column(nullable = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;

    /** 修改时间 */
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime;

    @PrePersist
    public void prePersist() {
        createTime = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        updateTime = new Date();
    }

}