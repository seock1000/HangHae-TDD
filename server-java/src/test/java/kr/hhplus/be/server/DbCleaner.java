package kr.hhplus.be.server;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Profile("test")
public class DbCleaner {
    @PersistenceContext
    private EntityManager entityManager;

    private final List<String> tables = new ArrayList<>();

    @PostConstruct
    public void init() {
        tables.addAll(
                entityManager.getMetamodel().getEntities().stream()
                        .filter(entity -> entity.getJavaType().isAnnotationPresent(Entity.class))
                        .map(entity -> {
                            // @Table 어노테이션이 있는지 확인하고, 없으면 테이블 이름을 추출하지 않음
                            Table tableAnnotation = entity.getJavaType().getAnnotation(Table.class);
                            if (tableAnnotation != null) {
                                return tableAnnotation.name();
                            } else {
                                // @Table 어노테이션이 없는 경우, 엔티티 이름을 그대로 사용하거나 빈 문자열 반환
                                return camelToSnakeCase(entity.getName());
                            }
                        })
                        .toList()
        );
    }

    // camelCase -> snake_case 변환 메서드
    private String camelToSnakeCase(String str) {
        Pattern pattern = Pattern.compile("([a-z0-9])([A-Z])");
        Matcher matcher = pattern.matcher(str);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, matcher.group(1) + "_" + matcher.group(2).toLowerCase());
        }
        matcher.appendTail(buffer);
        return buffer.toString().toLowerCase();
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        for (String table : tables) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + table).executeUpdate();
            try {
                entityManager.createNativeQuery("ALTER TABLE " + table + " AUTO_INCREMENT = 1").executeUpdate();
            } catch (Exception ignored) {
                // log.warn("Could not reset AUTO_INCREMENT for table: " + table, ignored);
            }
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}

