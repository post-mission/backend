## 📌 JPQL

-   Application이 필요한 data만 db에서 쏙쏙 뽑아서 가져오려면, 결국 검색 조건이 포함된 SQL이 필요하다.
-   JPQ는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어를 제공한다.
-   JPQL은 Entity 객체를 대상으로 쿼리한다.
    -   반면에, SQL은 Table 대상으로 쿼리한다.
    -   JPQL은 SQL로 변환돼서 실행된다.
-   JPQL을 한 마디로 정의하면 객체 지향 SQL이다.
-   쿼리에 의해 가져온 Entity는 전부 영속성 컨텍스트에 의해 관리된다.

### ▸ JPQL 실습 -1) wildcard

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

        // 영속성 컨텍스트
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

### ▸ JPQL 실습 -2) setParameter

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

        // 영속성 컨텍스트
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

### ▸ JPQL 실습 -3) paging

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

        // 영속성 컨텍스트
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
    -   첫 시작 index를 지정한다.
-   **setMaxResult()**
    -   몇 개를 가져올 것인지 지정한다.

## 📌 JPQL join

### ▸ inner join

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

        // 영속성 컨텍스트
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

### ▸ outer join

```java
List<Member> resultList = em.createQuery("select m from Member m left join m.team", Member.class)
        .getResultList();
```

### ▸ 서브쿼리

-   where , having 절에서 서브쿼리를 날릴 수 있다.
-   from 절의 서브 쿼리는 현재 JPQL에서는 불가능
    -   join으로 풀 수 있으면 join으로 해결하면 된다.

### ▸ cf) 묵시적 조인

```java
List<Team> resultList = em.createQuery("select m.team from Member m", Team.class)
        .getResultList();
```

-   join 조건을 명시해주지 않아도 join이 가능하다.
    -   이를 묵시적 join이라고 한다.
-   그런데, 이렇게 묵시적으로 하면 나중에 query 를 튜닝해야 했을 때 어렵다.
-   그래서 명시적으로 join해야 한다.

#### ▸ 참고

-   현재는 간단한 형태의 query만 다루었지만, JPQL 역시 자잘하게 학습해야 하는 양이 방대하다. 이번 스터디에서는 필수적으로 알아야하는 query의 형태에 대해서만 학습하고, 나머지는 프로젝트에서 실습하면서 학습하는 것이 좋을 것 같다.

## 📌 Spring data jpa

### ▸ 실습환경

* 자료 참조

### ▸ application.yml

```java
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: "jdbc:mysql://localhost:3306/스키마이름?userSSL=false&useUnicode=true&serverTimezone=Asia/Seoul"
    username: 이름
    password: 비밀번호
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

## 📌 실습 -1) save() , find()

### ▸ Member.java

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

### ▸ MemberJpaRepository.java

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

### ▸ MemberRepository.java

```java
package com.datajpastudy.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
```

### ▸ MemberJpaRepositoryTest.java

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

    @DisplayName("1. 기본 jpa 를 이용")
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

### ▸ 어떻게 interface만 있는데 기능을 가질 수 있는가?

```java
@Test
void testRepo(){
    System.out.println(memberRepository.getClass());
}
```

Spring data jpa가 상속받은 interface를 보고 구현 클래스를 알아서 만들어서 꽂아 버린다.

## 📌 인터페이스 기능

### ▸ MemberJpaRepository.java

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

위의 메서드들 전부 **jpaRepository**에서 제공한다.

## 📌 쿼리 메서드

### ▸ MemberRepository.java

```java
public interface MemberRepository extends JpaRepository<Member,Long> {
    List<Member> findByUsername(String username);
}
```

-   찾고 싶은 값이 있다면 위와 같이 findBy~ 로 메서드를 정의해주면 된다.
    -   만약 username과 age가 해당 parameter보다 큰 값을 찾고 싶다면?

### ▸ MemberJpaRepository.java

```java
public List<Member> findByNameAndAgeGreaterThan(String name,int age){
    return em.createQuery("select m from Member m where m.name = :name and m.age > :age")
            .setParameter("name",name)
            .setParameter("age",age)
            .getResultList();
}
```

### ▸ MemberRepository.java

```java
public interface MemberRepository extends JpaRepository<Member,Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username,int age);
}
```

### ▸ MemberJpaRepositoryTest.java

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

공식문서 참조

> [https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)

parameter가 2개 정도까지는 해당 기능을 이용하되, 2개가 넘어가면 JPQL을 이용하는게 낫다. ( QueryDSL )