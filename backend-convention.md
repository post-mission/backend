## πΒ REST API μ€κ³ κ·μΉ

* [https://devuna.tistory.com/79](https://devuna.tistory.com/79)

1. μλ¬Έμ μ¬μ©
2. νμ΄ν ( - ) μ¬μ© 
3. λ§μ§λ§ urlμ μ¬λμ ν¬ν¨ x
4. νμ ν¬ν¨ x

## πΒ status code

* [https://www.whatap.io/ko/blog/40/](https://www.whatap.io/ko/blog/40/)

- Success
    - 200 : OK
    - 201 : created
    - 204 : No content
- Redirect
    - 301 : URI λ³κ²½
    - 304 : Not Modified
        - (μ€λͺ : [http://wiki.gurubee.net/pages/viewpage.action?pageId=26739910](http://wiki.gurubee.net/pages/viewpage.action?pageId=26739910) )
- Fail ( Client )
    - 400 : Bad Request
        - APIμ μ μλμ§ μμ μμ²­
    - 401 : Unauthorized ( μΈμ¦ μ€λ₯ )
    - 403 : Forbidden
    - 404 : Not Found
    - 405 : Method Not Allowed
        - APIμμ μ μλμ§ μμ λ©μλ μμ²­
    - 409 : Conflict
    - 429 : Too Many Request
- Fail ( Server )
    - 500 : μλ² λ΄λΆ μ€λ₯

## πΒ μ½λ© μ»¨λ²€μ

* [https://naver.github.io/hackday-conventions-java/](https://naver.github.io/hackday-conventions-java/)
    * μ°Έμ‘° : λ€μ΄λ² μ½λ© μ»¨λ²€μ

* [https://jobc.tistory.com/212](https://jobc.tistory.com/212)
    * μ°Έμ‘°ν λ§ν μ½λ© μ»¨λ²€μ

### μ½λ© μ»¨λ²€μ μμ½

* ν¨ν€μ§ μ΄λ¦μ μλ¬Έμλ₯Ό μ¬μ©νμ¬ μμ±
    * λ¨μ΄λ³ κ΅¬λΆμ μν΄ _λ λλ¬Έμλ₯Ό μμ§ μλλ€.

```java
# BAD
package com.navercorp.apiGateway

package com.navercorp.api_gateway

# GOOD
package com.navercorp.apigateway
```

* λ€μ΄λ°
    * ν΄λμ€ : λͺμ¬
    * μΈν°νμ΄μ€ : λͺμ¬/νμ©μ¬
    * λ©μλ : λμ¬/μ μΉμ¬ μμ
* μμλ λλ¬Έμμ _ λ‘ κ΅¬μ±

```java
public final int UNLIMITED = -1;
public final String POSTAL_CODE_EXPRESSION = βPOSTβ;
```

* μμ λ³μ ( μμ λΈλ­ λ²μμ loopμμμμ λ³μ ) μΈμλ 1 κΈμ μ΄λ¦ μ¬μ© κΈμ§
* static import μλ§ μμΌλ μΉ΄λ(*) νμ©

```java
# BAD
import java.util.*;

# GOOD
import java.util.List;
import java.util.ArrayList;
```

* annotation μ μΈ ν μ μ€ μ¬μ©
    * λ§μ½ parameterκ° μκ³  annotationμ΄ ν κ°λΌλ©΄ νμ€μλ μ¬μ© κ°λ₯

```java
# GOOD
@RequestMapping("/guests")
public void findGuests() {}

# GOOD
@Override public void destroy() {}
```

* ν μ€μ ν λ¬Έμ₯

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

* tabμ μ΄μ©ν λ€μ¬μ°κΈ° νκΈ°
    * tabμ ν¬κΈ°λ 4κ°μ space
* μ‘°κ±΄ / λ°λ³΅λ¬Έμ μ€κ΄νΈ νμ μ¬μ©

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

* μ€κ΄νΈ λΆμ¬μ μ°κΈ°

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

* λΉμ€
    * package μ μΈ ν λΉ μ€ μ½μ
    * λ©μλ μ μΈμ΄ λλ ν λ€μ λ©μλ μ¬μ΄μ λΉμ€ λ£κΈ°

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

* λκ΄νΈ λ€μ κ³΅λ°± μ½μ

```java
# BAD
int[]masks = new int[]{0, 1, 1};

# GOOD
int[] masks = new int[] {0, 1, 1};
```

* μ€κ΄νΈ μμ μ , μ’λ£ ν κ³΅λ°± μ½μ

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

* μκ΄νΈ μμ κ³΅λ°±

```java
# GOOD
if (maxLine > LIMITED) {
    return false;
}
```

* μλ³μμ μ¬λ μκ΄νΈ μ¬μ΄μ κ³΅λ°± μ½μ

```java
# BAD
public StringProcessor () {} // μμ±μ

@Cached ("local")
public String removeEndingDot (String original) {
    assertNotNull (original);
    ...
}

# GOOD
public StringProcessor() {} // μμ±μ

@Cached("local")
public String removeEndingDot(String original) {
    assertNotNull(original);
    ...
}
```

* , ; λ€μ κ³΅λ°± μ½μ

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

## πΒ Spring Convention

1. lombok μμ± μμΉ
2. setter β builder
3. toString μ¬μ μ