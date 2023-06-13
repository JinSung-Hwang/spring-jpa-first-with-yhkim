package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
//  @PersistenceContext // JPA가 EntityManager를 만들어서 주입해준다.
  private final EntityManager em;

//  public MemberRepository(EntityManager em) {
//    this.em = em;
//  }


//  @PersistenceUnit // 엔티티매니저 팩토리를 직접 주입받고 싶다면 이렇게 할 수 있다. 근데 이렇게 잘안한다.
//  private EntityManagerFactory emf;

  public void save(Member member) {
    em.persist(member);
  }

  public Member findOne(Long id) {
    return em.find(Member.class, id);
  }

  public List<Member> findAll() {
    return em.createQuery("Select m From Member m", Member.class).getResultList(); // 자동으로 맴버 도메인 객체리스트로 반환한다.
    // JPA는 FROM절에 테이블 대상이 아니라 도메인 객체를 대상으로 조회를 한다.
  }

  public List<Member> findByName(String name) {
    return em.createQuery("Select m From Member m Where m.name =: name", Member.class)
            .setParameter("name", name)
            .getResultList();
  }


}
