## ๐ JPQL

-   Application์ด ํ์ํ data๋ง db์์ ์์ ๋ฝ์์ ๊ฐ์ ธ์ค๋ ค๋ฉด, ๊ฒฐ๊ตญ ๊ฒ์ ์กฐ๊ฑด์ด ํฌํจ๋ SQL์ด ํ์ํ๋ค.
-   JPQ๋ SQL์ ์ถ์ํํ JPQL์ด๋ผ๋ ๊ฐ์ฒด ์งํฅ ์ฟผ๋ฆฌ ์ธ์ด๋ฅผ ์ ๊ณตํ๋ค.
-   JPQL์ Entity ๊ฐ์ฒด๋ฅผ ๋์์ผ๋ก ์ฟผ๋ฆฌํ๋ค.
    -   ๋ฐ๋ฉด์, SQL์ Table ๋์์ผ๋ก ์ฟผ๋ฆฌํ๋ค.
    -   JPQL์ SQL๋ก ๋ณํ๋ผ์ ์คํ๋๋ค.
-   JPQL์ ํ ๋ง๋๋ก ์ ์ํ๋ฉด ๊ฐ์ฒด ์งํฅ SQL์ด๋ค.
-   ์ฟผ๋ฆฌ์ ์ํด ๊ฐ์ ธ์จ Entity๋ ์ ๋ถ ์์์ฑ ์ปจํ์คํธ์ ์ํด ๊ด๋ฆฌ๋๋ค.

### โธ JPQL ์ค์ต -1) wildcard

```java
import entity.*;

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
            Member memberA = new Member();
            memberA.setUsername("memberA");

            Member memberB = new Member();
            memberB.setUsername("BB");

            Member memberC = new Member();
            memberC.setUsername("memberC");

            Member memberD = new Member();
            memberD.setUsername("memberD");

            em.persist(memberA);
            em.persist(memberB);
            em.persist(memberC);
            em.persist(memberD);

            em.flush();
            em.clear();

            List<Member> memberList = em.createQuery("select m from Member m where m.username like 'member%'", Member.class)
                    .getResultList();
            memberList.forEach(System.out::println);

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

### โธ JPQL ์ค์ต -2) setParameter

```java
import entity.*;

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
            Member memberA = new Member();
            memberA.setUsername("memberA");

            Member memberB = new Member();
            memberB.setUsername("BB");

            Member memberC = new Member();
            memberC.setUsername("memberC");

            Member memberD = new Member();
            memberD.setUsername("memberD");

            em.persist(memberA);
            em.persist(memberB);
            em.persist(memberC);
            em.persist(memberD);

            em.flush();
            em.clear();

            Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username","memberA")
                    .getSingleResult();
            System.out.println(findMember);

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

### โธ JPQL ์ค์ต -3) paging

```java
import entity.*;

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
            Member memberA = new Member();
            memberA.setUsername("memberA");

            Member memberB = new Member();
            memberB.setUsername("BB");

            Member memberC = new Member();
            memberC.setUsername("memberC");

            Member memberD = new Member();
            memberD.setUsername("memberD");

            em.persist(memberA);
            em.persist(memberB);
            em.persist(memberC);
            em.persist(memberD);

            em.flush();
            em.clear();

            List<Member> memberList = em.createQuery("select m from Member m where m.username like 'member%'", Member.class)
                            .setFirstResult(0)
                                    .setMaxResults(2)
                                            .getResultList();
            memberList.forEach(System.out::println);

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

-   **setFirstResult()**
    -   ์ฒซ ์์ index๋ฅผ ์ง์ ํ๋ค.
-   **setMaxResult()**
    -   ๋ช ๊ฐ๋ฅผ ๊ฐ์ ธ์ฌ ๊ฒ์ธ์ง ์ง์ ํ๋ค.

## ๐ JPQL join

### โธ inner join

```java
import entity.*;

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

            Member memberA = new Member();
            memberA.setUsername("memberA");
            memberA.setTeam(teamA);

            Member memberB = new Member();
            memberB.setUsername("BB");

            Member memberC = new Member();
            memberC.setUsername("memberC");

            Member memberD = new Member();
            memberD.setUsername("memberD");

            em.persist(teamA);
            em.persist(memberA);
            em.persist(memberB);
            em.persist(memberC);
            em.persist(memberD);

            em.flush();
            em.clear();

            List<Member> resultList = em.createQuery("select m from Member m join m.team", Member.class)
                    .getResultList();

            resultList.forEach(System.out::println);

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

### โธ outer join

```java
List<Member> resultList = em.createQuery("select m from Member m left join m.team", Member.class)
        .getResultList();
```

### โธ ์๋ธ์ฟผ๋ฆฌ

-   where , having ์ ์์ ์๋ธ์ฟผ๋ฆฌ๋ฅผ ๋ ๋ฆด ์ ์๋ค.
-   from ์ ์ ์๋ธ ์ฟผ๋ฆฌ๋ ํ์ฌ JPQL์์๋ ๋ถ๊ฐ๋ฅ
    -   join์ผ๋ก ํ ์ ์์ผ๋ฉด join์ผ๋ก ํด๊ฒฐํ๋ฉด ๋๋ค.

### โธ cf) ๋ฌต์์  ์กฐ์ธ

```java
List<Team> resultList = em.createQuery("select m.team from Member m", Team.class)
        .getResultList();
```

-   join ์กฐ๊ฑด์ ๋ช์ํด์ฃผ์ง ์์๋ join์ด ๊ฐ๋ฅํ๋ค.
    -   ์ด๋ฅผ ๋ฌต์์  join์ด๋ผ๊ณ  ํ๋ค.
-   ๊ทธ๋ฐ๋ฐ, ์ด๋ ๊ฒ ๋ฌต์์ ์ผ๋ก ํ๋ฉด ๋์ค์ query ๋ฅผ ํ๋ํด์ผ ํ์ ๋ ์ด๋ ต๋ค.
-   ๊ทธ๋์ ๋ช์์ ์ผ๋ก joinํด์ผ ํ๋ค.

#### โธ ์ฐธ๊ณ 

-   ํ์ฌ๋ ๊ฐ๋จํ ํํ์ query๋ง ๋ค๋ฃจ์์ง๋ง, JPQL ์ญ์ ์์ํ๊ฒ ํ์ตํด์ผ ํ๋ ์์ด ๋ฐฉ๋ํ๋ค. ์ด๋ฒ ์คํฐ๋์์๋ ํ์์ ์ผ๋ก ์์์ผํ๋ query์ ํํ์ ๋ํด์๋ง ํ์ตํ๊ณ , ๋๋จธ์ง๋ ํ๋ก์ ํธ์์ ์ค์ตํ๋ฉด์ ํ์ตํ๋ ๊ฒ์ด ์ข์ ๊ฒ ๊ฐ๋ค.

## ๐ Spring data jpa

### โธ ์ค์ตํ๊ฒฝ

* ์๋ฃ ์ฐธ์กฐ

### โธ application.yml

```java
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: "jdbc:mysql://localhost:3306/์คํค๋ง์ด๋ฆ?userSSL=false&useUnicode=true&serverTimezone=Asia/Seoul"
    username: ์ด๋ฆ
    password: ๋น๋ฐ๋ฒํธ
    hikari:
      auto-commit: false
      connection-test-query: SELECT 1
      minimum-idle: 10
      maximum-pool-size: 50
      transaction-isolation: TRANSACTION_READ_UNCOMMITTED

  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
#        show_sql: true
        format_sql: true

logging:
  level:
    org.hibernate.SQL: debug
#    org.hibernate:type: trace
```

## ๐ ์ค์ต -1) save() , find()

### โธ Member.java

```java
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;

    public Member(String name) {
        this.name = name;
    }
}
```

### โธ MemberJpaRepository.java

```java
package com.datajpastudy.entity;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }
    public Member find(Long id){
        return em.find(Member.class,id);
    }
}
```

### โธ MemberRepository.java

```java
package com.datajpastudy.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
```

### โธ MemberJpaRepositoryTest.java

```java
package com.datajpastudy.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
@Rollback
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    MemberRepository memberRepository;

    @DisplayName("1. ๊ธฐ๋ณธ jpa ๋ฅผ ์ด์ฉ")
    @Test
    void testMember(){
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(savedMember.getId());

        Assertions.assertThat(savedMember.getId()).isEqualTo(findMember.getId());
    }

    @Test
    void testMember2() {
        Member member = new Member("memberB");
        Member savedMember = memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findById(savedMember.getId());
        findMember.ifPresent((mem)->{
            System.out.println("===============");
            System.out.println(mem.getName());
            System.out.println("===============");
        });

    }
}
```

### โธ ์ด๋ป๊ฒ interface๋ง ์๋๋ฐ ๊ธฐ๋ฅ์ ๊ฐ์ง ์ ์๋๊ฐ?

```java
@Test
void testRepo(){
    System.out.println(memberRepository.getClass());
}
```

Spring data jpa๊ฐ ์์๋ฐ์ interface๋ฅผ ๋ณด๊ณ  ๊ตฌํ ํด๋์ค๋ฅผ ์์์ ๋ง๋ค์ด์ ๊ฝ์ ๋ฒ๋ฆฐ๋ค.

## ๐ ์ธํฐํ์ด์ค ๊ธฐ๋ฅ

### โธ MemberJpaRepository.java

```java
@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public void delete(Member member){
        em.remove(member);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count(){
        return em.createQuery("select count(m) from Member m",Long.class)
                .getSingleResult();
    }

    public Member find(Long id){
        return em.find(Member.class,id);
    }
}
```

์์ ๋ฉ์๋๋ค ์ ๋ถ **jpaRepository**์์ ์ ๊ณตํ๋ค.

## ๐ ์ฟผ๋ฆฌ ๋ฉ์๋

### โธ MemberRepository.java

```java
public interface MemberRepository extends JpaRepository<Member,Long> {
    List<Member> findByUsername(String username);
}
```

-   ์ฐพ๊ณ  ์ถ์ ๊ฐ์ด ์๋ค๋ฉด ์์ ๊ฐ์ด findBy~ ๋ก ๋ฉ์๋๋ฅผ ์ ์ํด์ฃผ๋ฉด ๋๋ค.
    -   ๋ง์ฝ username๊ณผ age๊ฐ ํด๋น parameter๋ณด๋ค ํฐ ๊ฐ์ ์ฐพ๊ณ  ์ถ๋ค๋ฉด?

### โธ MemberJpaRepository.java

```java
public List<Member> findByNameAndAgeGreaterThan(String name,int age){
    return em.createQuery("select m from Member m where m.name = :name and m.age > :age")
            .setParameter("name",name)
            .setParameter("age",age)
            .getResultList();
}
```

### โธ MemberRepository.java

```java
public interface MemberRepository extends JpaRepository<Member,Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username,int age);
}
```

### โธ MemberJpaRepositoryTest.java

```java
@Test
void findByUsernameAndAgeGreaterThen() {
    // given
    Member m1 = new Member("AAA",10);
    Member m2 = new Member("AAA",20);
    Member m3 = new Member("AAA",30);
    // when

    memberJpaRepository.save(m1);
    memberJpaRepository.save(m2);
    memberJpaRepository.save(m3);

    List<Member> aaa = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

    aaa.forEach(System.out::println);
    // then
}
```

๊ณต์๋ฌธ์ ์ฐธ์กฐ

> [https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)

parameter๊ฐ 2๊ฐ ์ ๋๊น์ง๋ ํด๋น ๊ธฐ๋ฅ์ ์ด์ฉํ๋, 2๊ฐ๊ฐ ๋์ด๊ฐ๋ฉด JPQL์ ์ด์ฉํ๋๊ฒ ๋ซ๋ค. ( QueryDSL )