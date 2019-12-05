package com.leyou.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @Author zhangfeixiang
 * @Date 2019/11/25 16:44
 * @Version 1.0
 */
@Data
@Table(name = "tb_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Length(min = 4, max = 32, message = "用户名必须为4~32个字符")
	private String username;// 用户名

	@JsonIgnore
	@Length(min = 4, max = 32, message = "用户名必须为4~32个字符")
	private String password;// 密码
    @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\\\d{8}$", message = "手机号码不正确")
	private String phone;// 电话

	private Date created;// 创建时间

	@JsonIgnore
	@NotBlank
	private String salt;// 密码的盐值
}
