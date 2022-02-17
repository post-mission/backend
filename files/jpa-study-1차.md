### ▪︎ 전체 목차

1\. jpa의 동작 원리 영속성 컨텍스트 🔥  
2. Mapping 및 다양한 annotation  
3. Spring Data jpa

## 📌 jpa의 필요성

PPT 참조

## 📌 개발 환경 세팅

maven으로 프로젝트 생성

### ▸ maven으로 프로젝트 생성하는 이유

gradle과 spring boot로 순수 jpa를 이용해서 개발하려고 하면 복잡한 세팅을 해주어야 한다.

링크 : ([https://kth990303.tistory.com/30](https://kth990303.tistory.com/30)) - ([https://www.inflearn.com/questions/27532](https://www.inflearn.com/questions/27532))

다른 거는 세팅할 수 있는데, persistence-unit을 설정하는게 까다로워 보였다.

spring과 gradle이 핵심이 아니므로, 해당 스터디에서는 그냥 김영한 - jpa 강좌의 세팅 환경을 따라가기로 했다.

### ▸ porm.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>jpa-basic</groupId>
    <artifactId>ex1-hello-jpa</artifactId>
    <version>1.0.0</version>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
    <dependencies>
        <dependency>
            <groupId>javax.xml.bind</groupId>
            <artifactId>jaxb-api</artifactId>
            <version>2.3.0</version>
        </dependency>
        <!-- JPA 하이버네이트 -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.3.10.Final</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.26</version>
        </dependency>
    </dependencies>
</project>
```

### ▸ persistence.xml

resources > META-INF > persistence.xml 생성

```
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <persistence-unit name="hello">
        <properties>
            <!-- 필수 속성 -->
            <property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver" />
            <property name="javax.persistence.jdbc.user" value="DB아이디" />
            <property name="javax.persistence.jdbc.password" value="DB비밀번호" />
            <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/스키마이름" />
            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL5Dialect" />

            <!-- 옵션 -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true"/>
            <property name="hibernate.hbm2ddl.auto" value="create" />
        </properties>
    </persistence-unit>
</persistence>
```

### ▸ ddl auto option

* 자료 참조

## 📌 Jpa 구동 방식

* 자료 참조

#### ▸ 실습코드 - persist

```java
public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Member member = new Member();
            member.setId(1L);
            member.setName("memberA");

            em.persist(member);
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

-   persistence-unit에 적힌 이름으로 entityManagerFactory를 만들어준다.

설정 정보 읽어 들이기 → Factory 생성 → EntityManager 만들기 → EntityManager로부터 Transaction 가져오기

## 📌 영속성 컨텍스트

* 자료참조

#### ▸ 쓰기지연 SQL 저장소

* 자료 참조

### ▸ 실습코드 - find() : 동일한 객체

```java
public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Member member = new Member();
            member.setId(1L);
            member.setName("memberA");

            em.persist(member);
            tx.commit();

            Member member1 = em.find(Member.class, 1L);
            Member member2 = em.find(Member.class, 1L);

            System.out.println(member1 == member2);
        }catch(Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
```

## 📌 dirty checking ( 변경 감지 )

```java
public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Member member = new Member();
            member.setId(1L);
            member.setName("memberA");

            em.persist(member);
            
            member.setName("memberB");

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

이름을 memberB로 바꿔주고 아무일도 하지 않았는데, DB에 값이 저장된다.

이는 내부적으로 스냅샷 파일과 비교를 해서, 변경이 있다면 감지를 하는 JPA 기술 덕분이다.  

## 📌 필수 annotation

* 자료 참조

### ▸ 기본 annotation 목록들

-   @Entity
-   @Id
-   @GeneratedValue
-   @Column
-   @Enrumerated(EnumType.String) - ORDINAL