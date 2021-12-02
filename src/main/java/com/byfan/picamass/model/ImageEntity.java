package com.byfan.picamass.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 图片/视频表实体类
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Table(name = "image")
public class ImageEntity {

	/**
	 * 图片id
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
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 高度
	 */
	@Column(name = "height")
	private Double height;

	/**
	 * 宽度
	 */
	@Column(name = "width")
	private Double width;

	/**
	 * 大小
	 */
	@Column(name = "size")
	private String size;

	/**
	 * 类型
	 */
	@Column(name = "content_type")
	private String contentType;

	/**
	 * 图片存储路径
	 */
	@Column(name = "dir")
	private String dir;

	/**
	 *
	 */
	@Column(name = "path")
	private String path;

	/**
	 * markdown格式
	 */
	@Column(name = "markdown")
	private String markdown;

	/**
	 * html格式
	 */
	@Column(name = "html")
	private String html;

	/**
	 * url格式
	 */
	@Column(name = "url")
	private String url;

	/**
	 * 文件夹id
	 */
	@Column(name = "folder_id")
	private Integer folderId;

	/**
	 * md5
	 */
	@Column(name = "md5")
	private String md5;

	/**
	 * 状态。1 正常。0 回收站
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 上传时间
	 */
	@CreatedDate
	@Column(name = "upload_time")
	private Date uploadTime;

	/**
	 * 修改时间
	 */
	@LastModifiedDate
	@Column(name = "update_time")
	private Date updateTime;

	public String getUrl() {
		return path + "/" + name;
	}

	public String getDir() {
		return dir + name;
	}

	public String getMarkdown() {
		String mdFormat = "![%s](%s/%s)";
		return String.format(mdFormat,name,path,name);
	}

//	public String getHtml() {
//		String htmlFormat = "<img src=%s%s/%s%s />";
//		return String.format(htmlFormat,'"',path,name,'"');
//		final StringBuffer sb = new StringBuffer();
//		sb.append("<img src=")
//				.append("\\\"")
//				.append(path)
//				.append("/")
//				.append(name)
//				.append("\\\"")
//				.append("/>");
//		return sb.toString();
//	}

}