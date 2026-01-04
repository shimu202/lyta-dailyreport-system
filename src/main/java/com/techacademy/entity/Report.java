package com.techacademy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "reports")
@SQLRestriction("delete_flg = false")
public class Report {

	// ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
  //社員番号
    @Column(name ="employee_code", length = 10, nullable =false)
    private String employeeCode;
    
    @ManyToOne
    @JoinColumn(name = "employee_code", referencedColumnName = "code", insertable = false,
    		updatable = false)
    private Employee employee;
    
    public Employee getEmployee() {
    	return employee;
    }
    

    
    //日付
    @NotNull
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportDate;

   
   //タイトル
  @Column(length = 100, nullable = false)
  @Size(max = 100, message = "100文字以下で入力してください")
  @NotEmpty
   private String title;
  
  //内容
  @Column(columnDefinition="LONGTEXT", length = 600, nullable = false)
  @Size(max = 100, message ="600文字以下で入力してください")
  @NotEmpty
  private String content;
  
 
    
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
