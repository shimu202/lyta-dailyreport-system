package com.techacademy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table(name = "reports")
@SQLRestriction("delete_flg = false")
public class Report {

	public static enum Role {
		GENERAL("一般"), ADMIN("管理者");

		private String name;

		private Role(String name) {
			this.name= name;
		}

		public String getValue() {
			return this.name;
		}
	}

	// ID
    @Id
    @NotEmpty
    private int id;
    
    //日付
   @Column(nullable = false)
    private LocalDate report_date;
   
   //タイトル
  @Column(length = 100, nullable = false)
   private String titile;
  
  //内容
  
    
    
 // 削除フラグ(論理削除を行うため)
    @Column(columnDefinition="TINYINT", nullable = false)
    private boolean deleteFlg;

    // 登録日時
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 更新日時
    @Column(nullable = false)
    private LocalDateTime updatedAt;

}
