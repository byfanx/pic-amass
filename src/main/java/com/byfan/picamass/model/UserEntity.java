package com.byfan.picamass.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户表实体类
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "user")
public class UserEntity {

	/**
	 * 用户id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/**
	 * 用户名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 密码
	 */
	@Column(name = "password")
	private String password;

	/**
	 * 创建时间
	 */
	@CreatedDate
	@Column(name = "create_time")
	private Date createTime;

}