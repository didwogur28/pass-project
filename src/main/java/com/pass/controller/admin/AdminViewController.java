package com.pass.controller.admin;

import com.pass.service.packaze.PackageService;
import com.pass.service.pass.BulkPassService;
import com.pass.service.user.UserGroupMappingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/admin")
public class AdminViewController {

    private final PackageService packageService;
    private final BulkPassService bulkPassService;
    private final UserGroupMappingService userGroupMappingService;

    public AdminViewController(PackageService packageService, BulkPassService bulkPassService, UserGroupMappingService userGroupMappingService) {

        this.packageService = packageService;
        this.bulkPassService = bulkPassService;
        this.userGroupMappingService = userGroupMappingService;

    }

    @GetMapping("/bulk-pass")
    public ModelAndView registerBulkPass(ModelAndView modelAndView) {

        // bulk pass 목록 조회
        modelAndView.addObject("bulkPasses", bulkPassService.getAllBulkPasses());

        // bulk pass를 등록할 때 필요한 package 값을 위해 모든 package를 조회
        modelAndView.addObject("packages", packageService.getAllPackages());

        // bulk pass를 등록할 때 필요한 userGroupId 값을 위해 모든 userGroupId를 조회
        modelAndView.addObject("userGroupIds", userGroupMappingService.getAllUserGroupIds());

        // bulk pass request를 제공
        modelAndView.addObject("request", new BulkPassRequest());
        modelAndView.setViewName("admin/bulk-pass");

        return modelAndView;
    }

    @PostMapping("/bulk-pass")
    public String addBulkPass(@ModelAttribute("request") BulkPassRequest request, Model model) {

        // bulk pass를 생성
        bulkPassService.addBulkPass(request);
        return "redirect:/amdin/bulk-pass";
    }
}
