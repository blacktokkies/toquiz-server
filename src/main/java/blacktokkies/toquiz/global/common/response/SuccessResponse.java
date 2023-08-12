package blacktokkies.toquiz.global.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class SuccessResponse<T> {
    private final int statusCode;
    private final T result;

    public SuccessResponse(T result) {
        this.result = result;
        this.statusCode = HttpStatus.OK.value();
    }

    public SuccessResponse(T result, int statusCode) {
        this.result = result;
        this.statusCode = statusCode;
    }
}
