package org.baljaguk.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.chat.entity.ChatMessage;
import org.baljaguk.domain.chat.entity.ChatRoom;
import org.baljaguk.domain.chat.entity.LastReadTime;
import org.baljaguk.domain.chat.entity.dto.ChatRoomListResponse;
import org.baljaguk.domain.chat.entity.dto.ChatRoomResponse;
import org.baljaguk.domain.chat.entity.dto.LastChatInfoDto;
import org.baljaguk.domain.chat.repository.ChatMessageRepository;
import org.baljaguk.domain.chat.repository.ChatRoomRepository;
import org.baljaguk.domain.chat.repository.LastReadTimeRepository;
import org.baljaguk.domain.team.dto.ContestInfoDto;
import org.baljaguk.domain.team.repository.TeamMemberRepository;
import org.baljaguk.domain.team.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final LastReadTimeRepository lastReadTimeRepository;
    private final ChatMessageRepository chatMessageRepository;


    public ChatRoomListResponse getChatRooms(Long userId) {

        // 1. 사용자가 속한 모든 teamId 조회
        List<Long> teamIds = teamMemberRepository.findTeamIdsByMemberId(userId);
        if (teamIds.isEmpty()) {
            return new ChatRoomListResponse(Collections.emptyList());
        }

        // 2. teamId 목록으로 모든 ChatRoom 엔티티 조회 ->roomIds로 변환
        List<ChatRoom> chatRooms = chatRoomRepository.findByTeamIdIn(teamIds);
        List<Long> roomIds = chatRooms.stream().map(ChatRoom::getId).collect(Collectors.toList());
        if (roomIds.isEmpty()) {
            return new ChatRoomListResponse(Collections.emptyList());
        }

        // 3. teamId 목록으로 ContestInfoDto 조회 -> Map<teamId, ContestInfoDto>
        Map<Long, ContestInfoDto> contestInfoMap = teamRepository.findContestInfoByTeamIds(teamIds)
                .stream()
                .collect(Collectors.toMap(ContestInfoDto::getTeamId, Function.identity()));

        // 4. 사용자의 마지막 읽은 시간 정보 조회 -> Map<roomId, LocalDateTime>
        Map<Long, LocalDateTime> lastReadTimeMap = lastReadTimeRepository.findByUserId(userId)
                .stream()
                .collect(Collectors.toMap(LastReadTime::getRoomId, LastReadTime::getLastReadAt));

        // 5. 안 읽은 메시지 수 조회 -> Map<roomId, unreadCount>
        Map<Long, Long> unreadCountMap = chatMessageRepository.getUnreadCounts(lastReadTimeMap);

        // 6. 최신 메시지 정보 조회 -> Map<roomId, ChatMessage>
        Map<Long, ChatMessage> latestMessageMap = chatMessageRepository.findLatestMessagesByRoomIds(roomIds)
                .stream()
                .collect(Collectors.toMap(msg -> msg.getChatRoom().getId(), Function.identity()));

        // 7. 데이터 조립
        List<ChatRoomResponse> chatRoomResponses = chatRooms.stream().map(room -> {

            ContestInfoDto contestInfo = contestInfoMap.get(room.getTeam().getId());
            long unreadCount = unreadCountMap.getOrDefault(room.getId(), 0L);
            ChatMessage latestMessage = latestMessageMap.get(room.getId());

            LastChatInfoDto lastChatInfo = LastChatInfoDto.builder()
                    .lastMessage(latestMessage != null ? latestMessage.getMessage() : "아직 메시지가 없습니다.")
                    .lastMessageAt(latestMessage != null ? latestMessage.getCreatedAt() : null)
                    .unReadMessageCount(unreadCount)
                    .build();

            return new ChatRoomResponse(room.getId(), contestInfo, lastChatInfo);
        }).collect(Collectors.toList());

        return new ChatRoomListResponse(chatRoomResponses);
    }
}
