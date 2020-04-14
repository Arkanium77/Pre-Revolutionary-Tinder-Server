package team.isaz.prerevolutionarytinder.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.domain.entity.Like;
import team.isaz.prerevolutionarytinder.server.domain.repository.LikeRepository;
import team.isaz.prerevolutionarytinder.server.domain.repository.RoleRepository;
import team.isaz.prerevolutionarytinder.server.domain.repository.UserRepository;

import java.util.UUID;

@Service
public class LikeService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    UserRepository userRepository;
    RoleRepository roleRepository;
    LikeRepository likeRepository;

    public LikeService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.likeRepository = likeRepository;
    }

    public Response like(UUID who, UUID whom) {
        var like = new Like();
        like.setWho(who);
        like.setWhom(whom);
        likeRepository.save(like);
        if (likeRepository.existsLikeByWhoAndWhom(whom, who)) {
            return new Response(true, "Вы любимы");
        }
        return new Response(true, "Любовь проявлена");
    }

    public Response getTable() {
        var likes = likeRepository.findAll().iterator();

        var builder = new StringBuilder("<table>");
        builder.append("<tr>")
                .append("<th>ID</th>")
                .append("<th>WHO</th>")
                .append("<th>WHOM</th>")
                .append("</tr>");

        while (likes.hasNext()) {
            var like = likes.next();
            builder.append("<tr>");
            builder.append("<th>").append(like.getId()).append("</th>");
            builder.append("<th>").append(like.getWho()).append("</th>");
            builder.append("<th>").append(like.getWhom()).append("</th>");
            builder.append("</tr>");
        }

        builder.append("</table>");
        return new Response(true, builder.toString());
    }
}