## ๐ ๋ชฉ์ฐจ

1\. jpa์ ๋์ ์๋ฆฌ ์์์ฑ ์ปจํ์คํธย   
2\. Mapping ๋ฐ ๋ค์ํ annotation ๐ฅ  
3.ย Springย Dataย jpa

## ๐ ์คํฐ๋์ ๋ชฉํ

-   โ1์ฐจ ์คํฐ๋ ๋ณต์ต :์์์ฑ ์ปจํ์คํธ์ ๋์ ์๋ฆฌ์ ๋ํด์ ์ดํดํ  ์ ์๋ค.
    -   EntityManager(์์์ฑ ์ปจํ์คํธ)
        -   1์ฐจ์บ์
        -   repeatable read ๋ณด์ฅ
        -   ์ฐ๊ธฐ ์ง์ฐ sql ์ ์ฅ์
    -   em.persist
    -   em.flush
    -   ๋ณ๊ฒฝ ๊ฐ์ง
-   ํ์ annotation๋ค์ ๋ํด์ ์ดํดํ  ์ ์๋ค.
    -   @Id
    -   @Entity
    -   @Column
    -   @Enumerated
    -   @GeneratedValue
-   ์ฐ๊ด๊ด๊ณ ๋งคํ์ ๋ํด ์ดํดํ  ์ ์๋ค.  
    -   @OneToMany  
        -   ์ฐ๊ด๊ด๊ณ ๋ฉ์๋
    -   @ManyToOne
    -   @OneToOne
    -   ์ธ๋ํค์ ์ฃผ์ธ
    -   @ManyToMany
    -   mappedBy
-   3์ฐจ ์คํฐ๋ ์์   
    -   proxy์ ๊ฐ๋
    -   FetchType.LAZY , FetchType.EAGERย 
    -   ์์ ๊ด๊ณ ๋งคํ @DiscriminatorValue ( DTYPE )
    -   @Embedded ๊ฐ ํ์
    -   cascade
    -   JPQL - QueryDSL
    -   N+1๋ฌธ์ 
    -   Spring Data JPA
-   ์ค์ต -1)

Team - Member Entity ์์ฑ ๋ฐ ์ค์ต

-   ์ค์ต -2)

## ๐ ํ์ annotation๋ค

### โธ @Entity

-   Jpa๊ฐ ๊ด๋ฆฌํ๋ ํด๋์ค์์ ๋ช์ํด์ค๋ค.
-   ๊ธฐ๋ณธ ์์ฑ์ ํ์ : ๋ด๋ถ์ ์ผ๋ก reflection ๊ฐ์ ๊ธฐ์ ์ ํ์ฉํ๋ ค๋ฉด ๊ธฐ๋ณธ ์์ฑ์๊ฐ ํ์ํ๋ค.

### โธ @Column

-   ์ปฌ๋ผ ์์ฑ์ ์ง์ ํด์ค ์ ์๋ค.
-   **@Column(name="")** ๊ณผ ๊ฐ์ด name ์์ฑ์ ์ฃผ๋ก ๋ง์ด ์ฌ์ฉํ๋ค.
    -   nullable ( ๋ง์ด ์ฌ์ฉ๋๋ค. )
    -   unique ( ๊ฑฐ์ ์ฌ์ฉ๋์ง ์๋๋ค. )
    -   length

### โธ @Enumerated

-   @Enumerated(EnumType.ORDINAL)
    -   ์ซ์๋ก data ๊ฐ์ ์ ์ฅํ๋ ๋ฐฉ์
-   @Enumerated(EnumType.STRING)
    -   ๊ณ ์ ๋ String์ผ๋ก ๊ฐ์ ์ ์ฅํ๋ ๋ฐฉ์

EnumType์ default๊ฐ ORDINAL์ธ๋ฐ, ๋ฐ๋์ STRING์ ์ฌ์ฉํด์ผ ํ๋ค.

๋ง์ฝ ORDINAL์ ์ฐ๊ฒ ๋๋ฉด enum์ ์์๊ฐ ๋ฐ๊ผ์ ๊ฒฝ์ฐ ๋์ฐธ์ฌ๊ฐ ์ผ์ด๋  ์ ์๋ค.

### โธ @GeneratedValue

-   ๊ธฐ๋ณธํค mapping ๋ฐฉ๋ฒ
    -   ์ง์  ํ ๋น : @Id
    -   ์๋ ์์ฑ : @GeneratedValue
-   @GeneratedValue(strategyย =ย GenerationType.IDENTITY)
    -   IDENTITY : ๋ฐ์ดํฐ ๋ฒ ์ด์ค์ ์์
    -   AUTO : DB์ ๋ฐฉ์ธ์ ๋ฐ๋ผ ์๋ ์ง์ . default

## ๐ ์ฐ๊ด๊ด๊ณ ๋งคํ

### โธ Member - Team ๊ด๊ณ ๋งคํํ๊ธฐ

์ค์ต

### โธ @ManyToOne, @OneToMany

**@ManyToOne** , **@OneToMany**๋ ์์ (ํด๋น Entity)๋ฅผ ๊ธฐ์ค์ผ๋ก ์๋ Entity์์ ๊ด๊ณ๋ฅผ ๋ช์ํด์ค๋ค.

-   Member - Team์ ๊ด๊ณ
    -   Member : Team = N : 1 ๊ด๊ณ
    -   Member์์๋ Team์๊ฒ **@ManyToOne**
    -   Team์์๋ Member์๊ฒ **@OneToMany**

### โธ @JoinColumn

-   ์ธ๋ํค์ ์ฃผ์ธ์ ์ค์ ํด์ฃผ์ด์ผ ํ๋ค.
    -   db๋ ์ธ๋ํค ํ๋๋ก ์๋ค๊ฐ๋ค ํ์ง๋ง, ๊ฐ์ฒด๋ ์์ชฝ์ ๋ชจ๋ ์ ์ธํด์ฃผ์ด์ ์๋ค๊ฐ๋ค ํ  ์ ์๋ค.
-   @JoinColumn์ ๋ช์ํด์ฃผ์ง ์์ผ๋ฉด @JoinTable์ด๋ผ๋ ์ ๋ต์ default๋ก ์ฌ์ฉํด์, ํ์ด๋ธ์ ํ๋ ๋ ์์ฑํ๋ค.
-   ์ธ๋ํค์ ์ฃผ์ธ์ธ ์ชฝ์ @JoinColumn์ ๋ช์ํด์ค๋ค.

### โธ mappedBy

-   ์ฐ๊ด๊ด๊ณ์ ์ฃผ์ธ๋ง์ด ์ธ๋ ํค๋ฅผ ๊ด๋ฆฌํ๋ค.
-   ์ฃผ์ธ์ด ์๋ ์ชฝ์ด๋ฉด ์ฝ๊ธฐ๋ง ๊ฐ๋ฅํ๋ค.
    -   ์ฃผ์ธ์ด ์๋ ์ชฝ์ mappedBy ์์ฑ์ผ๋ก ์ฃผ์ธ์ ์ง์ ํด์ค๋ค.

### โธ ์ธ๋ํค์ ์ฃผ์ธ - @OneToMany ์์์ ์์ 

-   @JoinColumn์ด ์๋ ์ชฝ์ด ์ธ๋ํค์ ์ฃผ์ธ์ด๋ค.
-   @OneToMany์ ์ค์ ํ  ์๋ ์๊ณ , @ManyToOne์ ์ค์ ํ  ์๋ ์๋ค.

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

-   Team์ ๊ฒฝ์ฐ memberList๋ฅผ ๊ฐ๊ณ  ์๋ค.
-   ๊ทธ๋ฌ๋ ์์ ์์ ์ฒ๋ผ memberList์ member๋ฅผ ์ถ๊ฐํด์ฃผ์ด๋ db์ ๋ค์ด๊ฐ๋ฉด team ์ค์ ์ด ๋์ง ์๋๋ค.
    -   ์ธ๋ํค์ ์ฃผ์ธ๋ง ์ฐ๊ธฐ๊ฐ ๊ฐ๋ฅํ๊ณ , ์ฃผ์ธ์ด ์๋์ชฝ์ ์ฝ๊ธฐ๋ง ๊ฐ๋ฅํ๊ธฐ ๋๋ฌธ์ด๋ค.
-   @OneToMany์ ๊ฒฝ์ฐ ์ ๋ง ํ์ํ ๊ฒฝ์ฐ์๋ง ์ ์ธํด์ ์ฌ์ฉํ  ๊ฒ
    -   ์ ์ง๋ณด์ / ๊ด๋ฆฌ๊ฐ ํ๋ค์ด์ง๋ค.

### โธ @ManyToMany

-   ์ฌ์ฉํ๋ฉด ์๋๋ ๋งคํ
-   ์ปฌ๋ผ ์ถ๊ฐ๊ฐ ๋ถ๊ฐ๋ฅํ๋ค.

## ๐ ์ค์ต ํ๊ฒฝ

```
-- MySQL Workbench Synchronization
-- Generated: 2022-01-11 17:41
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: ์ํ์ค

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