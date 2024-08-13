package com.example.fes2.courseMS.services;

import com.example.fes2.courseMS.feignclient.AuthFeignClient;
import com.example.fes2.courseMS.models.Courses;
import com.example.fes2.courseMS.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AuthFeignClient authFeignClient;

    public Courses addCourse(Courses courses){
        return courseRepository.save(courses);
    }

    public List<Courses> getAllCourse(){
        return courseRepository.findAll();
    }

    public List<Courses> findByTechnology(String technology){
        return courseRepository.findByTechnology(technology);
    }

    public void deleteCourse(String courseName){
        courseRepository.deleteById(String.valueOf(courseRepository.findByCourseName(courseName).getId()));
    }

    public List<Courses> findCourseByDuration(String technology, int from, int to){
        return courseRepository.findByTechnologyAndCourseDurationBetween(technology, from, to);
    }

}
