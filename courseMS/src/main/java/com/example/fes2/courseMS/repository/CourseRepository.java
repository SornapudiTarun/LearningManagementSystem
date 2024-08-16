package com.example.fes2.courseMS.repository;

import com.example.fes2.courseMS.models.Courses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Courses,String> {
    List<Courses> findByTechnology(String technology);
    List<Courses> findByTechnologyAndCourseDurationBetween(String technology, int durationFrom, int durationTo);
    Courses findByCourseName(String courseName);
}
