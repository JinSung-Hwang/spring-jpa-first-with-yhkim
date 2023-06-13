package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // transactional이 걸려야 lazyloading이 걸린다. public method는 transactional이 걸린다.
//@AllArgsConstructor
@RequiredArgsConstructor // final이 걸려있는 member변수에 생성자를 만들어준다.
public class MemberService {

  private final MemberRepository memberRepository;

//  @Autowired // 요즘 스프링은 @Autowired를 안걸어도 멤버가 1개면 알아서 찾아서 인잭션해준다.
//  public MemberService(MemberRepository memberRepository) {
//    this.memberRepository = memberRepository;
//  }

  /**
   * 회원 가입
   * @param member
   * @return
   */
  @Transactional // 서비스에 @Transactional(readOnly = true)을 걸고 데이터 변경하는 메소드에는 @Transactional를 거는 형태를 적용하자. 근데 만약 커맨드성 ServiceClass에는 전체적으로 @Transactional를 걸자.
  public Long join(Member member) {
    validateDuplicateMember(member);
    memberRepository.save(member); // persistence context에 member를 저장하면 member에 id값이 부여됨
    return member.getId();
  }

  private void validateDuplicateMember(Member member) {
    List<Member> findMembers = memberRepository.findByName(member.getName()); // @@@@이렇게 데이터를 미리 조회해도 동시에 데이터를 save하면 에러가 날 수 있다. 따라서 name에 유니크 인덱스를 걸어두자.@@@@
    if (!findMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }

//  @Transactional(readOnly = true) transactional의 readOnly true를 하면 JPA에서는 읽기를 더 최적화를 한다고한다. 그래서 읽기에는 가급적 readonly = true 를 거는것이 좋다.
  public List<Member> findMembers() {
    return memberRepository.findAll();
  }

  public Member findOne(Long memberId) {
    return memberRepository.findOne(memberId);
  }
}
