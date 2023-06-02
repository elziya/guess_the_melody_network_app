package ru.kpfu.itis.messages;

public enum MessageTypes {
    JOIN_ROOM,
    JOIN_ROOM_ANSWER,
    UPDATE_ROOM_INFO,
    STAGE_MUSIC,//сообщения с инфой об этапе(stage): 4 категории, каждая содержит 4 песни, после START_GAME
    PREPARED_ROOM_MESSAGE,
    DEFAULT_SONG,
    NOTE_SELECTED,
    CLICK_ANSWER_BUTTON,
    SEND_ANSWER,
    SEND_CHECKED_ANSWER,
    SHOW_RESULTS,
    DISCONNECTED,
    DISABLE_ANSWER_BUTTONS,
    UPDATE_USER_INFO,//и с клиента, и с сервера выслается обновленная инфа комнаты
    SEND_ROOM_LIST,
    SEND_PERMISSION_TO_GET_RESULTS,
    SEND_GAME_OVER_RESULTS//высылается словарь с игроками в тпорядке от первого места до третьего
}

