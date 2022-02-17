## 📌 REST API 설계 규칙

* [https://devuna.tistory.com/79](https://devuna.tistory.com/79)

1. 소문자 사용
2. 하이픈 ( - ) 사용 
3. 마지막 url에 슬래시 포함 x
4. 행위 포함 x

## 📌 status code

* [https://www.whatap.io/ko/blog/40/](https://www.whatap.io/ko/blog/40/)

- Success
    - 200 : OK
    - 201 : created
    - 204 : No content
- Redirect
    - 301 : URI 변경
    - 304 : Not Modified
        - (설명 : [http://wiki.gurubee.net/pages/viewpage.action?pageId=26739910](http://wiki.gurubee.net/pages/viewpage.action?pageId=26739910) )
- Fail ( Client )
    - 400 : Bad Request
        - API에 정의되지 않은 요청
    - 401 : Unauthorized ( 인증 오류 )
    - 403 : Forbidden
    - 404 : Not Found
    - 405 : Method Not Allowed
        - API에서 정의되지 않은 메서드 요청
    - 409 : Conflict
    - 429 : Too Many Request
- Fail ( Server )
    - 500 : 서버 내부 오류

## 📌 코딩 컨벤션

* [https://naver.github.io/hackday-conventions-java/](https://naver.github.io/hackday-conventions-java/)
    * 참조 : 네이버 코딩 컨벤션

* [https://jobc.tistory.com/212](https://jobc.tistory.com/212)
    * 참조할만한 코딩 컨벤션

### 코딩 컨벤션 요약

* 패키지 이름은 소문자를 사용하여 작성
    * 단어별 구분을 위해 _나 대문자를 섞지 않는다.

```java
# BAD
package com.navercorp.apiGateway

package com.navercorp.api_gateway

# GOOD
package com.navercorp.apigateway
```

* 네이밍
    * 클래스 : 명사
    * 인터페이스 : 명사/형용사
    * 메서드 : 동사/전치사 시작
* 상수는 대문자와 _ 로 구성

```java
public final int UNLIMITED = -1;
public final String POSTAL_CODE_EXPRESSION = “POST”;
```

* 임시 변수 ( 작은 블럭 범위의 loop안에서의 변수 ) 외에는 1 글자 이름 사용 금지
* static import 에만 와일드 카드(*) 허용

```java
# BAD
import java.util.*;

# GOOD
import java.util.List;
import java.util.ArrayList;
```

* annotation 선언 후 새 줄 사용
    * 만약 parameter가 없고 annotation이 한 개라면 한줄에도 사용 가능

```java
# GOOD
@RequestMapping("/guests")
public void findGuests() {}

# GOOD
@Override public void destroy() {}
```

* 한 줄에 한 문장

```java
# BAD
int base = 0; int weight = 2;

# GOOD
int base = 0;
int weight = 2;

# BAD
int base, weight;

# GOOD
int base;
int weight;
```

* tab을 이용한 들여쓰기 하기
    * tab의 크기는 4개의 space
* 조건 / 반복문에 중괄호 필수 사용

```java
# BAD
if (exp == null) return false;

for (char ch : exp.toCharArray()) if (ch == 0) return false;

# GOOD
if (exp == null) {
    return false;
}

for (char ch : exp.toCharArray()) {

    if (ch == 0) {
        return false;
    }

}
```

* 중괄호 붙여서 쓰기

```java
# BAD
if (line.startWith(WARNING_PREFIX)) {
    return LogPattern.WARN;
}
else if (line.startWith(DANGER_PREFIX)) {
    return LogPattern.DANGER;
}
else {
    return LogPattern.NORMAL;
}

# GOOD
if (line.startWith(WARNING_PREFIX)) {
    return LogPattern.WARN;
} else if (line.startWith(DANGER_PREFIX)) {
    return LogPattern.NORMAL;
} else {
    return LogPattern.NORMAL;
}
```

* 빈줄
    * package 선언 후 빈 줄 삽입
    * 메서드 선언이 끝난 후 다음 메서드 사이에 빈줄 넣기

```java
# GOOD
package com.naver.lucy.util;

import java.util.Date;

# GOOD
public void setId(int id) {
    this.id = id;
}

public void setName(String name) {
    this.name = name;
}
```

* 대괄호 뒤에 공백 삽입

```java
# BAD
int[]masks = new int[]{0, 1, 1};

# GOOD
int[] masks = new int[] {0, 1, 1};
```

* 중괄호 시작 전, 종료 후 공백 삽입

```java
# GOOD
public void printWarnMessage(String line) {
    if (line.startsWith(WARN_PREFIX)) {
        ...
    } else {
        ...
    }
}
```

* 소괄호 앞에 공백

```java
# GOOD
if (maxLine > LIMITED) {
    return false;
}
```

* 식별자와 여는 소괄호 사이에 공백 삽입

```java
# BAD
public StringProcessor () {} // 생성자

@Cached ("local")
public String removeEndingDot (String original) {
    assertNotNull (original);
    ...
}

# GOOD
public StringProcessor() {} // 생성자

@Cached("local")
public String removeEndingDot(String original) {
    assertNotNull(original);
    ...
}
```

* , ; 뒤에 공백 삽입

```java
# BAD
for (int i = 0;i < length;i++) {
    display(level,message,i)
}

# GOOD
for (int i = 0; i < length; i++) {
    display(level, message, i)
}
```

## 📌 Spring Convention

1. lombok 생성 위치
2. setter → builder
3. toString 재정의