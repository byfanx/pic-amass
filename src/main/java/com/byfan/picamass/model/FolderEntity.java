package com.byfan.picamass.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 文件夹表实体类
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "folder")
public class FolderEntity implements Serializable {

	/**
	 * 文件夹id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	/**
	 * 文件夹名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 描述
	 */
	@Column(name = "description")
	private String description;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Integer userId;

	/**
	 * 封面
	 */
	@Column(name = "cover_image")
	private String coverImage;


	/**
	 * 创建时间
	 */
	@CreatedDate
	@Column(name = "create_time")
	private Date createTime;

}