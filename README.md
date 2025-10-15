# 🧩 QueryDSL 정리

## 1️⃣ QueryDSL을 왜 사용할까?
- 동적 쿼리 작성 용이  
- 복잡한 쿼리 작성 용이  
- 컴파일러 단계에서 쿼리 오류를 잡아줌 (타입 안정성)  
- Transformer 기능을 통해 복잡한 결과 매핑을 간결하게 처리  

---

## 2️⃣ QueryDSL을 사용하기 위한 세팅

### 📦 Gradle 의존성 추가
```groovy
// QueryDSL 기본 설정
implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
annotationProcessor("com.querydsl:querydsl-apt:5.1.0:jakarta")
annotationProcessor("jakarta.annotation:jakarta.annotation-api")
annotationProcessor("jakarta.persistence:jakarta.persistence-api")

// 추가 기능 (SQL, Blaze-Persistence)
implementation("com.querydsl:querydsl-sql:5.1.0")
implementation("com.blazebit:blaze-persistence-integration-querydsl-expressions-jakarta:1.6.14")
```

### ⚙️ JPAQueryFactory Bean 등록
`JPAQueryFactory`는 QueryDSL 쿼리를 생성하고 실행하는 핵심 객체로,  
모든 Repository에서 주입받아 사용할 수 있도록 스프링 빈으로 등록한다.

```java
package com.seungmin.homework.global.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.blazebit.persistence.querydsl.JPQLNextTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Configuration
@RequiredArgsConstructor
public class QueryDSLConfig {

    private final EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(JPQLNextTemplates.DEFAULT, entityManager);
    }
}
```

> 💡 JPAQueryFactory는 내부적으로 EntityManager를 사용하여  
> 타입 안정(type-safe)한 쿼리를 컴파일 타임에 검증하며 실행한다.

---

## 3️⃣ QueryDSL 사용하는 방법

### 🧾 기본 쿼리 작성 예시
```java
private final JPAQueryFactory jpaQueryFactory;
private final QLocation location = QLocation.location;
private final QScript script = QScript.script;
private final QUser user = QUser.user;
private final QFileLocation fileLocation = QFileLocation.fileLocation;
private final QFile file = QFile.file;

public GetLocationRes getLocationById(long id) {
    return findLocationById(id)
            .orElseThrow(() -> new NotFoundLocationException(id));
}

private Optional<GetLocationRes> findLocationById(long id) {
    return Optional.ofNullable(
            jpaQueryFactory
                    .select(new QGetLocationRes(
                            location.name,
                            location.latitude,
                            location.longitude,
                            location.territory.stringValue(),
                            location.time,
                            location.era.stringValue(),
                            location.url,
                            location.address,
                            file.url
                    ))
                    .from(location)
                    .leftJoin(fileLocation).on(fileLocation.id.locationId.eq(location))
                    .leftJoin(fileLocation.id.fileId, file)
                    .where(location.tid.eq(id))
                    .limit(1)
                    .fetchOne()
    );
}
```

### 🧩 fetch 메서드 종류
| 메서드 | 설명 |
|--------|------|
| `fetch()` | 여러 건 리스트 반환 |
| `fetchOne()` | 단일 결과 반환 (없으면 null, 여러 건이면 예외 발생) |
| `fetchFirst()` | 첫 번째 결과 한 건만 반환 |
| `fetchCount()` | count 쿼리 실행 |

> 💡 JPAQueryFactory는 클래스이며, EntityManager 기반으로 동작하기 때문에 인터페이스가 아니다.

---

## 4️⃣ DTO 매핑 – @QueryProjection 사용

QueryDSL에서 DTO를 바로 select하려면 **Q클래스 기반 생성자**를 만들어야 하며,  
`@QueryProjection` 어노테이션을 추가해야 QueryDSL이 QDTO 클래스를 생성한다.

```java
public record BookmarkRes(
        long id,
        String name,
        String file
) {
    @QueryProjection
    public BookmarkRes {}
}
```

이후 쿼리 작성 시:
```java
.select(new QBookmarkRes(member.id, member.name, file.url))
```

---

## 5️⃣ Transformer 사용 (groupBy, list)

`transform()`은 QueryDSL의 고급 기능으로,  
GroupBy 및 여러 테이블의 조인 결과를 한 번에 DTO로 매핑할 수 있다.

```java
@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public UserRes getDetailsById(long userId){
        return findDetailsById(userId).orElseThrow(NotFoundUserException::new);
    }

    private Optional<UserRes> findDetailsById(long userId){
        QUser user = QUser.user;
        QUserLanguage language = QUserLanguage.userLanguage;

        return jpaQueryFactory
                .from(user)
                .leftJoin(language).on(language.userId.id.eq(userId))
                .where(user.id.eq(userId))
                .orderBy(language.priority.asc())
                .transform(groupBy(user.id).list(
                        new QUserRes(user.name,
                                user.file.url,
                                list(language.language.stringValue()),
                                user.bookmark.size(),
                                user.like.size())
                ))
                .stream().findFirst();
    }
}
```

> 💡 transform은 SQL의 GROUP BY 결과를 자바 객체로 깔끔하게 묶을 때 사용한다.  
> 여러 조인 결과를 하나의 DTO로 매핑하기 좋다.