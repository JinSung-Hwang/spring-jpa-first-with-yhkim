package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/members/new")
  public String createForm(Model model) {
    model.addAttribute("memberForm", new MemberForm());
    return "members/createMemberForm";
  }

  // 만약 MemberForm을 통해서 데이터를 받지 않고 entity을 통해서 데이터를 받으면 entity에 화면 종속적인 필드나 메서드가 생기게 되면서 유지보수성이 어려워진다.
  // 특히 JPA를 사용하면 entity를 순수하게 유지해야한다. 오직 핵심 비지니스 로직만 있도록 entity를 만들어야한다.
  @PostMapping("/members/new")
  public String create(@Valid MemberForm form, BindingResult result) { //@Valid 를 붙이면 MemberForm 클래스의 밸리데이션 어노테이션이 밸리데이션을 진행한다.
    // 파라미터의 BindingResult는 @Valid에서 오류나면 BindingResult에 값을 담아준다.
    // 즉 @Valid에서 오류가 나도 Service 로직을 실행 시킬 수 있도록 만들어준다.
    if (result.hasErrors()) {
      return "members/createMemberForm";
    }

    Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
    Member member = new Member();
    member.setName(form.getName());
    member.setAddress(address);

    memberService.join(member);
    return "redirect:/";
  }

  @GetMapping("/members")
  public String list(Model model) {
    List<Member> members = memberService.findMembers();
    model.addAttribute("members", members); // 실무에서는 entity를 바로 반환하기 보다는 dto를 통해서 화면에서 꼭 필요한 데이터만 보내야한다.
    // entity를 바로 반환한다면 entity가 바뀔때 API의 스펙도 바로 바뀌게 되는 불안정성이 생긴다.
    return "members/memberList";
  }

}
