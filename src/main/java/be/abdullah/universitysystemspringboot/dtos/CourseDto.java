package be.abdullah.universitysystemspringboot.dtos;


import lombok.Data;

@Data
public class CourseDto {
    private Long id;
    private String name;
    private Integer studyPoints;
    private LecturerDto lecturerDto;
}
