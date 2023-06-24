package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
  private final ItemRepository itemRepository;

  @Transactional
  public void saveItem(Item item) {
    itemRepository.save(item);
  }

  @Transactional
  public void updateItem(Long itemId, String name, int price, int stockQuantity) { // 변경 감지 방법
    Item findItem = itemRepository.findOne(itemId);
//    findItem.setName(name);
//    findItem.setPrice(price);
//    findItem.setStockQuantity(stockQuantity);
    findItem.change(name, price, stockQuantity);  // 위의 3라인 코드보다 이렇게 속성을 한번에 변경할 수 있는 메서드를 만드는 것이 좋다.
    // 이렇게 속성을 한번에 바꾸면 데이터 변경시 추적이 쉽다. set으로 호출하면 도메인 로직이 코드에 잘 표현도 되지 않으면서 데이터 변경점을 찾기 어렵다.
    // 하지만 이렇게 속성을 바꾸는 메서드를 만들면 데이터 변경 메서드 이름을 가지고 찾을 수 있기 때문에 추적도 편하다.

  } // 메서드가 끝나면 트랜잭션이 끝나고 트랜잭션이 끝나면 db에 flush를 날린다. flush를 날릴때 변경 감지가 일어나면서 update query도 같이 날라간다.

  public List<Item> findItems() {
    return itemRepository.findAll();
  }

  public Item findOne(Long itemId) {
    return itemRepository.findOne(itemId);
  }
}
