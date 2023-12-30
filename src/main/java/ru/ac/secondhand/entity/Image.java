package ru.ac.secondhand.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Сущность, представляющая изображение.
 * <p>
 * Этот класс аннотирован как сущность JPA с помощью аннотации {@code @Entity},
 * что позволяет сохранять объекты Image в базе данных. Используется таблица "image"
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
 * Поле {@code id} представляет уникальный идентификатор изображения и аннотировано
 * как первичный ключ с автоматической генерацией значений.
 * </p>
 * <p>
 * Поле {@code imagePath} содержит путь к изображению.
 * </p>
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "image_path")
    private String imagePath;
}
