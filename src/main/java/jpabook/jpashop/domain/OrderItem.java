package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

  @Id @GeneratedValue
  @Column(name = "order_item_id")
  private Long id;

  @ManyToOne(fetch = LAZY) // ManyToOne은 기본 fetchType이 Eager이기때문에 Lazy로 바꿔줘야한다.
  @JoinColumn(name = "item_id")
  private Item item;

  @ManyToOne(fetch = LAZY) // ManyToOne은 기본 fetchType이 Eager이기때문에 Lazy로 바꿔줘야한다.
  @JoinColumn(name = "order_id")
  private Order order;

  private int orderPrice; // 주문 가격
  private int count; // 주문 수량

//  protected OrderItem() {} // 생성 방법을 통일하기 위해서 protected로 바꿈, 라인 15의 @NoArgsConstructor(access = AccessLevel.PROTECTED)와 같은 코드이고 라인15은 롬북으로 표현함

  //== 생성 메서드==//
  public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
    OrderItem orderItem = new OrderItem();
    orderItem.setItem(item);
    orderItem.setOrderPrice(orderPrice);
    orderItem.setCount(count);

    item.removeStock(count);
    return orderItem;
  }

  //==비지니스 로직==//
  public void cancel() {
    getItem().addStock(count);
  }

  //==조회 로직==//

  /**
   * 주문 상품 전체 가격 조회
   */
  public int getTotalPrice() {
    return getOrderPrice() * getCount();
  }
}
