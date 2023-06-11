package jpabook.jpashop.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
  @Id @GeneratedValue
  @Column(name = "order_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "member_id") // 어떤 칼럼으로 조인할것인지 적어줘야한다.
  private Member member;

  @OneToMany(mappedBy = "order")
  private List<OrderItem> orderItem = new ArrayList<>() ;

  @OneToOne
  @JoinColumn(name = "delivery_id")
  // 1:1 관계에서는 외래키를 어디에 둬도 상관없다. 여기서는 order든 delivery든 상관없다.
  // 하지만 많이 조회하는 쪽에 외래키를 두는것이 좋다. 아마 이 비지니스에서는 order에서 delivery조회가 많이 예상되어서 order에 두었다.
  private Delivery delivery;

  private LocalDateTime orderDate;

  @Enumerated(EnumType.STRING)
  private OrderStatus status; // 주문 상태 [order, cancel]
}
