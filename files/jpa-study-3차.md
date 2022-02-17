## 📌 목차

-   Proxy
    -   proxy의 개념
    -   FetchType.LAZY , FetchType.EAGER
    -   패치조인
    -   N+1 문제
    -   참조) @ManyToMany
    -   @Embedded 값 타입

-   이후 학습해야 할 사항
    -   JPQL -> ( QueryDSL : 스터디에서는 학습 x )
    -   Spring Data JPA

## 📌 Proxy

-   **em.find()** - **em.getReference()**
    -   **em.find()**
        -   DB에서 실제 Entity 객체를 조회
    -   **em.getReference()**
        -   DB조회를 아직 하지 않은, 가짜 객체인 Proxy객체를 조회

### ▸ proxy 예제

```java
import entity.Member;
import entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 영속성 컨텍스트
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("teamA");

            Member memberA = new Member();
            memberA.setUsername("memberA");
            memberA.setTeam(teamA);

            em.persist(memberA);
            em.persist(teamA);

            em.flush();
            em.clear();

            Member reference = em.getReference(Member.class, memberA.getId());
            System.out.println(reference.getUsername());
            System.out.println(reference.getClass());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
```

proxy 객체가 조회되는 것을 확인할 수 있다.

또한 현재 member를 찾아왔음에도 select query가 나가지 않는 것을 볼 수 있다.

이는 member가 proxy객체로 찾아와 졌기 때문에, 실제로 조회되기 전까지는 select query가 나가지 않는 것이다.

해당 **System.out.println()** 코드의 주석을 풀면 그때서야 **select query**가 나가는 것을 확인할 수 있다.

## 📌 프록시의 특징

DB 트랜잭션 격리수준에서 repeatable read를 보장한다.

> 트랜잭션 격리수준  
> [https://imsfromseoul.tistory.com/156?category=867222](https://imsfromseoul.tistory.com/156?category=867222)

### ▸ repeatableRead 보장 예제

```java
import entity.Member;
import entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 영속성 컨텍스트
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("teamA");

            Member memberA = new Member();
            memberA.setUsername("memberA");
            memberA.setTeam(teamA);

            em.persist(memberA);
            em.persist(teamA);

            em.flush();
            em.clear();

            Member reference = em.getReference(Member.class, memberA.getId());
            Member member = em.find(Member.class, memberA.getId());
            
            System.out.println(reference.getClass());
            System.out.println(member.getClass());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
```

---

```java
Member member = em.find(Member.class, memberA.getId());
Member reference = em.getReference(Member.class, memberA.getId());
```

순서만 바꿔서 다시 출력해보자.

이는 모두 한 trasaction 안에서 repeatable read를 보장해주기 위함이다.

## 📌 Proxy 조회 - LAZY

[##_Image|kage@k4u4I/btrq8JEuPTW/qLInQaqOVkOkKsMav3Ynkk/img.png|CDM|1.3|{"originWidth":1280,"originHeight":638,"style":"alignCenter","width":550,"height":274}_##]

### ▸ Member.class

```java
package entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;
}
```

-   **fetch = FetchType.LAZY** 를 설정해준다.

### ▸ Team 조회

```java
import entity.Member;
import entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 영속성 컨텍스트
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("teamA");

            Member memberA = new Member();
            memberA.setUsername("memberA");
            memberA.setTeam(teamA);

            em.persist(memberA);
            em.persist(teamA);

            em.flush();
            em.clear();

            Member member = em.find(Member.class, memberA.getId());
            System.out.println(member.getTeam().getClass());
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
```

Team의 경우 proxy 객체가 조회된다.

proxy 예제와 마찬가지로, 실제 team 객체 조회시 쿼리가 나간다.

## 📌 EAGER의 경우

**EAGER**로 매핑돼 있는 해당 Entity의 연관관계 Entity를 전부 가져온다.

( **@ManyToOne**의 경우 default 가 EAGER이다. )

위의 LAZY예제에서 Team과의 연관관계를 EAGER로 바꾸면 Team객체가 proxy객체가 아닌 실제 객체로 조회되는 것을 볼 수 있다.

### ▸ EAGER 실습 예제

-   참고) Order

Entity이름을 Order로 생성하면 생성이 안된다. Orders로 생성해야 한다. 키워드가 겹치기 때문이다.

-   em.find() 를 해서 찾으면 select query가 join이 돼서 찾아와진다. 문제는 JPQL을 사용할 때의 문제.
    -   JPA에서 제공하는 method만으로 db에 있는 데이터를 다 조회하는 것은 불가능하다. 결국은 query를 생성해서 db의 값을 조회해야 한다.

```java
import entity.Member;
import entity.OrderStatus;
import entity.Orders;
import entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 영속성 컨텍스트
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{
            Team teamA = new Team();
            teamA.setName("teamA");

            Team teamB = new Team();
            teamB.setName("teamB");

            Team teamC = new Team();
            teamC.setName("teamC");

            Member memberA = new Member();
            memberA.setUsername("memberA");
            memberA.setOrderStatus(OrderStatus.cancel);
            memberA.setTeam(teamA);

            Member memberB = new Member();
            memberB.setUsername("memberB");
            memberB.setOrderStatus(OrderStatus.cancel);
            memberB.setTeam(teamB);

            Member memberC = new Member();
            memberC.setUsername("memberC");
            memberC.setOrderStatus(OrderStatus.cancel);
            memberC.setTeam(teamB);

            Member memberD = new Member();
            memberD.setUsername("memberD");
            memberD.setOrderStatus(OrderStatus.cancel);
            memberD.setTeam(teamC);

            Orders orderA = new Orders();
            orderA.setName("orderA");
            orderA.setMember(memberA);

            Orders orderB = new Orders();
            orderA.setName("orderB");
            orderB.setMember(memberB);

            em.persist(teamA);
            em.persist(teamB);
            em.persist(teamC);

            em.persist(memberA);
            em.persist(memberB);
            em.persist(memberC);
            em.persist(memberD);

            em.persist(orderA);
            em.persist(orderB);

            em.flush();
            em.clear();

            System.out.println("===========LINE===========");

            List<Orders> ordersList = em.createQuery("select o from Orders o", Orders.class)
                    .getResultList();

//            ordersList.forEach(System.out::println);

            tx.commit();

        }catch(Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
```

Order를 조회하는 첫번째의 쿼리 1, 나머지 data의 개수만큼 N번의 쿼리가 추가로 나갔다. 이런 문제를 **N+1문제**라고 한다.

### ▸ 궁금증?

-   만약에 **Orders** > **member** 만 **LAZY**로 잡는다면?

select query가 하나만 나간다.

### ▸ EAGER는 어떤 상황에서 사용할까?

### ▸ 그렇다면 LAZY에서는 N+1문제가 없을까?

-   Orders안에 있는 Member와의 관계를 LAZY로 설정해놓고, OrderA의 Member를 가져와서 getName()을 출력해보자.
    -   해당 Member의 수만큼 쿼리가 추가로 나간다.
-   이 또한 N+1문제를 야기한다.

### ▸ N+1문제 해결방법

-   **fetch join(패치 조인)**
    -   처음에 가져올 때 전부 다 조회해서 가져오는 방법

```java
List<Orders> ordersList = em.createQuery("select o from Orders o join fetch o.member", Orders.class)
        .getResultList();

for(Orders orders : ordersList){
    String username = orders.getMember().getUsername();
}
```
한 번에 조인해서 가져온다.

그런데 fetch join은 한 번만 사용이 가능하다.

만약 위의 예에서 아래와 같이 Team을 조회하려고 하면 다시 Team에 대한 쿼리가 나간다.

```java
for(Orders orders : ordersList){
    String username = orders.getMember().getTeam().getName();
}
```

최상위 조건에서 모든 조건을 한 번에 join해서 가져오는 방법은 없을까?

잘 몰라서, 검색을 해보니 여러가지를 더 알아야 하나 보다.

(참조 링크 : [https://www.inflearn.com/questions/205272](https://www.inflearn.com/questions/205272))

## 📌 참조 @ManyToMany

다대다 관계에서 **@ManyToMany**로 매핑을 할 수는 있다.

그러나 사용하면 안된다.

**@ManyToMany**는 DB에서 중간 테이블이 만들어지는데, Jpa에서 해당 중간 테이블에 **column**을 추가할 방법이 없다.

그런데 실무에서는 무조건 해당 테이블에 **createdAt** 이라든지, 여러 **column**을 추가할 일이 무조건 생긴다.

## 📌 값 타입

값 타입은 int, String 과 같은 말 그대로 '값' 타입이다.

JPA에서는 이를 임베디드 타입(embedded type)이라고 한다.

잘 설계한 ORM application은 매핑한 테이블의 수보다 클래스의 수가 더 많다.

이는 높은 응집도에 대한 설계를 바탕으로 한다.

-   @Embeddable : 값 타입을 정의하는 곳에 표시
-   @Embedded : 값 타입을 사용하는 곳에 표시

### ▸ Period.java

```java
@Embeddable
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Period(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
```

### ▸ Member.java

```java
@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private Period workPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;
}
```

---

#### ▸ 참조 ) ddl auto option으로 table 생성시 위와 같이 erd diagram에서 화살표가 보이지 않는 문제

> [https://stackoverflow.com/questions/42506921/er-diagram-not-displaying-relationships-in-datagrip](https://stackoverflow.com/questions/42506921/er-diagram-not-displaying-relationships-in-datagrip)

table 생성시 query 방식에서 ddl auto option으로 날린 쿼리로 table을 생성하면 화살표가 생성되지 않는 것 같다.

#### ▸ 에러 참조 )

> unexpected token: Member

Member가 내가 생성한 Member가 아닌 다른 Member가 import 돼서 생긴 문제.

entity인 Member 구분을 위해 entity 폴더를 따로 생성해서 진행하자.

> [https://m.blog.naver.com/writer0713/221498059985](https://m.blog.naver.com/writer0713/221498059985)