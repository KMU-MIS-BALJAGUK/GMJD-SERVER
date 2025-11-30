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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final TeamMemberRepository teamMemberRepository;

    private final ChatRoomRepository chatRoomRepository;


    @Transactional(readOnly = true)
    public ChatMessageResponse getChatHistory(Long roomId, Long cursorMessageId, LocalDateTime cursorMessageAt, Integer size) {

       //chatRoom 조회
       ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElse(null);

       // chat history 조회 (size+1)로 조회함으로써 hasNext 판단
       List<ChatMessage> chatMessages = chatMessageRepository.findMessagesByCursor(roomId, cursorMessageId, cursorMessageAt, size+1);

       boolean hasNext = false;

       // size보다 큰 messages가 들어오면 hasNext = true
       if(chatMessages.size()>size) {
           hasNext=true;
           chatMessages.remove(chatMessages.size()-1);
       }

       // 다음 커서 정보 추출
       ChatMessage lastMessage = chatMessages.get(chatMessages.size()-1);
       Long nextCursorMessageId = lastMessage.getId();
       LocalDateTime nextCursorMessageAt= lastMessage.getCreatedAt();

       //ChatMessageResponse 클래스 chatMessages 필드
       List<ChatMessageDto> chatMessageDtoList = chatMessages.stream()
               .map(ChatMessageDto::from)
               .toList();

       //ChatMessageResponse 클래스 teamMembers 필드
       List<TeamMemberInfoDto> teamMemberInfoDto = teamMemberRepository.findAllWithUserByTeamId(Objects.requireNonNull(chatRoom).getTeam().getId())
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
