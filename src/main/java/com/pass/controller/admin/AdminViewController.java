package com.pass.controller.admin;

import com.pass.service.packaze.PackageService;
import com.pass.service.pass.BulkPassService;
import com.pass.service.statistics.StatisticsService;
import com.pass.service.user.UserGroupMappingService;
import com.pass.util.LocalDateTimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Controller
@RequestMapping(value = "/admin")
public class AdminViewController {

    private final PackageService packageService;
    private final BulkPassService bulkPassService;
    private final UserGroupMappingService userGroupMappingService;
    private final StatisticsService statisticsService;

    public AdminViewController(PackageService packageService, BulkPassService bulkPassService, UserGroupMappingService userGroupMappingService, StatisticsService statisticsService) {

        this.packageService = packageService;
        this.bulkPassService = bulkPassService;
        this.userGroupMappingService = userGroupMappingService;
        this.statisticsService = statisticsService;

    }

    @GetMapping
    public ModelAndView home(ModelAndView modelAndView, @RequestParam("to") String toString) {

        LocalDateTime to = LocalDateTimeUtils.parseDate(toString);

        modelAndView.addObject("chartData", statisticsService.makeChartData(to));
        modelAndView.setViewName("admin/index");
        return modelAndView;
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
