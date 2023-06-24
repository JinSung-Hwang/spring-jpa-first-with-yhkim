package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

  private final EntityManager em;

  public void save(Order order) {
    em.persist(order);
  }

  public Order findOne(Long id) {
    return em.find(Order.class, id);
  }

  // 동적 쿼리를 jpql을 조건문을 통해서 동적 생성하는 방식:: 너무 불편하고 코드도 길어지고 가독성도 떨어짐... 사용하면 이렇게 코딩하면 안된다.
  public List<Order> findAllByString(OrderSearch orderSearch) {
    String jpql = "Select o From Order o join o.member m ";
    boolean isFirstCondition = true;
    // 주문 상태 검색
    if (orderSearch.getOrderStatus() != null) {
      if(isFirstCondition) {
        jpql += " where ";
        isFirstCondition = false;
      } else {
        jpql += " and ";
      }
      jpql += " o.status = : status";
    }

    // 회원 이름 검색
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      if (isFirstCondition) {
        jpql += " where ";
        isFirstCondition = false;
      } else {
        jpql += " and ";
      }
      jpql += " m.name like :name";
    }

    TypedQuery<Order> query = em.createQuery(jpql, Order.class)
            .setMaxResults(1000);// 최대 1000건 조회

    if (orderSearch.getOrderStatus() != null) {
      query = query.setParameter("status", orderSearch.getOrderStatus());
    }
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      query = query.setParameter("name", orderSearch.getMemberName());
    }

    return query.getResultList();
  }


  /**
   * JPA Criteria
   */
  // 이 방식은 실무에서 쓰라고 만들것이 아니다. 코드를 보고 실제 쿼리가 직관적으로 떠오르지 않는다... 유지보수가 하기 힘들다. 내가 보기엔 문법이 어렵다.
  public List<Order> findAllByCriteria(OrderSearch orderSearch) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Order> cq = cb.createQuery(Order.class);
    Root<Order> o = cq.from(Order.class); // o라는 alias 설정
    Join<Object, Object> m = o.join("member", JoinType.INNER); // m라는 alias 설정

    List<Predicate> criteria = new ArrayList<>();

    // 주문 상태 검색
    if (orderSearch.getOrderStatus() != null) {
      Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
      criteria.add(status);
    }
    // 회원 이름 검색
    if (StringUtils.hasText(orderSearch.getMemberName())) {
      Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
      criteria.add(name);
    }

    cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
    TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
    return query.getResultList();
  }



}
