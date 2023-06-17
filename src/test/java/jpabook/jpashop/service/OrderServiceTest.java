package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.awt.image.PixelGrabber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional // 테스트가 끝나면 Rollback을 진행함
class OrderServiceTest {

  @Autowired EntityManager em;
  @Autowired OrderService orderService;
  @Autowired OrderRepository orderRepository;

  @DisplayName("상품 주문")
  @Test
  public void 상품주문() throws Exception {
    // Given
    Member member = createMember();

    Book book = createBook("시골 JPA", 10000, 10);

    int orderCount = 2;
    // When
    Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

    // Then
    Order getOrder = orderRepository.findOne(orderId);

    assertThat(OrderStatus.ORDER).isEqualTo(getOrder.getStatus()).describedAs("상품 주문시 상태는 ORDER");
    assertThat(1).isEqualTo(getOrder.getOrderItems().size()).describedAs("주문한 상품 종류 수가 정확해야 한다.");
    assertThat(10000 * orderCount).isEqualTo(getOrder.getTotalPrice()).describedAs("주문 가격은 가격 * 수량이다.");
    assertThat(8).isEqualTo(book.getStockQuantity()).describedAs("주문 수량만큼 재고가 줄어야 한다.");
  }


  @DisplayName("상품주문_재고수량초과")
  @Test
  public void 상품주문_재고수량초과() throws Exception {
    // Given
    Member member = createMember();
    Item item = createBook("시골 JPA", 10000, 10);

    int orderCount = 11;
    // When

    Throwable thrown = catchThrowable(() -> { orderService.order(member.getId(), item.getId(), orderCount); });

    // Then
    assertThat(thrown).isInstanceOf(NotEnoughStockException.class).describedAs("재고 수량 부족 예외가 발생해야 한다.");
  }

  @DisplayName("상품 취소")
  @Test()
  public void 상품주문_취소() throws Exception {
    // Given
    Member member = createMember();
    Book item = createBook("시골 JPA", 10000, 10);

    int orderCount = 2;

    Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

    // When
    orderService.cancelOrder(orderId);

    // Then
    Order getOrder = orderRepository.findOne(orderId);

    assertThat(OrderStatus.CANCEL).isEqualTo(getOrder.getStatus()).describedAs("주문 취소시 상태는 CANCEL이다.");
    assertThat(10).isEqualTo(item.getStockQuantity()).describedAs("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");
  }

  private Book createBook(String name, int price, int stockQuantity) {
    Book book = new Book();
    book.setName(name);
    book.setPrice(price);
    book.setStockQuantity(stockQuantity);
    em.persist(book);
    return book;
  }

  private Member createMember() {
    Member member = new Member();
    member.setName("회원1");
    member.setAddress(new Address("서울", "경기", "123-123"));
    em.persist(member);
    return member;
  }
}