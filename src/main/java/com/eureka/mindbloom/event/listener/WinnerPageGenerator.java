package com.eureka.mindbloom.event.listener;

import com.eureka.mindbloom.common.exception.BaseException;
import com.eureka.mindbloom.event.domain.EventParticipant;
import com.eureka.mindbloom.event.dto.BatchCompletedEvent;
import com.eureka.mindbloom.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class WinnerPageGenerator {

    @EventListener
    public void handleBatchCompletedEvent(BatchCompletedEvent event) {
        List<EventParticipant> winners = event.winners();
        int eventId = event.eventId();
        int count = 0;

        File file = new File("src/main/resources/static/html/getWinnerPage.html");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html lang=\"ko\">\n");
            writer.write("<head>\n");
            writer.write("    <meta charset=\"UTF-8\">\n");
            writer.write("    <title>" + eventId + "회 이벤트 당첨자 발표</title>\n");
            writer.write("    <link rel=\"stylesheet\" href=\"/css/getWinner.css\">\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("<h1>" + eventId + "회 이벤트 당첨자 발표</h1>\n");
            writer.write("<table>\n");
            writer.write("    <thead>\n");
            writer.write("    <tr>\n");
            writer.write("        <th>순번</th>\n");
            writer.write("        <th>이름</th>\n");
            writer.write("        <th>전화번호</th>\n");
            writer.write("        <th>이메일</th>\n");
            writer.write("    </tr>\n");
            writer.write("    </thead>\n");
            writer.write("    <tbody>\n");

            for (EventParticipant winner : winners) {
                Member member = winner.getMember();

                writer.write("    <tr>\n");
                writer.write("        <td>" + ++count + "</td>\n");
                writer.write("        <td>" + member.getName() + "</td>\n");
                writer.write("        <td>" + member.getPhone() + "</td>\n");
                writer.write("        <td>" + member.getEmail() + "</td>\n");
                writer.write("    </tr>\n");
            }

            writer.write("    </tbody>\n");
            writer.write("</table>\n");
            writer.write("</body>\n");
            writer.write("</html>\n");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
