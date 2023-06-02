package ru.kpfu.itis.messages;

import ru.kpfu.itis.models.AnswerStatus;

//высылается остальным игрокам, когда тот, кто нажал кнопку написал ответ
public class SendCheckedAnswer extends Message<AnswerStatus>{
    private int senderId;
    private AnswerStatus status;

    public SendCheckedAnswer(AnswerStatus status, int senderId){
        this.status = status;
        this.senderId = senderId;
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.SEND_CHECKED_ANSWER;
    }

    @Override
    public AnswerStatus getContent() {
        return status;
    }

    @Override
    public int getSenderId() {
        return senderId;
    }
}

