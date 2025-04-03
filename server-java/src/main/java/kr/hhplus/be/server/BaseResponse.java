package kr.hhplus.be.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Builder(access = PRIVATE)
@JsonInclude(NON_NULL)
public class BaseResponse<T>{
    private Integer code;
    private String status;
    private String message;
    private T data;

    private BaseResponse(Integer code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    private BaseResponse(Integer code, String status, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static BaseResponse<Void> success() {
        return new BaseResponse<>(HttpStatus.OK.value(), "OK", "요칭이 정상적으로 처리되었습니다.");
    }
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(HttpStatus.OK.value(), "OK", "요칭이 정상적으로 처리되었습니다.", data);
    }

    public static BaseResponse<Void> created() {
        return new BaseResponse<>(HttpStatus.CREATED.value(), "CREATED", "요칭이 정상적으로 처리되었습니다.");
    }
    public static <T> BaseResponse<T> created(T data) {
        return new BaseResponse<>(HttpStatus.CREATED.value(), "CREATED", "요칭이 정상적으로 처리되었습니다.", data);
    }

    public static <T> BaseResponse<T> fail(HttpStatus status, String message) {
        return new BaseResponse<>(status.value(), status.name(), message);
    }
}
