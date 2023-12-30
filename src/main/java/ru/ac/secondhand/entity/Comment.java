package ru.ac.secondhand.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Сущность, представляющая комментарий к объявлению.
 * <p>
 * Этот класс аннотирован как сущность JPA с помощью аннотации {@code @Entity},
 * что позволяет сохранять объекты Comment в базе данных. Используется таблица "comments"
 * для хранения данных.
 * </p>
 * <p>
 * Аннотация {@code @Data} от Lombok автоматически генерирует методы для доступа к полям,
 * такие как геттеры и сеттеры, а также методы {@code equals}, {@code hashCode} и {@code toString}.
 * </p>
 * <p>
 * Конструктор без аргументов создается с помощью аннотации {@code @NoArgsConstructor}.
 * </p>
 * <p>
 * Поле {@code id} представляет уникальный идентификатор комментария и аннотировано
 * как первичный ключ с автоматической генерацией значений.
 * </p>
 * <p>
 * Поле {@code text} содержит текст комментария.
 * </p>
 * <p>
 * Поле {@code createdAt} автоматически заполняется временной меткой создания комментария
 * при сохранении в базе данных с помощью аннотации {@code @CreationTimestamp}.
 * </p>
 * <p>
 * Поля {@code user} и {@code ad} представляют связи между комментарием, пользователем и объявлением.
 * Они аннотированы как множественные связи (Many-to-One), где каждый комментарий принадлежит
 * определенному пользователю и объявлению.
 * </p>
 * <p>
 * Аннотация {@code @ToString.Exclude} исключает поле {@code user} и {@code ad} из автоматически
 * сгенерированного метода {@code toString}, чтобы избежать потенциальных циклических ссылок.
 * </p>
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String text;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User user;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id", referencedColumnName = "id")
    private Ad ad;
}
