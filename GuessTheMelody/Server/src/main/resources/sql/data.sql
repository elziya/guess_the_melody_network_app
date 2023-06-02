insert into category(name)
values ('Музыка нулевых');
insert into category(name)
values ('Рок-легенды');
insert into category(name)
values ('Саундтреки к американским фильмам');
insert into category(name)
values ('Рок-баллады');
insert into category(name)
values ('Саундтреки к российским фильмам');

insert into song(pathname, name, points)
values('C:\Users\Полина\ITIS\3sem\ОРИС\practice\SEM_2\semester_work2_2021_prog\GuessTheMelody\Server\src\main\resources\music\call_me_cruella_florence.mp4', 'Call me Cruella', 100);
insert into song(pathname, name, points)
values('C:\Users\Полина\ITIS\3sem\ОРИС\practice\SEM_2\semester_work2_2021_prog\GuessTheMelody\Server\src\main\resources\music\led_zeppelin_black_dog.mp4', 'Black Dog', 150);
insert into song(pathname, name, points)
values('C:\Users\Полина\ITIS\3sem\ОРИС\practice\SEM_2\semester_work2_2021_prog\GuessTheMelody\Server\src\main\resources\music\мой_рок-н-ролл_би_2.mp4', 'Мой рок-н-ролл', 200);
insert into song(pathname, name, points)
values('C:\Users\Полина\ITIS\3sem\ОРИС\practice\SEM_2\semester_work2_2021_prog\GuessTheMelody\Server\src\main\resources\music\молитва_би_2.mp4', 'Молитва', 50);
insert into song(pathname, name, points)
values('C:\Users\Полина\ITIS\3sem\ОРИС\practice\SEM_2\semester_work2_2021_prog\GuessTheMelody\Server\src\main\resources\music\полковнику_никто_не_пишет_би_2.mp4', 'Полковнику никто не пишет', 210);
insert into song(pathname, name, points)
values('C:\Users\Полина\ITIS\3sem\ОРИС\practice\SEM_2\semester_work2_2021_prog\GuessTheMelody\Server\src\main\resources\music\хочешь_земфира.mp4', 'Хочешь', 130);
insert into song(pathname, name, points)
values('C:\Users\Полина\ITIS\3sem\ОРИС\practice\SEM_2\semester_work2_2021_prog\GuessTheMelody\Server\src\main\resources\music\я_искала_тебя_земфира.mp4', 'Я искала тебя', 150);
insert into song(pathname, name, points)
values('C:\Users\Полина\ITIS\3sem\ОРИС\practice\SEM_2\semester_work2_2021_prog\GuessTheMelody\Server\src\main\resources\music\AURORA_Midas_Touch.mp4', 'Midas Touch', 90);
insert into song(pathname, name, points)
values('C:\Users\Полина\ITIS\3sem\ОРИС\practice\SEM_2\semester_work2_2021_prog\GuessTheMelody\Server\src\main\resources\music\lorde_Everybody_Wants_To_Rule_The_World.mp4', 'Everybody Wants To Rule The World', 120);
insert into song(pathname, name, points)
values('C:\Users\Полина\ITIS\3sem\ОРИС\practice\SEM_2\semester_work2_2021_prog\GuessTheMelody\Server\src\main\resources\music\sufjan-stevens_mystery-of-love_.mp4', 'Mystery Of Love', 220);
insert into song(pathname, name, points)
values('C:\Users\Полина\ITIS\3sem\ОРИС\practice\SEM_2\semester_work2_2021_prog\GuessTheMelody\Server\src\main\resources\music\если_бы_нет_lil_kate.mp4', 'Если бы не ты', 50);


insert into category_song(category_id, song_id)
values (6, 4);
insert into category_song(category_id, song_id)
values (6, 8);
insert into category_song(category_id, song_id)
values (6, 7);
insert into category_song(category_id, song_id)
values (6, 6);
insert into category_song(category_id, song_id)
values (6, 3);
insert into category_song(category_id, song_id)
values (7, 3);
insert into category_song(category_id, song_id)
values (7, 6);
insert into category_song(category_id, song_id)
values (7, 7);
insert into category_song(category_id, song_id)
values (7, 8);
insert into category_song(category_id, song_id)
values (8, 2);
insert into category_song(category_id, song_id)
values (9, 4);
insert into category_song(category_id, song_id)
values (9, 5);
insert into category_song(category_id, song_id)
values (9, 7);
insert into category_song(category_id, song_id)
values (9, 8);
insert into category_song(category_id, song_id)
values (10, 8);
insert into category_song(category_id, song_id)
values (10, 5);
insert into category_song(category_id, song_id)
values (10, 6);
insert into category_song(category_id, song_id)
values (8, 9);
insert into category_song(category_id, song_id)
values (8, 10);
insert into category_song(category_id, song_id)
values (8, 11);
insert into category_song(category_id, song_id)
values (10, 12);

select category_id, category_name, song_id, pathname, s.name as song_name,
            points from (select category_id, c.name as category_name, song_id from category_song left join category c
            on c.id = category_song.category_id order by category_id) t left join song s on song_id = s.id;