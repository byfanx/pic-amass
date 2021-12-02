package com.byfan.picamass.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户个性配置表实体类
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "config")
public class ConfigEntity {

	/**
	 * 配置id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Integer userId;

	/**
	 * 配置的key
	 */
	@Column(name = "conf_key")
	private String confKey;

	/**
	 * 配置的值
	 */
	@Column(name = "conf_value")
	private String confValue;


	/**
	 * 创建时间
	 */
	@CreatedDate
	@Column(name = "create_time")
	private Date createTime;

}