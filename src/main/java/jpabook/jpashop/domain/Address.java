package jpabook.jpashop.domain;


import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // 내장타입으로 사용될 수 있도록함, @Embedded와 @Embeddable중 하나만 사용해도 된다고 한다. 그치만 보통 두개를 사용함
@Getter
public class Address {
  private String city;
  private String street;
  private String zipcode;

  // JPA에서는 기본 생성자를 이용해서 reflection이나 proxy를 사용해야하는데 없으면 안되기때문에 기본 생성자를 추가해줬다.
  // public은 다른 개발자가 쓸 수도 있고 JPA에서는 protected까지만 허용한다.
  protected Address() {

  }

  public Address(String city, String street, String zipcode) {
    this.city = city;
    this.street = street;
    this.zipcode = zipcode;
  }
}
