package org.baljaguk.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baljaguk.domain.chat.entity.ChatMessage;
import org.baljaguk.domain.chat.entity.ChatRoom;
import org.baljaguk.domain.chat.entity.dto.ChatMessageDto;
import org.baljaguk.domain.chat.entity.dto.ChatMessageResponse;
import org.baljaguk.domain.chat.entity.dto.TeamMemberInfoDto;
import org.baljaguk.domain.chat.repository.ChatMessageRepository;
import org.baljaguk.domain.chat.repository.ChatRoomRepository;
import org.baljaguk.domain.team.repository.TeamMemberRepository;
import org.baljaguk.domain.user.dto.CustomUserDetails;
import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ChatRoomRepository chatRoomRepository;


    @Transactional(readOnly = true)
    public ChatMessageResponse getChatHistory(CustomUserDetails customUserDetails, Long roomId, Long cursorMessageId, LocalDateTime cursorMessageAt, Integer size) {

        // 1. roomId로 ChatRoom 조회, 없으면 예외 발생
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new GeneralException(ErrorCode.NOT_FOUND_TEAM));

        // 2. 사용자가 속한 Team 목록 조회
        List<Long> userTeams = teamMemberRepository.findTeamIdsByMemberId(customUserDetails.getUserId());

        // 3. 사용자가 해당 팀(채팅방)에 속해 있는지 확인
        boolean isMember = userTeams.stream()
                .anyMatch(teamMember -> teamMember.equals(chatRoom.getTeam().getId()));

        if (!isMember) {
            throw new GeneralException(ErrorCode.NOT_MY_TEAM);
        }

        // chat history 조회 (size+1)로 조회함으로써 hasNext 판단
        List<ChatMessage> chatMessages = chatMessageRepository.findMessagesByCursor(roomId, cursorMessageId, cursorMessageAt, size + 1);

        boolean hasNext;

        // size보다 큰 messages가 들어오면 hasNext = true
        if (chatMessages.size() > size) {
            hasNext = true;
            //마지막 삭제
            chatMessages.remove(chatMessages.size() - 1);
        }else hasNext = false;

        Long nextCursorMessageId;
        LocalDateTime nextCursorMessageAt;

        // 다음 커서 정보 추출 (메시지가 있을 경우에만)
        if (!chatMessages.isEmpty()) {
            ChatMessage lastMessage = chatMessages.get(chatMessages.size() - 1);
            nextCursorMessageId = lastMessage.getId();
            nextCursorMessageAt = lastMessage.getCreatedAt();
        }else {
            nextCursorMessageId = null;
            nextCursorMessageAt = null;
        }


        //ChatMessageResponse 클래스 chatMessages 필드
        List<ChatMessageDto> chatMessageDtoList = chatMessages.stream()
                .map(ChatMessageDto::from)
                .toList();

        //ChatMessageResponse 클래스 teamMembers 필드
        List<TeamMemberInfoDto> teamMemberInfoDto = teamMemberRepository.findAllWithUserByTeamId(chatRoom.getTeam().getId())
                .stream()
                .map(TeamMemberInfoDto::from)
                .toList();

        return ChatMessageResponse.builder()
                .roomId(roomId)
                .messages(chatMessageDtoList)
                .hasNext(hasNext)
                .lastMessageAt(nextCursorMessageAt)
                .lastMessageId(nextCursorMessageId)
                .teamMembers(teamMemberInfoDto)
                .build();
    }
}
