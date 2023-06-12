package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
public class Order {
  @Id @GeneratedValue
  @Column(name = "order_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) // ManyToOne은 디폴트가 Eager이기때문에 Lazy로 바꿔줘야한다.
  @JoinColumn(name = "member_id") // 어떤 칼럼으로 조인할것인지 적어줘야한다.
  private Member member;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)  // casecadeType.ALL을하면 orderItem을 영속성 컨텍스트에 넣는 로직을 작성 안해도 같이 저장된다.
  // OneToMany는 디폴트가 Lazy이기때문에 FetchType을 그대로 두어도 된다.
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

}
