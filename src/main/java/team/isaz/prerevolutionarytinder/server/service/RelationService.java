package team.isaz.prerevolutionarytinder.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.domain.entity.Relation;
import team.isaz.prerevolutionarytinder.server.domain.repository.RelationRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис взаимодействия с RelationRepository
 */
public class RelationService {
    Logger logger = LoggerFactory.getLogger(RelationService.class);

    RelationRepository relationRepository;

    public RelationService(RelationRepository relationRepository) {
        this.relationRepository = relationRepository;
    }

    /**
     * <b>Операция Like</b>
     *
     * @param who  кто ставит лайк
     * @param whom кому ставят лайк
     * @return {@link Response Response} со статусом false, в случае дублирования установки отношения и true в других.
     * attach параметр содержит строку-пояснение для конечного пользователя.
     */
    public Response like(UUID who, UUID whom) {
        logger.debug("Начало операции like пользователем {}", who);

        if (tryCreateRelation(who, whom, true)) {
            if (relationRepository.existsLikeByWhoAndWhomAndLiked(whom, who, true)) {
                return new Response(true, "Вы любимы");
            }
            return new Response(true, "Любовь проявлена");
        }
        return new Response(false, "Дубликатъ");
    }

    /**
     * <b>Попытаться создать и сохранить отношение</b>
     *
     * @param who    от кого
     * @param whom   кому
     * @param isLike это лайк? True, если Like, false, если Dislike
     * @return true, в случае успешного создания и сохранения отношения, false если отношение уже существует.
     */
    private boolean tryCreateRelation(UUID who, UUID whom, boolean isLike) {
        Relation relation = createRelation(who, whom, isLike);
        if (relationRepository.existsLikeByWhoAndWhom(who, whom)) {
            return false;
        }
        relationRepository.save(relation);
        return true;
    }

    /**
     * <b>Создание отношения</b><br>
     * Своеобразная замена all-parameter конструктору.
     *
     * @param who    от кого
     * @param whom   кому
     * @param isLike это лайк? True, если Like, false, если Dislike
     * @return объект класса Relation, содержащий вышеназванные значения.
     */
    private Relation createRelation(UUID who, UUID whom, boolean isLike) {
        var relation = new Relation();
        relation.setWho(who);
        relation.setWhom(whom);
        relation.setLiked(isLike);
        return relation;
    }

    /**
     * <b>Операция Dislike</b><br>
     *
     * @param who  кто ставит дизлайк
     * @param whom кому ставят дизлайк
     * @return {@link Response Response} со статусом false, в случае дублирования установки отношения и true в других.
     * attach параметр содержит строку-пояснение для конечного пользователя.
     */
    public Response dislike(UUID who, UUID whom) {
        logger.debug("Начало операции dislike пользователем {}", who);
        if (tryCreateRelation(who, whom, false)) return new Response(true, "Видно не судьба, видно нѣтъ любви.");
        return new Response(false, "Дубликатъ");
    }

    /**
     * <b>Проверка существования отношения между двумя UUID</b>
     *
     * @param firstUser  UUID пользователя
     * @param secondUser UUID другого пользователя
     * @return true, если нет отношения между firstYser и secondUser и secondUser не ставил дизлайк firstUser
     */
    public boolean isNotExistRelation(UUID firstUser, UUID secondUser) {
        return !(relationRepository.existsLikeByWhoAndWhom(firstUser, secondUser) || relationRepository.existsLikeByWhoAndWhomAndLiked(secondUser, firstUser, false));
    }


    /**
     * Генерация HTML-табличного представления всего RelationRepository
     *
     * @return Response с attach представляющим собой таблицу
     */
    public Response getTable() {
        var likes = relationRepository.findAll().iterator();

        var builder = new StringBuilder("<table>");
        builder.append("<tr>")
                .append("<th>ID</th>")
                .append("<th>WHO</th>")
                .append("<th>WHOM</th>")
                .append("<th>LIKED?</th>")
                .append("</tr>");

        while (likes.hasNext()) {
            var like = likes.next();
            builder.append("<tr>");
            builder.append("<th>").append(like.getId()).append("</th>");
            builder.append("<th>").append(like.getWho()).append("</th>");
            builder.append("<th>").append(like.getWhom()).append("</th>");
            builder.append("<th>").append(like.isLiked()).append("</th>");
            builder.append("</tr>");
        }

        builder.append("</table>");
        return new Response(true, builder.toString());
    }

    public List<UUID> getAllMatchesById(UUID user) {
        return relationRepository.findAllByWhoAndLiked(user, true)
                .stream()
                .filter(relation ->
                        relationRepository.existsLikeByWhoAndWhomAndLiked(relation.getWhom(), user, true))
                .map(Relation::getWhom)
                .collect(Collectors.toList());
    }
}
