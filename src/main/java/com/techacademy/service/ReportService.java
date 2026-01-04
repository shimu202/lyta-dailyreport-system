package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

import jakarta.transaction.Transactional;

@Service
public class ReportService {
	private final ReportRepository reportRepository;
	
	public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;

}


    // 従業員一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 1件を検索
    public Report findByCode(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }
   
    public Employee getLoginEmployee() {
        UserDetail user =(UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user.getEmployee();
    }
    //保存
    @Transactional
    public ErrorKinds save(Report report) {
    	
    	
    	Employee employee = getLoginEmployee();
 
     
        
    	 if (reportRepository.existsByEmployeeCodeAndReportDate(
    	            employee.getCode(), report.getReportDate())) {
    	        return ErrorKinds.DATECHECK_ERROR;
    	    }
    	 
    	 report.setDeleteFlg(false);

         report.setEmployee(employee);
         report.setEmployeeCode(employee.getCode());

         LocalDateTime now = LocalDateTime.now();
         report.setCreatedAt(now);
         report.setUpdatedAt(now);

         reportRepository.save(report);
         return ErrorKinds.SUCCESS;
     }
    
    // 従業員削除
    @Transactional
    public ErrorKinds delete(Integer id, UserDetail userDetail) {
    	Report report = findByCode(id);
    	if (report == null) {
    		return ErrorKinds.LOGINCHECK_ERROR;
    	}
    	report.setDeleteFlg(true);
    	report.setUpdatedAt(LocalDateTime.now());
		return ErrorKinds.SUCCESS;
    	
    }
     
  public List<Report> findByEmployee(Employee employee){
	  return reportRepository.findByEmployeeAndDeleteFlgFalse(employee);
  }
        

}