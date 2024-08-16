package com.example.fes2.courseMS.controller;

import com.example.fes2.courseMS.feignclient.AuthFeignClient;
import com.example.fes2.courseMS.models.CourseDTO;
import com.example.fes2.courseMS.models.Courses;
import com.example.fes2.courseMS.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private AuthFeignClient authFeignClient;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("UP", HttpStatus.OK);
    }


    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> addNewCourse(@Valid @RequestBody CourseDTO courseDTO,
                                               @RequestHeader("Authorization") String token){
        if (authFeignClient.getRole(token).equals("ADMIN")){
            if (authFeignClient.isValidToken(token)){
                Courses newCourses = new Courses();
                newCourses.setCourseName(courseDTO.getCourseName());
                newCourses.setCourseDuration(courseDTO.getCourseDuration());
                newCourses.setCourseDescription(courseDTO.getCourseDescription());
                newCourses.setTechnology(courseDTO.getTechnology());
                newCourses.setLaunchUrl(courseDTO.getLaunchUrl());

                return ResponseEntity.ok().body(courseService.addCourse(newCourses));
            } else {
                return ResponseEntity.badRequest().body("Invalid Token provided");
            }
        } else {
            return ResponseEntity.badRequest().body("You Doesn't have the access to add a new course");
        }
    }


    @GetMapping("/getall")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<Object> getAllCourse(@RequestHeader("Authorization") String token){
        if (authFeignClient.getRole(token).equals("ADMIN")){
            if (authFeignClient.isValidToken(token)){
                return ResponseEntity.ok().body(courseService.getAllCourse());
            }else {
                return ResponseEntity.badRequest().body("Invalid Token provided");
            }
        } else {
            return ResponseEntity.badRequest().body("You Doesn't have the access to view this endpoint");
        }
    }

    @GetMapping("/info/{technology}")
    public ResponseEntity<Object> getCourseByTechnology(@PathVariable String technology,
                                                        @RequestHeader("Authorization") String token){
        if (authFeignClient.isValidToken(token)){
            return ResponseEntity.ok().body(courseService.findByTechnology(technology));
        }else {
            return ResponseEntity.badRequest().body("Invalid Token provided");
        }
    }

    @DeleteMapping("/delete/{courseName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteCourse(@PathVariable String courseName,
                                               @RequestHeader("Authorization") String token){
        if (authFeignClient.getRole(token).equals("ADMIN")){
            if (authFeignClient.isValidToken(token)){
                courseService.deleteCourse(courseName);
                return ResponseEntity.ok().body("Successfully deleted course");
            } else {
                return ResponseEntity.badRequest().body("Invalid Token provided");
            }
        } else {
            return ResponseEntity.badRequest().body("You Doesn't have the permission to delete a Course");
        }
    }

    @GetMapping("get/{technology}/{from}/{to}")
    public ResponseEntity<Object> findCourseByDuration(@PathVariable String technology,
                                                       @PathVariable int from,
                                                       @PathVariable int to,
                                                       @RequestHeader("Authorization") String token){
        if (authFeignClient.isValidToken(token)){
            return ResponseEntity.ok().body(courseService.findCourseByDuration(technology,from,to));
        } else {
            return ResponseEntity.badRequest().body("Invalid Token provided");
        }
    }

}
