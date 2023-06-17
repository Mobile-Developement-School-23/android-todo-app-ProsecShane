package com.prosecshane.todoapp.data

import java.text.SimpleDateFormat

// Класс-репозиторий дел
class TodoItemsRepository {
    val items = mutableListOf<TodoItem>()

    // Хард-код примеры
    init {
        addItem(TodoItem(
            id = "example_1",
            text = "Купить деньги",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_2",
            text = "Купить очень много денег, целую кучу, прямо гора!",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_3",
            text = "Купить столько денег, что пока рассказываешь, сколько денег ты купил, у тебя кончится место в описании и закончится все троеточием, а ты не узнаешь, сколько денег я в итоге купил",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_4",
            text = "Сходить на зачет",
            importance = Importance.LOW,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_5",
            text = "Сходить за продуктами",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_6",
            text = "Сходить погулять с друзьями",
            importance = Importance.HIGH,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_7",
            text = "Сделать домашку",
            importance = Importance.MID,
            done = true,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_8",
            text = "Успеть ее сдать",
            importance = Importance.MID,
            deadline = SimpleDateFormat("dd.MM.yyyy").parse("17.06.2023"),
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_9",
            text = "Купить гамбургер",
            importance = Importance.MID,
            deadline = SimpleDateFormat("dd.MM.yyyy").parse("01.01.2000"),
            done = true,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_10",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_11",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_12",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_13",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_14",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_15",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_16",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_17",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_18",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_19",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_20",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_21",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_22",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_23",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_24",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
        addItem(TodoItem(
            id = "example_25",
            text = "Задание #${(0..100).random()}",
            importance = Importance.MID,
            done = false,
            createdOn = SimpleDateFormat("dd.MM.yyyy").parse("09.10.2003")
        ))
    }

    // Добавить дело
    fun addItem(item: TodoItem) {
        items.add(item)
    }

    // Получить дела (можно без сделанных)
    fun getItems(onlyUndone: Boolean = false): List<TodoItem> {
        if (onlyUndone) return items.filter { !it.done }
        return items
    }
}
