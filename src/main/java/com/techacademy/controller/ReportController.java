package com.techacademy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(@AuthenticationPrincipal UserDetail userDetail, Model model) {

    	Employee employee =userDetail.getEmployee();
    	List<Report> reportList;
    	
    	if(employee.getRole().toString().equals("ADMIN")) {
    		reportList = reportService.findAll();
    	}else {
    		reportList =reportService.findByEmployee(userDetail.getEmployee());
    	}
        model.addAttribute("listSize", reportList.size());
        model.addAttribute("reportList", reportList);

        return "reports/list";
    }
    
    
    //日報詳細画面
    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable("id") int id , Model model) {

        model.addAttribute("report", reportService.findByCode(id));
        return "reports/detail";
    }

    
    //日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report report) {

    	Employee employee = reportService.getLoginEmployee();
    	

        report.setEmployee(employee);
        report.setEmployeeCode(employee.getCode());
        return "reports/new";
    }
    
    //新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Report report, BindingResult res, Model model) {
    
    	Employee employee = reportService.getLoginEmployee();
    	

        report.setEmployee(employee);
        report.setEmployeeCode(employee.getCode());
        
        
     // 入力チェック
        if (res.hasErrors()) {
            return "reports/new";
        }

        // 論理削除を行った従業員番号を指定すると例外となるためtry~catchで対応
        // (findByIdでは削除フラグがTRUEのデータが取得出来ないため)
        try {
            ErrorKinds result = reportService.save(report);

            if (ErrorMessage.contains(result)) {
                model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
                return "reports/new";
            }

        } catch (DataIntegrityViolationException e) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DUPLICATE_EXCEPTION_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_EXCEPTION_ERROR));
            return "reports/new";
        }


        
       return"redirect:/reports";

    }   
    
    //日報更新画面を表示
    @GetMapping(value = "/{id}/update")
    public String edit(@PathVariable("id") int id, Model model) {
    	
    	 Report  report= reportService.findByCode(id);

    		model.addAttribute("report", report );
    	
    	return "reports/update";
    }
    
    //更新処理
    
    @PostMapping(value = "/{id}/update")
    public String update(@PathVariable int id, @Validated Report report, BindingResult res, Model model) {
    	
    	if (res.hasErrors()) {
             return edit(id, model);
         }
    	
    	 try {
             ErrorKinds result = reportService.update(id, report);

             if (ErrorMessage.contains(result)) {
                 model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
                 return edit(id,model);
             }

         } catch (DataIntegrityViolationException e) {
             model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DUPLICATE_EXCEPTION_ERROR),
                     ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_EXCEPTION_ERROR));
             return edit(id,model);
         }
    	//一覧画面にリダイレクト
    	return "redirect:/reports";
}
    
    
    // 従業員削除処理
    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable("id") int id, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        ErrorKinds result = reportService.delete(id, userDetail);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            model.addAttribute("report", reportService.findByCode(id));
            return detail(id, model);
        } 
        return "redirect:/reports";
    }


    

   }