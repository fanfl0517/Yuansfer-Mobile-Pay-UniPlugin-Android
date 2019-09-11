package uni.yuansfer.payplugin.util;

/**
 * @Author Fly-Android
 * @CreateDate 2019/5/23 15:48
 * @Desciption Yuansfer异常
 */
public class YuansferException extends RuntimeException {

    public YuansferException() {
        super();
    }

    public YuansferException(String message) {
        super(message);
    }

    public YuansferException(String message, Throwable cause) {
        super(message, cause);
    }

    public YuansferException(Throwable cause) {
        super(cause);
    }

}
