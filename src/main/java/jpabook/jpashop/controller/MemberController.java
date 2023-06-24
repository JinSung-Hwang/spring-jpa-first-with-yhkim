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

@Controller
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/members/new")
  public String createForm(Model model) {
    model.addAttribute("memberForm", new MemberForm());
    return "members/createMemberForm";
  }

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

}
