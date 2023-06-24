package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
  private final EntityManager em;

  public void save(Item item) {
    if (item.getId() == null) {
      em.persist(item);
    } else {
      em.merge(item);
      // merge(병합)의 주의점
      // 변경감지(더티체킹)을하면 변경된 값만 db에 바꾸지만, merge(병합)을하면 모든 속성이 변경된다.
      // 그래서 병합시 값이 없으면 null로 업데이트 할 위험도 있다.
      // 따라서 실무에서 merge는 선택의 방법은 아니다. 필드가 계속 늘어나면 merge도 계속 관리해야하고 잘못해서 데이터가 null로 바뀔 수 가 있다.
    }
  }

  public Item findOne(Long id) {
    return em.find(Item.class, id);
  }

  public List<Item> findAll() {
    return em.createQuery("Select i From Item i", Item.class)
            .getResultList();
  }

}
