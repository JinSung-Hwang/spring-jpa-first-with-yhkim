package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

  @Id
  @GeneratedValue
  @Column(name = "member_id") // 칼럼 명을 따로 바꿔서 사용할떄 사용하는 어노테이션, // 프로그래밍할떄 member.id라고하면 찾기 쉽지만 db에서 id라고하면 찾기 어려워서 member_id라는 컨벤션을 사용함
  private Long id;

  private String name;

  @Embedded // Address라는 내장 타입을 사용할 수 있도록 함
  private Address address;

  @OneToMany(mappedBy = "member") // member는 order에 의해서 맵핑이 되었다는 것을 JPA에게 알려주는 어노테이션
  // 항상 외래키가 있는 테이블이 연관 관계 주인이다.
  // member와 order는 1:N관계이기 때문에 order에 외래키가 있으며 연관 관계의 주인이다.
  // 여기에 값을 어떻게 넣는다고 연관 관계가 바뀌지 않는다.
  // 단, order class에서 member값을 바꾸면 연관 관계가 바뀐다.
  private List<Order> orders = new ArrayList<>();

}
