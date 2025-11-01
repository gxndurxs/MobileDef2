# Отчёт по выполнению практической работы № 3/4

### Информация о студенте
* **Студент:** Островский Антон Ильич
* **Группа:** БСБО-09-22
* **Номер по списку:** 20

---

## Тема: Разработка мобильных приложений. Модификация архитектуры и расширение функциональности.

## Цель работы:
*   Провести рефакторинг приложения, внедрив архитектурный паттерн **MVVM (Model-View-ViewModel)**.
*   Научиться использовать ключевые компоненты Android Architecture Components: `ViewModel`, `LiveData`, `ViewModelProvider` и `MediatorLiveData`.
*   Решить проблему потери состояния при пересоздании `Activity` (например, при повороте экрана).
*   Значительно расширить функциональность приложения: улучшить UI, добавить несколько экранов и реализовать полный **CRUD**-цикл (Create, Read, Update, Delete) для сущности "Привычка".

---

## Ход работы:

### 1. Анализ и рефакторинг архитектуры (переход на MVVM)

Изначально проект имел `Activity`, напрямую взаимодействующую с `UseCases`. Это приводило к повторным загрузкам данных при повороте экрана и перегруженности `Activity` логикой. Было принято решение о переходе на архитектуру MVVM.

#### 1.1. Подключение библиотек
В файл `app/build.gradle.kts` были добавлены зависимости для `androidx.lifecycle`, необходимые для работы `ViewModel` и `LiveData`.

<img width="628" height="402" alt="image" src="https://github.com/user-attachments/assets/e53e35bb-e5df-4bfb-9bb4-94466621eb81" />

#### 1.2. Создание `MainViewModel` и `MainViewModelFactory`
Вся бизнес-логика и работа с данными была вынесена из `MainActivity` в новый класс `MainViewModel`. Для его корректной инициализации с зависимостями была создана фабрика `MainViewModelFactory`.

```
package ru.mirea.ostrovskiy.habittracker.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.mirea.ostrovskiy.habittracker.domain.models.Habit;
import ru.mirea.ostrovskiy.habittracker.domain.repository.HabitRepository;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetHabitsUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetUserNameUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.GetWeatherUseCase;
import ru.mirea.ostrovskiy.habittracker.domain.usecases.LogoutUserUseCase;

public class MainViewModel extends ViewModel {

    private final GetHabitsUseCase getHabitsUseCase;
    private final LogoutUserUseCase logoutUserUseCase;
    private final GetUserNameUseCase getUserNameUseCase;
    private final GetWeatherUseCase getWeatherUseCase;

    private final MutableLiveData<List<Habit>> habits = new MutableLiveData<>();
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<WeatherState> weatherState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutEvent = new MutableLiveData<>();
    private final MediatorLiveData<String> screenTitle = new MediatorLiveData<>();

    public MainViewModel(GetHabitsUseCase getHabitsUseCase, LogoutUserUseCase logoutUserUseCase, GetUserNameUseCase getUserNameUseCase, GetWeatherUseCase getWeatherUseCase) {
        this.getHabitsUseCase = getHabitsUseCase;
        this.logoutUserUseCase = logoutUserUseCase;
        this.getUserNameUseCase = getUserNameUseCase;
        this.getWeatherUseCase = getWeatherUseCase;

        screenTitle.addSource(userName, name -> {
            List<Habit> currentHabits = habits.getValue();
            updateTitle(name, currentHabits);
        });

        screenTitle.addSource(habits, habitList -> {
            String currentName = userName.getValue();
            updateTitle(currentName, habitList);
        });
    }
    private void updateTitle(String name, List<Habit> habitList) {
        if (name == null || habitList == null) {
            return;
        }
        int habitsCount = habitList.size();
        screenTitle.setValue("Здравствуйте, " + name + "! Количество привычек: " + habitsCount);
    }

    public LiveData<List<Habit>> getHabits() {
        return habits;
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<WeatherState> getWeatherState() {
        return weatherState;
    }

    public LiveData<Boolean> getLogoutEvent() {
        return logoutEvent;
    }

    public LiveData<String> getScreenTitle() {
        return screenTitle;
    }

    public void loadInitialData() {
        String name = getUserNameUseCase.execute();
        userName.setValue(name);
        loadHabits();
    }

    public void loadHabits() {
        isLoading.setValue(true);
        getHabitsUseCase.execute(new HabitRepository.HabitCallback() {
            @Override
            public void onHabitsLoaded(List<Habit> loadedHabits) {
                habits.setValue(loadedHabits);
                isLoading.setValue(false);
            }

            @Override
            public void onError(String message) {
                isLoading.setValue(false);
            }
        });
    }

    public void fetchWeather() {
        weatherState.setValue(new WeatherState(true, null, null, null));
        getWeatherUseCase.execute("Moscow", new GetWeatherUseCase.WeatherCallback() {
            @Override
            public void onSuccess(String temperature, String description) {
                weatherState.setValue(new WeatherState(false, temperature, description, null));
            }

            @Override
            public void onError(String message) {
                weatherState.setValue(new WeatherState(false, null, null, message));
            }
        });
    }

    public void logout() {
        logoutUserUseCase.execute();
        logoutEvent.setValue(true);
    }

    public static class WeatherState {
        public final boolean isLoading;
        public final String temperature;
        public final String description;
        public final String error;

        WeatherState(boolean isLoading, String temperature, String description, String error) {
            this.isLoading = isLoading;
            this.temperature = temperature;
            this.description = description;
            this.error = error;
        }
    }
}
```

#### 1.3. Рефакторинг `MainActivity`
`MainActivity` была упрощена: из нее удалена вся логика, вместо этого она "подписывается" на `LiveData` из `ViewModel` и только отображает полученные данные. Это решило проблему с поворотом экрана — `ViewModel` переживает это событие, и данные не загружаются заново.

#### 1.4. Использование `MediatorLiveData`
Для объединения данных из нескольких источников (`userName` и списка привычек) был применен `MediatorLiveData`. Он формирует общую приветственную строку, которая обновляется при изменении любого из источников.

<img width="490" height="965" alt="image" src="https://github.com/user-attachments/assets/a4a8467b-611f-4455-9af4-83dda32c5487" />

### 2. Расширение функциональности и улучшение UI/UX

После приведения архитектуры в порядок была проведена работа по улучшению пользовательского опыта и добавлению нового функционала.

#### 2.1. Модернизация UI главного экрана
Простой текстовый список был заменен на современный интерфейс с использованием `CardView` для каждого элемента. В каждую карточку был добавлен `ProgressBar` для наглядного отображения прогресса выполнения привычки.

#### 2.2. Реализация CRUD: Добавление привычки (Create)
На главный экран была добавлена `FloatingActionButton` (+), по нажатию на которую открывается новый экран — `HabitEditActivity`. На этом экране пользователь может ввести название, описание и срок выполнения привычки. После сохранения данные добавляются в базу данных `Room`.

<img width="438" height="866" alt="image" src="https://github.com/user-attachments/assets/4c4b1bc0-6a62-460c-9cd8-ec764c9073d9" />

#### 2.3. Реализация детального просмотра (Read)
Элементы списка на главном экране стали кликабельными. При нажатии на привычку открывается новый экран — `HabitDetailActivity`, где отображается вся подробная информация о ней.

<img width="432" height="865" alt="image" src="https://github.com/user-attachments/assets/ebdf866e-090f-4c67-80b5-014a589fde53" />

#### 2.4. Реализация CRUD: Редактирование и Удаление (Update, Delete)
На экран `HabitDetailActivity` были добавлены кнопки "Редактировать" и "Удалить".
*   **Редактирование:** Открывает тот же экран `HabitEditActivity`, но в режиме редактирования, с уже заполненными полями. После сохранения изменения записываются в базу данных.
*   **Удаление:** Показывает диалог с подтверждением и при согласии удаляет запись из базы данных.

<img width="440" height="864" alt="image" src="https://github.com/user-attachments/assets/53cfeb01-4270-4fa6-8361-42307b046735" />

#### 2.5. Интерактивное обновление прогресса
На экран `HabitDetailActivity` был добавлен `SeekBar` (ползунок). Перемещая его, пользователь может в реальном времени изменять свой прогресс. Как только пользователь отпускает ползунок, новое значение прогресса автоматически сохраняется в базу данных.

---

## Выводы:

В ходе выполнения работы была не только успешно освоена и внедрена архитектура **MVVM**, но и значительно расширена функциональность приложения. Проект был превращен из технического примера в полноценное интерактивное приложение с несколькими экранами и полным циклом управления данными (CRUD).

Ключевые результаты:
*   **Разделение ответственности:** Логика представления полностью отделена от UI.
*   **Устойчивость к изменениям конфигурации:** Приложение корректно сохраняет состояние.
*   **Современный и интуитивный UI:** Приложение стало удобным и приятным в использовании.
*   **Полноценный функционал:** Реализованы все базовые операции: создание, просмотр, редактирование, удаление и обновление прогресса привычек.
