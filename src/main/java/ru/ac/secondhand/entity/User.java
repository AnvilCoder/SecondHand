package ru.ac.secondhand.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.ac.secondhand.entity.enums.Role;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

/**
 * Сущность, представляющая пользователя системы.
 * <p>
 * Этот класс аннотирован как сущность JPA с помощью аннотации {@code @Entity},
 * что позволяет сохранять объекты User в базе данных. Используется таблица "users"
 * для хранения данных.
 * </p>
 * <p>
 * Поля данного класса представляют информацию о пользователе, такую как имя, фамилия,
 * логин, пароль, телефон и роль пользователя. Аннотации {@code @Getter}, {@code @Setter},
 * {@code @ToString}, {@code @EqualsAndHashCode} и {@code @NoArgsConstructor} / {@code @AllArgsConstructor}
 * используются для автоматической генерации методов доступа к полям, методов {@code toString},
 * {@code equals} и {@code hashCode}, а также конструкторов.
 * </p>
 * <p>
 * Поле {@code id} представляет уникальный идентификатор пользователя и аннотировано
 * как первичный ключ с автоматической генерацией значений.
 * </p>
 * <p>
 * Поле {@code image} представляет связь с изображением пользователя (One-to-One), и
 * аннотировано как ленивая загрузка (Lazy Loading), что означает, что изображение будет
 * загружено только при необходимости.
 * </p>
 * <p>
 * Поля {@code username}, {@code password}, {@code firstName}, {@code lastName}, {@code phone}
 * представляют информацию о пользователе, включая уникальный логин и пароль.
 * </p>
 * <p>
 * Поле {@code role} представляет роль пользователя и использует перечисление (Enum) {@code Role}.
 * </p>
 * <p>
 * Аннотации {@code @JsonIgnore} применены к полям {@code ads} и {@code comments} для исключения
 * их из сериализации при преобразовании в JSON, что может быть полезно в определенных случаях.
 * </p>
 * <p>
 * Поля {@code ads} и {@code comments} представляют связи с объявлениями и комментариями,
 * соответственно, и аннотированы как множественные связи (One-to-Many) с каскадным удалением
 * (CascadeType.REMOVE), что позволяет удалять связанные объявления и комментарии при удалении пользователя.
 * </p>
 */
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Ad> ads;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments;
}
