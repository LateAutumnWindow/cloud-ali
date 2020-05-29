//package com.yan.cloud.security;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class TestSecurityController {
//
//    @GetMapping(value = "/r1")
//    @PreAuthorize("hasAnyAuthority('p1')")
//    public String r1(){
//        return "访问资源1";
//    }
//
//}
