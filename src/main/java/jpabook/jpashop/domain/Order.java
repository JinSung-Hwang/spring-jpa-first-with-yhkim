package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
  @Id @GeneratedValue
  @Column(name = "order_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) // ManyToOne은 디폴트가 Eager이기때문에 Lazy로 바꿔줘야한다.
  @JoinColumn(name = "member_id") // 어떤 칼럼으로 조인할것인지 적어줘야한다.
  private Member member;

  // OneToMany는 디폴트가 Lazy이기때문에 FetchType을 따로 작성 안해도 된다.
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)  // casecadeType.ALL을하면 order만 영속성 컨텍스트에 넣으면 orderItem도 같이 영속성 컨텍스트에 넣어서 관리된다.
  // 따라서 order만 세이브하면 orderItem도 같이 세이브된다. // 그럼 casecade를 어디까지 걸어야할까? orderItem, delivery가 order와 영속성을 동일하게 관리할떄 쓰고 orderItem, delivery가 다른곳에서 참조되지 않을때 casecade를 쓸수 있다.
  // 즉 영속성 컨텍스트 라이브 싸이클이 동일할때 casecade를 쓸 수있다.
  private List<OrderItem> orderItems = new ArrayList<>() ;

  @OneToOne(fetch=LAZY, cascade = CascadeType.ALL) // OneToOne은 디폴트가 Eager이기때문에 Lazy로 바꿔줘야한다.
  @JoinColumn(name = "delivery_id")
  // 1:1 관계에서는 외래키를 어디에 둬도 상관없다. 여기서는 order든 delivery든 상관없다.
  // 하지만 많이 조회하는 쪽에 외래키를 두는것이 좋다. 아마 이 비지니스에서는 order에서 delivery조회가 많이 예상되어서 order에 두었다.
  private Delivery delivery;

  private LocalDateTime orderDate;

  @Enumerated(EnumType.STRING)
  private OrderStatus status; // 주문 상태 [order, cancel]

  //==연관관계 메서드==//
  public void setMember(Member member) {
    this.member = member;
    member.getOrders().add(this);
  }

  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  public void setDelivery(Delivery delivery) {
    this.delivery = delivery;
    delivery.setOrder(this);
  }

  //==생성 메서드==//
  public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
    Order order = new Order();
    order.setMember(member);
    order.setDelivery(delivery);
    for (OrderItem orderItem: orderItems) {
      order.addOrderItem(orderItem);
    }
    order.setStatus(OrderStatus.ORDER);
    order.setOrderDate(LocalDateTime.now());
    return order;
  }

  //==비지니스 로직==//
  /**
   * 주문 취소
   */
  public void cancel() {
    if (delivery.getStatus() == DeliveryStatus.COMP) {
      throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
    }
    this.setStatus(OrderStatus.CANCEL);
    for (OrderItem orderItem: this.orderItems) {
      orderItem.cancel();
    }
  }

  //==조회 로직==//
  /**
   * 전체 주문 가격 조회
   */
  public int getTotalPrice() {
    return orderItems.stream()
            .mapToInt(OrderItem::getTotalPrice)
            .sum();
  }


}
