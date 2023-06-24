package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class itemController {
  private final ItemService itemService;

  @GetMapping("/items/new")
  public String createForm(Model model) {
    model.addAttribute("form", new BookForm());
    return "items/createItemForm";
  }

  @PostMapping("/items/new")
  public String create(BookForm form) {
    Book book = new Book();
    book.setName(form.getName());
    book.setPrice(form.getPrice());
    book.setStockQuantity(form.getStockQuantity());
    book.setAuthor(form.getAuthor());
    book.setIsbn(form.getIsbn());

    itemService.saveItem(book);
    return "redirect:/";
  }

  @GetMapping("/items")
  public String list(Model model) {
    List<Item> items = itemService.findItems();
    model.addAttribute("items", items);
    return "items/itemList";
  }

  @GetMapping("items/{itemsId}/edit")
  public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
    Book item = (Book) itemService.findOne(itemId);

    BookForm form = new BookForm();
    form.setId(item.getId());
    form.setName(item.getName());
    form.setPrice(item.getPrice());
    form.setStockQuantity(item.getStockQuantity());
    form.setAuthor(item.getAuthor());
    form.setIsbn(item.getIsbn());

    model.addAttribute("form", form);
    return "items/updateItemForm";
  }

  @PostMapping("items/{itemId}/edit")
  public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) { // @ModelAttribute안에 있는 "from"이라는 값은 updateItemForm.html의 object="${form}"와 매칭되는 값이다.
    // 준영속 엔티티?
    // 영속성 컨텍스트가 더는 관리하지 않는 엔티티를 말한다.
    // (여기서는 itemService.saveItem(book)에서 수정을 시도하는 Book객체다. Book객체는 이미 DB에 한번 저장되어서 식별자가 존재한다.
    // 이렇게 임의로 만들어낸 엔티티라도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있다.)

//    Book book = new Book(); // 지금 book은 준영속 엔티티이다. 준영속 엔티티는 데이터가 바뀐다고 더티체킹으로 DB에 데이터가 변경되지는 않는다.
//    book.setId(form.getId()); // 70번 라인처럼 controller에서 어설프게 엔티티를 만들지 말고 79번라인처럼 값을 따로 따로 넘기던지 아니면 dto를 통해서 값을 넣기도록 하자.
//    book.setName(form.getName());
//    book.setPrice(form.getPrice());
//    book.setStockQuantity(form.getStockQuantity());
//    book.setAuthor(form.getAuthor());
//    book.setIsbn(form.getIsbn());
//    itemService.saveItem(book);

    itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());

    return "redirect:items";
  }

}
