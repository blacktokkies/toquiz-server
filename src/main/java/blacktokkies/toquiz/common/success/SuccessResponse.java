package blacktokkies.toquiz.common.success;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class SuccessResponse<T> {
    private final int statusCode;
    private final T result;

    public SuccessResponse(T result, int statusCode) {
        this.result = result;
        this.statusCode = statusCode;
    }
}
