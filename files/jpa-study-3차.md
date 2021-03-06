## ๐ ๋ชฉ์ฐจ

-   Proxy
    -   proxy์ ๊ฐ๋
    -   FetchType.LAZY , FetchType.EAGER
    -   ํจ์น์กฐ์ธ
    -   N+1 ๋ฌธ์ 
    -   ์ฐธ์กฐ) @ManyToMany
    -   @Embedded ๊ฐ ํ์

-   ์ดํ ํ์ตํด์ผ ํ  ์ฌํญ
    -   JPQL -> ( QueryDSL : ์คํฐ๋์์๋ ํ์ต x )
    -   Spring Data JPA

## ๐ Proxy

-   **em.find()** - **em.getReference()**
    -   **em.find()**
        -   DB์์ ์ค์  Entity ๊ฐ์ฒด๋ฅผ ์กฐํ
    -   **em.getReference()**
        -   DB์กฐํ๋ฅผ ์์ง ํ์ง ์์, ๊ฐ์ง ๊ฐ์ฒด์ธ Proxy๊ฐ์ฒด๋ฅผ ์กฐํ

### โธ proxy ์์ 

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

        // ์์์ฑ ์ปจํ์คํธ
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

proxy ๊ฐ์ฒด๊ฐ ์กฐํ๋๋ ๊ฒ์ ํ์ธํ  ์ ์๋ค.

๋ํ ํ์ฌ member๋ฅผ ์ฐพ์์์์๋ select query๊ฐ ๋๊ฐ์ง ์๋ ๊ฒ์ ๋ณผ ์ ์๋ค.

์ด๋ member๊ฐ proxy๊ฐ์ฒด๋ก ์ฐพ์์ ์ก๊ธฐ ๋๋ฌธ์, ์ค์ ๋ก ์กฐํ๋๊ธฐ ์ ๊น์ง๋ select query๊ฐ ๋๊ฐ์ง ์๋ ๊ฒ์ด๋ค.

ํด๋น **System.out.println()** ์ฝ๋์ ์ฃผ์์ ํ๋ฉด ๊ทธ๋์์ผ **select query**๊ฐ ๋๊ฐ๋ ๊ฒ์ ํ์ธํ  ์ ์๋ค.

## ๐ ํ๋ก์์ ํน์ง

DB ํธ๋์ญ์ ๊ฒฉ๋ฆฌ์์ค์์ repeatable read๋ฅผ ๋ณด์ฅํ๋ค.

> ํธ๋์ญ์ ๊ฒฉ๋ฆฌ์์ค  
> [https://imsfromseoul.tistory.com/156?category=867222](https://imsfromseoul.tistory.com/156?category=867222)

### โธ repeatableRead ๋ณด์ฅ ์์ 

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

        // ์์์ฑ ์ปจํ์คํธ
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

์์๋ง ๋ฐ๊ฟ์ ๋ค์ ์ถ๋ ฅํด๋ณด์.

์ด๋ ๋ชจ๋ ํ trasaction ์์์ repeatable read๋ฅผ ๋ณด์ฅํด์ฃผ๊ธฐ ์ํจ์ด๋ค.

## ๐ Proxy ์กฐํ - LAZY

[##_Image|kage@k4u4I/btrq8JEuPTW/qLInQaqOVkOkKsMav3Ynkk/img.png|CDM|1.3|{"originWidth":1280,"originHeight":638,"style":"alignCenter","width":550,"height":274}_##]

### โธ Member.class

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

-   **fetch = FetchType.LAZY** ๋ฅผ ์ค์ ํด์ค๋ค.

### โธ Team ์กฐํ

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

        // ์์์ฑ ์ปจํ์คํธ
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

Team์ ๊ฒฝ์ฐ proxy ๊ฐ์ฒด๊ฐ ์กฐํ๋๋ค.

proxy ์์ ์ ๋ง์ฐฌ๊ฐ์ง๋ก, ์ค์  team ๊ฐ์ฒด ์กฐํ์ ์ฟผ๋ฆฌ๊ฐ ๋๊ฐ๋ค.

## ๐ EAGER์ ๊ฒฝ์ฐ

**EAGER**๋ก ๋งคํ๋ผ ์๋ ํด๋น Entity์ ์ฐ๊ด๊ด๊ณ Entity๋ฅผ ์ ๋ถ ๊ฐ์ ธ์จ๋ค.

( **@ManyToOne**์ ๊ฒฝ์ฐ default ๊ฐ EAGER์ด๋ค. )

์์ LAZY์์ ์์ Team๊ณผ์ ์ฐ๊ด๊ด๊ณ๋ฅผ EAGER๋ก ๋ฐ๊พธ๋ฉด Team๊ฐ์ฒด๊ฐ proxy๊ฐ์ฒด๊ฐ ์๋ ์ค์  ๊ฐ์ฒด๋ก ์กฐํ๋๋ ๊ฒ์ ๋ณผ ์ ์๋ค.

### โธ EAGER ์ค์ต ์์ 

-   ์ฐธ๊ณ ) Order

Entity์ด๋ฆ์ Order๋ก ์์ฑํ๋ฉด ์์ฑ์ด ์๋๋ค. Orders๋ก ์์ฑํด์ผ ํ๋ค. ํค์๋๊ฐ ๊ฒน์น๊ธฐ ๋๋ฌธ์ด๋ค.

-   em.find() ๋ฅผ ํด์ ์ฐพ์ผ๋ฉด select query๊ฐ join์ด ๋ผ์ ์ฐพ์์์ง๋ค. ๋ฌธ์ ๋ JPQL์ ์ฌ์ฉํ  ๋์ ๋ฌธ์ .
    -   JPA์์ ์ ๊ณตํ๋ method๋ง์ผ๋ก db์ ์๋ ๋ฐ์ดํฐ๋ฅผ ๋ค ์กฐํํ๋ ๊ฒ์ ๋ถ๊ฐ๋ฅํ๋ค. ๊ฒฐ๊ตญ์ query๋ฅผ ์์ฑํด์ db์ ๊ฐ์ ์กฐํํด์ผ ํ๋ค.

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

        // ์์์ฑ ์ปจํ์คํธ
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

Order๋ฅผ ์กฐํํ๋ ์ฒซ๋ฒ์งธ์ ์ฟผ๋ฆฌ 1, ๋๋จธ์ง data์ ๊ฐ์๋งํผ N๋ฒ์ ์ฟผ๋ฆฌ๊ฐ ์ถ๊ฐ๋ก ๋๊ฐ๋ค. ์ด๋ฐ ๋ฌธ์ ๋ฅผ **N+1๋ฌธ์ **๋ผ๊ณ  ํ๋ค.

### โธ ๊ถ๊ธ์ฆ?

-   ๋ง์ฝ์ **Orders** > **member** ๋ง **LAZY**๋ก ์ก๋๋ค๋ฉด?

select query๊ฐ ํ๋๋ง ๋๊ฐ๋ค.

### โธ EAGER๋ ์ด๋ค ์ํฉ์์ ์ฌ์ฉํ ๊น?

### โธ ๊ทธ๋ ๋ค๋ฉด LAZY์์๋ N+1๋ฌธ์ ๊ฐ ์์๊น?

-   Orders์์ ์๋ Member์์ ๊ด๊ณ๋ฅผ LAZY๋ก ์ค์ ํด๋๊ณ , OrderA์ Member๋ฅผ ๊ฐ์ ธ์์ getName()์ ์ถ๋ ฅํด๋ณด์.
    -   ํด๋น Member์ ์๋งํผ ์ฟผ๋ฆฌ๊ฐ ์ถ๊ฐ๋ก ๋๊ฐ๋ค.
-   ์ด ๋ํ N+1๋ฌธ์ ๋ฅผ ์ผ๊ธฐํ๋ค.

### โธ N+1๋ฌธ์  ํด๊ฒฐ๋ฐฉ๋ฒ

-   **fetch join(ํจ์น ์กฐ์ธ)**
    -   ์ฒ์์ ๊ฐ์ ธ์ฌ ๋ ์ ๋ถ ๋ค ์กฐํํด์ ๊ฐ์ ธ์ค๋ ๋ฐฉ๋ฒ

```java
List<Orders> ordersList = em.createQuery("select o from Orders o join fetch o.member", Orders.class)
        .getResultList();

for(Orders orders : ordersList){
    String username = orders.getMember().getUsername();
}
```
ํ ๋ฒ์ ์กฐ์ธํด์ ๊ฐ์ ธ์จ๋ค.

๊ทธ๋ฐ๋ฐ fetch join์ ํ ๋ฒ๋ง ์ฌ์ฉ์ด ๊ฐ๋ฅํ๋ค.

๋ง์ฝ ์์ ์์์ ์๋์ ๊ฐ์ด Team์ ์กฐํํ๋ ค๊ณ  ํ๋ฉด ๋ค์ Team์ ๋ํ ์ฟผ๋ฆฌ๊ฐ ๋๊ฐ๋ค.

```java
for(Orders orders : ordersList){
    String username = orders.getMember().getTeam().getName();
}
```

์ต์์ ์กฐ๊ฑด์์ ๋ชจ๋  ์กฐ๊ฑด์ ํ ๋ฒ์ joinํด์ ๊ฐ์ ธ์ค๋ ๋ฐฉ๋ฒ์ ์์๊น?

์ ๋ชฐ๋ผ์, ๊ฒ์์ ํด๋ณด๋ ์ฌ๋ฌ๊ฐ์ง๋ฅผ ๋ ์์์ผ ํ๋ ๋ณด๋ค.

(์ฐธ์กฐ ๋งํฌ : [https://www.inflearn.com/questions/205272](https://www.inflearn.com/questions/205272))

## ๐ ์ฐธ์กฐ @ManyToMany

๋ค๋๋ค ๊ด๊ณ์์ **@ManyToMany**๋ก ๋งคํ์ ํ  ์๋ ์๋ค.

๊ทธ๋ฌ๋ ์ฌ์ฉํ๋ฉด ์๋๋ค.

**@ManyToMany**๋ DB์์ ์ค๊ฐ ํ์ด๋ธ์ด ๋ง๋ค์ด์ง๋๋ฐ, Jpa์์ ํด๋น ์ค๊ฐ ํ์ด๋ธ์ **column**์ ์ถ๊ฐํ  ๋ฐฉ๋ฒ์ด ์๋ค.

๊ทธ๋ฐ๋ฐ ์ค๋ฌด์์๋ ๋ฌด์กฐ๊ฑด ํด๋น ํ์ด๋ธ์ **createdAt** ์ด๋ผ๋ ์ง, ์ฌ๋ฌ **column**์ ์ถ๊ฐํ  ์ผ์ด ๋ฌด์กฐ๊ฑด ์๊ธด๋ค.

## ๐ ๊ฐ ํ์

๊ฐ ํ์์ int, String ๊ณผ ๊ฐ์ ๋ง ๊ทธ๋๋ก '๊ฐ' ํ์์ด๋ค.

JPA์์๋ ์ด๋ฅผ ์๋ฒ ๋๋ ํ์(embedded type)์ด๋ผ๊ณ  ํ๋ค.

์ ์ค๊ณํ ORM application์ ๋งคํํ ํ์ด๋ธ์ ์๋ณด๋ค ํด๋์ค์ ์๊ฐ ๋ ๋ง๋ค.

์ด๋ ๋์ ์์ง๋์ ๋ํ ์ค๊ณ๋ฅผ ๋ฐํ์ผ๋ก ํ๋ค.

-   @Embeddable : ๊ฐ ํ์์ ์ ์ํ๋ ๊ณณ์ ํ์
-   @Embedded : ๊ฐ ํ์์ ์ฌ์ฉํ๋ ๊ณณ์ ํ์

### โธ Period.java

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

### โธ Member.java

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

#### โธ ์ฐธ์กฐ ) ddl auto option์ผ๋ก table ์์ฑ์ ์์ ๊ฐ์ด erd diagram์์ ํ์ดํ๊ฐ ๋ณด์ด์ง ์๋ ๋ฌธ์ 

> [https://stackoverflow.com/questions/42506921/er-diagram-not-displaying-relationships-in-datagrip](https://stackoverflow.com/questions/42506921/er-diagram-not-displaying-relationships-in-datagrip)

table ์์ฑ์ query ๋ฐฉ์์์ ddl auto option์ผ๋ก ๋ ๋ฆฐ ์ฟผ๋ฆฌ๋ก table์ ์์ฑํ๋ฉด ํ์ดํ๊ฐ ์์ฑ๋์ง ์๋ ๊ฒ ๊ฐ๋ค.

#### โธ ์๋ฌ ์ฐธ์กฐ )

> unexpected token: Member

Member๊ฐ ๋ด๊ฐ ์์ฑํ Member๊ฐ ์๋ ๋ค๋ฅธ Member๊ฐ import ๋ผ์ ์๊ธด ๋ฌธ์ .

entity์ธ Member ๊ตฌ๋ถ์ ์ํด entity ํด๋๋ฅผ ๋ฐ๋ก ์์ฑํด์ ์งํํ์.

> [https://m.blog.naver.com/writer0713/221498059985](https://m.blog.naver.com/writer0713/221498059985)