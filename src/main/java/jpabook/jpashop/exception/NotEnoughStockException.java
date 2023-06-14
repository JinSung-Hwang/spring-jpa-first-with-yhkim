package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException {
  // 기본적으로 아래 메서드를 오버라이딩해서 사용하는것이 좋다. 여러 메세지를 찍을 수 있다.
  public NotEnoughStockException() {
    super();
  }

  public NotEnoughStockException(String message) {
    super(message);
  }

  public NotEnoughStockException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotEnoughStockException(Throwable cause) {
    super(cause);
  }
}
