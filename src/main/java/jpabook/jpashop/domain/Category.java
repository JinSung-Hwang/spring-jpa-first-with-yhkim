package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Category {
  @Id @GeneratedValue
  @Column(name = "category_id")
  private Long id;

  private String name;

  @ManyToMany // 실무에서는 사용하지 않는다. 실무할떄는 날짜를 넣고 등등 추가 정보가 필요한데 이거는 매핑밖에 못한다.
  @JoinTable(
          name = "category_item",
          joinColumns = @JoinColumn(name = "category_id"),
          inverseJoinColumns = @JoinColumn(name = "item_id")
  )
  private List<Item> items = new ArrayList<>();

  // 셀프 조인을 위한것 parent와 child 생성
  @ManyToOne(fetch = LAZY) // ManyToOne은 기본 fetchType이 Eager이기때문에 Lazy로 바꿔줘야한다.
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent")
  private List<Category> child = new ArrayList<>();

  //==연관관계 메서드==//
  public void addChildCategory(Category child) {
    this.child.add(child);
    child.setParent(this);
  }

}
