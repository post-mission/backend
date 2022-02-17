## 📌 목차

1\. jpa의 동작 원리 영속성 컨텍스트   
2\. Mapping 및 다양한 annotation 🔥  
3. Spring Data jpa

## 📌 스터디의 목표

-   →1차 스터디 복습 :영속성 컨텍스트와 동작 원리에 대해서 이해할 수 있다.
    -   EntityManager(영속성 컨텍스트)
        -   1차캐시
        -   repeatable read 보장
        -   쓰기 지연 sql 저장소
    -   em.persist
    -   em.flush
    -   변경 감지
-   필수 annotation들에 대해서 이해할 수 있다.
    -   @Id
    -   @Entity
    -   @Column
    -   @Enumerated
    -   @GeneratedValue
-   연관관계 매핑에 대해 이해할 수 있다.  
    -   @OneToMany  
        -   연관관계 메서드
    -   @ManyToOne
    -   @OneToOne
    -   외래키의 주인
    -   @ManyToMany
    -   mappedBy
-   3차 스터디 예정  
    -   proxy의 개념
    -   FetchType.LAZY , FetchType.EAGER 
    -   상속 관계 매핑 @DiscriminatorValue ( DTYPE )
    -   @Embedded 값 타입
    -   cascade
    -   JPQL - QueryDSL
    -   N+1문제
    -   Spring Data JPA
-   실습 -1)

Team - Member Entity 생성 및 실습

-   실습 -2)

## 📌 필수 annotation들

### ▸ @Entity

-   Jpa가 관리하는 클래스임을 명시해준다.
-   기본 생성자 필수 : 내부적으로 reflection 같은 기술을 활용하려면 기본 생성자가 필요하다.

### ▸ @Column

-   컬럼 속성을 지정해줄 수 있다.
-   **@Column(name="")** 과 같이 name 속성을 주로 많이 사용한다.
    -   nullable ( 많이 사용된다. )
    -   unique ( 거의 사용되지 않는다. )
    -   length

### ▸ @Enumerated

-   @Enumerated(EnumType.ORDINAL)
    -   숫자로 data 값을 저장하는 방식
-   @Enumerated(EnumType.STRING)
    -   고정된 String으로 값을 저장하는 방식

EnumType은 default가 ORDINAL인데, 반드시 STRING을 사용해야 한다.

만약 ORDINAL을 쓰게 되면 enum의 순서가 바꼈을 경우 대참사가 일어날 수 있다.

### ▸ @GeneratedValue

-   기본키 mapping 방법
    -   직접 할당 : @Id
    -   자동 생성 : @GeneratedValue
-   @GeneratedValue(strategy = GenerationType.IDENTITY)
    -   IDENTITY : 데이터 베이스에 위임
    -   AUTO : DB의 방언에 따라 자동 지정. default

## 📌 연관관계 매핑

### ▸ Member - Team 관계 매핑하기

실습

### ▸ @ManyToOne, @OneToMany

**@ManyToOne** , **@OneToMany**는 자신(해당 Entity)를 기준으로 상대 Entity와의 관계를 명시해준다.

-   Member - Team의 관계
    -   Member : Team = N : 1 관계
    -   Member에서는 Team에게 **@ManyToOne**
    -   Team에서는 Member에게 **@OneToMany**

### ▸ @JoinColumn

-   외래키의 주인을 설정해주어야 한다.
    -   db는 외래키 하나로 왔다갔다 하지만, 객체는 양쪽에 모두 선언해주어서 왔다갔다 할 수 있다.
-   @JoinColumn을 명시해주지 않으면 @JoinTable이라는 전략을 default로 사용해서, 테이블을 하나 더 생성한다.
-   외래키의 주인인 쪽에 @JoinColumn을 명시해준다.

### ▸ mappedBy

-   연관관계의 주인만이 외래 키를 관리한다.
-   주인이 아닌 쪽이면 읽기만 가능하다.
    -   주인이 아닌 쪽은 mappedBy 속성으로 주인을 지정해준다.

### ▸ 외래키의 주인 - @OneToMany 에서의 예제

-   @JoinColumn이 있는 쪽이 외래키의 주인이다.
-   @OneToMany에 설정할 수도 있고, @ManyToOne에 설정할 수도 있다.

```java
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Team team = new Team();
            team.setId(1L);
            team.setName("teamA");

            Member memberA = new Member();
            memberA.setName("memberA");

            Member memberB = new Member();
            memberB.setName("memberB");

            em.persist(memberA);
            em.persist(memberB);

            team.getMemberList().add(memberA);
            team.getMemberList().add(memberB);

            em.persist(team);

            tx.commit();

            Team team1 = em.find(Team.class, 1L);
            team1.getMemberList().forEach(System.out::println);


        }catch(Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
```

-   Team의 경우 memberList를 갖고 있다.
-   그러나 위의 예제처럼 memberList에 member를 추가해주어도 db에 들어가면 team 설정이 되지 않는다.
    -   외래키의 주인만 쓰기가 가능하고, 주인이 아닌쪽은 읽기만 가능하기 때문이다.
-   @OneToMany의 경우 정말 필요한 경우에만 선언해서 사용할 것
    -   유지보수 / 관리가 힘들어진다.

### ▸ @ManyToMany

-   사용하면 안되는 매핑
-   컬럼 추가가 불가능하다.

## 📌 실습 환경

```
-- MySQL Workbench Synchronization
-- Generated: 2022-01-11 17:41
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: 임형준

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE TABLE IF NOT EXISTS `jpa_study`.`member` (
  `member_id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`member_id`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8mb3;

CREATE TABLE IF NOT EXISTS `jpa_study`.`order` (
  `order_id` BIGINT(20) NOT NULL,
  `member_id` BIGINT(20) NOT NULL,
  `order_date` DATETIME NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`order_id`),
  INDEX `fk_order_member1_idx` (`member_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_member1`
    FOREIGN KEY (`member_id`)
    REFERENCES `jpa_study`.`member` (`member_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `jpa_study`.`item` (
  `item_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `category_id` BIGINT(20) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `price` INT(11) NOT NULL,
  `stockQuantity` INT(11) NOT NULL,
  PRIMARY KEY (`item_id`),
  INDEX `fk_item_category1_idx` (`category_id` ASC) VISIBLE,
  CONSTRAINT `fk_item_category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `jpa_study`.`category` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `jpa_study`.`category` (
  `category_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`category_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `jpa_study`.`order_has_item` (
  `order_has_item_id` VARCHAR(45) NOT NULL,
  `order_id` BIGINT(20) NOT NULL,
  `item_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`order_has_item_id`),
  INDEX `fk_order_has_item_item1_idx` (`item_id` ASC) VISIBLE,
  INDEX `fk_order_has_item_order1_idx` (`order_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_has_item_order1`
    FOREIGN KEY (`order_id`)
    REFERENCES `jpa_study`.`order` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_has_item_item1`
    FOREIGN KEY (`item_id`)
    REFERENCES `jpa_study`.`item` (`item_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
```