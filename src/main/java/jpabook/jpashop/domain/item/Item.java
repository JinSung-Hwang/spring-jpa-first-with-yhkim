package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 전략
//    JOINED:: 가장 정규화된 형태
//    SINGLE_TABLE:: 하나의 테이블에 모두 넣는 형태
//    TABLE_PER_CLASS:: 부모테이블(item)없이 하위 테이블만(book, movie, album) 생성됨
@Getter @Setter
public abstract class Item {
  @Id @GeneratedValue
  @Column(name = "item_id")
  private Long id;

  private String name;
  private int price;
  private int stockQuantity;

  @ManyToMany(mappedBy = "items")
  private List<Category> categories = new ArrayList<>();
}

