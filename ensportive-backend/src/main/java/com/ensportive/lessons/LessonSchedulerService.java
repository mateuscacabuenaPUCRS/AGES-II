package com.ensportive.lessons;

import com.ensportive.courses.CourseEntity;
import com.ensportive.courses.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
public class LessonSchedulerService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    private final String CRON_EXPRESSION = "0 0 0 * * MON";

    @Scheduled(cron = CRON_EXPRESSION)
    public void scheduleLessons() {
        createLessonsUpTo20WeeksAhead();
    }

    private void createLessonsUpTo20WeeksAhead() {
        final int WEEKS_AHEAD = 24;

        List<CourseEntity> classes = courseRepository.findAll();
        LocalDate today = LocalDate.now();

        for (CourseEntity course : classes) {

            if (course.getUniqueLesson()) {
                continue;
            }

            DayOfWeek lessonDay = course.getWeekDay();
            LocalTime lessonTime = course.getHour();

            // If today is the lesson day, returns today, otherwise returns the next lesson date
            LocalDate nextLessonStartDate = today.with(TemporalAdjusters.nextOrSame(lessonDay));

            LocalDate targetDate = nextLessonStartDate.plusWeeks(WEEKS_AHEAD);

            Optional<LessonEntity> lastLessonOpt = lessonRepository.findTopByCourseOrderByDateDesc(course);

            // If there is no lesson, the last lesson date is yesterday
            LocalDate lastLessonDate = lastLessonOpt
                    .map(lesson -> lesson.getDate().toLocalDate())
                    .orElse(today.minusDays(1));

            if (lastLessonDate.isBefore(targetDate)) {
                LocalDate nextLessonDate = lastLessonDate.isBefore(nextLessonStartDate) ? nextLessonStartDate : lastLessonDate.with(TemporalAdjusters.next(lessonDay));

                while (!nextLessonDate.isAfter(targetDate)) {
                    LocalDateTime nextLessonDateTime = LocalDateTime.of(nextLessonDate, lessonTime);
                    LessonEntity lesson = new LessonEntity();
                    lesson.setCourse(course);
                    lesson.setDate(nextLessonDateTime);
                    if (course.getTeacher() != null) {
                        lesson.setTeacher(course.getTeacher());
                    }
                    lessonRepository.save(lesson);

                    nextLessonDate = nextLessonDate.with(TemporalAdjusters.next(lessonDay));
                }
            }
        }
    }
}