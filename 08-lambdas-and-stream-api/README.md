# Spotify Explorer :musical_note:

## Условие

Ще създадем програма, която анализира и предоставя статистики върху реален `dataset`, съдържащ данни за [Spotify](https://www.spotify.com//) музикални парчета (тракове), издадени в периода 1921-2020 година. `Dataset`-ът е генериран чрез публичното Spotify Web API, съдържа над 160.000 записа и е взет от [kaggle](https://www.kaggle.com/yamaerenay/spotify-dataset-19212020-160k-tracks), онлайн платформата за machine learning и data science на Google.

Всеки ред от файла съдържа информация за един Spotify трак, в следния формат:

`id   artists   name   year   popularity   duration_ms   tempo   loudness   valence   acousticness   danceability   energy   liveness   speechiness   explicit`

**Забележки:**

- Първият ред на файла съдържа имената на полетата
- Разделител между полетата на всеки ред е запетая (`,`)
- Полето `artists` съдържа списък от имената на един или повече изпълнители, като всяко име е оградено с апострофи (`'`), а ако изпълнителите са няколко, имената им са разделени с `;`
- Имената на някои изпълнители и тракове в `dataset`-а съдържат символи от азбуки, различни от латинската. Ако искате тези имена да останат human-readable, погрижете се, създавайки входния поток за четене на `dataset`-а, да укажете като encoding charset `StandardCharsets.UTF_8` - има такъв опционален параметър в конструктора или static factory метода
- Семантиката и възможните стойности на всички полета, може да разгледате в [kaggle](https://www.kaggle.com/yamaerenay/spotify-dataset-19212020-160k-tracks)
- Траковете се идентифицират еднозначно от `id` полето си, така че два трака са еднакви, ако са еднакви `id`-тата им

### Тестване

Валидирайте решението си чрез unit тестове, като трябва да покриете минимален code coverage 80%.

## Структура на проекта

```bash
src
╷
├─ bg/sofia/uni/fmi/mjt/spotify/
│   ├─ SpotifyExplorer.java
│   └─ SpotifyTrack.java
│   └─ (...)
test
╷
└─ bg/sofia/uni/fmi/mjt/spotify/
    └─ (...)
```

В sapera.org качете общ `zip` архив на папки `src` и `test`.
