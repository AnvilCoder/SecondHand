package ru.ac.secondhand.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.ac.secondhand.dto.ad.AdDTO;
import ru.ac.secondhand.dto.ad.Ads;
import ru.ac.secondhand.dto.ad.CreateOrUpdateAd;
import ru.ac.secondhand.dto.ad.ExtendedAd;
import ru.ac.secondhand.entity.Ad;
import ru.ac.secondhand.entity.Image;
import ru.ac.secondhand.entity.User;
import ru.ac.secondhand.exception.AdNotFoundException;
import ru.ac.secondhand.mapper.AdMapper;
import ru.ac.secondhand.repository.AdRepository;
import ru.ac.secondhand.service.AdService;
import ru.ac.secondhand.service.ImageService;
import ru.ac.secondhand.service.UserService;
import ru.ac.secondhand.utils.MethodLog;

import java.util.List;

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
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserService userService;
    private final AdMapper mapper;
    private final ImageService imageService;

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
    @Override
    @Transactional(readOnly = true)
    public Ads getAll() {
        log.info("Method {}", MethodLog.getMethodName());
        List<Ad> ads = adRepository.findAll();
        return mapper.toAds(ads);
    }

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
    @Override
    @Transactional(readOnly = true)
    public ExtendedAd getAdInfo(Integer id) {
        log.info("Method {}", MethodLog.getMethodName());
        Ad ad = getAdById(id);
        return mapper.toExtendedAd(ad);
    }

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
    @Override
    @Transactional(readOnly = true)
    public Ads getUsersAds() {
        log.info("Method {}", MethodLog.getMethodName());
        User user = userService.findUser();
        List<Ad> ads = adRepository.findAdsByUserId(user.getId());
        return mapper.toAds(ads);
    }

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
    @Override
    public AdDTO createAd(CreateOrUpdateAd adDTO, MultipartFile image) {
        log.info("Method {}", MethodLog.getMethodName());
        User user = userService.findUser();
        log.debug("User found");

        Ad ad = mapper.toAdEntity(adDTO);
        ad.setUser(user);

        if (image != null && !image.isEmpty()) {
            Image newImage = imageService.saveImage(image);
            ad.setImage(newImage);
            log.info("Image {} saved", newImage.getId());
        }

        adRepository.save(ad);
        log.info("Ad {} {} saved", ad.getId(), ad.getTitle());
        return mapper.toAdDTO(ad);
    }


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
    @Override
    public AdDTO updateAd(Integer id, CreateOrUpdateAd adDTO) {
        log.info("Method {}", MethodLog.getMethodName());
        Ad ad = getAdById(id);
        ad.setTitle(adDTO.getTitle());
        ad.setPrice(adDTO.getPrice());
        ad.setDescription(adDTO.getDescription());
        adRepository.save(ad);
        log.info("Ad {} {} saved", ad.getId(), ad.getTitle());
        return mapper.toAdDTO(ad);
    }

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
    @Override
    public void updateAdImage(Integer id, MultipartFile image) {
        log.info("Method {}", MethodLog.getMethodName());
        Ad ad = getAdById(id);
        if (ad.getImage() != null) {
            imageService.deleteImage(ad.getImage().getId());
        }
        Image newImage = imageService.saveImage(image);
        ad.setImage(newImage);
        adRepository.save(ad);
        log.info("Image {} saved", newImage.getId());
    }

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
    @Override
    public void deleteAd(Integer id) {
        Ad ad = getAdById(id);
        log.info("Ad {} {} deleted", ad.getId(), ad.getTitle());
        adRepository.delete(ad);
    }

    /**
     * Получает объявление по его идентификатору.
     *
     * @param adId Идентификатор объявления, которое нужно получить.
     * @return Найденное объявление.
     * @throws AdNotFoundException если объявление с указанным идентификатором не найдено.
     */
    @Override
    @Transactional(readOnly = true)
    public Ad getAdById(Integer adId) {
        log.info("Fetching Ad with id: {}", adId);
        return adRepository.findById(adId).orElseThrow(() -> {
            log.warn("Ad not found for id: {}", adId);
            return new AdNotFoundException("Ad not found for id: " + adId);
        });
    }

    /**
     * Проверка, что юзер является владельцем объявления,
     * для предоставления ему право на удаление/обновление
     *
     * @param username - имя пользователя
     * @param id       - id объявления
     */
    public boolean isOwner(String username, Integer id) {
        log.info("Method {}, user {}, ad {}", MethodLog.getMethodName(), username, id);

        Ad ad = getAdById(id);
        if (!ad.getUser().getUsername().equals(username)) {
            log.warn("Trying to access foreign ad {} by user {}", id, username);
            return false;
        }
        return true;
    }
}