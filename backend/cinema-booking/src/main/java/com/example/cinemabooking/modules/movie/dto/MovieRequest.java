package com.example.cinemabooking.modules.movie.dto;

import com.example.cinemabooking.common.enums.MovieStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Du lieu client gui len khi tao/sua phim.
 * Khong co averageRating: truong nay chi duoc tinh tu bang reviews, client khong duoc tu gan.
 */
@Getter
@Setter
@NoArgsConstructor
public class MovieRequest {

    @NotBlank(message = "Tên phim không được để trống")
    @Size(max = 255, message = "Tên phim tối đa 255 ký tự")
    private String title;

    private String description;

    @NotNull(message = "Thời lượng không được để trống")
    @Positive(message = "Thời lượng phải lớn hơn 0")
    private Integer durationMinutes;

    @NotNull(message = "Ngày khởi chiếu không được để trống")
    private LocalDate releaseDate;

    /** Co the null; neu co thi phai >= releaseDate (kiem tra trong Service). */
    private LocalDate endDate;

    @Size(max = 255, message = "Poster URL tối đa 255 ký tự")
    private String posterUrl;

    @Size(max = 255, message = "Trailer URL tối đa 255 ký tự")
    private String trailerUrl;

    @Size(max = 255, message = "Tên đạo diễn tối đa 255 ký tự")
    private String director;

    private String castList;

    @Size(max = 255, message = "Ngôn ngữ tối đa 255 ký tự")
    private String language;

    @Size(max = 255, message = "Phụ đề tối đa 255 ký tự")
    private String subtitle;

    /** P, C13, C16, C18... */
    @Size(max = 10, message = "Phân loại độ tuổi tối đa 10 ký tự")
    private String ageRating;

    /** Null khi tao moi -> mac dinh COMING_SOON. */
    private MovieStatus status;

    @NotEmpty(message = "Phim phải có ít nhất 1 thể loại")
    private List<@NotNull Long> categoryIds;
}
