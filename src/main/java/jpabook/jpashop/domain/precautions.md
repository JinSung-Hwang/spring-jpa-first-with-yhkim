## 엔티티 설계시 주의점

### 엔티티에는 가급적 setter를 사용하지 말자 
setter가 모두 열려있다면 변경 포인트가 너무 많아서 유지보수가 어렵다. </br> 
(내 생각에는 setter로 변경하면 변경하는 맥락을 코드에 잘 표현하지 못하게 된다.) </br> 

### 모든 연관 관계는 lazy loading을 사용해야한다. 
eager를 하면 연관된 가져오는데 그러면 어떤 SQL이 실행되는지 추적이 어렵다.  </br> 
특히 N + 1 문제가 자주 발생할 수 있다. </br> 
@xxToOne(@OneToOne, @ManyToOne)은 fetchType이 기본적으로 Eager이기 때문에 fetchType을 Lazy로 바꿔야한다. </br> 

FetchType.LAZY를 static으로 쓰면 좀더 깔끔하다.  </br> 
@ManyToOne(fetch = FetchType.LAZY) </br> 
-> </br> 
@ManyToOne(fetch = LAZY) </br> 

eager을 쓰고 싶을때는 fetchJoin등을 쓰면된다. </br>
다 해결책이 있다.  

### 컬렉션 필드에서 초기화를 하자.
컬렉션은 필드에서 바로 초기화 하는것이 안전하다. (생성자에서 초기화 하면 안된다.)
하이버네이트는 엔티티를 영속화할때 컬렉션을 감싸서 하이버 네이트가 제공하는 내장컬렉션으로 변경한다.
내장컬렉션을 사용하는 이유는 컬렉션 변경 감지를하여 영속성 관리를 하기 위해서 그렇게한다.
하이버네이트가 컬렉션을 하이버네이트의 내장컬렉션으로 변경했는데 거기에서 다시 초기화를하면 꼬여서 버그가 난다.

### 테이블 칼럼명 생성 전략
스프링 부트에서 하이버네이트 기본 매핑 전략을 변경해서 실제 테이블 피륻명은 다름
하이버네이트 기존 구현: 엔티티의 필드명을 그대로 테이블 명으로 사용
(SpringPhysicalNamingStrategy)

만약 spring.jpa.hibernate.naming.physical-strategy의 클래스를 바꿔치기하면 회사 컨벤션에 맞춰서 네이밍 룰을 만들 수 있음
(username -> usernm)


