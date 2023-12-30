package ru.ac.secondhand.service;

import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.AdDTO;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.entity.Ad;
import ru.ac.secondhand.exception.AdNotFoundException;
import ru.ac.secondhand.mapper.AdMapper;
import ru.ac.secondhand.repository.AdRepository;

/**
 * Сервис для управления объявлениями.
 * <p>
 * Этот сервис предоставляет методы для создания, обновления, удаления и получения объявлений.
 * Включает функционал для работы с изображениями объявлений и обработки данных объявлений.
 * Сервис поддерживает транзакционность на уровне класса, что обеспечивает целостность данных
 * при выполнении операций с объявлениями.
 * </p>
 * <p>
 * В классе используется логирование для отслеживания ключевых событий, таких как
 * создание, обновление и удаление объявлений.
 * </p>
 *
 * @author fifimova
 * @see AdRepository
 * @see UserService
 * @see ImageService
 */
public interface AdService {

    /**
     * Получает список всех объявлений из репозитория.
     * <p>
     * Метод выполняется в контексте транзакции только для чтения, что оптимизирует производительность.
     * </p>
     * <p>
     * Сначала из репозитория извлекается список всех объявлений. После этого он преобразуется
     * в формат {@code Ads} с помощью маппера {@code AdMapper}.
     * </p>
     *
     * @return Объект Ads, содержащий список всех объявлений.
     * @see AdMapper
     */
    Ads getAll();

    /**
     * Получает информацию об объявлении по его идентификатору.
     * <p>
     * Этот метод выполняется в транзакционном контексте только для чтения, оптимизируя
     * производительность для операций, не требующих изменения данных.
     * </p>
     * <p>
     * Сначала производится поиск объявления в репозитории по предоставленному идентификатору.
     * Если объявление не найдено, генерируется исключение {@code AdNotFoundException},
     * а в журнал записывается предупреждение. В случае успешного нахождения объявления,
     * оно преобразуется в расширенный DTO формат с помощью маппера.
     * </p>
     *
     * @param id идентификатор объявления для поиска.
     * @return объект {@code ExtendedAd}, содержащий расширенную информацию об объявлении.
     * @throws AdNotFoundException если объявление с указанным идентификатором не найдено.
     * @see AdMapper#toExtendedAd(Ad)
     */
    ExtendedAd getAdInfo(Integer id);

    /**
     * Получает список объявлений, принадлежащих текущему пользователю.
     * <p>
     * Метод выполняется в контексте транзакции только для чтения, что повышает эффективность
     * операции за счёт сокращения нагрузки на базу данных.
     * </p>
     * <p>
     * Сначала определяется текущий пользователь с помощью {@code userService.findUser()}.
     * Затем, используя идентификатор пользователя, из репозитория извлекается список его объявлений.
     * Наконец, этот список преобразуется в формат {@code Ads} с помощью маппера.
     * </p>
     *
     * @return Объект Ads, содержащий список объявлений текущего пользователя.
     * @see UserService#findUser()
     * @see AdRepository#findAdsByUserId(Integer)
     * @see AdMapper
     */
    Ads getUsersAds();

    /**
     * Создаёт новое объявление на основе предоставленных данных.
     * <p>
     * В начале определяется текущий пользователь через {@code userService.findUser()}.
     * Затем данные объявления из DTO преобразуются в сущность {@code Ad} с помощью маппера.
     * Пользователь устанавливается для объявления, и оно сохраняется в репозитории.
     * На заключительном этапе создается и возвращается DTO нового объявления.
     * </p>
     *
     * @param adDTO DTO для создания объявления.
     * @return DTO созданного объявления.
     * @see UserService#findUser()
     * @see AdMapper#toAdEntity(CreateOrUpdateAd)
     * @see AdMapper#toAdDTO(Ad)
     */
    AdDTO createAd(CreateOrUpdateAd ad, MultipartFile image);

    /**
     * Обновляет существующее объявление с заданным идентификатором.
     * <p>
     * Сначала производится поиск объявления по идентификатору. Если объявление не найдено,
     * выбрасывается исключение {@code AdNotFoundException}. В случае нахождения, данные объявления
     * обновляются на основе предоставленного DTO и затем сохраняются в репозитории.
     * </p>
     *
     * @param id    Идентификатор обновляемого объявления.
     * @param adDTO DTO с новыми данными для объявления.
     * @return DTO обновлённого объявления.
     * @see AdMapper#toAdDTO(Ad)
     */
    AdDTO updateAd(Integer id, CreateOrUpdateAd ad);

    /**
     * Обновляет изображение объявления по заданному идентификатору.
     * <p>
     * Метод начинает с поиска объявления по его идентификатору. Если объявление не найдено,
     * выбрасывается исключение {@code AdNotFoundException}. В случае нахождения, проверяется наличие
     * существующего изображения и, если оно есть, оно удаляется. После этого загружается новое
     * изображение, обновляются данные объявления, и объявление сохраняется в репозитории.
     * </p>
     * <p>
     * Возвращает URL нового изображения.
     * </p>
     *
     * @param id    Идентификатор объявления, изображение которого нужно обновить.
     * @param image Файл нового изображения.
     * @see ImageService
     */
    void updateAdImage(Integer id, MultipartFile image);

    /**
     * Удаляет объявление по заданному идентификатору.
     * <p>
     * Поиск объявления осуществляется по его идентификатору. Если объявление не найдено,
     * генерируется исключение {@code AdNotFoundException}. В случае успешного нахождения,
     * объявление удаляется из репозитория.
     * </p>
     *
     * @param id Идентификатор удаляемого объявления.
     */
    void deleteAd(Integer id);

    /**
     * Получает объявление по его идентификатору.
     *
     * @param adId Идентификатор объявления, которое нужно получить.
     * @return Найденное объявление.
     * @throws AdNotFoundException если объявление с указанным идентификатором не найдено.
     */
    Ad getAdById(Integer adId);

    boolean isOwner(String username, Integer id);
}
