package com.example.pet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pet.dao.dto.DialogueDto;
import com.example.pet.dao.dto.UnreadUserDto;
import com.example.pet.dao.entity.Article;
import com.example.pet.dao.entity.Dialogue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DialogueMapper extends BaseMapper<Dialogue> {
    // 更高效的SQL实现方式
    @Select("SELECT " +
            "  d.dialogueUserId as userId, " +
            "  u.userName as username, " +
            "  u.userPic as avatar, " +
            "  MAX(d.dialogueContent) as lastContent, " +
            "  d.dialoguePid as receiverId " +
            "FROM dialogue d " +
            "LEFT JOIN user u ON d.dialogueUserId = u.userId " +
            "WHERE d.dialoguePid = #{receiverId} AND d.dialogueRead = '0' " +
            "GROUP BY d.dialogueUserId, u.userName, u.userPic, d.dialoguePid " +
            "ORDER BY MAX(d.dialogueTime) DESC")
    List<UnreadUserDto> selectUsersWithUnreadMessages(@Param("receiverId") String receiverId);



    @Select("SELECT " +
            "   t.userId, " +
            "   u.userName, " +
            "   u.userPic, " +
            "   u.userAddress, " +
            "   u.userGender, " +
            "   u.userDate, " +
            "   #{currentUserId} AS userIdNow, " +
            "   u.userTagt AS userTagt " +
            "FROM ( " +
            "   SELECT " +
            "       CASE " +
            "           WHEN d.dialogueUserId = #{currentUserId} THEN d.dialoguePid " +
            "           ELSE d.dialogueUserId " +
            "       END AS userId, " +
            "       MAX(d.dialogueTime) AS latestTime " +
            "   FROM dialogue d " +
            "   WHERE d.dialogueUserId = #{currentUserId} OR d.dialoguePid = #{currentUserId} " +
            "   GROUP BY userId " +
            ") t " +
            "LEFT JOIN user u ON u.userId = t.userId " +
            "ORDER BY t.latestTime DESC")
    List<DialogueDto> selectChatPartners(@Param("currentUserId") String currentUserId);



    @Update("UPDATE dialogue SET dialogueRead = '1' " +
            "WHERE dialoguePid = #{receiverId} AND dialogueUserId = #{senderId} AND dialogueRead = '0'")
    int markMessagesAsRead(@Param("receiverId") String receiverId,
                           @Param("senderId") String senderId);

    @Select("SELECT * FROM dialogue " +
            "WHERE (dialogueUserId = #{userId1} AND dialoguePid = #{userId2}) " +
            "   OR (dialogueUserId = #{userId2} AND dialoguePid = #{userId1}) " +
            "ORDER BY dialogueTime ASC")
    List<Dialogue> getDialoguesBetween(@Param("userId1") String userId1, @Param("userId2") String userId2);
}
